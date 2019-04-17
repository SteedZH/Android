package com.example.comp6239;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.GridView;

import com.example.comp6239.utility.ListAdapter2;
import com.example.comp6239.utility.ListViewData;

import java.util.ArrayList;
import java.util.List;

public class StudentNeedsActivity extends AppCompatActivity {

    private List<ListViewData> data_list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_needs);
//        Button bt_needs_back = findViewById(R.id.needs_back);
//        bt_needs_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(StudentNeedsActivity.this,StudentMainActivity.class);
//                startActivity(intent);
//                StudentNeedsActivity.this.finish();
//            }
//        });
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        initData();
        ListAdapter2 adapter=new ListAdapter2(StudentNeedsActivity.this, R.layout.listview_student_needs,data_list);
        GridView gridView=findViewById(R.id.student_needs_grid_view);
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


    private void initData(){
        data_list.add(new ListViewData("math"));
        data_list.add(new ListViewData("English"));
    }
}