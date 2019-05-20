package com.example.comp6239;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.app.TimePickerDialog;
import android.widget.TimePicker;


import com.example.comp6239.utility.AppConfigs;
import com.example.comp6239.utility.AppUser;

import org.json.JSONObject;

public class StudentRegisterActivity extends AppCompatActivity {
    private int mYear, mMonth, mDay;

    AutoCompleteTextView edtFirstname, edtLastname, edtEmail, edtDOB;
    EditText edtUsername, edtPw, edtConfirm;

    RadioGroup rdoGender;

    Button btnConfirm;

    ProgressBar progressBar;

    private StudentRegisterTask mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

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

                        DatePickerDialog datePickerDialog = new DatePickerDialog(StudentRegisterActivity.this,
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

                return true;
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

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showRegistering(true);
            mAuthTask = new StudentRegisterActivity.StudentRegisterTask(username, firstname, lastname, dob, gender, email, password);
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

            btnConfirm.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);

        } else {
            edtUsername.setEnabled(true);
            edtFirstname.setEnabled(true);
            edtLastname.setEnabled(true);

            edtEmail.setEnabled(true);
            edtPw.setEnabled(true);
            edtConfirm.setEnabled(true);

            edtDOB.setEnabled(true);

            btnConfirm.setEnabled(true);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class StudentRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mFirstname;
        private final String mLastname;
        private final String mDob;
        private final String mGender;
        private final String mEmail;
        private final String mPassword;
        JSONObject receiveObj;

        String jsonresult = "";
        String jsondetails = "";

        StudentRegisterTask(String username, String firstname, String lastname, String dob, String gender, String email, String password) {
            mUsername = username;
            mFirstname = firstname;
            mLastname = lastname;
            mDob = dob;
            mGender = gender;
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            JSONObject sendObj = new JSONObject();

            BufferedReader bufferedReader;
            StringBuffer stringBuffer = new StringBuffer();
            try {
                URL url = new URL(AppConfigs.BACKEND_URL + "Registration/reg_student.php");
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

                Log.d("StudentRegister",stringBuffer.toString());

                receiveObj = new JSONObject(stringBuffer.toString());
                Log.d("StudentRegister",receiveObj.toString());

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
                    Intent intent = new Intent();

                    intent.setClass(StudentRegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    Toast.makeText(StudentRegisterActivity.this, "Student account: " + mUsername + " is registered. ", Toast.LENGTH_LONG).show();
                    StudentRegisterActivity.this.finish();

                }else {
                    showRegistering(false);
                    Toast.makeText(StudentRegisterActivity.this, "The given username or email are registered. ", Toast.LENGTH_LONG).show();
                }


            } else {
                showRegistering(false);
                Toast.makeText(StudentRegisterActivity.this, "Network Error. Please check your internet connection. ", Toast.LENGTH_LONG).show();

            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showRegistering(false);
            Toast.makeText(StudentRegisterActivity.this, "Registration cancelled", Toast.LENGTH_LONG).show();
        }
    }
}
