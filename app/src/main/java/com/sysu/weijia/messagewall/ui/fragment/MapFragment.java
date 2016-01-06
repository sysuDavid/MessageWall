package com.sysu.weijia.messagewall.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.sysu.weijia.messagewall.R;

public class MapFragment extends Fragment implements LocationSource, AMapLocationListener{
    // 地图显示变量
    private MapView mapView;
    private AMap amap;
    // 定位变量
    private LocationSource.OnLocationChangedListener mLocationChangeListener = null;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationCLientOption;
    // 坐标变量
    private double currentLatitude;
    private double currentLongitude;

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
        // 地图基本显示
        mapView = (MapView)view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        amap = mapView.getMap();
        // 设置缩放级别：3~20
        amap.moveCamera(CameraUpdateFactory.zoomTo(16));
        return view;
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
                Log.i("yuan", "currentLatitude: "+currentLatitude+", currentLongitude: "+currentLongitude);
                addMarkTest();
                mLocationChangeListener.onLocationChanged(aMapLocation);
            }
        }
    }

    public void addMarkTest() {
        MarkerOptions makerOptions = new MarkerOptions();
        makerOptions.position(new LatLng(currentLatitude+0.0005, currentLongitude+0.0005));
        makerOptions.title("Test");
        // 使用默认标记
        makerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        amap.addMarker(makerOptions);
    }
}
