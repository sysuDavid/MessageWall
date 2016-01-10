package com.sysu.weijia.messagewall.presenter.listener;

import com.avos.avoscloud.AVUser;
import com.sysu.weijia.messagewall.model.entity.User;

/**
 * Created by weijia on 16-1-8.
 */
public interface OnUserGetListener {
    void onUserGetSuccess(AVUser avUser);
    void onUserGetError(String error);
}
