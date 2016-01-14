package com.sysu.weijia.messagewall.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.avos.avoscloud.AVObject;
import com.sysu.weijia.messagewall.R;
import com.sysu.weijia.messagewall.model.entity.Subject;
import com.sysu.weijia.messagewall.ui.activity.SubjectDetailActivity;
import com.sysu.weijia.messagewall.ui.adapter.SubjectListAdapter;

import java.util.List;

public class ListFragment extends Fragment {

    private ListView listView;
    private Context context;
    private List<AVObject> subjectList;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        listView = (ListView)view.findViewById(R.id.listView_subject);
        listView.setAdapter(new SubjectListAdapter(context, subjectList));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Subject subject = (Subject)parent.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.setClass(context, SubjectDetailActivity.class);
                intent.putExtra("subjectId", subject.getObjectId());
                startActivity(intent);
            }
        });
        return view;

    }

    public void setData(List<AVObject> list){
        subjectList = list;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
