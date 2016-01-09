package com.sysu.weijia.messagewall.model;

import com.avos.avoscloud.AVGeoPoint;
import com.sysu.weijia.messagewall.model.entity.Subject;
import com.sysu.weijia.messagewall.presenter.listener.OnSubjectAddListener;
import com.sysu.weijia.messagewall.presenter.listener.OnSubjectsGetListener;

/**
 * Created by weijia on 16-1-7.
 */
public interface SubjectModel {
    void upload(Subject subject, OnSubjectAddListener listener);
    void getSubjects(AVGeoPoint userLocation, OnSubjectsGetListener listener);
}
