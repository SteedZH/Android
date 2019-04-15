package com.example.comp6239;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StudentNeedsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_needs);
        Button bt_needs_back = findViewById(R.id.needs_back);
        bt_needs_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(StudentNeedsActivity.this,StudentMainActivity.class);
                startActivity(intent);
                StudentNeedsActivity.this.finish();
            }
        });
    }
}
