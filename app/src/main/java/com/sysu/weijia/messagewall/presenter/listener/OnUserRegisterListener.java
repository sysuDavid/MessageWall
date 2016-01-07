package com.sysu.weijia.messagewall.presenter.listener;

import com.sysu.weijia.messagewall.model.entity.User;

/**
 * Created by weijia on 16-1-6.
 */
public interface OnUserRegisterListener {
    void onRegisterSuccess();
    void onRegisterError(String error);
}
