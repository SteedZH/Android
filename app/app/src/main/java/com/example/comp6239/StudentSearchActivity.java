package com.example.comp6239;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comp6239.adapter.ListViewData;
import com.example.comp6239.adapter.SearchAdapter;
import com.example.comp6239.utility.GetDataFromPHP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StudentSearchActivity extends AppCompatActivity {

    private List<ListViewData> dataList = new ArrayList<>();
    private SearchView searchView;
    public static ListView listView;
    private RadioGroup radioGroup;
    private boolean chooseName =true;
    private SearchAdapter tutorListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_search);
        listView = findViewById(R.id.list_view_show_tutor);
        radioGroup = findViewById(R.id.search_radio_group);

        tutorListAdapter = new SearchAdapter(StudentSearchActivity.this, dataList, chooseName);
        listView.setAdapter(tutorListAdapter);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioLocation:
                        chooseName = false;
                        Toast.makeText(getApplicationContext(), "You checked Location", Toast.LENGTH_SHORT).show();
                        //Set Adapter
                        tutorListAdapter = new SearchAdapter(StudentSearchActivity.this, dataList, chooseName);
                        listView.setAdapter(tutorListAdapter);
                        break;
                    case R.id.radioName:
                        chooseName = true;
                        Toast.makeText(getApplicationContext(), "You checked Name", Toast.LENGTH_SHORT).show();
                        //Set Adapter
                        tutorListAdapter = new SearchAdapter(StudentSearchActivity.this, dataList, chooseName);
                        listView.setAdapter(tutorListAdapter);
                        break;
                    default:
                        //Set Adapter
                        tutorListAdapter = new SearchAdapter(StudentSearchActivity.this, dataList, true);
                        listView.setAdapter(tutorListAdapter);
                        break;
                }
            }
        });

        //Back Bar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        //To profile Activity
        Button bt_profile= findViewById(R.id.profile);
        bt_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(StudentSearchActivity.this,StudentProfileActivity.class);
                startActivity(intent);
                StudentSearchActivity.this.finish();
            }
        });

        //Get subject ID
        int subject_id ;
        subject_id = getIntent().getExtras().getInt("subject_id");
//        System.out.println(subject_id);
        initData("",subject_id, "");





        //SearchView Listener
        listView.setTextFilterEnabled(true);
        searchView = findViewById(R.id.search_view);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ListAdapter adapter = listView.getAdapter();
                if (adapter instanceof Filterable) {
                    Filter filter = ((Filterable)adapter).getFilter();
                    if(newText==null || newText.length()==0){
                        filter.filter(null);
                    }else{
                        filter.filter(newText);
                    }
                }
                return true;
            }
        });

        //To TutorDetail Activity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = view.findViewById(R.id.tutor_id);
                String tutor_id = textView.getText().toString();
                Intent intent = new Intent(getApplicationContext(), TutorDetailActivity.class);
                intent.putExtra("tutor_id",tutor_id);
                startActivity(intent);
            }
        });
    }



    //Back Bar
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, StudentMainActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                finish();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    //Init listView Data
    private void initData(String tutorName, int tutorSubject, String tutorLocation){
            String string = GetDataFromPHP.getTutor(tutorName,tutorSubject,tutorLocation);

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
