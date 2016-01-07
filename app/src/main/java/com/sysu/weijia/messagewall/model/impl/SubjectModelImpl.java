package com.sysu.weijia.messagewall.model.impl;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.sysu.weijia.messagewall.model.SubjectModel;
import com.sysu.weijia.messagewall.model.entity.Subject;
import com.sysu.weijia.messagewall.presenter.listener.OnSubjectAddListener;

/**
 * Created by weijia on 16-1-7.
 */
public class SubjectModelImpl implements SubjectModel {
    @Override
    public void upload(Subject subject, final OnSubjectAddListener listener) {
        subject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    listener.onSubjectAddSuccess();
                } else {
                    listener.onSubjectAddError(e.getMessage());
                }
            }
        });
    }
}
