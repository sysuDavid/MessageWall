package com.sysu.weijia.messagewall.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.avos.avoscloud.AVUser;
import com.sysu.weijia.messagewall.R;

public class StartActivity extends AppCompatActivity {

    private Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().hide();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 方便看到启动界面
                    Thread.sleep(2000);
                    Intent intent = new Intent();
                    if (AVUser.getCurrentUser() == null) {
                        intent.setClass(context, LoginActivity.class);
                    } else {
                        intent.setClass(context, MainActivity.class);
                    }
                    startActivity(intent);
                } catch (InterruptedException e) {

                }
            }
        }).start();

    }
}
