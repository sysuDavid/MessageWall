package com.sysu.weijia.messagewall.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.sysu.weijia.messagewall.R;
import com.sysu.weijia.messagewall.model.entity.Subject;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by weijia on 16-1-14.
 */
public class SubjectListAdapter extends BaseAdapter{

    private Context context;
    private List<AVObject> subjectList;

    private TextView titleTextView;
    private TextView addressTextView;

    public SubjectListAdapter(Context c, List<AVObject> list) {
        context = c;
        subjectList = list;

    }

    @Override
    public int getCount() {
        return subjectList.size();
    }

    @Override
    public Subject getItem(int position) {
        return (Subject)subjectList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.subject_item, null);
            titleTextView = (TextView)convertView.findViewById(R.id.textView_title_subject);
            addressTextView = (TextView)convertView.findViewById(R.id.textView_address_subject);
        }
        Subject subject = (Subject)subjectList.get(position);
        titleTextView.setText(subject.getTitle());
        addressTextView.setText(subject.getAddress());
        return convertView;
    }
}
