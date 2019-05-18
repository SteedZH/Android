package com.example.comp6239;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comp6239.adapter.ListViewData;
import com.example.comp6239.adapter.RequestListAdapter;
import com.example.comp6239.adapter.RequestTutorAdapter;
import com.example.comp6239.utility.AppUser;
import com.example.comp6239.utility.GetDataFromPHP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdminMainActivity extends AppCompatActivity {

    private List<Tutor> tutorRequests;
    private List<ListViewData> data_list = new ArrayList<>();
    private RequestTutorAdapter adapter;
    private ListView listView;
    private Button subject_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        subject_button = findViewById(R.id.subject_change);
        subject_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ManagerSubjectActivity.class);
                startActivity(intent);
            }
        });


        initData();
        adapter = new RequestTutorAdapter(getApplicationContext(), R.layout.listview_admin_main, data_list);
        listView = findViewById(R.id.tutor_application_list);
        listView.setAdapter(adapter);

        /*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = view.findViewById(R.id.request_tutor_id);
                String tutor_id = textView.getText().toString();
                Intent intent = new Intent(getApplicationContext(), TutorRequestDetailActivity.class);
                intent.putExtra("tutor_id",tutor_id);
                startActivity(intent);
            }
        });
        */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            new AlertDialog.Builder(AdminMainActivity.this)
                            .setTitle("Logout")
                            .setMessage("Do you want to logout from (" + AppUser.getUsername() + ")? ")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {

                                    Intent intent = new Intent(AdminMainActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);

                                    AppUser.logout();
                                    Toast.makeText(AdminMainActivity.this, "You have logout. ", Toast.LENGTH_LONG).show();

                                    finish();
                                }})
                            .setNegativeButton(android.R.string.no, null).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_admin_main, menu);
        MenuItem btnLogout = menu.findItem(R.id.menuLogout);
        btnLogout.setTitle(getResources().getString(R.string.action_logout) + " (" + AppUser.getUsername() + ")");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.menuManageCategory:
                Toast.makeText(this, "\"Manage Category\" selected", Toast.LENGTH_SHORT).show();
                break;
            // action with ID action_settings was selected
            case R.id.menuLogout:
                logout();
                break;
            default:
                break;
        }

        return true;
    }

    public void logout() {
        AppUser.logout();

        Intent intent = new Intent(AdminMainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void initData(){
        String string = GetDataFromPHP.getTutorRequest();

        JSONObject jsonObject;
        JSONArray jsonArray;
        JSONObject info;
        try {
            jsonObject = new JSONObject(string);
            jsonArray = jsonObject.getJSONArray("tutors");
            for (int i = 0; i < jsonArray.length(); i++) {
                info = jsonArray.getJSONObject(i);
                data_list.add(new ListViewData(info.getString("first_name"),info.getString("user_id"),info.getString("email"), info.getString("subject_id")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
