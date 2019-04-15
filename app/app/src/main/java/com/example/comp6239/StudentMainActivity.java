package com.example.comp6239;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StudentMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);
        Button bt_needs = findViewById(R.id.needs);
        bt_needs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(StudentMainActivity.this,StudentNeedsActivity.class);
                startActivity(intent);
                StudentMainActivity.this.finish();
            }
        });
        Button bt_profile= findViewById(R.id.profile);
        bt_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(StudentMainActivity.this,StudentProfileActivity.class);
                startActivity(intent);
                StudentMainActivity.this.finish();
            }
        });
    }

}
