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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
import com.sysu.weijia.messagewall.model.entity.Subject;
import com.sysu.weijia.messagewall.presenter.SubjectPresenter;
import com.sysu.weijia.messagewall.presenter.impl.SubjectPresenterImpl;
import com.sysu.weijia.messagewall.ui.fragment.ListFragment;
import com.sysu.weijia.messagewall.ui.fragment.MapFragment;
import com.sysu.weijia.messagewall.ui.view.GetSubjectsView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements GetSubjectsView{
    // MainActivity主要处理菜单栏按钮处理事件

    private MapFragment mapFragment;
    private ListFragment listFragment;
    private Context context;

    private List<AVObject> mSubjectList;
    private SubjectPresenter mSubjectPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        context = this;
        setDefaultFragment();
        mSubjectPresenter = new SubjectPresenterImpl(this);
    }

    public void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mapFragment = new MapFragment();
        transaction.replace(R.id.fragment_content, mapFragment, "map");
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
                if (mSubjectList == null) {
                    Toast.makeText(this, "还没定位，请先定位", Toast.LENGTH_LONG).show();
                    return true;
                }
                listFragment.setData(mSubjectList);
                transaction.replace(R.id.fragment_content, listFragment);
                transaction.addToBackStack(null);
                item.setTitle(getString(R.string.action_switch_toMap));
            }

            if (title.equals(getString(R.string.action_switch_toMap))) {
                if (mapFragment == null) {
                    mapFragment = new MapFragment();
                }
                transaction.replace(R.id.fragment_content, mapFragment, "map");
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
                            finish();
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

    @Override
    public void setNearSubjects(List<AVObject> list) {
        mSubjectList = list;
        mapFragment.setNearSubjectsMarker();
    }

    public void startGetSubjects() {
        mSubjectPresenter.getSubjectList(mapFragment.currentLocation());
    }

    public List<AVObject> getMySubjectList() {
        return mSubjectList;
    }
    
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){

            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();                                exitTime = System.currentTimeMillis();
            }
            else{
                finish();
                System.exit(0);
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
