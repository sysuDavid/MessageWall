package com.sysu.weijia.messagewall.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.Projection;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.sysu.weijia.messagewall.R;
import com.sysu.weijia.messagewall.model.entity.Subject;
import com.sysu.weijia.messagewall.ui.activity.CreateSubjectActivity;
import com.sysu.weijia.messagewall.ui.activity.MainActivity;
import com.sysu.weijia.messagewall.ui.adapter.MyInfoWindowAdapter;
import com.sysu.weijia.messagewall.ui.view.LoginView;

import java.util.ArrayList;

public class MapFragment extends Fragment implements LocationSource, AMapLocationListener{
    // 地图显示变量
    private MapView mapView;
    private AMap amap;
    // 定位变量
    private LocationSource.OnLocationChangedListener mLocationChangeListener = null;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationCLientOption;
    // 添加的Marker
    Marker addedMarker;

    // 坐标变量
    private double currentLatitude;
    private double currentLongitude;

    private double addedLatitude;
    private double addedLongitude;

    private Button createButton;
    private Button confirmButton;
    private Button cancelButton;
    private TextView tipTextView;

    private Context context;
    private MainActivity parentActivity;

    private Dialog confirmDialog;
    private Dialog cancelDialog;

    private ArrayList<Marker> markerArrayList;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        init(view);

        // 地图基本显示
        mapView = (MapView)view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        amap = mapView.getMap();
        // 设置缩放级别：3~20
        amap.moveCamera(CameraUpdateFactory.zoomTo(16));
        // 设置地图的自定义事件

        amap.setInfoWindowAdapter(new MyInfoWindowAdapter(parentActivity));

