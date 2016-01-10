package com.sysu.weijia.messagewall.model;

import com.sysu.weijia.messagewall.model.entity.User;
import com.sysu.weijia.messagewall.presenter.listener.OnUserGetByIdListener;
import com.sysu.weijia.messagewall.presenter.listener.OnUserGetListener;
import com.sysu.weijia.messagewall.presenter.listener.OnUserLoginListener;
import com.sysu.weijia.messagewall.presenter.listener.OnUserRegisterListener;

/**
 * Created by weijia on 16-1-6.
 */
public interface UserModel {
    void register(User user, OnUserRegisterListener listener);
    void login(String username, String password, OnUserLoginListener listener);
    void getUser(String username, OnUserGetListener listener);
    void getUserByObjectId(String id, OnUserGetByIdListener listener);
}
