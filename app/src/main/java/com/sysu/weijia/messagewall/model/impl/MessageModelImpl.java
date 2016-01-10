package com.sysu.weijia.messagewall.model.impl;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.sysu.weijia.messagewall.model.MessageModel;
import com.sysu.weijia.messagewall.model.entity.Message;
import com.sysu.weijia.messagewall.presenter.listener.OnMessageAddListener;

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
}
