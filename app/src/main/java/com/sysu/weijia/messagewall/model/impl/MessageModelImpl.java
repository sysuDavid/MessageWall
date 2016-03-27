package com.sysu.weijia.messagewall.model.impl;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVRelation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.sysu.weijia.messagewall.model.MessageModel;
import com.sysu.weijia.messagewall.model.entity.Message;
import com.sysu.weijia.messagewall.model.entity.Subject;
import com.sysu.weijia.messagewall.model.entity.User;
import com.sysu.weijia.messagewall.presenter.listener.OnMessageAddListener;
import com.sysu.weijia.messagewall.presenter.listener.OnMessageDeleteListener;
import com.sysu.weijia.messagewall.presenter.listener.OnMessageGetOfSubjectListener;
import com.sysu.weijia.messagewall.presenter.listener.OnMessageLikeListener;
import com.sysu.weijia.messagewall.presenter.listener.OnMessageLikeWithdrawListener;

import java.util.List;

/**
 * Created by weijia on 16-1-10.
 */
public class MessageModelImpl implements MessageModel{
    @Override
    public void getMessageOfSubject(Subject subject, final OnMessageGetOfSubjectListener listener) {
        AVQuery<Message> query = AVQuery.getQuery("Message");
        query.whereEqualTo("subject", subject);
        query.orderByDescending("likeNum");
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
    public void deleteMessage(Message message, final OnMessageDeleteListener listener) {
        message.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    listener.onMessageDeleteSuccess();
                } else {
                    listener.onMessageDeleteError(e.getMessage());
                }
            }
        });
    }

    @Override
    public void likeMessage(Message message, final OnMessageLikeListener listener) {
        AVUser user = AVUser.getCurrentUser();
        AVRelation<Message> relation = user.getRelation("likes");
        relation.add(message);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Log.i("yuan", "relation like success");
                } else {
                    Log.i("yuan", "relation like error" + e.getMessage());
                }
            }
        });
        message.setFetchWhenSave(true);
        message.incrementLikeNum();
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    listener.onMessageLikeSuccess();
                } else {
                    listener.onMessageLikeError(e.getMessage());
                }
            }
        });
    }

    @Override
    public void withdrawLikeMessage(Message message, final OnMessageLikeWithdrawListener listener) {
        AVUser user = AVUser.getCurrentUser();
        AVRelation<Message> relation = user.getRelation("likes");
        relation.remove(message);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Log.i("yuan", "relation withdraw like success");
                } else {
                    Log.i("yuan", "relation withdraw like error" + e.getMessage());
                }
            }
        });
        message.setFetchWhenSave(true);
        message.decrementLikeNum();
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    listener.onMessageLikeWithdrawSuccess();
                } else {
                    listener.onMessageLikeWithdrawError(e.getMessage());
                }
            }
        });
    }
}
