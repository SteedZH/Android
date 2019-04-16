package com.example.comp6239;

public class AppUser {
    private static int permission = 0;

    private static String username = "Dunno";
    private static String email = "email@dunno.com";
    private static String authToken = "123456";

    public static int getPermission() {return permission;}
    public static String getUsername() {return username;}
    public static String getEmail() {return email;}
    public static String getAuthToken() {return authToken;}

    public static void login(int permission, String username, String email, String authToken) {
        AppUser.permission = permission;
        AppUser.username = username;
        AppUser.email = email;
        AppUser.authToken = authToken;
    }

    public static void logout() {
        AppUser.permission = 0;
        AppUser.username = "";
        AppUser.email = "";
        AppUser.authToken = "";
    }

}
