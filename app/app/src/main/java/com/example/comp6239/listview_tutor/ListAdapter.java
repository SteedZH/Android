package com.example.comp6239.listview_tutor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.comp6239.R;

import java.util.List;

public class ListAdapter extends ArrayAdapter {
    private final int resourceId;

    public ListAdapter(Context context, int textViewResourceId, List<ListViewData> objects){
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
        dataName.setText(listView.getList_name());
        startTime.setText(listView.getStart_time());
        endTime.setText(listView.getEnd_time());
        return view;
    }
}
