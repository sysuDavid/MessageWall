package com.sysu.weijia.messagewall.presenter.listener;

/**
 * Created by weijia on 16-1-10.
 */
public interface OnMessageAddListener {
    void onMessageAddSuccess();
    void onMessageAddError(String error);
}
