package com.example.dss;

public class UserManager {
    private static String userRole;

    public static String getUserRole() {
        return userRole;
    }

    public static void setUserRole(String userRole) {
        UserManager.userRole = userRole;
    }
}
