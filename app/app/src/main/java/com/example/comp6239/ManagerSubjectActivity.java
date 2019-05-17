package com.example.comp6239;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comp6239.utility.AppUser;
import com.example.comp6239.utility.GetDataFromPHP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagerSubjectActivity extends AppCompatActivity {

    private GridView gridView;
    private List<Map<String, Object>> dataList;
    private SimpleAdapter adapter = null;
    private List<Map<String, String>> subjectArray;
    private TextView textView_id;
    private TextView textView_name;
    private Button add_button;


    private int[] icons = {
            R.mipmap.all,
            R.mipmap.math,
            R.mipmap.computer,
            R.mipmap.physics,
            R.mipmap.chemistry,
            R.mipmap.biology,
            R.mipmap.history,
            R.mipmap.music,
            R.mipmap.law
    };
    private String[] text = {"All", "Math", "Computer", "Physics", "Chemistry", "Biology", "History", "Music", "Law"};

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_subject);
        textView_id = findViewById(R.id.add_subject_id);
        textView_name = findViewById(R.id.add_subject_name);
        add_button = findViewById(R.id.subject_add_update);


        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        gridView = findViewById(R.id.manager_grid_view);
        dataList = new ArrayList<>();
        subjectArray = new ArrayList<>();
        initSubjectData();
        initData();

        String[] from = {"image", "text"};
        int[] to = {R.id.subject_image, R.id.subject_text};
        adapter = new SimpleAdapter(this, dataList, R.layout.gridview_subjects, from, to);
        gridView.setAdapter(adapter);

        gridView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                TextView subject_text = v.findViewById(R.id.subject_text);
                String text = subject_text.getText().toString();
                for (int j = 0; j < subjectArray.size(); j++) {
                    if (text.equals(subjectArray.get(j).get("name"))) {
                        GetDataFromPHP.deleteSubject(subjectArray.get(j).get("id"));
                    }
                }
                return true;
            }
        });

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textView_id.getText().toString().equals("")) {
                    GetDataFromPHP.addSubject(textView_name.getText().toString());
                    Toast.makeText(getApplicationContext(), "You add subject" + textView_name.getText().toString() + "successfully!", Toast.LENGTH_LONG).show();
                }else {
                    GetDataFromPHP.updateSubject(textView_id.getText().toString(), textView_name.getText().toString());
                    Toast.makeText(getApplicationContext(), "You update subject" + textView_name.getText().toString() + "successfully!", Toast.LENGTH_LONG).show();
                }
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView subject_text = view.findViewById(R.id.subject_text);
                String text = subject_text.getText().toString();
                for (int j = 0; j < subjectArray.size(); j++) {
                    if (text.equals(subjectArray.get(j).get("name"))) {
                        String str = GetDataFromPHP.deleteSubject(subjectArray.get(j).get("id"));
                        try {
                            JSONObject jsonObject = new JSONObject(str);
                            if (jsonObject.getString("result").equals("SUCCESS")) {
                                Toast.makeText(ManagerSubjectActivity.this, "Delete successfully!", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(ManagerSubjectActivity.this, "System also contain tutor of this subject.", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
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

    //Back Bar
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Do you want to logout from (" + AppUser.getUsername() + ")? ")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        AppUser.logout();
                        Toast.makeText(getApplicationContext(), "You have logout. ", Toast.LENGTH_LONG).show();

                        finish();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private List<Map<String, Object>> initData() {
        for (int i = 1; i < icons.length; i++) {
            for (int j = 0; j<subjectArray.size(); j++) {
                if (text[i].equals(subjectArray.get(j).get("name"))) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("image", icons[i]);
                    map.put("text", text[i]);
                    dataList.add(map);
                    break;
                }
            }
        }
        return dataList;
    }

    private List<Map<String, String>> initSubjectData(){
        String string = GetDataFromPHP.getSubjects();

        JSONObject jsonObject;
        JSONArray jsonArray;
        JSONObject info;
        try {
            jsonObject = new JSONObject(string);
            jsonArray = jsonObject.getJSONArray("subjects");
            for (int i = 0; i < jsonArray.length(); i++) {
                info = jsonArray.getJSONObject(i);
                Map<String, String> subject = new HashMap<>();
                subject.put("id", info.getString("subject_id"));
                subject.put("name", info.getString("name"));
                subjectArray.add(subject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return subjectArray;
    }


}