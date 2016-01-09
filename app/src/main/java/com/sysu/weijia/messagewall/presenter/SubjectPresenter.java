package com.sysu.weijia.messagewall.presenter;

import com.avos.avoscloud.AVGeoPoint;

/**
 * Created by weijia on 16-1-7.
 */
public interface SubjectPresenter {
    void setCurrentUserAsCreator();
    void addSuject();
    void getSubjectList(AVGeoPoint point);
}
