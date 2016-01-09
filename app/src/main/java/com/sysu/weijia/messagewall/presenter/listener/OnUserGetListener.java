package com.sysu.weijia.messagewall.presenter.listener;

import com.avos.avoscloud.AVUser;

/**
 * Created by weijia on 16-1-8.
 */
public interface OnUserGetListener {
    void onUserGetSuccess(AVUser user);
    void onUserGetError(String error);
}
