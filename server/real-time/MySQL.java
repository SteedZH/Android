package com.steed.server;

import java.sql.*;
import java.util.ArrayList;

public class MySQL {
//    public static void main(String[] args) {
//        MySQL mySQL = new MySQL();
//        mySQL.writeToSQL("6", "7", "fdaofjoad", 0);
//        mySQL.readFromSQL("2");
//    }
    private Connection connection = null;

    private PreparedStatement ps = null;
    private PreparedStatement ps2 = null;
    private ResultSet set = null;

    public MySQL() {
        try {
            //loading driver class
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("fail to load");
        }
    }

    public ArrayList<String> readFromSQL(String receiverId) {
        String sql = "select message_id, details, is_read from COMP6239.Message where receiver_user_id = ? and is_read = 0 ;";
        try {
            connection = DriverManager.getConnection("jdbc:mysql://steedzh.cb5bdohx0yuo.eu-west-2.rds.amazonaws.com:3306", "admin", "pwCOMP6239");
            ps2 = connection.prepareStatement(sql,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ps2.setObject(1, Integer.parseInt(receiverId));

            set = ps2.executeQuery();
            if (set.next()) {
                ArrayList<String> msg = new ArrayList<>();
                set.previous();
                while (set.next()) {
                    msg.add(set.getString("details"));
                    set.updateString("is_read", "1");
                    set.updateRow();
                }

                return msg;
            }else
                return null;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (set != null) {
                try {
                    set.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps2 != null) {
                try {
                    ps2.close();
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
        return null;
    }

    public void writeToSQL(String senderId, String receiverId, String msg, int is_read) {
        try{
            connection = DriverManager.getConnection("jdbc:mysql://steedzh.cb5bdohx0yuo.eu-west-2.rds.amazonaws.com:3306","admin", "pwCOMP6239");
            String sql = "INSERT INTO `COMP6239`.`Message` (`sender_user_id`, `receiver_user_id`, `details`, `is_read`) VALUES (?, ?, ?, ?);";
            ps = connection.prepareStatement(sql);
            ps.setObject(1, senderId);
            ps.setObject(2, receiverId);
            ps.setObject(3, msg);
            ps.setObject(4, is_read);

            ps.execute();
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
    }



}

