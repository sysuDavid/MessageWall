package com.sysu.weijia.messagewall.presenter.impl;

import android.util.Log;

import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.sysu.weijia.messagewall.model.SubjectModel;
import com.sysu.weijia.messagewall.model.UserModel;
import com.sysu.weijia.messagewall.model.entity.Subject;
import com.sysu.weijia.messagewall.model.impl.SubjectModelImpl;
import com.sysu.weijia.messagewall.model.impl.UserModelImpl;
import com.sysu.weijia.messagewall.presenter.SubjectPresenter;
import com.sysu.weijia.messagewall.presenter.listener.OnSubjectAddListener;
import com.sysu.weijia.messagewall.presenter.listener.OnSubjectsGetListener;
import com.sysu.weijia.messagewall.presenter.listener.OnUserGetListener;
import com.sysu.weijia.messagewall.ui.view.CreateSubjectView;
import com.sysu.weijia.messagewall.ui.view.GetSubjectsView;

import java.util.List;

/**
 * Created by weijia on 16-1-7.
 */
public class SubjectPresenterImpl implements SubjectPresenter,
        OnSubjectAddListener, OnUserGetListener, OnSubjectsGetListener{
    SubjectModel mSubjectModel;
    UserModel mUserModel;
    CreateSubjectView mCreateSubjectView;
    GetSubjectsView mGetSubjectsView;


    public SubjectPresenterImpl(CreateSubjectView view) {
        mCreateSubjectView = view;
        mUserModel = new UserModelImpl();
        if (mSubjectModel == null)
            mSubjectModel = new SubjectModelImpl();
    }

    public SubjectPresenterImpl(GetSubjectsView view) {
        mGetSubjectsView = view;
        if (mSubjectModel == null)
            mSubjectModel = new SubjectModelImpl();
    }

    @Override
    public void addSuject() {
        mCreateSubjectView.showLoading();
        mSubjectModel.upload(mCreateSubjectView.newSubject(), this);
    }

    @Override
    public void onSubjectAddSuccess() {
        mCreateSubjectView.onSuccess();
    }

    @Override
    public void onSubjectAddError(String error) {
        mCreateSubjectView.onError(error);
    }

    @Override
    public void setCurrentUserAsCreator() {

        mUserModel.getUser(AVUser.getCurrentUser().getUsername(), this);
    }

    @Override
    public void onUserGetSuccess(AVUser user) {
        mCreateSubjectView.currentUserAsCreator(user);
        Log.i("yuan", "get nickname: " + user.get("nickname") + " success.");
    }

    @Override
    public void onUserGetError(String error) {
        Log.i("yuan", "onUserGetError " + error);
    }

    @Override
    public void getSubjectList(AVGeoPoint point) {
        mSubjectModel.getSubjects(point, this);
    }

    @Override
    public void onSubjectsGetSuccess(List<AVObject> list) {
        mGetSubjectsView.setNearSubjects(list);
    }

    @Override
    public void onSubjectsGetError(String error) {
        Log.i("yuan", "subjects get error: " + error);
    }
}
