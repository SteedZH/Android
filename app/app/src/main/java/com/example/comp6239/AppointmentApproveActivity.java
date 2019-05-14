package com.example.comp6239;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.comp6239.adapter.ListViewData;
import com.example.comp6239.adapter.TutorDetailsAdapter;
import com.example.comp6239.utility.AppUser;
import com.example.comp6239.utility.GetDataFromPHP;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AppointmentApproveActivity extends AppCompatActivity {

    private Button chat_button;
    private Button approve_button;
    private ArrayList<ListViewData> dataList = new ArrayList<>();
    private String student_id;
    private int tutor_id;
    private String appointment_id;
    private TutorDetailsAdapter tutorDetailsAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_approve);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        chat_button = findViewById(R.id.to_chat_with_student);
        approve_button = findViewById(R.id.to_approve);
        listView = findViewById(R.id.student_details_listview);

        Intent intent = getIntent();
        appointment_id = intent.getStringExtra("appointment_id");

        initData(appointment_id);

        tutor_id = AppUser.getUserId();
        tutor_id = 11;
        chat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TutorChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("counterpart_id", student_id);
                bundle.putString("my_id", String.valueOf(tutor_id));
                bundle.putString("appointment_id", appointment_id);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
                finish();
            }
        });

        approve_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String res = GetDataFromPHP.approveAppointment(Integer.parseInt(appointment_id));
                String result = null;
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    result = jsonObject.getString("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (result.equals("SUCCESS")) {
                    Toast.makeText(getApplicationContext(),"The request accepted successfully, you could check it in appointment page.", Toast.LENGTH_LONG).show();
                    Intent homeIntent = new Intent(getApplicationContext(), TutorRequestActivity.class);
                    homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homeIntent);
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"Sorry, this request is invalid.", Toast.LENGTH_LONG).show();
                }
            }
        });

        tutorDetailsAdapter = new TutorDetailsAdapter(getApplicationContext(),R.layout.listview_tutor_details, dataList);
        listView.setAdapter(tutorDetailsAdapter);
    }

    private void initData(String appointment_id){
        String string = GetDataFromPHP.getRequestDetails(Integer.parseInt(appointment_id));

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(string);
            student_id = jsonObject.getString("student_user_id");
            dataList.add(new ListViewData("FirstName: ",jsonObject.getString("first_name")));
            dataList.add(new ListViewData("LastName: ",jsonObject.getString("last_name")));
            dataList.add(new ListViewData("Date of Birth: ",jsonObject.getString("dob")));
            dataList.add(new ListViewData("Gender:  ",jsonObject.getString("gender")));
            dataList.add(new ListViewData("StartTime: ",jsonObject.getString("start_time")));
            dataList.add(new ListViewData("EndTime: ",jsonObject.getString("end_time")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, TutorRequestActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                finish();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}
