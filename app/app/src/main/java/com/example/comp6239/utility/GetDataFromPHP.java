package com.example.comp6239.utility;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.SearchView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class GetDataFromPHP extends AsyncTask<String, Integer, String> {
    @Override
    protected String doInBackground(String... strings) {
        BufferedReader bufferedReader;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setUseCaches(false);

            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Type", "application/Json");

//            Log.d(strings[0],strings[1]);

            DataOutputStream out = new DataOutputStream(
                    httpURLConnection.getOutputStream());


            if (strings[1]!= null) {
                out.writeBytes(strings[1]);
                out.flush();
            }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    public static String getTutor(String tutorName, int tutorSubject, String tutorLocation){
        String string = "";
        GetDataFromPHP getTutors = new GetDataFromPHP();
        String params = "{\"tutorName\":\""+tutorName+"\",\"tutorSubject\":"+tutorSubject+",\"tutorLocation\":\""+tutorLocation+"\"}";
        try {
            string=getTutors.execute(AppConfigs.BACKEND_URL + "Tutor/get_tutors.php", params).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return string;
    }

    public static String getAppointment(int tutorId){
        String string = "";
        GetDataFromPHP getAppointments = new GetDataFromPHP();
        String params = "{\"tutor_id\":"+tutorId+"}";
        try {
            string = getAppointments.execute(AppConfigs.BACKEND_URL + "Appointment/get_appointments.php", params).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return string;
    }

    public static String getTutorDetails(int tutorId) {
        String string = "";
        GetDataFromPHP getTutorDetails = new GetDataFromPHP();
        String params = "{\"tutor_id\":"+tutorId+"}";
        try {
            string = getTutorDetails.execute(AppConfigs.BACKEND_URL + "Tutor/get_tutor_details.php", params).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }

    public static String getSubjects(){
        String string = "";
        GetDataFromPHP getSubjects = new GetDataFromPHP();
        String params = "";
        try {
            string = getSubjects.execute(AppConfigs.BACKEND_URL + "Subject/get_subjects.php", params).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return string;
    }

    public static String getRequests(int tutorId){
        String string = "";
        GetDataFromPHP getRequests = new GetDataFromPHP();
        String params = "{\"tutor_id\":"+tutorId+"}";
        try {
            string = getRequests.execute(AppConfigs.BACKEND_URL + "Appointment/get_requests.php", params).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return string;
    }

    public static String setAppointmentRequest(int studentId, int tutorId, String startTime, String endTime) {
        String string = "";
        GetDataFromPHP getTutors = new GetDataFromPHP();
        String params = "{\"student_id\":"+studentId+",\"tutor_id\":"+tutorId+",\"start_time\":\""+startTime+"\",\"end_time\":\""+endTime+"\"}";
        try {
            string=getTutors.execute(AppConfigs.BACKEND_URL + "Appointment/set_appointment_request.php", params).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return string;
    }

    public static String getChatMessage(int senderId, int receiverId) {
        String string = "";
        GetDataFromPHP getTutors = new GetDataFromPHP();
        String params = "{\"sender_user_id\":"+senderId+",\"receiver_user_id\":"+receiverId+"}";
        try {
            string=getTutors.execute(AppConfigs.BACKEND_URL + "Message/get_chat_messages.php", params).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return string;
    }

    public static String getRequestDetails(int appointmentId){
        String string = "";
        GetDataFromPHP getAppointment = new GetDataFromPHP();
        String params = "{\"appointment_id\":"+appointmentId+"}";
        try {
            string = getAppointment.execute(AppConfigs.BACKEND_URL + "Appointment/get_request_details.php", params).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return string;
    }

    public static String approveAppointment(int appointment_id) {
        String string = "";
        GetDataFromPHP approveAppointment = new GetDataFromPHP();
        String params = "{\"appointment_id\":"+appointment_id+",\"is_confirm\": 1}";
        try {
            string = approveAppointment.execute(AppConfigs.BACKEND_URL + "Appointment/update_appointment_approval.php", params).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return string;
    }

    public static String getTutorRequest(){
        String string = "";
        GetDataFromPHP getTutorRequest = new GetDataFromPHP();
        String params = "";
        try {
            string = getTutorRequest.execute(AppConfigs.BACKEND_URL + "Tutor/get_tutor_requests.php", params).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return string;
    }

    public static String updateTutorRequest(String tutor_id){
        String string = "";
        GetDataFromPHP getTutorRequest = new GetDataFromPHP();
        String params = "{\"user_id\":"+tutor_id+",\"is_approved\":1}";
        try {
            string = getTutorRequest.execute(AppConfigs.BACKEND_URL + "Tutor/update_tutor_approvals.php", params).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return string;
    }

    public static String deleteSubject(String subjectId){
        String string = "";
        GetDataFromPHP getRequests = new GetDataFromPHP();
        String params = "{\"subject_id\":"+subjectId+"}";
        try {
            string = getRequests.execute(AppConfigs.BACKEND_URL + "Subject/delete_subject.php", params).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return string;
    }

    public static String updateSubject(String subjectId, String subject_name){
        String string = "";
        GetDataFromPHP getRequests = new GetDataFromPHP();
        String params = "{\"subject_id\":"+subjectId +", \"name\":\""+subject_name+"\"}";
        try {
            string = getRequests.execute(AppConfigs.BACKEND_URL + "Subject/update_subject.php", params).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return string;
    }

    public static String addSubject(String subject_name){
        String string = "";
        GetDataFromPHP getRequests = new GetDataFromPHP();
        String params = "{\"name\":\""+subject_name+"\"}";
        try {
            string = getRequests.execute(AppConfigs.BACKEND_URL + "Subject/set_subject.php", params).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return string;
    }

}

