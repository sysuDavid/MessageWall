package com.sysu.weijia.messagewall.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.sysu.weijia.messagewall.R;
import com.sysu.weijia.messagewall.model.entity.Message;
import com.sysu.weijia.messagewall.model.entity.Subject;
import com.sysu.weijia.messagewall.model.entity.User;
import com.sysu.weijia.messagewall.presenter.MessagePresenter;
import com.sysu.weijia.messagewall.presenter.impl.MessagePresenterImpl;
import com.sysu.weijia.messagewall.ui.adapter.MessageListAdapter;
import com.sysu.weijia.messagewall.ui.view.MessageView;

public class SubjectDetailActivity extends AppCompatActivity implements MessageView {

    private TextView titleTextView;
    private TextView addressTextView;
    private TextView introductionTextView;
    private EditText messageEditText;
    private Button addMessageButton;
    private ListView listView;

    private Context context;

    private MessagePresenter mMessagePresenter;

    private String subjectId;
    private String currentUserId;
    private Subject mSubject;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_detail);
        // 设置刚进入的时候EditText的`软键盘不要弹出
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        context = this;
        mMessagePresenter = new MessagePresenterImpl(this);
        initView();
        // 设置toolbar返回
        setToolBarBack();
        // 从传进来的intent获取subjectId
        subjectId = getSubjectIdFromIntent();
        currentUserId = AVUser.getCurrentUser().getObjectId();
        getSubjectById();
        getUserById();
        // 设置listView
        listView.setAdapter(new MessageListAdapter(this));
        // 设置button事件
        addMessageButton.setOnClickListener(new AddMessageButtonClickListener());
    }

    public void initView() {
        titleTextView = (TextView)findViewById(R.id.textView_title_detail);
        addressTextView = (TextView)findViewById(R.id.textView_address_detail);
        introductionTextView = (TextView)findViewById(R.id.textView_introduction_detail);
        messageEditText = (EditText)findViewById(R.id.editText_message);
        addMessageButton = (Button)findViewById(R.id.button_add_message);
        listView = (ListView)findViewById(R.id.listView_message);
    }

    public void setToolBarBack() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_subject_detail);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    String getSubjectIdFromIntent() {
        String id;
        Intent intent = getIntent();
        id = intent.getStringExtra("subjectId");
        return id;
    }

    void setView() {
        if (mSubject!= null) {
            Log.i("yuan", "subject title: " + mSubject.getTitle());
            titleTextView.setText(mSubject.getTitle());
            addressTextView.setText(mSubject.getAddress());
            introductionTextView.setText(mSubject.getIntroduction());
        } else {
            Log.i("yuan", "subject null");
        }
    }
    class AddMessageButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public Message newMessage() {
        return null;
    }

    @Override
    public void getSubjectById() {
        mMessagePresenter.getSubjectById(subjectId);
    }

    @Override
    public void OnGetSubjectSuccess(Subject subject) {
        mSubject = subject;
        // 设置每个view的值
        setView();
    }

    @Override
    public void OnGetSubjectError(String error) {
        Log.i("yuan", "OnGetSubjectError: " + error);
    }

    @Override
    public void getUserById() {
        mMessagePresenter.getUserById(currentUserId);
    }

    @Override
    public void OnGetUserSuccess(User user) {
        mUser = user;
        Log.i("yuan", "user nickname: " + mUser.getNickname());
    }

    @Override
    public void OnGetUserError(String error) {
        Log.i("yuan", "OnGetUserError: " + error);
    }
}
