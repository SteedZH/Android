package com.example.comp6239.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.comp6239.R;

import java.util.List;

public class TutorDetailsAdapter extends ArrayAdapter {
    private final int resourceId;

    public TutorDetailsAdapter(Context context, int textViewResourceId, List<ListViewData> objects){
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewData listView = (ListViewData) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView text =view.findViewById(R.id.text);
        TextView details = view.findViewById(R.id.tutor_details);
        text.setText(listView.getArg1());
        details.setText(listView.getArg2());
        return view;
    }
}