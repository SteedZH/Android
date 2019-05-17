package com.example.comp6239;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.comp6239.adapter.ListViewData;
import com.example.comp6239.adapter.TutorDetailsAdapter;
import com.example.comp6239.utility.GetDataFromPHP;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TutorRequestDetailActivity extends AppCompatActivity {

    private ArrayList<ListViewData> dataList = new ArrayList<>();
    private Button approve_button;
    private ListView listView;
    private String tutor_id;
    private TutorDetailsAdapter tutorDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Init
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_tutor_detail);
        approve_button = findViewById(R.id.to_approve);
        listView = findViewById(R.id.tutor_request_details_listview);
        tutor_id = "";

        //BackTool Bar
        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        //Get tutor_id from last activity and flash paper
        tutor_id = getIntent().getStringExtra("tutor_id");
//        Toast.makeText(getApplicationContext(), "receive  " + tutor_id + " ok", Toast.LENGTH_SHORT).show();
        initData(tutor_id);
        tutorDetailsAdapter = new TutorDetailsAdapter(TutorRequestDetailActivity.this, R.layout.listview_tutor_details, dataList);
        listView.setAdapter(tutorDetailsAdapter);

        approve_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetDataFromPHP.updateTutorRequest(tutor_id);
                Toast.makeText(getApplicationContext(),"This tutor has been approved successfully!",Toast.LENGTH_LONG).show();
                finish();
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
