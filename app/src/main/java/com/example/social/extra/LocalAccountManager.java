package com.example.social.extra;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalAccountManager {


    private static final String PREF_NAME = "LocalAccountPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";

    private SharedPreferences sharedPreferences;

    public LocalAccountManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveLocalAccount(String username, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    public String getSavedUsername() {
        return sharedPreferences.getString(KEY_USERNAME, "");
    }

    public String getSavedPassword() {
        return sharedPreferences.getString(KEY_PASSWORD, "");
    }
}
