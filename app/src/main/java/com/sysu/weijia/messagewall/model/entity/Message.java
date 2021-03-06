package com.sysu.weijia.messagewall.model.entity;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weijia on 16-1-10.
 */

@AVClassName("Message")
public class Message extends AVObject {
    private String content;
    private int likeNum;
    private Subject subject;
    private User user;
    private ArrayList<User> likeUsers;

    // 点赞数
    //private int agreeNum;

    public Message() {
        likeUsers = new ArrayList<User>();
    }

    public void setContent(String c) {
        content = c;
        put("content", content);
    }
    public String getContent() {
        content = getString("content");
        return content;
    }

    public void putLikeNum() {
        put("likeNum", 0);
    }
    public void incrementLikeNum() {
        increment("likeNum");
    }
    public void setUserLike() {

    }
    public void decrementLikeNum() {
        increment("likeNum", -1);

    }
    public int getLikeNum() {
        likeNum = getInt("likeNum");
        return likeNum;
    }

    public void setSubject(Subject s) {
        subject = s;
        put("subject", subject);
    }

    public Subject getSubject() {

        subject = getAVObject("subject");
        return subject;
    }

    public void setSubjectByObjectId(String id) {
        put("subject", AVObject.createWithoutData("Subject", id));
    }

    // 子类对象get方法不能向下转型，故不写
    // getSubject(), getUser()不写

    public void setUser(User u) {
        user = u;
        put("user", user);
    }

    public User getUser() {
        user = (User)getAVUser("user");
        return user;
    }
    public void setUserByObjectId(String id) {
        put("user", AVUser.createWithoutData("_User", id));
    }

    // 基类实现了Parcelable接口，这里强制必须有Creator
    public static final Creator CREATOR = AVObjectCreator.instance;
}
