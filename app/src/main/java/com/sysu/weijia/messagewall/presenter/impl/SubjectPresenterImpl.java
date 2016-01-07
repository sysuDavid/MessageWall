package com.sysu.weijia.messagewall.presenter.impl;

import com.sysu.weijia.messagewall.model.SubjectModel;
import com.sysu.weijia.messagewall.model.entity.Subject;
import com.sysu.weijia.messagewall.model.impl.SubjectModelImpl;
import com.sysu.weijia.messagewall.presenter.SubjectPresenter;
import com.sysu.weijia.messagewall.presenter.listener.OnSubjectAddListener;
import com.sysu.weijia.messagewall.ui.view.CreateSubjectView;

/**
 * Created by weijia on 16-1-7.
 */
public class SubjectPresenterImpl implements SubjectPresenter, OnSubjectAddListener {
    SubjectModel mSubjectModel;
    CreateSubjectView mCreateSubjectView;

    public SubjectPresenterImpl(CreateSubjectView view) {
        mCreateSubjectView = view;
        mSubjectModel = new SubjectModelImpl();
    }

    @Override
    public void addSuject() {
        mCreateSubjectView.showLoading();
        mSubjectModel.upload(mCreateSubjectView.getSubject(), this);
    }

    @Override
    public void onSubjectAddSuccess() {
        mCreateSubjectView.onSuccess();
    }

    @Override
    public void onSubjectAddError(String error) {
        mCreateSubjectView.onError(error);
    }


}
