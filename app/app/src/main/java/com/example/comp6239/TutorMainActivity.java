package com.example.comp6239;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.comp6239.adapter.AppointmentListAdapter;
import com.example.comp6239.adapter.ListViewData;
import com.example.comp6239.utility.AppUser;
import com.example.comp6239.utility.GetDataFromPHP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TutorMainActivity extends AppCompatActivity {

    private List<ListViewData> data_list = new ArrayList<>();
    private int id;

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
        id = AppUser.getUserId();
        id = 11;
        initData();
        final AppointmentListAdapter adapter=new AppointmentListAdapter(TutorMainActivity.this, R.layout.listview_tutor_main,data_list);
        final ListView listView=findViewById(R.id.tutor_main_list_view);
        listView.setAdapter(adapter);
    }

    private void initData(){

        String string = GetDataFromPHP.getAppointment(id);

        JSONObject jsonObject;
        JSONArray jsonArray;
        JSONObject info;
        try {
            jsonObject = new JSONObject(string);
            jsonArray = jsonObject.getJSONArray("appointments");
            for (int i = 0; i < jsonArray.length(); i++) {
                info = jsonArray.getJSONObject(i);
                data_list.add(new ListViewData(info.getString("first_name"),info.getString("start_time"),info.getString("end_time")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
