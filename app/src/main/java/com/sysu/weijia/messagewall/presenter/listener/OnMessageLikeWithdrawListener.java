package com.sysu.weijia.messagewall.presenter.listener;

/**
 * Created by weijia on 16-3-27.
 */
public interface OnMessageLikeWithdrawListener {
    void onMessageLikeWithdrawSuccess();
    void onMessageLikeWithdrawError(String error);
}
