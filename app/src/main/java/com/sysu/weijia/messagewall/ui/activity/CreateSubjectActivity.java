package com.sysu.weijia.messagewall.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.sysu.weijia.messagewall.R;
import com.sysu.weijia.messagewall.model.entity.Subject;
import com.sysu.weijia.messagewall.model.entity.User;
import com.sysu.weijia.messagewall.presenter.SubjectPresenter;
import com.sysu.weijia.messagewall.presenter.impl.SubjectPresenterImpl;
import com.sysu.weijia.messagewall.ui.view.CreateSubjectView;

import java.util.List;

public class CreateSubjectActivity extends AppCompatActivity implements GeocodeSearch.OnGeocodeSearchListener,
        CreateSubjectView {
    private Toolbar toolbar;
    private EditText addressEditText;
    private EditText titleEditText;
    private EditText introductionEditText;
    private Button defaultButton;
    private Dialog backAlertDialog;
    private Dialog loadingProgressDialog;

    private String defaultAddress;

    private AVUser currentUser;
    // 逆地理编码
    private GeocodeSearch geocodeSearch;
    private LatLonPoint latLonPoint;

    // mvp的presenter
    private SubjectPresenter subjectPresenter;

    // 测试
    private String testSubTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_subject);
        init();
    }

    public void init() {
        // presenter实例
        subjectPresenter = new SubjectPresenterImpl(this);

        // 退出二次确认
        backAlertDialog = new AlertDialog.Builder(this)
                .setTitle("退出")
                .setMessage("当前地址会被撤销，确定退出吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                })
                .setNegativeButton("取消", null)
                .create();

        // 通过坐标获取地址
        geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(this);

        // 设置toolbar的返回键
        toolbar = (Toolbar)findViewById(R.id.toolbar_create_subject);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    backAlertDialog.show();
                }
            });

        }

        Intent intent = getIntent();
        double latitude = intent.getDoubleExtra("latitude", 0);
        double longitude = intent.getDoubleExtra("longitude", 0);
        if (latitude == 0 && longitude == 0) {
            Toast.makeText(this, "坐标传输有误", Toast.LENGTH_LONG).show();
        }
        Log.i("yuan", "传入的latitude: " + latitude + ", longitude:" + longitude);
        latLonPoint = new LatLonPoint(latitude, longitude);
        getAddress(latLonPoint);


        addressEditText = (EditText)findViewById(R.id.editText_address);
        titleEditText = (EditText)findViewById(R.id.editText_title);
        introductionEditText = (EditText)findViewById(R.id.editText_introduction);
        defaultButton = (Button)findViewById(R.id.button_default);

        // 重置默认地址
        defaultAddress = addressEditText.getText().toString();
        defaultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressEditText.setText(defaultAddress);
            }
        });

        // 进度提醒
        loadingProgressDialog = new ProgressDialog(this);
        loadingProgressDialog.setTitle("正在上传留言墙...");

    }

    // 逆地理编码
    public void getAddress(LatLonPoint latLonPoint) {
        // 第一个参数是坐标点，第二个参数表示范围多少米， 第三个参数表示坐标系类型
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 50,  GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(query);

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    // 逆地理编码的回调
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        Log.i("yuan", "逆地理编码回调结果: " + i);
        if (i == 0) {
            if (regeocodeResult != null && regeocodeResult.getRegeocodeAddress() != null &&
                    regeocodeResult.getRegeocodeAddress().getFormatAddress() != null) {
                defaultAddress = regeocodeResult.getRegeocodeAddress().getFormatAddress();
                addressEditText.setText(defaultAddress);
            }
        } else {
            Toast.makeText(this, "获取地址出错", Toast.LENGTH_LONG).show();
        }
    }

    // 菜单栏
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_subject, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (R.id.action_finish == id) {
            if (addressEditText.getText().toString().isEmpty()) {
                Toast.makeText(this, "地址不能为空", Toast.LENGTH_LONG).show();
            } else if (titleEditText.getText().toString().isEmpty()) {
                Toast.makeText(this, "标题不能为空", Toast.LENGTH_LONG).show();
            } else if (introductionEditText.getText().toString().isEmpty()) {
                Toast.makeText(this, "简介不能为空", Toast.LENGTH_LONG).show();
            } else {
                //这里根本不需要获取整个User，可以使用User的objectId将两个类关联
                //有很多代码需要删除，后期注意删除
                //subjectPresenter.setCurrentUserAsCreator();
                subjectPresenter.addSuject();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    // 处理返回键点击事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backAlertDialog.show();
        }
        return false;
    }

    @Override
    public void currentUserAsCreator(AVUser avUser) {
        currentUser = avUser;
        subjectPresenter.addSuject();
    }

    @Override
    public Subject newSubject() {
        Subject subject = new Subject();
        subject.setTitle(titleEditText.getText().toString());
        subject.setAddress(addressEditText.getText().toString());
        subject.setIntroduction(introductionEditText.getText().toString());
        subject.setLocation(new AVGeoPoint(latLonPoint.getLatitude(), latLonPoint.getLongitude()));
//        if (currentUser != null)
//            subject.setCreator(currentUser);
//        else
//            Log.i("yuan", "currentUser is null");
        subject.setCreatorByCreatorId(AVUser.getCurrentUser().getObjectId());
        // 测试
        testSubTitle = titleEditText.getText().toString();

        return subject;
    }

    @Override
    public void showLoading() {
        loadingProgressDialog.show();
    }

    @Override
    public void onSuccess() {
        loadingProgressDialog.dismiss();
        // 测试creator 是否添加
        AVQuery<AVObject> query = new AVQuery<AVObject>("Subject");
        query.whereEqualTo("title", testSubTitle);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (!list.isEmpty()) {
                        AVObject sub = list.get(0);
                        if (sub != null) {
                            sub.getAVObject("creator")
                                    .fetchIfNeededInBackground(new GetCallback<AVObject>() {
                                        @Override
                                        public void done(AVObject avObject, AVException e) {
                                            if (e == null) {
                                                Log.i("yuan", "creator test: " + avObject.get("nickname"));
                                            } else {
                                                Log.i("yuan", "creator fetch error: " + e.getMessage());
                                            }
                                        }
                                    });
                        }
                    }
                } else {
                    Log.i("yuan", "creator test error: " + e.getMessage());
                }
            }
        });

        Toast.makeText(this, "上传成功", Toast.LENGTH_LONG).show();
        onBackPressed();
    }

    @Override
    public void onError(String error) {
        loadingProgressDialog.dismiss();
        Log.i("yuan", "addsubject error: "+error);
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }
}
