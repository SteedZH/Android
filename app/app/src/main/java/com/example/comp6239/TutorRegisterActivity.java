package com.example.comp6239;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comp6239.utility.AppConfigs;
import com.example.comp6239.utility.GetDataFromPHP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TutorRegisterActivity extends AppCompatActivity {
    private int mYear, mMonth, mDay;
    private List<Map<String, String>> subjectArray;

    AutoCompleteTextView edtFirstname, edtLastname, edtEmail, edtDOB;
    EditText edtUsername, edtPw, edtConfirm, edtPostcode, edtAddress, edtEducation;

    RadioGroup rdoGender;
    Spinner spnSubjects;

    Button btnConfirm;

    ProgressBar progressBar;

    private TutorRegisterActivity.TutorRegisterTask mAuthTask = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_register);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        btnConfirm = (Button)findViewById(R.id.username_sign_in_button);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        edtUsername = (EditText)findViewById(R.id.username);
        edtFirstname = (AutoCompleteTextView)findViewById(R.id.first_name);
        edtLastname = (AutoCompleteTextView)findViewById(R.id.last_name);

        edtEmail = (AutoCompleteTextView)findViewById(R.id.email);
        edtPw = (EditText) findViewById(R.id.password);
        edtConfirm = (EditText) findViewById(R.id.confirm_password);

        edtPostcode = (EditText) findViewById(R.id.postcode);
        edtAddress = (EditText) findViewById(R.id.address);
        edtEducation = (EditText) findViewById(R.id.education);



        rdoGender = (RadioGroup) findViewById(R.id.gender);

        edtDOB = (AutoCompleteTextView)findViewById(R.id.dob);
        edtDOB.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final Calendar c = Calendar.getInstance();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        edtDOB.setFocusable(true);
                        edtDOB.requestFocus();
                        break;
                    case MotionEvent.ACTION_UP:
                        // Get Current Date

                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(TutorRegisterActivity.this,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                        edtDOB.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                        break;
                    default:
                        break;
                }
                return true;

            }
        });

        subjectArray = new ArrayList<>();
        spnSubjects = (Spinner)findViewById(R.id.subject);
        initSubjectData();
        initData();

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                /*
                Intent homeIntent = new Intent(this, LoginActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                */
                finish();


        }
        return (super.onOptionsItemSelected(menuItem));
    }

    //Back Bar
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Registration")
                .setMessage("Are you sure to withdraw your registration of a student account and return to login page? ")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        finish();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        if (view.getId() == R.id.checkBox) {
            btnConfirm.setEnabled(checked);
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegister() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        edtUsername.setError(null);
        edtFirstname.setError(null);
        edtLastname.setError(null);

        edtEmail.setError(null);
        edtPw.setError(null);
        edtConfirm.setError(null);

        edtPostcode.setError(null);
        edtAddress.setError(null);
        edtEducation.setError(null);

        edtDOB.setError(null);

        // Store values at the time of the login attempt.
        String username = edtUsername.getText().toString();
        String firstname = edtFirstname.getText().toString();
        String lastname = edtLastname.getText().toString();
        String dob = edtDOB.getText().toString();
        String gender = ((RadioButton)findViewById(rdoGender.getCheckedRadioButtonId())).getText().toString();
        String email = edtEmail.getText().toString();
        String password = edtPw.getText().toString();
        String confirm_password = edtConfirm.getText().toString();

        int subject_id = Integer.parseInt(subjectArray.get(spnSubjects.getSelectedItemPosition()).get("id"));
        String education = edtEducation.getText().toString();
        String postcode = edtPostcode.getText().toString();
        String address = edtAddress.getText().toString();



        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            edtPw.setError(getString(R.string.error_invalid_password));
            focusView = edtPw;
            cancel = true;
        }

        // Check for password and confirm is matched.
        if (!password.equals(confirm_password)) {
            edtConfirm.setError(getString(R.string.error_invalid_confirm_password));
            focusView = edtConfirm;
            cancel = true;
        }

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            edtUsername.setError(getString(R.string.error_field_required));
            focusView = edtUsername;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            edtUsername.setError(getString(R.string.error_invalid_username));
            focusView = edtUsername;
            cancel = true;
        }

        // Check for a valid first and last name.
        if (TextUtils.isEmpty(firstname)) {
            edtFirstname.setError(getString(R.string.error_field_required));
            focusView = edtFirstname;
            cancel = true;
        }
        if (TextUtils.isEmpty(lastname)) {
            edtLastname.setError(getString(R.string.error_field_required));
            focusView = edtLastname;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            edtEmail.setError(getString(R.string.error_field_required));
            focusView = edtEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            edtEmail.setError(getString(R.string.error_invalid_email_address));
            focusView = edtEmail;
            cancel = true;
        }

        // Check for a valid DOB.
        if (TextUtils.isEmpty(dob)) {
            edtDOB.setError(getString(R.string.error_field_required));
            focusView = edtDOB;
            cancel = true;
        }

        // Check for a valid Education.
        if (TextUtils.isEmpty(education)) {
            edtEducation.setError(getString(R.string.error_field_required));
            focusView = edtEducation;
            cancel = true;
        }

        // Check for a valid Postcode.
        if (TextUtils.isEmpty(postcode)) {
            edtPostcode.setError(getString(R.string.error_field_required));
            focusView = edtPostcode;
            cancel = true;
        }

        // Check for a valid Address.
        if (TextUtils.isEmpty(address)) {
            edtAddress.setError(getString(R.string.error_field_required));
            focusView = edtAddress;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showRegistering(true);
            mAuthTask = new TutorRegisterActivity.TutorRegisterTask(username, firstname, lastname,
                    dob, gender, email, password, subject_id, postcode, address, education);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isUsernameValid(String username) {
        //TODO: Replace this with your own logic
        return username.length() >= 5 && username.length() <= 20;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.length() >= 6 && email.length() <= 255 && email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 8 && password.length() <= 32;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showRegistering(final boolean show) {
        if (show) {

            edtUsername.setEnabled(false);
            edtFirstname.setEnabled(false);
            edtLastname.setEnabled(false);

            edtEmail.setEnabled(false);
            edtPw.setEnabled(false);
            edtConfirm.setEnabled(false);

            edtDOB.setEnabled(false);

            spnSubjects.setEnabled(false);
            edtPostcode.setEnabled(false);
            edtEducation.setEnabled(false);
            edtAddress.setEnabled(false);

            btnConfirm.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);

        } else {
            edtUsername.setEnabled(true);
            edtFirstname.setEnabled(true);
            edtLastname.setEnabled(true);

            edtEmail.setEnabled(true);
            edtPw.setEnabled(true);
            edtConfirm.setEnabled(true);

            spnSubjects.setEnabled(true);
            edtPostcode.setEnabled(true);
            edtEducation.setEnabled(true);
            edtAddress.setEnabled(true);

            edtDOB.setEnabled(true);

            btnConfirm.setEnabled(true);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void initData() {
        String subjects[] = new String[subjectArray.size()];
        for(int i=0; i<subjects.length; i++) {
            subjects[i] = subjectArray.get(i).get("name");
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, subjects);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spnSubjects.setAdapter(spinnerArrayAdapter);
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

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class TutorRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mFirstname;
        private final String mLastname;
        private final String mDob;
        private final String mGender;
        private final String mEmail;
        private final String mPassword;
        private final int mSubjectID;
        private final String mPostcode;
        private final String mAddress;
        private final String mEducation;
        JSONObject receiveObj;

        String jsonresult = "";
        String jsondetails = "";

        TutorRegisterTask(String username, String firstname, String lastname, String dob, String gender, String email, String password,
                          int subject_id, String postcode, String address, String education) {
            mUsername = username;
            mFirstname = firstname;
            mLastname = lastname;
            mDob = dob;
            mGender = gender;
            mEmail = email;
            mPassword = password;

            mSubjectID = subject_id;
            mPostcode = postcode;
            mAddress = address;
            mEducation = education;

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            JSONObject sendObj = new JSONObject();

            BufferedReader bufferedReader;
            StringBuffer stringBuffer = new StringBuffer();
            try {
                URL url = new URL(AppConfigs.BACKEND_URL + "Registration/reg_tutor.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setUseCaches(false);

                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/Json");

                DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());

                sendObj.put("username", mUsername);
                sendObj.put("firstname", mFirstname);
                sendObj.put("lastname", mLastname);
                sendObj.put("gender", mGender);
                sendObj.put("dob", mDob);
                sendObj.put("email", mEmail);
                sendObj.put("password", mPassword);

                sendObj.put("subject_id", mSubjectID);
                sendObj.put("postcode", mPostcode);
                sendObj.put("address", mAddress);
                sendObj.put("education", mEducation);

                out.writeBytes(sendObj.toString());
                out.flush();
                out.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                bufferedReader = new BufferedReader(inputStreamReader);

                String str;
                while ((str = bufferedReader.readLine()) != null) {
                    stringBuffer.append(str);
                }
//            System.out.println(stringBuffer);
                bufferedReader.close();
                inputStreamReader.close();

                inputStream.close();
                httpURLConnection.disconnect();

                Log.d("TutorRegister",stringBuffer.toString());

                receiveObj = new JSONObject(stringBuffer.toString());
                Log.d("TutorRegister",receiveObj.toString());

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showRegistering(false);

            if (success) {
                jsonresult = receiveObj.optString("result");
                if (jsonresult.equals("SUCCESS")) {

                    Toast.makeText(TutorRegisterActivity.this, "Tutor account: " + mUsername + " is registered. ", Toast.LENGTH_LONG).show();

                    new AlertDialog.Builder(TutorRegisterActivity.this)
                            .setTitle("Needs Approval")
                            .setMessage("You have registered a tutor account: " + mUsername + ", and we are validating your application. ")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Intent intent = new Intent();

                                    intent.setClass(TutorRegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);

                                    TutorRegisterActivity.this.finish();
                                }}).show();


                }else {
                    showRegistering(false);
                    Toast.makeText(TutorRegisterActivity.this, "The given username or email are registered. ", Toast.LENGTH_LONG).show();
                }


            } else {
                showRegistering(false);
                Toast.makeText(TutorRegisterActivity.this, "Network Error. Please check your internet connection. ", Toast.LENGTH_LONG).show();

            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showRegistering(false);
            Toast.makeText(TutorRegisterActivity.this, "Registration cancelled", Toast.LENGTH_LONG).show();
        }
    }
}
