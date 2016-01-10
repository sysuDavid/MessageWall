package com.sysu.weijia.messagewall.ui.view;

import com.sysu.weijia.messagewall.model.entity.Message;
import com.sysu.weijia.messagewall.model.entity.Subject;
import com.sysu.weijia.messagewall.model.entity.User;

/**
 * Created by weijia on 16-1-10.
 */
public interface MessageView {
    Message newMessage();
    void getSubjectById();
    void OnGetSubjectSuccess(Subject subject);
    void OnGetSubjectError(String error);
    void getUserById();
    void OnGetUserSuccess(User user);
    void OnGetUserError(String error);
}
