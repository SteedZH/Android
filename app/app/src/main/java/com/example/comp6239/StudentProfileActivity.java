package com.example.comp6239;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.comp6239.utility.AppUser;
import com.example.comp6239.utility.MySQL;

public class StudentProfileActivity extends AppCompatActivity {

    EditText editName;
    EditText editDob;
    EditText description;
    Button confirmButton;
    RadioButton radioMale;

    String name;
    String Dob;
    String describe;
    String gender;
    int stu_id;

    String result = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        editName = findViewById(R.id.NameEdit);
        editDob = findViewById(R.id.DobEdit);
        description = findViewById(R.id.Description);
        radioMale = findViewById(R.id.MaleButton);
        confirmButton = findViewById(R.id.button);
        stu_id = AppUser.getUserId();
        stu_id = 101;


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = editName.getText().toString();
                Dob = editDob.getText().toString();
                describe = description.getText().toString();
                if (radioMale.isChecked()) {
                    gender = "M";
                }
                else {
                    gender = "F";
                }
                result = MySQL.writeToSQL(name, Dob, gender, describe, Integer.toString(stu_id));
                if (result.equals("SUCCESS")) {
                    Toast.makeText(StudentProfileActivity.this, "Update successly! ", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                //Intent homeIntent = new Intent(this, StudentSearchActivity.class);
                //homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //startActivity(homeIntent);
                StudentProfileActivity.this.finish();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}