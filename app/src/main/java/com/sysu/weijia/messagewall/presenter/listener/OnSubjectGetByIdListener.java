package com.sysu.weijia.messagewall.presenter.listener;

import com.sysu.weijia.messagewall.model.entity.Subject;

/**
 * Created by weijia on 16-1-10.
 */
public interface OnSubjectGetByIdListener {
    void onSubjectGetByIdSuccess(Subject subject);
    void onSubjectGetByIdError(String error);
}
