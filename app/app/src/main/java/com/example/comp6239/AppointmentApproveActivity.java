package com.example.comp6239;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.comp6239.adapter.ListViewData;
import com.example.comp6239.adapter.TutorDetailsAdapter;
import com.example.comp6239.utility.AppUser;
import com.example.comp6239.utility.GetDataFromPHP;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AppointmentApproveActivity extends AppCompatActivity {

    private Button button;
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

        button = findViewById(R.id.to_chat_with_student);
        listView = findViewById(R.id.student_details_listview);

        Intent intent = getIntent();
        appointment_id = intent.getStringExtra("appointment_id");

        initData(appointment_id);

        tutor_id = AppUser.getUserId();
        tutor_id = 11;
        button.setOnClickListener(new View.OnClickListener() {
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

        tutorDetailsAdapter = new TutorDetailsAdapter(getApplicationContext(),R.layout.listview_tutor_details, dataList);
        listView.setAdapter(tutorDetailsAdapter);
    }

    private void initData(String appointment_id){
        String string = GetDataFromPHP.getOneAppointment(Integer.parseInt(appointment_id));

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
}
