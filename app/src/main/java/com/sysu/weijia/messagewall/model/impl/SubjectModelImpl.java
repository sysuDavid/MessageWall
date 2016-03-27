package com.sysu.weijia.messagewall.model.impl;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.sysu.weijia.messagewall.model.SubjectModel;
import com.sysu.weijia.messagewall.model.entity.Subject;
import com.sysu.weijia.messagewall.presenter.listener.OnSubjectAddListener;
import com.sysu.weijia.messagewall.presenter.listener.OnSubjectGetByIdListener;
import com.sysu.weijia.messagewall.presenter.listener.OnSubjectsGetListener;

import java.util.List;

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

    @Override
    public void getSubjects(AVGeoPoint userLocation, final OnSubjectsGetListener listener) {
        AVQuery<AVObject> query = new AVQuery<AVObject>("Subject");
        query.whereNear("location", userLocation);
        //query.whereWithinMiles("location", userLocation, 1);
        Log.i("yuan", "10~1000");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    listener.onSubjectsGetSuccess(list);
                } else {
                    listener.onSubjectsGetError(e.getMessage());
                }
            }
        });
    }

    @Override
    public void getSubjectById(String id, final OnSubjectGetByIdListener listener) {
        AVQuery<Subject> query = AVQuery.getQuery("Subject");
        query.getInBackground(id, new GetCallback<Subject>() {
            @Override
            public void done(Subject subject, AVException e) {
                if (e == null) {
                    listener.onSubjectGetByIdSuccess(subject);
                } else {
                    listener.onSubjectGetByIdError(e.getMessage());
                }
            }
        });
    }
}
