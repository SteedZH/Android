package com.example.comp6239.utility;

import android.os.AsyncTask;

import java.sql.*;
import java.util.concurrent.ExecutionException;

public class MySQL extends AsyncTask<String, Integer, String> {
    private Connection connection = null;

    private PreparedStatement ps = null;
    private PreparedStatement ps2 = null;
    private ResultSet set = null;
    private String  res;

    @Override
    protected String doInBackground(String... strings) {
        try{

            connection = DriverManager.getConnection("jdbc:mysql://steedzh.cb5bdohx0yuo.eu-west-2.rds.amazonaws.com:3306","admin", "pwCOMP6239");
            String sql = "UPDATE `COMP6239`.`Student` SET `first_name` = ?, `dob` = ?, `gender` = ?, `description` = ? WHERE (`user_id` = ?);";
            ps = connection.prepareStatement(sql);
            ps.setObject(1, strings[0]);
            ps.setObject(2, strings[1]);
            ps.setObject(3, strings[2]);
            ps.setObject(4, strings[3]);
            ps.setObject(5, strings[4]);

            if (ps.executeUpdate() != 0) {
                res = "SUCCESS";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    public MySQL() {
        try {
            //loading driver class
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("fail to load");
        }
    }

    public static String writeToSQL(String name, String dob, String gender, String description, String user_id) {
        String res = "";
        MySQL profile = new MySQL();
        try {
            res=profile.execute(name, dob, gender,description,user_id).get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return res;
    }
}
