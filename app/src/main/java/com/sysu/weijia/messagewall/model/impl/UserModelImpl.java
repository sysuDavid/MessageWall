package com.sysu.weijia.messagewall.model.impl;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;
import com.sysu.weijia.messagewall.model.UserModel;
import com.sysu.weijia.messagewall.model.entity.User;
import com.sysu.weijia.messagewall.presenter.listener.OnUserLoginListener;
import com.sysu.weijia.messagewall.presenter.listener.OnUserRegisterListener;

/**
 * Created by weijia on 16-1-6.
 */
public class UserModelImpl implements UserModel{
    @Override
    public void register(User user_, final OnUserRegisterListener listener) {
        User user = new User();
        user.setUsername(user_.getUsername());
        user.setPassword(user_.getPassword());
        user.setNickname(user_.getNickname());
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    listener.onRegisterSuccess();
                } else {
                    Log.i("yuan", e.getMessage());
                    listener.onRegisterError(e.getMessage());
                }
            }
        });
    }

    @Override
    public void login(String username, String password, final OnUserLoginListener listener) {
        User.logInInBackground(username, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null) {
                    listener.onLoginSuccess();
                } else {
                    listener.onLoginError(e.getMessage());
                }
            }
        });
    }
}
