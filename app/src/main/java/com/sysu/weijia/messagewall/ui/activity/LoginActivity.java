package com.sysu.weijia.messagewall.ui.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sysu.weijia.messagewall.R;
import com.sysu.weijia.messagewall.model.entity.User;
import com.sysu.weijia.messagewall.presenter.UserPresenter;
import com.sysu.weijia.messagewall.presenter.impl.UserPresentImpl;
import com.sysu.weijia.messagewall.ui.view.LoginView;

public class LoginActivity extends AppCompatActivity implements LoginView{

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView toRegisterTextView;
    private TextView toFindPasswordTextView;
    private Dialog dialog;
    private Context context;
    private UserPresenter userPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    public void init() {
        context = this;
        emailEditText = (EditText)findViewById(R.id.editText_email);
        passwordEditText = (EditText)findViewById(R.id.editText_password);
        loginButton = (Button)findViewById(R.id.button_login);
        toRegisterTextView = (TextView)findViewById(R.id.textView_toRegister);
        toFindPasswordTextView = (TextView)findViewById(R.id.textView_toFindPassword);

        loginButton.setOnClickListener(new LoginListener());
        toRegisterTextView.setOnClickListener(new ToRegisterListener());

        dialog = new ProgressDialog(this);
        dialog.setTitle("登录中...");

        userPresenter = new UserPresentImpl(this);
    }

    class LoginListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String emailString = emailEditText.getText().toString();
            String passwordString = passwordEditText.getText().toString();
            if (emailString.isEmpty()) {
                Toast.makeText(context, "用户名不能为空", Toast.LENGTH_LONG).show();
            } else if (passwordString.isEmpty()) {
                Toast.makeText(context, "密码不能为空", Toast.LENGTH_LONG).show();
            } else {
                userPresenter.userLogin();
            }
        }
    }

    class ToRegisterListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(context, RegisterActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public User getLoginUser() {
        User user = new User();
        user.setUsername(emailEditText.getText().toString());
        user.setPassword(passwordEditText.getText().toString());
        return user;
    }

    @Override
    public void showLoading() {
        dialog.show();
    }

    @Override
    public void onSuccess() {
        dialog.dismiss();
        Toast.makeText(this, "登录成功", Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
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
