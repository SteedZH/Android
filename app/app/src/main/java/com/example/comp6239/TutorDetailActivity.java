package com.example.comp6239;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.comp6239.adapter.ListViewData;
import com.example.comp6239.adapter.TutorDetailsAdapter;
import com.example.comp6239.utility.AppUser;
import com.example.comp6239.utility.GetDataFromPHP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TutorDetailActivity extends AppCompatActivity {

    Calendar startC = Calendar.getInstance();
    Calendar endC = Calendar.getInstance();
    int startYear, startMonth, startDay, startHour, startMinute;
    int endYear, endMonth, endDay, endHour, endMinute;
    String startBook, endBook;

    private ArrayList<ListViewData> dataList = new ArrayList<>();
    private Button book_button;
    private Button chat_button;
    private TextView start_text;
    private TextView end_text;
    private ListView listView;
    private String tutor_id;
    private int student_id;
    private TutorDetailsAdapter tutorDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Init
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_detail);
        book_button = findViewById(R.id.to_approve);
        chat_button = findViewById(R.id.to_chat);
        //start_text = findViewById(R.id.time_slot_from);
        //end_text = findViewById(R.id.time_slot_to);
        student_id = AppUser.getUserId();
        listView = findViewById(R.id.tutor_request_details_listview);
        tutor_id = "";

        //BackTool Bar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        //Get tutor_id from last activity and flash paper
        tutor_id = getIntent().getStringExtra("tutor_id");
//        Toast.makeText(getApplicationContext(), "receive  " + tutor_id + " ok", Toast.LENGTH_SHORT).show();
        initData(tutor_id);
        tutorDetailsAdapter = new TutorDetailsAdapter(TutorDetailActivity.this,R.layout.listview_tutor_details, dataList);
        listView.setAdapter(tutorDetailsAdapter);

        //Book Slot
        book_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startC = Calendar.getInstance();
                endC = Calendar.getInstance();


                startYear = startC.get(Calendar.YEAR);
                startMonth = startC.get(Calendar.MONTH);
                startDay = startC.get(Calendar.DAY_OF_MONTH);
                startHour = startC.get(Calendar.HOUR_OF_DAY);
                startMinute = startC.get(Calendar.MINUTE);
                endYear = endC.get(Calendar.YEAR);
                endMonth = endC.get(Calendar.MONTH);
                endDay = endC.get(Calendar.DAY_OF_MONTH);
                endHour = endC.get(Calendar.HOUR_OF_DAY);
                endMinute = endC.get(Calendar.MINUTE);

                inputStartDay();
            }
        });


        chat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TutorDetailActivity.this, StudentChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("counterpart_id", String.valueOf(tutor_id));
                bundle.putString("my_id", String.valueOf(student_id));
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });
    }

    public boolean inputStartDay() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        startBook = (year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        inputStartTime();
                    }
                }, startYear, startMonth, startDay);
        datePickerDialog.setTitle("Select starting date of booking: ") ;
        datePickerDialog.show();
        return true;
    }

    public boolean inputStartTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        startBook += (" " + hourOfDay + ":" + minute);
                        inputEndDay();
                    }
                }, startHour, startMinute, false);
        timePickerDialog.setTitle("Select starting time of booking: ") ;
        timePickerDialog.show();
        return true;
    }

    public boolean inputEndDay() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        endBook = (year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        inputEndTime();
                    }
                }, endYear, endMonth, endDay);
        datePickerDialog.setTitle("Select ending date of booking: ") ;
        datePickerDialog.show();
        return true;
    }

    public boolean inputEndTime() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        endBook += (" " + hourOfDay + ":" + minute);
                        finalConfirmBooking();
                    }
                }, endHour, endMinute, false);
        timePickerDialog.setTitle("Select ending time of booking: ") ;
        timePickerDialog.show();
        return true;
    }

    public void finalConfirmBooking() {
        startC.set(startYear, startMonth, startDay, startHour, startMinute);
        endC.set(endYear, endMonth, endDay, endHour, endMinute);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date sd = new Date();
        Date ed = new Date();
        try{
            sd = sdf.parse(startBook);
            ed = sdf.parse(endBook);
        }catch (ParseException e){
            e.printStackTrace();
        }


        //if (sdf.format(startC.getTime()).equals(sdf.format(endC.getTime()))) {
        if (sd.compareTo(ed) < 0 && sd.compareTo(new Date()) > 0) {
            String string = GetDataFromPHP.setAppointmentRequest(student_id, Integer.parseInt(tutor_id), startBook+":00", endBook+":00");
            String res_string="";
            try {
                JSONObject jsonObject = new JSONObject(string);
                res_string = jsonObject.getString("result");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (res_string.equals("SUCCESS")) {
                Toast.makeText(getApplicationContext(), "You Have Book A Slot Successfully", Toast.LENGTH_LONG).show();
                new AlertDialog.Builder(TutorDetailActivity.this)
                        .setTitle("Booking")
                        .setMessage("Your booking request is submitted to the tutor and awaiting for approval. ")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                            }}).show();
            } else {
                Toast.makeText(getApplicationContext(), "Please Check Your Input Format", Toast.LENGTH_LONG).show();
                new AlertDialog.Builder(TutorDetailActivity.this)
                        .setTitle("Booking")
                        .setMessage("You have requested a time slot that is outside of the tutor\'s time preferences on that weekday. Your booking is rejected. ")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                            }}).show();
            }
        }else {
            new AlertDialog.Builder(TutorDetailActivity.this)
                    .setTitle("Booking")
                    .setMessage("The ending datetime of booking must be set after the starting datetime. The starting datetime must be set after today. ")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                        }}).show();
        }
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


    private void initData(String tutor_id){
        String string = GetDataFromPHP.getTutorDetails(Integer.parseInt(tutor_id));

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(string);
            dataList.add(new ListViewData("Username: ",jsonObject.getString("username") + " (" + jsonObject.getString("gender") + ")"));
            dataList.add(new ListViewData("Name: ",jsonObject.getString("first_name") + " " + jsonObject.getString("last_name")));
            dataList.add(new ListViewData("Date of Birth: ",jsonObject.getString("dob")));
            dataList.add(new ListViewData("address: ",jsonObject.getString("address") + " (" + jsonObject.getString("postcode") +")"));
            dataList.add(new ListViewData("Education: ",jsonObject.getString("educations")));
            dataList.add(new ListViewData("Price: ","£" + jsonObject.getString("price") + " per hour"));

            String tp="";
            JSONArray timePreferences = jsonObject.getJSONArray("daytime");
            for(int i=0; i < timePreferences.length(); i++){
                JSONObject myObj = timePreferences.getJSONObject(i);
                switch(myObj.optInt("weekday")){
                    case 0:
                        tp+="Sun";
                        break;
                    case 1:
                        tp+="Mon";
                        break;
                    case 2:
                        tp+="Tue";
                        break;
                    case 3:
                        tp+="Wed";
                        break;
                    case 4:
                        tp+="Apr";
                        break;
                    case 5:
                        tp+="Fri";
                        break;
                    case 6:
                        tp+="Sat";
                        break;
                    default:
                        tp+="Bank Holiday";
                        break;
                }
                switch(myObj.optInt("datetime")){
                    case 1:
                        tp+="(AM), ";
                        break;
                    case 2:
                        tp+="(PM), ";
                        break;
                    default:
                        tp+="(AM+PM), ";
                        break;
                }
            }

            dataList.add(new ListViewData("Time Preferences: ",tp));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void refresh() {
        finish();
        startActivity(getIntent());
    }
}
