package com.sysu.weijia.messagewall.presenter.impl;

import android.util.Log;

import com.sysu.weijia.messagewall.model.MessageModel;
import com.sysu.weijia.messagewall.model.SubjectModel;
import com.sysu.weijia.messagewall.model.UserModel;
import com.sysu.weijia.messagewall.model.entity.Message;
import com.sysu.weijia.messagewall.model.entity.Subject;
import com.sysu.weijia.messagewall.model.entity.User;
import com.sysu.weijia.messagewall.model.impl.MessageModelImpl;
import com.sysu.weijia.messagewall.model.impl.SubjectModelImpl;
import com.sysu.weijia.messagewall.model.impl.UserModelImpl;
import com.sysu.weijia.messagewall.presenter.MessagePresenter;
import com.sysu.weijia.messagewall.presenter.listener.OnMessageAddListener;
import com.sysu.weijia.messagewall.presenter.listener.OnMessageGetOfSubjectListener;
import com.sysu.weijia.messagewall.presenter.listener.OnSubjectGetByIdListener;
import com.sysu.weijia.messagewall.presenter.listener.OnUserGetByIdListener;
import com.sysu.weijia.messagewall.ui.view.MessageView;

import java.util.List;

/**
 * Created by weijia on 16-1-10.
 */
public class MessagePresenterImpl implements MessagePresenter, OnMessageGetOfSubjectListener,
        OnMessageAddListener, OnSubjectGetByIdListener, OnUserGetByIdListener {

    private MessageModel mMessageModel;
    private SubjectModel mSubjectModel;
    private UserModel mUserModel;
    private MessageView mMessageView;

    public MessagePresenterImpl(MessageView view) {
        mMessageView = view;
        mMessageModel = new MessageModelImpl();
        mSubjectModel = new SubjectModelImpl();
        mUserModel = new UserModelImpl();
    }

    @Override
    public void addMessage() {
        mMessageModel.uploadMessage(mMessageView.newMessage(), this);
    }

    @Override
    public void onMessageAddSuccess() {
        mMessageView.onUploadMessageSuccess();

    }

    @Override
    public void onMessageAddError(String error) {
        mMessageView.onUploadMessageError(error);


    }

    @Override
    public void getSubjectById(String id) {
        mSubjectModel.getSubjectById(id, this);
    }

    @Override
    public void onSubjectGetByIdSuccess(Subject subject) {
        mMessageView.onGetSubjectSuccess(subject);
    }

    @Override
    public void onSubjectGetByIdError(String error) {
        mMessageView.onGetSubjectError(error);
    }

    @Override
    public void getUserById(String id) {
        mUserModel.getUserByObjectId(id, this);
    }

    @Override
    public void OnUserGetByIdSuccess(User user) {
        mMessageView.onGetUserSuccess(user);
    }

    @Override
    public void OnUserGetByIdError(String error) {
        mMessageView.onGetSubjectError(error);
    }

    @Override
    public void getMessageOfSubject(Subject subject) {
        mMessageModel.getMessageOfSubject(subject, this);
    }

    @Override
    public void onMessageGetOfSubjectSuccess(List<Message> list) {
        mMessageView.onGetMessageSuccess(list);
    }

    @Override
    public void onMessageGetOfSubjectError(String error) {
        mMessageView.onGetMessageError(error);
    }
}
