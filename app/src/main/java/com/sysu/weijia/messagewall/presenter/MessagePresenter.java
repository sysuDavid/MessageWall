package com.sysu.weijia.messagewall.presenter;

import com.sysu.weijia.messagewall.model.entity.Message;
import com.sysu.weijia.messagewall.model.entity.Subject;

/**
 * Created by weijia on 16-1-10.
 */
public interface MessagePresenter {
    void getMessageOfSubject(Subject subject);
    void addMessage();
    void getSubjectById(String id);
    void getUserById(String id);
    void likeMessage(Message message);
    void withdrawLikeMessage(Message message);
    void deleteMessage(Message message);
}
