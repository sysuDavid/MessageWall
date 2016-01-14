package com.sysu.weijia.messagewall.presenter.listener;

import com.sysu.weijia.messagewall.model.entity.Message;

import java.util.List;

/**
 * Created by weijia on 16-1-11.
 */
public interface OnMessageGetOfSubjectListener {
    void onMessageGetOfSubjectSuccess(List<Message> list);
    void onMessageGetOfSubjectError(String error);
}
