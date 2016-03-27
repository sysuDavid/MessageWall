package com.sysu.weijia.messagewall.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sysu.weijia.messagewall.R;
import com.sysu.weijia.messagewall.model.entity.Message;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by weijia on 16-1-10.
 */
public class MessageListAdapter extends BaseAdapter {

    private Context context;
    private List<Message> messageList;
    private TextView nicknameTextView;
    private TextView timeTextView;
    private TextView contentTextView;
    private RelativeLayout likeLayout;
    private TextView likeNumTextView;
    public MessageListAdapter(Context c, List<Message> list) {
        context = c;
        messageList = list;
    }
    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Message getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.message_item, null);
            nicknameTextView = (TextView)convertView.findViewById(R.id.textView_nickname);
            timeTextView = (TextView)convertView.findViewById(R.id.textView_time);
            contentTextView = (TextView)convertView.findViewById(R.id.texiView_content);
            likeLayout = (RelativeLayout)convertView.findViewById(R.id.layout_like);
            likeNumTextView = (TextView)convertView.findViewById(R.id.textView_likeNum);
        //}
        Message message = messageList.get(position);
        nicknameTextView.setText(message.getUser().getNickname());
        String dateString;
        Date date = message.getCreatedAt();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateString = dateFormat.format(date);
        timeTextView.setText(dateString);
        contentTextView.setText(message.getContent());
        int likeNum = message.getLikeNum();
        likeNumTextView.setText(String.valueOf(likeNum));
        if (likeNum != 0) {
            likeLayout.setVisibility(View.VISIBLE);
        } else {
            likeLayout.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }


}
