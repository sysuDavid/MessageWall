package com.sysu.weijia.messagewall.presenter.impl;

import com.sysu.weijia.messagewall.model.UserModel;
import com.sysu.weijia.messagewall.model.entity.User;
import com.sysu.weijia.messagewall.model.impl.UserModelImpl;
import com.sysu.weijia.messagewall.presenter.listener.OnUserLoginListener;
import com.sysu.weijia.messagewall.presenter.listener.OnUserRegisterListener;
import com.sysu.weijia.messagewall.presenter.UserPresenter;
import com.sysu.weijia.messagewall.ui.view.LoginView;
import com.sysu.weijia.messagewall.ui.view.RegisterView;

/**
 * Created by weijia on 16-1-6.
 */
public class UserPresentImpl implements UserPresenter, OnUserRegisterListener, OnUserLoginListener {
    private UserModel mUserModel;
    private RegisterView mRegisterView;
    private LoginView mLoginView;

    // 在RegisterActivity中调用这个构造函数
    public UserPresentImpl(RegisterView registerView) {
        mRegisterView = registerView;
        mUserModel = new UserModelImpl();
    }

    // 在LoginActivity中调用这个构造函数
    public UserPresentImpl(LoginView loginView) {
        mLoginView = loginView;
        mUserModel = new UserModelImpl();
    }

    // 实现UserPresenter接口的函数
    @Override
    public void userRegister() {
        mRegisterView.showLoading();
        mUserModel.register(mRegisterView.getRegisterUser(), this);
    }

    @Override
    public void userLogin() {
        mLoginView.showLoading();
        User user = mLoginView.getLoginUser();
        mUserModel.login(user.getUsername(), user.getPassword(), this);
    }

    // 实现onUserRegisterListener接口的函数
    @Override
    public void onRegisterSuccess() {
        mRegisterView.onSuccess();
    }

    @Override
    public void onRegisterError(String error) {
        mRegisterView.onError(error);
    }

    // 实现onUserLoginListener接口的函数
    @Override
    public void onLoginSuccess() {
        mLoginView.onSuccess();
    }

    @Override
    public void onLoginError(String error) {
        mLoginView.onError(error);
    }


}
