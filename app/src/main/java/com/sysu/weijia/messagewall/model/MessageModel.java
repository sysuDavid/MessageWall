package com.sysu.weijia.messagewall.model;

import com.sysu.weijia.messagewall.model.entity.Message;
import com.sysu.weijia.messagewall.model.entity.Subject;
import com.sysu.weijia.messagewall.presenter.listener.OnMessageAddListener;
import com.sysu.weijia.messagewall.presenter.listener.OnMessageGetOfSubjectListener;

/**
 * Created by weijia on 16-1-10.
 */
public interface MessageModel {
    void uploadMessage(Message message, OnMessageAddListener listener);
    void getMessageOfSubject(Subject subject, OnMessageGetOfSubjectListener listener);
}
