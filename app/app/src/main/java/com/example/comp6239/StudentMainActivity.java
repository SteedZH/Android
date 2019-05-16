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
import android.widget.Toast;

import com.example.comp6239.utility.AppUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentMainActivity extends AppCompatActivity {

    private GridView gridView;
    private List<Map<String, Object>> dataList;
    private SimpleAdapter adapter = null;

    private int[] icons = {
            R.mipmap.picture0,
            R.mipmap.picture1,
            R.mipmap.picture2,
            R.mipmap.picture3,
            R.mipmap.picture4,
            R.mipmap.picture5,
            R.mipmap.picture6,
            R.mipmap.picture7,
            R.mipmap.picture8
    };
    private String[] text = {"All", "Math", "Computer", "Physics", "Chemistry", "Biology", "History", "Music", "Law"};

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);
//        ActionBar actionBar = getSupportActionBar();
//        assert actionBar != null;
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeButtonEnabled(true);



        //To profile Activity
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

        gridView = findViewById(R.id.student_needs_grid_view);
        dataList = new ArrayList<>();
        initData();

        String[] form = {"image", "text"};
        int[] to = {R.id.subject_image, R.id.subject_text};
        adapter = new SimpleAdapter(this, dataList, R.layout.gridview_subjects, form, to);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(StudentMainActivity.this, "你点击了第" + i, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), StudentSearchActivity.class);
                intent.putExtra("subject_id",i);

                startActivity(intent);
            }
        });
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
            Map<String, Object> map = new HashMap<>();
            map.put("image", icons[i]);
            map.put("text", text[i]);
            dataList.add(map);
        }
        return dataList;
    }
}