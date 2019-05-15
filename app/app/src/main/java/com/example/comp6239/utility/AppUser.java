package com.example.comp6239.utility;

public class AppUser {

    private static int user_id = 0;
    private static int permission = 0;
    private static String username = "DUNNO";
    private static String email = "email@dunno.com";

    public static int getUserId() {return user_id;}
    public static int getPermission() {return permission;}
    public static String getUsername() {return username;}
    public static String getEmail() {return email;}

    public static void login(int user_id, int permission, String username, String email) {
        AppUser.user_id = user_id;
        AppUser.permission = permission;
        AppUser.username = username;
        AppUser.email = email;
    }

    public static void logout() {
        AppUser.user_id = 0;
        AppUser.permission = 0;
        AppUser.username = "";
        AppUser.email = "";
    }

}
