package com.example.comp6239;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Button login = findViewById(R.id.button2);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(TestActivity.this, LoginActivity.class);
                startActivity(intent);
                TestActivity.this.finish();
            }
        });
        Button tutor = findViewById(R.id.button9);
        tutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(TestActivity.this, StudentMainActivity.class);
                startActivity(intent);
                TestActivity.this.finish();
            }
        });
        Button student = findViewById(R.id.button10);
        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(TestActivity.this, TutorMainActivity.class);
                startActivity(intent);
                TestActivity.this.finish();
            }
        });

        Button admin = findViewById(R.id.button11);
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(TestActivity.this, AdminMainActivity.class);
                startActivity(intent);
                TestActivity.this.finish();
            }
        });

        Button Test = findViewById(R.id.button12);
        Test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(TestActivity.this, StudentChatActivity.class);
                startActivity(intent);
                TestActivity.this.finish();
            }
        });
    }
}
