package com.sysu.weijia.messagewall.presenter.listener;

import com.avos.avoscloud.AVObject;

import java.util.List;

/**
 * Created by weijia on 16-1-9.
 */
public interface OnSubjectsGetListener {
    void onSubjectsGetSuccess(List<AVObject> list);
    void onSubjectsGetError(String error);
}
