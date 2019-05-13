package com.example.comp6239;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.comp6239.adapter.ListViewData;
import com.example.comp6239.adapter.TutorDetailsAdapter;
import com.example.comp6239.utility.GetDataFromPHP;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TutorDetailActivity extends AppCompatActivity {

    private ArrayList<ListViewData> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_detail);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        String tutor_id = "";
        tutor_id = getIntent().getStringExtra("tutor_id");
//        Toast.makeText(getApplicationContext(), "receive  " + tutor_id + " ok", Toast.LENGTH_SHORT).show();
        initData(tutor_id);
        final TutorDetailsAdapter tutorDetailsAdapter = new TutorDetailsAdapter(TutorDetailActivity.this,R.layout.listview_tutor_details, dataList);
        final ListView listView = findViewById(R.id.tutor_details_listview);
        listView.setAdapter(tutorDetailsAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, StudentSearchActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(homeIntent);
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
