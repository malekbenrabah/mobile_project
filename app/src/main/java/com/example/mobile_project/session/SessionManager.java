package com.example.mobile_project.session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_ID = "userId";
    private static final String KEY_USERNAME = "userName";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_LOGGED_IN = "loggedIn";
    private static final String KEY_PHOTO_URL = "photoUrl";

    private static final String KEY_IS_ADMIN = "isAdmin";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void createSession(int id, String userName, String email, String phone, String password, String photoUrl, boolean isAdmin) {
        editor.putInt(KEY_ID, id);
        editor.putString(KEY_USERNAME, userName);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_PHOTO_URL, photoUrl);
        editor.putBoolean(KEY_LOGGED_IN, true);
        editor.putBoolean(KEY_IS_ADMIN, isAdmin);
        editor.apply();
    }

    public void updateUserInfo(int id, String userName, String email, String phone, String password) {
        editor.putInt(KEY_ID, id);
        editor.putString(KEY_USERNAME, userName);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(KEY_LOGGED_IN, false);
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }

    public int getUserId() {
        return preferences.getInt(KEY_ID, 0); // You can set a default value as needed
    }

    public String getUserName() {
        return preferences.getString(KEY_USERNAME, null);
    }

    public String getEmail() {
        return preferences.getString(KEY_EMAIL, null);
    }

    public String getPhone() {
        return preferences.getString(KEY_PHONE, null);
    }

    public String getPassword() {
        return preferences.getString(KEY_PASSWORD, null);
    }

    public String getPhotoUrl() {
        return preferences.getString(KEY_PHOTO_URL, null);
    }

    public boolean getIsAdmin() {
        return preferences.getBoolean(KEY_IS_ADMIN, false);
    }

    public void updateProfileImage(String newPhotoPath) {
        editor.putString(KEY_PHOTO_URL, newPhotoPath);
        editor.apply();
    }
}

