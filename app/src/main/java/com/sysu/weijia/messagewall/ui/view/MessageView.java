package com.sysu.weijia.messagewall.ui.view;

import com.sysu.weijia.messagewall.model.entity.Message;
import com.sysu.weijia.messagewall.model.entity.Subject;
import com.sysu.weijia.messagewall.model.entity.User;

import java.util.List;

/**
 * Created by weijia on 16-1-10.
 */
public interface MessageView {
    void getMessageOfSubject();
    void onGetMessageSuccess(List<Message> list);
    void onGetMessageError(String error);
    Message newMessage();
    void onUploadMessageSuccess();
    void onUploadMessageError(String error);
    void getSubjectById();
    void onGetSubjectSuccess(Subject subject);
    void onGetSubjectError(String error);
    void getUserById();
    void onGetUserSuccess(User user);
    void onGetUserError(String error);
}
