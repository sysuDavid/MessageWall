package com.sysu.weijia.messagewall.model;

import com.sysu.weijia.messagewall.model.entity.Message;
import com.sysu.weijia.messagewall.presenter.listener.OnMessageAddListener;

/**
 * Created by weijia on 16-1-10.
 */
public interface MessageModel {
    void uploadMessage(Message message, OnMessageAddListener listener);
}
