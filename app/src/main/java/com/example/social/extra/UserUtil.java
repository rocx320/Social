package com.example.social.extra;

public class UserUtil {
    private static String userId;

    public static void setUserId(String id) {
        userId = id;
    }

    public static String getUserId() {
        return userId;
    }
}
