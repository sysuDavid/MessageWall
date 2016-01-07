package com.sysu.weijia.messagewall.model.entity;

import android.os.Parcel;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

/**
 * Created by weijia on 16-1-6.
 */
@AVClassName("User")
public class User extends AVUser {
    private String username;
    private String password;
    private String nickname;
    private String email;
    public User() {

    }

    public User(Parcel in) {
        super(in);
    }

    public void setUsername(String un) {
        username = un;
        super.setUsername(username);
        // 使用邮箱作为用户名注册
        setEmail(un);
    }

    public void setPassword(String p) {
        password = p;
        super.setPassword(password);
    }

    public void setNickname(String nn) {
        nickname = nn;
        put("nickname", nickname);
    }

    public void setEmail(String e) {
        email = e;
        super.setEmail(email);
    }

    public String getUsername() {
        username = getString("username");
        return username;
    }

    public String getPassword() {
        password = getString("password");
        return password;
    }

    public String getNickname() {
        nickname = getString("nickname");
        return nickname;
    }

    public String getEmail() {
        email = getString("email");
        return email;
    }

    // 基类实现了Parcelable接口，这里强制必须有Creator
    public static final Creator CREATOR = AVObjectCreator.instance;
}
