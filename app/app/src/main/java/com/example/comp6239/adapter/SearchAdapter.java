package com.example.comp6239.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.comp6239.R;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends BaseAdapter implements Filterable {

    private Activity activity;
    private boolean chooseName;
    private List<ListViewData> list;
    private static LayoutInflater inflater=null;

    private MyFilter mFilter;
    private Object mLock = new Object();

    public SearchAdapter(Activity a, List<ListViewData> list, boolean chooseName) {
        this.activity = a;
        this.list = list;
        this.chooseName = chooseName;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return list.size();
    }
    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;
        if(convertView==null){
            view = inflater.inflate(R.layout.listview_student_main, null);
        }
        ListViewData data = (ListViewData) getItem(position);
        TextView tutorId =view.findViewById(R.id.tutor_id);
        TextView tutorName = view.findViewById(R.id.tutor_name);
        TextView tutorProfile = view.findViewById(R.id.tutor_profile);
        tutorId.setText(data.getArg1());
        tutorName.setText(data.getArg2());
        tutorProfile.setText(data.getArg3());
        return view;
    }

    @Override
    public Filter getFilter() {
        if(mFilter==null){
            mFilter = new MyFilter(list);
        }
        return mFilter;
    }

    class MyFilter extends Filter {

        private List<ListViewData> original;

        public MyFilter(List<ListViewData> original) {
            this.original = original;
        }

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if(prefix == null || prefix.length() == 0){
                    results.values = original;
                    results.count = original.size();
            } else{
                String prefixString = prefix.toString().toLowerCase();
                List<ListViewData> newValues = new ArrayList<>();
                if (chooseName) {
                for(ListViewData value : original){
                    String title = value.getArg2().toLowerCase();
                    if(title.contains(prefixString)){
                        newValues.add(value);
                    }
                }
                }else {
                    for(ListViewData value : original){
                        String title = value.getArg3().toLowerCase();
                        if(title.contains(prefixString)){
                            newValues.add(value);
                        }
                    }
                }
                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            list = (List<ListViewData>) results.values;
            if(results.count>0){
                notifyDataSetChanged();
            }else{
                notifyDataSetInvalidated();
            }
        }
    }
}
