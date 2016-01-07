package com.sysu.weijia.messagewall.ui.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sysu.weijia.messagewall.R;
import com.sysu.weijia.messagewall.model.entity.User;
import com.sysu.weijia.messagewall.presenter.UserPresenter;
import com.sysu.weijia.messagewall.presenter.impl.UserPresentImpl;
import com.sysu.weijia.messagewall.ui.view.RegisterView;

public class RegisterActivity extends AppCompatActivity implements RegisterView, View.OnClickListener{

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText passwordComfirmEditText;
    private EditText nicknameEditText;
    private Button registerButton;
    private Dialog dialog;

    private UserPresenter userPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    public void init() {
        emailEditText = (EditText)findViewById(R.id.editText_email);
        passwordEditText = (EditText)findViewById(R.id.editText_password);
        passwordComfirmEditText = (EditText)findViewById(R.id.editText_password_confirm);
        nicknameEditText = (EditText)findViewById(R.id.editText_nickname);
        registerButton = (Button)findViewById(R.id.button_register);
        registerButton.setOnClickListener(this);
        userPresenter = new UserPresentImpl(this);
        dialog = new ProgressDialog(this);
        dialog.setTitle("注册中...");
    }

    @Override
    public void onClick(View v) {
        String emailString = emailEditText.getText().toString();
        String passwordString = passwordEditText.getText().toString();
        String passwordConfirmString = passwordComfirmEditText.getText().toString();
        String nicknameString = nicknameEditText.getText().toString();
        if (emailString.isEmpty()) {
            Toast.makeText(this, "邮箱不能为空", Toast.LENGTH_LONG).show();
        } else if (passwordString.isEmpty()) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_LONG).show();
        } else if(passwordConfirmString.isEmpty()) {
            Toast.makeText(this, "请再次输入密码", Toast.LENGTH_LONG).show();
        } else if (nicknameString.isEmpty()) {
            Toast.makeText(this, "昵称不能为空", Toast.LENGTH_LONG).show();
        } else if (!passwordString.equals(passwordConfirmString)){
            Toast.makeText(this, "两次密码输入不相同", Toast.LENGTH_LONG).show();
        } else {
            userPresenter.userRegister();
        }
    }

    @Override
    public User getRegisterUser() {
        User user = new User();
        user.setUsername(emailEditText.getText().toString());
        user.setPassword(passwordEditText.getText().toString());
        user.setNickname(nicknameEditText.getText().toString());
        return user;
    }

    @Override
    public void showLoading() {
        dialog.show();
    }

    @Override
    public void onSuccess() {
        dialog.dismiss();
        Toast.makeText(this, "注册成功", Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onError(String error) {
        dialog.dismiss();
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    // 点击非文本框位置，隐藏软键盘
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            return inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }
}
