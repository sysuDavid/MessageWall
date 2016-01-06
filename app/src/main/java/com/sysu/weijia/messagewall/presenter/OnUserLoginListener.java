package com.sysu.weijia.messagewall.presenter;

/**
 * Created by weijia on 16-1-6.
 */
public interface OnUserLoginListener {
    void onLoginSuccess();
    void onLoginError(String error);
}
