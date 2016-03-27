package com.sysu.weijia.messagewall.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;
import com.sysu.weijia.messagewall.R;
import com.sysu.weijia.messagewall.ui.activity.SubjectDetailActivity;

/**
 * Created by weijia on 16-1-10.
 */
public class MyInfoWindowAdapter implements AMap.InfoWindowAdapter {
    private Activity activity;
    String subjectId = null;
    public MyInfoWindowAdapter(Activity a) {
        activity = a;
    }
    @Override
    public View getInfoWindow(Marker marker) {
        if (marker.getObject() != null) {
            subjectId = (String)marker.getObject();
        } else {
            return null;
        }
        final View infoView = activity.getLayoutInflater().inflate(R.layout.info_window, null);

        String title = marker.getTitle();
        TextView titleTextView = (TextView)infoView.findViewById(R.id.textView_title);
        titleTextView.setText(title);
        Button button = (Button)infoView.findViewById(R.id.button_to_detail);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(activity, SubjectDetailActivity.class);
                intent.putExtra("subjectId", subjectId);
                activity.startActivity(intent);
            }
        });
        return infoView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
