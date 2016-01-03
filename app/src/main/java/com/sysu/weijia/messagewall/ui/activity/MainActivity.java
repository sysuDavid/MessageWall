package com.sysu.weijia.messagewall.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.sysu.weijia.messagewall.R;

public class MainActivity extends AppCompatActivity implements LocationSource, AMapLocationListener {

    private MapView mapView;
    private AMap amap;
    private OnLocationChangedListener mLocationChangeListener = null;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationCLientOption;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        // mapview show test
        mapView = (MapView)findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        amap = mapView.getMap();

        locationTest();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    void locationTest() {
        MyLocationStyle mLocationstyle = new MyLocationStyle();
        amap.setMyLocationStyle(mLocationstyle);
        amap.setLocationSource(this);
        amap.getUiSettings().setMyLocationButtonEnabled(true);
        amap.setMyLocationEnabled(true);
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mLocationChangeListener = onLocationChangedListener;
        if (mLocationClient== null) {
            mLocationClient = new AMapLocationClient(this);
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

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mLocationChangeListener != null && aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                mLocationChangeListener.onLocationChanged(aMapLocation);
            }
        }
    }
}
