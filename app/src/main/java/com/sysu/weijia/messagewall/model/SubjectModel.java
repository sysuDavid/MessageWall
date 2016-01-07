package com.sysu.weijia.messagewall.model;

import com.sysu.weijia.messagewall.model.entity.Subject;
import com.sysu.weijia.messagewall.presenter.listener.OnSubjectAddListener;

/**
 * Created by weijia on 16-1-7.
 */
public interface SubjectModel {
    void upload(Subject subject, OnSubjectAddListener listener);
}
