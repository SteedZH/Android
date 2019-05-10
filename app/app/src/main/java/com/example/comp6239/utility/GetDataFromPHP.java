package com.example.comp6239.utility;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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


            DataOutputStream out = new DataOutputStream(
                    httpURLConnection.getOutputStream());


            out.writeBytes(strings[1]);
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
            // 释放资源
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
            string=getTutors.execute("http://35.178.209.191/COMP6239/server/api/Tutor/get_tutors.php", params).get();
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
            string = getAppointments.execute("http://35.178.209.191/COMP6239/server/api/Appointment/get_appointments.php", params).get();
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
            string = getTutorDetails.execute("http://35.178.209.191/COMP6239/server/api/Tutor/get_tutor_details.php", params).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return string;
    }
}

