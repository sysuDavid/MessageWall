package com.sysu.weijia.messagewall.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVRelation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.sysu.weijia.messagewall.R;
import com.sysu.weijia.messagewall.model.entity.Message;
import com.sysu.weijia.messagewall.model.entity.Subject;
import com.sysu.weijia.messagewall.model.entity.User;
import com.sysu.weijia.messagewall.presenter.MessagePresenter;
import com.sysu.weijia.messagewall.presenter.impl.MessagePresenterImpl;
import com.sysu.weijia.messagewall.ui.adapter.MessageListAdapter;
import com.sysu.weijia.messagewall.ui.view.MessageView;

import java.util.List;

public class SubjectDetailActivity extends AppCompatActivity implements MessageView {

    private TextView titleTextView;
    private TextView addressTextView;
    private TextView introductionTextView;
    private TextView messageEmptyTextView;
    private EditText messageEditText;
    private Button addMessageButton;
    private ListView listView;

    private Context context;

    private MessagePresenter mMessagePresenter;

    private String subjectId;
    private String currentUserId;
    private Subject mSubject;
    private User mUser;

    private List<Message> messagesList = null;

    private List<Message> currentUserLikeMessages = null;
    private boolean userLikesTheMessage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_detail);
        context = this;
        mMessagePresenter = new MessagePresenterImpl(this);
        initView();
        // 设置toolbar返回
        setToolBarBack();
        // 从传进来的intent获取subjectId
        subjectId = getSubjectIdFromIntent();
        currentUserId = AVUser.getCurrentUser().getObjectId();
        getSubjectById();

        // 设置button事件
        addMessageButton.setOnClickListener(new AddMessageButtonClickListener());
    }

    public void initView() {
        titleTextView = (TextView)findViewById(R.id.textView_title_detail);
        addressTextView = (TextView)findViewById(R.id.textView_address_detail);
        introductionTextView = (TextView)findViewById(R.id.textView_introduction_detail);
        messageEmptyTextView = (TextView)findViewById(R.id.textView_message_empty);
        messageEditText = (EditText)findViewById(R.id.editText_message);
        addMessageButton = (Button)findViewById(R.id.button_add_message);
        listView = (ListView)findViewById(R.id.listView_message);
        registerForContextMenu(listView);
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
            if (messageEditText.getText().toString().isEmpty()) {
                Toast.makeText(context, "留言不能为空", Toast.LENGTH_LONG).show();
            } else {
                mMessagePresenter.addMessage();
                messageEditText.clearFocus();
            }
        }
    }

    @Override
    public Message newMessage() {
        Message message = new Message();
        message.setContent(messageEditText.getText().toString());
        message.setSubjectByObjectId(subjectId);
        message.setUserByObjectId(currentUserId);
        message.putLikeNum();
        return message;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        userLikesTheMessage = false;
        if (v.getId() == R.id.listView_message) {
            ListView lv = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo)menuInfo;
            Message message = (Message) lv.getItemAtPosition(acmi.position);
            if (message.getUser().getObjectId().equals(currentUserId)) {
                menu.add(0, 1, 0, "删除留言");
            } else {
                if (currentUserLikeMessages != null && currentUserLikeMessages.contains(message)) {
                    menu.add(0, 1, 0, "取消赞");
                } else {
                    menu.add(0, 1, 0, "赞留言");
                }
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        Message message = messagesList.get(menuInfo.position);
        String itemTitle = item.getTitle().toString();
        if (itemTitle.equals("赞留言")) {
            mMessagePresenter.likeMessage(message);
        }
        if (itemTitle.equals("取消赞")) {
            mMessagePresenter.withdrawLikeMessage(message);
        }
        if (itemTitle.equals("删除留言")) {
            mMessagePresenter.deleteMessage(message);
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onUploadMessageSuccess() {
        messageEditText.setText("");
        getMessageOfSubject();
    }

    @Override
    public void onUploadMessageError(String error) {
        Log.i("yuan", "add message error: " + error);
    }

    @Override
    public void getSubjectById() {
        mMessagePresenter.getSubjectById(subjectId);
    }

    @Override
    public void onGetSubjectSuccess(Subject subject) {
        mSubject = subject;
        // 设置每个view的值
        setView();
        getCurrentUserLikeMessages();

    }

    @Override
    public void onGetSubjectError(String error) {
        Log.i("yuan", "OnGetSubjectError: " + error);
    }

    @Override
    public void getUserById() {
        mMessagePresenter.getUserById(currentUserId);
    }

    @Override
    public void onGetUserSuccess(User user) {
        mUser = user;
    }

    @Override
    public void onGetUserError(String error) {
        Log.i("yuan", "OnGetUserError: " + error);
    }

    public void getCurrentUserLikeMessages() {
        // 获取当前用户赞过的留言列表
        AVUser user = AVUser.getCurrentUser();
        AVRelation<Message> relation = user.getRelation("likes");
        relation.getQuery().findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> list, AVException e) {
                if (e == null) {
                    currentUserLikeMessages = list;
                    getMessageOfSubject();
                } else {
                    Log.i("yuan", "get current user like messages error: " + e.getMessage());
                }
            }
        });
    }
    @Override
    public void getMessageOfSubject() {
        mMessagePresenter.getMessageOfSubject(mSubject);
    }

    @Override
    public void onGetMessageSuccess(List<Message> list) {
        messagesList = list;
        // 设置listView
        if (messagesList.size() != 0) {
            Log.i("yuan", "have messages");
            messageEmptyTextView.setVisibility(View.INVISIBLE);
            listView.setAdapter(new MessageListAdapter(this, messagesList));
        } else {
            Log.i("yuan", "no messages");
            messageEmptyTextView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onGetMessageError(String error) {
        Log.i("yuan", "onGetMessageError: " + error);
    }

    @Override
    public void onLikeMessageSuccess() {
        getCurrentUserLikeMessages();
    }

    @Override
    public void onLikeMessageError(String error) {
        Log.i("yuan", "onLikeMessageError: " + error);
    }

    @Override
    public void onWithdrawLikeMessageSuccess() {
        getCurrentUserLikeMessages();
    }

    @Override
    public void onWithdrawLikeMessageError(String error) {
        Log.i("yuan", "onWithdrawLikeMessageError: " + error);
    }

    @Override
    public void onDeleteMessageSuccess() {
        getMessageOfSubject();
    }

    @Override
    public void onDeleteMessageError(String error) {
        Log.i("yuan", "onDeleteMessageError: " + error);
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