        return view;
    }

    public void init(View view) {
        context = getActivity();
        parentActivity = (MainActivity)getActivity();
        createButton = (Button)view.findViewById(R.id.button_create);
        confirmButton = (Button)view.findViewById(R.id.button_confirm_create);
        cancelButton = (Button)view.findViewById(R.id.button_cancel_create);
        tipTextView = (TextView)view.findViewById(R.id.textView_tip);

        ButtonClickListener buttonClickListener = new ButtonClickListener();
        createButton.setOnClickListener(buttonClickListener);
        confirmButton.setOnClickListener(buttonClickListener);
        cancelButton.setOnClickListener(buttonClickListener);

        confirmDialog = new AlertDialog.Builder(context)
                .setTitle("确定位置")
                .setMessage("确定该位置吗？")
                .setPositiveButton("确定",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        recoverState();
                        addedMarker.destroy();
                        addedMarker = null;
                        Intent intent = new Intent();
                        intent.setClass(context, CreateSubjectActivity.class);
                        Log.i("yuan", "addedLatitude: " + addedLatitude + ", addedLongitude: "+addedLongitude);
                        intent.putExtra("latitude", addedLatitude);
                        intent.putExtra("longitude", addedLongitude);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        cancelDialog = new AlertDialog.Builder(context)
                .setTitle("撤销添加")
                .setMessage("确定撤销留言墙添加吗？")
                .setPositiveButton("确定",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        recoverState();

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // 调用定位
        location();

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    void location() {
        // 设置定位标记的风格，此处使用默认蓝点
        MyLocationStyle mLocationstyle = new MyLocationStyle();
        amap.setMyLocationStyle(mLocationstyle);
        // 设置定位源
        amap.setLocationSource(this);
        // 设置默认定位按钮
        amap.getUiSettings().setMyLocationButtonEnabled(true);
        // 设置定位层可触发定位
        amap.setMyLocationEnabled(true);
    }

    // 激活位置接口
    @Override
    public void activate(LocationSource.OnLocationChangedListener onLocationChangedListener) {
        mLocationChangeListener = onLocationChangedListener;
        if (mLocationClient== null) {
            mLocationClient = new AMapLocationClient(this.getActivity());
            mLocationClient.setLocationListener(this);
            mLocationCLientOption = new AMapLocationClientOption();
            mLocationCLientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //mLocationCLientOption.setInterval(20000);
            mLocationCLientOption.setOnceLocation(true);
            mLocationClient.setLocationOption(mLocationCLientOption);
            mLocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mLocationChangeListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    // 定位成功后的回调函数
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mLocationChangeListener != null && aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                currentLatitude = aMapLocation.getLatitude();
                currentLongitude = aMapLocation.getLongitude();
                Log.i("yuan", "currentLatitude: " + currentLatitude + ", currentLongitude: " + currentLongitude);

                parentActivity.startGetSubjects();
                mLocationChangeListener.onLocationChanged(aMapLocation);
            }
        }
    }


    public void changeState() {
        tipTextView.setText(getString(R.string.tip_drag));
        createButton.setVisibility(View.INVISIBLE);
        confirmButton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.VISIBLE);
    }

    public void recoverState() {
        tipTextView.setText(getString(R.string.tip_create));
        createButton.setVisibility(View.VISIBLE);
        confirmButton.setVisibility(View.INVISIBLE);
        cancelButton.setVisibility(View.INVISIBLE);
        if (addedMarker != null && addedMarker.isVisible()) {
            addedMarker.setVisible(false);
        }

    }

    class ButtonClickListener implements View.OnClickListener {
        MarkerOptions dragMarkerOptions = new MarkerOptions();

        public void initOptions() {

            // 新留言墙的坐标默认为当前坐标
            addedLatitude = currentLatitude;
            addedLongitude = currentLongitude;

            if (null == addedMarker) {
                dragMarkerOptions.position(new LatLng(currentLatitude, currentLongitude));
                dragMarkerOptions.title("新留言墙");
                dragMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                dragMarkerOptions.draggable(true);

                addedMarker = amap.addMarker(dragMarkerOptions);

            } else if (!addedMarker.isVisible()) {
                addedMarker.setVisible(true);
            }
            amap.setOnMarkerDragListener(new DragListener());
        }


        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.button_create) {
                if (0 == currentLatitude && 0 == currentLongitude) {
                    Toast.makeText(context, "还没定位，请先定位", Toast.LENGTH_LONG).show();
                    return;
                }
                initOptions();
                changeState();

            }
            if (v.getId() == R.id.button_confirm_create) {
                confirmDialog.show();
            }
            if (v.getId() == R.id.button_cancel_create) {
                cancelDialog.show();
            }
        }
    }

    // 标记拖拽监听
    class DragListener implements AMap.OnMarkerDragListener {
        @Override
        public void onMarkerDrag(Marker marker) {

        }

        @Override
        public void onMarkerDragStart(Marker marker) {
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.arrow));
        }

        @Override
        public void onMarkerDragEnd(Marker marker) {
            addedLatitude = marker.getPosition().latitude;
            addedLongitude  = marker.getPosition().longitude;
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
        }
    }

    // 标记点击事件
    class MarkerClickListener implements AMap.OnMarkerClickListener {
        @Override
        public boolean onMarkerClick(Marker marker) {
            jumpPoint(marker);

            return false;
        }
    }

    class InfoWindowClickListener implements AMap.OnInfoWindowClickListener {
        @Override
        public void onInfoWindowClick(Marker marker) {

        }
    }

    public AVGeoPoint currentLocation() {
        return  new AVGeoPoint(currentLatitude, currentLongitude);
    }

    public void setNearSubjectsMarker() {
        if (markerArrayList == null)
            markerArrayList = new ArrayList<Marker>();
        for (AVObject avObject : parentActivity.getMySubjectList()) {
            Subject subject = (Subject)avObject;
            AVGeoPoint point = (AVGeoPoint)subject.get("location");
            MarkerOptions options = new MarkerOptions();
            options.position(new LatLng(subject.getLocation().getLatitude(), subject.getLocation().getLongitude()));
            options.title(subject.getTitle());
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            Marker marker = amap.addMarker(options);
            marker.setObject(subject.getObjectId());
            markerArrayList.add(marker);
        }
        amap.setOnMarkerClickListener(new MarkerClickListener());
    }

    /**
     * marker点击时跳动一下
     */
    public void jumpPoint(final Marker marker) {
        if (marker.isInfoWindowShown())
            return;
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = amap.getProjection();
        final LatLng latLng = marker.getPosition();
        Point startPoint = proj.toScreenLocation(latLng);
        startPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * latLng.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * latLng.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                setMarkersIcon();
                amap.invalidate();// 刷新地图
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    void setMarkersIcon() {
        for (Marker marker : markerArrayList) {
            if (marker.isInfoWindowShown())
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            else
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }
    }
}
