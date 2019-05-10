package com.example.comp6239;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.comp6239.listview.ListViewData;
import com.example.comp6239.listview.TutorListAdapter;
import com.example.comp6239.utility.GetDataFromPHP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StudentMainActivity extends AppCompatActivity {

    private List<ListViewData> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);
        Button bt_needs = findViewById(R.id.needs);
        bt_needs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(StudentMainActivity.this,StudentNeedsActivity.class);
                startActivity(intent);
                StudentMainActivity.this.finish();
            }
        });
        Button bt_profile= findViewById(R.id.profile);
        bt_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(StudentMainActivity.this,StudentProfileActivity.class);
                startActivity(intent);
                StudentMainActivity.this.finish();
            }
        });


        initData();
        final TutorListAdapter tutorListAdapter = new TutorListAdapter(StudentMainActivity.this, R.layout.listview_student_main, dataList);
        final ListView listView = findViewById(R.id.list_view_show_tutor);
        listView.setAdapter(tutorListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = view.findViewById(R.id.tutor_id);
                String tutor_id = textView.getText().toString();
//                Toast.makeText(getApplicationContext(),"get context "+tutor_id+" ok" ,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), TutorDetailActivity.class);
                intent.putExtra("tutor_id",tutor_id);
                startActivity(intent);
            }
        });
    }
        private void initData(){
            String string = GetDataFromPHP.getTutor("",0,"");

            JSONObject jsonObject;
            JSONArray jsonArray;
            JSONObject info;
            try {
                jsonObject = new JSONObject(string);
                jsonArray = jsonObject.getJSONArray("tutors");
                for (int i = 0; i < jsonArray.length(); i++) {
                    info = jsonArray.getJSONObject(i);
                    dataList.add(new ListViewData(info.getString("user_id"),info.getString("first_name"),info.getString("postcode")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


}
