package com.example.comp6239;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comp6239.utility.AppUser;
import com.example.comp6239.utility.GetDataFromPHP;
import com.example.comp6239.utility.ChangeSubjectDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagerSubjectActivity extends AppCompatActivity{

    String[] from = {"image", "text"};
    int[] to = {R.id.subject_image, R.id.subject_text};

    private GridView gridView;
    private List<Map<String, Object>> dataList;
    private SimpleAdapter adapter = null;
    private List<Map<String, String>> subjectArray;
    private String subject_name;


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
        setContentView(R.layout.activity_manager_subject);


        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        gridView = findViewById(R.id.manager_grid_view);
        dataList = new ArrayList<>();
        subjectArray = new ArrayList<>();

        initSubjectData();
        initData();


        adapter = new SimpleAdapter(this, dataList, R.layout.gridview_subjects, from, to);
        gridView.setAdapter(adapter);

        gridView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                TextView subject_text = v.findViewById(R.id.subject_text);
                String text = subject_text.getText().toString();
                for (int j = 0; j < subjectArray.size(); j++) {
                    if (text.equals(subjectArray.get(j).get("name"))) {
                        GetDataFromPHP.deleteSubject(subjectArray.get(j).get("id"));
                    }
                }
                return true;
            }
        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView subject_text = view.findViewById(R.id.subject_text);
                subject_name = subject_text.getText().toString();
                new AlertDialog.Builder(ManagerSubjectActivity.this)
                        .setTitle("Amend Subject")
                        .setMessage("Please choose operation on (" + subject_name + ")? ")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                for (int j = 0; j < subjectArray.size(); j++) {
                                    if (subject_name.equals(subjectArray.get(j).get("name"))) {
                                        String str = GetDataFromPHP.deleteSubject(subjectArray.get(j).get("id"));
                                        try {
                                            JSONObject jsonObject = new JSONObject(str);
                                            if (jsonObject.getString("result").equals("SUCCESS")) {
                                                Toast.makeText(ManagerSubjectActivity.this, "Delete successfully!", Toast.LENGTH_SHORT).show();
                                                refresh();
                                            }else{
                                                Toast.makeText(ManagerSubjectActivity.this, "System also contain tutor of this subject.", Toast.LENGTH_SHORT).show();
                                                refresh();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }})
                        .setNegativeButton("Change", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                changeDialog();

                            }
                        })
                        .setNeutralButton("Cancel", null).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_admin_subject, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menuAddSubject:
                addDialog();
                break;
            case android.R.id.home:
                Intent homeIntent =new Intent();
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                homeIntent = new Intent(ManagerSubjectActivity.this, AdminMainActivity.class);
                startActivity(homeIntent);
                finish();
            default:
                break;
        }
        return true;
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

                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        AppUser.logout();
                        Toast.makeText(getApplicationContext(), "You have logout. ", Toast.LENGTH_LONG).show();

                        finish();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private List<Map<String, Object>> initData() {
        for (int i = 1; i < icons.length; i++) {
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
        return subjectArray;
    }

    public void changeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                ManagerSubjectActivity.this);
        // Get the layout inflater
        LayoutInflater inflater = ManagerSubjectActivity.this.getLayoutInflater();
        final View mView = inflater.inflate(R.layout.dialog_add_change, null);
        builder.setView(mView)
                .setTitle("Please input subject to substitute")
                // Add action buttons
                .setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                final EditText subject_editText = mView.findViewById(R.id.original_subject);
                                final String subject_text = subject_editText.getText().toString();
                                for (int j = 0; j < subjectArray.size(); j++) {
                                    if (subject_name.equals(subjectArray.get(j).get("name"))) {
                                        String str = GetDataFromPHP.updateSubject(subjectArray.get(j).get("id"), subject_text);
                                        refresh();
                                    }
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        });
        // return builder.create();
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void addDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                ManagerSubjectActivity.this);
        // Get the layout inflater
        LayoutInflater inflater = ManagerSubjectActivity.this.getLayoutInflater();
        final View mView = inflater.inflate(R.layout.dialog_add_change, null);
        builder.setView(mView)
                .setTitle("Please input subject to add")
                // Add action buttons
                .setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                final EditText subject_editText = mView.findViewById(R.id.original_subject);
                                final String subject_text = subject_editText.getText().toString();
                                        String str = GetDataFromPHP.addSubject(subject_text);
                                        refresh();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        });
        // return builder.create();
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
//    public void showNoticeDialog() {
//        // Create an instance of the dialog fragment and show it
//        DialogFragment dialog = new ChangeSubjectDialog();
//        dialog.show(getSupportFragmentManager(), "ChangeSubjectDialog");
//    }
//
//    // The dialog fragment receives a reference to this Activity through the
//    // Fragment.onAttach() callback, which it uses to call the following methods
//    // defined by the NoticeDialogFragment.NoticeDialogListener interface
//    @Override
//    public void onDialogPositiveClick(DialogFragment dialog) {
//        EditText origin_subject = dialog.getView().findViewById(R.id.original_subject);
//        String origin_text = origin_subject.getText().toString();
//        // User touched the dialog's positive button
//        for (int j = 0; j < subjectArray.size(); j++) {
//            if (subject_name.equals(subjectArray.get(j).get("name"))) {
//                String str = GetDataFromPHP.updateSubject(subjectArray.get(j).get("id"), origin_text);
//            }
//        }
//    }
//
//    @Override
//    public void onDialogNegativeClick(DialogFragment dialog) {
//        // User touched the dialog's negative button
//        Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
//    }

    public void refresh() {
        finish();
        startActivity(getIntent());
    }
}
