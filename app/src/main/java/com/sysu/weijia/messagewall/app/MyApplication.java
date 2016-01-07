package com.sysu.weijia.messagewall.app;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.sysu.weijia.messagewall.R;
import com.sysu.weijia.messagewall.model.entity.Subject;
import com.sysu.weijia.messagewall.model.entity.User;

/**
 * Created by weijia on 16-1-5.
 */
public class MyApplication extends Application {
    private static MyApplication mInstance;

    public static MyApplication getInstance() {
        return mInstance;
    }

    public MyApplication() {
        mInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AVObject.registerSubclass(User.class);
        AVObject.registerSubclass(Subject.class);
        AVOSCloud.initialize(this, getString(R.string.LeanCloud_APP_ID).toString(), getString(R.string.LeanCloud_APP_Key).toString());
    }
}
