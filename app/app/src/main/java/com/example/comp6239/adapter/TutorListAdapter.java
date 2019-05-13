package com.example.comp6239.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.comp6239.R;

import java.util.List;

public class TutorListAdapter extends ArrayAdapter {
    private final int resourceId;

    public TutorListAdapter(Context context, int textViewResourceId, List<ListViewData> objects){
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListViewData listView = (ListViewData) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView tutorId =view.findViewById(R.id.tutor_id);
        TextView tutorName = view.findViewById(R.id.tutor_name);
        TextView tutorProfile = view.findViewById(R.id.tutor_profile);
        tutorId.setText(listView.getArg1());
        tutorName.setText(listView.getArg2());
        tutorProfile.setText(listView.getArg3());
        return view;
    }
}
