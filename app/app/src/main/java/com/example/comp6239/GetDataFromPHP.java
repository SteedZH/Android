package com.example.comp6239;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetDataFromPHP {
    public static String getJson(String path, String params) {
        BufferedReader bufferedReader;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            URL url = new URL(path);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setUseCaches(false);

            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Type", "application/Json");


            DataOutputStream out = new DataOutputStream(
                    httpURLConnection.getOutputStream());


            out.writeBytes(params);
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

    public static String getTutor(String tutorName, String tutorSubject, String tutorLocation){
        String string = "";
        String params = "{\"tutorName\":\""+tutorName+"\",\"tutorSubject\":"+tutorSubject+",\"tutorLocation\":\""+tutorLocation+"\"}";
        string = GetDataFromPHP.getJson("http://35.178.209.191/COMP6239/server/api/Tutor/get_tutors.php", params);
        return string;
    }

    public static String getAppointment(String tutorId){
        String string = "";
        String params = "{\"tutor_id\":"+tutorId+"}";
        string = GetDataFromPHP.getJson("http://35.178.209.191/COMP6239/server/api/Appointment/get_appointments.php", params);
        return string;
    }

//    public static void main(String[] args) {
//        System.out.println(GetDataFromPHP.getTutor("Sybil", "0", ""));
//    }
}

