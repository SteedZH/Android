package com.example.comp6239.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.comp6239.R;

import java.util.List;

public class AppointmentListAdapter extends ArrayAdapter {
    private final int resourceId;

    public AppointmentListAdapter(Context context, int textViewResourceId, List<ListViewData> objects){
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewData listView = (ListViewData) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView dataName = view.findViewById(R.id.list_student);
        TextView startTime = view.findViewById(R.id.start_time);
        TextView endTime = view.findViewById(R.id.end_time);
        dataName.setText(listView.getArg1());
        startTime.setText(listView.getArg2());
        endTime.setText(listView.getArg3());
        return view;
    }
}
