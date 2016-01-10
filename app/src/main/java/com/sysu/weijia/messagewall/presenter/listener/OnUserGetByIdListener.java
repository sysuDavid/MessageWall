package com.sysu.weijia.messagewall.presenter.listener;

import com.sysu.weijia.messagewall.model.entity.User;

/**
 * Created by weijia on 16-1-10.
 */
public interface OnUserGetByIdListener {
    void OnUserGetByIdSuccess(User user);
    void OnUserGetByIdError(String error);
}
