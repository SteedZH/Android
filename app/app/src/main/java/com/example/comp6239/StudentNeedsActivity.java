package com.example.comp6239;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentNeedsActivity extends AppCompatActivity {

    private GridView gridView;
    private List<Map<String, Object>> dataList;
    private SimpleAdapter adapter = null;

    private int[] icons = {R.mipmap.picture1,
            R.mipmap.picture2,
            R.mipmap.picture3,
            R.mipmap.picture4,
            R.mipmap.picture5,
            R.mipmap.picture6};
    private String[] text = {"Computer", "Math", "Chemistry", "Biology", "Physics", "History"};

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_needs);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        gridView = findViewById(R.id.student_needs_grid_view);
        dataList = new ArrayList<>();
        initData();

        String[] form = {"image", "text"};
        int[] to = {R.id.subject_image, R.id.subject_text};
        adapter = new SimpleAdapter(this, dataList, R.layout.gridview_subjects, form, to);
        gridView.setAdapter(adapter);
    }

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