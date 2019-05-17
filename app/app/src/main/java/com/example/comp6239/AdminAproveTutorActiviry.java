package com.example.comp6239;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.comp6239.adapter.ListViewData;
import com.example.comp6239.adapter.RequestListAdapter;
import com.example.comp6239.utility.AppUser;
import com.example.comp6239.utility.GetDataFromPHP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

    public class AdminAproveTutorActiviry extends AppCompatActivity {

        private List<ListViewData> data_list = new ArrayList<>();
        private RequestListAdapter adapter;
        private ListView listView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_tutor_request);
            ActionBar actionBar = getSupportActionBar();
            assert actionBar != null;
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);

            initData();
            adapter = new RequestListAdapter(getApplicationContext(), R.layout.listview_tutor_main, data_list);
            listView = findViewById(R.id.requestListView);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView textView = view.findViewById(R.id.appointment_id);

                    String appointment_id = textView.getText().toString();
                    Intent intent = new Intent();
                    intent.putExtra("appointment_id", appointment_id);
                    startActivity(intent);
                    finish();
                }
            });
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case android.R.id.home:
                    Intent homeIntent = new Intent(this, TutorMainActivity.class);
                    homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homeIntent);
                    finish();
                    return true;
            }
                return (super.onOptionsItemSelected(menuItem));
        }

        private void initData() {
            String string = GetDataFromPHP.getTutorRequest();

            JSONObject jsonObject;
            JSONArray jsonArray;
            JSONObject info;
            try {
                jsonObject = new JSONObject(string);
                jsonArray = jsonObject.getJSONArray("tutors");
                for (int i = 0; i < jsonArray.length(); i++) {
                    info = jsonArray.getJSONObject(i);
                    data_list.add(new ListViewData(info.getString("first_name"), info.getString("user_id"), info.getString("email"), info.getString("appointment_id")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }