package com.example.comp6239;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
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

public class StudentMainActivity extends AppCompatActivity {

    private GridView gridView;
    private List<Map<String, Object>> dataList;
    private SimpleAdapter adapter = null;
    private List<Map<String, String>> subjectArray;

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
        setContentView(R.layout.activity_student_main);

        //To profile Activity
        /*
        Button bt_profile= findViewById(R.id.profile);
        bt_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(StudentMainActivity.this,StudentProfileActivity.class);
                startActivity(intent);
                //StudentMainActivity.this.finish();
            }
        });
        */


        gridView = findViewById(R.id.student_needs_grid_view);
        dataList = new ArrayList<>();
        subjectArray = new ArrayList<>();
        initSubjectData();
        initData();

        String[] from = {"image", "text"};
        int[] to = {R.id.subject_image, R.id.subject_text};
        adapter = new SimpleAdapter(this, dataList, R.layout.gridview_subjects, from, to);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView subject_text = view.findViewById(R.id.subject_text);
                String text = subject_text.getText().toString();
                for (int j = 0; j < subjectArray.size(); j++) {
                    if (text.equals(subjectArray.get(j).get("name"))) {
                        Intent intent = new Intent(getApplicationContext(), StudentSearchActivity.class);
                        intent.putExtra("subject_id",subjectArray.get(j).get("id"));
//                        String iii = subjectArray.get(j).get("id");
//                        String jjj = subjectArray.get(j).get("name");
//                        Toast.makeText(StudentMainActivity.this, "你点击了第" + + "subject is "+subjectArray.get(j).get("name"), Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_student_main, menu);
        MenuItem btnLogout = menu.findItem(R.id.menuLogout);
        btnLogout.setTitle(getResources().getString(R.string.action_logout) + " (" + AppUser.getUsername() + ")");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.menuUnReadMsg:
                intent = new Intent();
                intent.setClass(StudentMainActivity.this,StudentProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.menuMyProfile:
                intent = new Intent();
                intent.setClass(StudentMainActivity.this,StudentProfileActivity.class);
                startActivity(intent);
                break;
            // action with ID action_settings was selected
            case R.id.menuLogout:
                onBackPressed();
                break;
            default:
                break;
        }
        return true;
    }

    public void logout() {
        AppUser.logout();

        Intent intent = new Intent(StudentMainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
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

                    Intent intent = new Intent(StudentMainActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    AppUser.logout();
                    Toast.makeText(StudentMainActivity.this, "You have logout. ", Toast.LENGTH_LONG).show();

                    finish();
                }})
            .setNegativeButton(android.R.string.no, null).show();
    }

    private List<Map<String, Object>> initData() {
        for (int i = 0; i < icons.length; i++) {
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
        Map<String, String> temp = new HashMap<>();
        temp.put("id", "0");
        temp.put("name", "All");
        subjectArray.add(temp);
        return subjectArray;
    }
}