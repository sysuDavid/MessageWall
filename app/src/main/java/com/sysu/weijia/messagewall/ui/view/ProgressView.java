package com.sysu.weijia.messagewall.ui.view;

/**
 * Created by weijia on 16-1-6.
 */
public interface ProgressView {
    void showLoading();
    void onSuccess();
    void onError(String error);
}
