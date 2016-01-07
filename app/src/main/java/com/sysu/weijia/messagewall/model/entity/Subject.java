package com.sysu.weijia.messagewall.model.entity;

import android.os.Parcel;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.GetCallback;

/**
 * Created by weijia on 16-1-7.
 */

// 表名：Subject
@AVClassName("Subject")
public class Subject extends AVObject{

    private String title;
    private String address;
    private String introduction;
    private double latitude;
    private double longitude;
    private User creator;

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
    public void setLatitude(double la) {
        latitude = la;
        put("latitude", latitude);
    }
    public void setLongitude(double lo) {
        longitude = lo;
        put("longitude", longitude);
    }
    public void setCreator(User c) {
        creator = c;
        put("creator", creator);
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
    public double getLatitude() {
        latitude = getDouble("latitude");
        return latitude;
    }
    public double getLongitude() {
        longitude = getDouble("longitude");
        return longitude;
    }
    public User getCreator() {
        getAVObject("creator").fetchInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    creator = (User)avObject;
                }
            }
        });
        return creator;
    }
    // 基类实现了Parcelable接口，这里强制必须有Creator
    public static final Creator CREATOR = AVObjectCreator.instance;
}
