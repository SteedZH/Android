package com.example.comp6239;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.comp6239.listview_tutor.ListAdapter;
import com.example.comp6239.listview_tutor.ListViewData;

import java.util.ArrayList;
import java.util.List;

public class TutorMainActivity extends AppCompatActivity {

    private List<ListViewData> data_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_main);
        Button unread = findViewById(R.id.tutor_main_unread);
        unread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(TutorMainActivity.this,TutorRequestActivity.class);
                startActivity(intent);
                TutorMainActivity.this.finish();
            }
        });
        initData();
        ListAdapter adapter=new ListAdapter(TutorMainActivity.this, R.layout.listview_tutor_main,data_list);
        ListView listView=findViewById(R.id.tutor_main_list_view);
        listView.setAdapter(adapter);
    }

    private void initData(){
        data_list.add(new ListViewData("student1","2019-08-01 00:00:00","2019-08-01 00:01:00"));
        data_list.add(new ListViewData("student2","2019-08-02 00:00:00","2019-08-02 00:01:00"));
    }
}
