package com.sysu.weijia.messagewall.model.entity;

import android.os.Parcel;
import android.util.Log;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.sysu.weijia.messagewall.presenter.listener.OnSubjectGetByIdListener;

/**
 * Created by weijia on 16-1-7.
 */

// 表名：Subject
@AVClassName("Subject")
public class Subject extends AVObject{

    private String title;
    private String address;
    private String introduction;
    private AVGeoPoint location;
    private AVUser creator;

    public Subject() {

    }
    public Subject(Parcel in) {
        super(in);
    }
    public void setTitle(String t) {
        title = t;
        put("title", title);
    }
    public void setAddress(String a) {
        address = a;
        put("address", address);
    }
    public void setIntroduction(String d) {
        introduction = d;
        put("introduction", introduction);
    }
    public void setLocation(AVGeoPoint p) {
        location = p;
        put("location", location);
    }
    public void setCreator(AVUser c) {
        creator = c;
        put("creator", creator);
    }
    public void setCreatorByCreatorId(String id) {
        put("creator", AVUser.createWithoutData("_User", id));
    }
    public String getTitle() {
        title = getString("title");
        return title;
    }
    public String getAddress() {
        address = getString("address");
        return address;
    }
    public String getIntroduction() {
        introduction = getString("introduction");
        return introduction;
    }
    public AVGeoPoint getLocation() {
        location = (AVGeoPoint)get("location");
        return location;
    }
    public AVUser getCreator() {
        // 向下转型，可能会出错
        // 如果是查找得到的对象，是AVObject类型的，无法调用此函数
        creator = (AVUser)getAVObject("creator");
        getAVObject("creator").fetchInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    creator = (AVUser)avObject;
                }
            }
        });
        return creator;
    }
    // 基类实现了Parcelable接口，这里强制必须有Creator
    public static final Creator CREATOR = AVObjectCreator.instance;

}
