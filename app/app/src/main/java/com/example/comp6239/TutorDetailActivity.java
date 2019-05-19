package com.example.comp6239;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comp6239.adapter.ListViewData;
import com.example.comp6239.adapter.TutorDetailsAdapter;
import com.example.comp6239.utility.AppUser;
import com.example.comp6239.utility.GetDataFromPHP;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TutorDetailActivity extends AppCompatActivity {

    private ArrayList<ListViewData> dataList = new ArrayList<>();
    private Button book_button;
    private Button chat_button;
    private TextView start_text;
    private TextView end_text;
    private ListView listView;
    private String tutor_id;
    private int student_id;
    private TutorDetailsAdapter tutorDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Init
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_detail);
        book_button = findViewById(R.id.to_approve);
        chat_button = findViewById(R.id.to_chat);
        start_text = findViewById(R.id.time_slot_from);
        end_text = findViewById(R.id.time_slot_to);
        student_id = AppUser.getUserId();
        listView = findViewById(R.id.tutor_request_details_listview);
        tutor_id = "";

        //BackTool Bar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        //Get tutor_id from last activity and flash paper
        tutor_id = getIntent().getStringExtra("tutor_id");
//        Toast.makeText(getApplicationContext(), "receive  " + tutor_id + " ok", Toast.LENGTH_SHORT).show();
        initData(tutor_id);
        tutorDetailsAdapter = new TutorDetailsAdapter(TutorDetailActivity.this,R.layout.listview_tutor_details, dataList);
        listView.setAdapter(tutorDetailsAdapter);

        //Book Slot
        book_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = GetDataFromPHP.setAppointmentRequest(student_id, Integer.parseInt(tutor_id), start_text.getText().toString(), end_text.getText().toString());
                String res_string="";
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    res_string = jsonObject.getString("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (res_string.equals("SUCCESS")) {
                    Toast.makeText(getApplicationContext(), "You Have Book A Slot Successfully", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Please Check Your Input Format", Toast.LENGTH_LONG).show();
                }
            }
        });


        chat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StudentChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("counterpart_id", String.valueOf(tutor_id));
                bundle.putString("my_id", String.valueOf(student_id));
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });
    }

    //Back Tool Bar settings
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                //Intent homeIntent = new Intent(this, StudentSearchActivity.class);
                //homeIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                //startActivity(homeIntent);
                finish();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }


    private void initData(String tutor_id){
        String string = GetDataFromPHP.getTutorDetails(Integer.parseInt(tutor_id));

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(string);
            dataList.add(new ListViewData("FirstName: ",jsonObject.getString("first_name")));
            dataList.add(new ListViewData("LastName: ",jsonObject.getString("last_name")));
            dataList.add(new ListViewData("Date of Birth: ",jsonObject.getString("dob")));
            dataList.add(new ListViewData("Gender:  ",jsonObject.getString("gender")));
            dataList.add(new ListViewData("Postcode: ",jsonObject.getString("postcode")));
            dataList.add(new ListViewData("address: ",jsonObject.getString("address")));
            dataList.add(new ListViewData("Education: ",jsonObject.getString("educations")));
            dataList.add(new ListViewData("Price: ","£" + jsonObject.getString("price") + " per hour"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
