package com.sysu.weijia.messagewall.ui.activity;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.sysu.weijia.messagewall.R;
import com.sysu.weijia.messagewall.ui.fragment.ListFragment;
import com.sysu.weijia.messagewall.ui.fragment.MapFragment;

public class MainActivity extends AppCompatActivity{
    // MainActivity主要处理菜单栏按钮处理事件

    private MapFragment mapFragment;
    private ListFragment listFragment;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        context = this;
        setDefaultFragment();
    }

    public void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mapFragment = new MapFragment();
        transaction.replace(R.id.fragment_content, mapFragment);
        transaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_switch) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            String title = item.getTitle().toString();

            if (title.equals(getString(R.string.action_switch_toList))) {
                if (listFragment == null) {
                    listFragment = new ListFragment();
                }
                transaction.replace(R.id.fragment_content, listFragment);
                item.setTitle(getString(R.string.action_switch_toMap));
            }

            if (title.equals(getString(R.string.action_switch_toMap))) {
                if (mapFragment == null) {
                    mapFragment = new MapFragment();
                }
                transaction.replace(R.id.fragment_content, mapFragment);
                item.setTitle(getString(R.string.action_switch_toList));
            }

            transaction.commit();
            return true;
        }
        if (id == R.id.action_logout) {
            new AlertDialog.Builder(this)
                    .setTitle("退出")
                    .setMessage("确定退出当前用户吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AVUser.logOut();
                            Intent intent = new Intent();
                            intent.setClass(context, LoginActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create().show();
        }
        return super.onOptionsItemSelected(item);
    }
}
