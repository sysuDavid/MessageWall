package com.sysu.weijia.messagewall.model.impl;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.sysu.weijia.messagewall.model.MessageModel;
import com.sysu.weijia.messagewall.model.entity.Message;
import com.sysu.weijia.messagewall.model.entity.Subject;
import com.sysu.weijia.messagewall.model.entity.User;
import com.sysu.weijia.messagewall.presenter.listener.OnMessageAddListener;
import com.sysu.weijia.messagewall.presenter.listener.OnMessageGetOfSubjectListener;

import java.util.List;

/**
 * Created by weijia on 16-1-10.
 */
public class MessageModelImpl implements MessageModel{
    @Override
    public void uploadMessage(Message message, final OnMessageAddListener listener) {
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    listener.onMessageAddSuccess();
                } else {
                    listener.onMessageAddError(e.getMessage());
                }
            }
        });
    }

    @Override
    public void getMessageOfSubject(Subject subject, final OnMessageGetOfSubjectListener listener) {
        AVQuery<Message> query = AVQuery.getQuery("Message");
        query.whereEqualTo("subject", subject);
        query.include("user");
        query.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> list, AVException e) {
                if (e == null) {
                    listener.onMessageGetOfSubjectSuccess(list);
                } else {
                    listener.onMessageGetOfSubjectError(e.getMessage());
                }
            }
        });
    }
}
