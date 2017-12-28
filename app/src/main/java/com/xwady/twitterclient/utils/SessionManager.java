package com.xwady.twitterclient.utils;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class SessionManager {

    private static final String PREF_NAME = "app";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context mContext;

    public SessionManager(Context context) {
        this.mContext = context;
        pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(com.xwady.twitterclient.models.User user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString("user", json);
        editor.commit();
    }


    /**
     * Check login method wil check user login status
     * If false it will redirect user to welcomeActivity page
     * Else won't do anything
     */
    public void checkLogin(Class<?> welcomeActivity) {
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(mContext, welcomeActivity);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);
        }
    }

    /**
     * Get stored session data
     */
    public com.xwady.twitterclient.models.User getUserDetails() {
        Gson gson = new Gson();
        String json = pref.getString("user", "");
        return gson.fromJson(json, com.xwady.twitterclient.models.User.class);
    }


    /**
     * Clear session details
     */
    public void logoutUser(Class<?> welcomeActivity) {
        editor.clear();
        editor.commit();
        Intent i = new Intent(mContext, welcomeActivity);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);
    }


    // Get Login State
    public boolean isLoggedIn() {
        if (null != getUserDetails()){
        return getUserDetails().getAccountStatus() == com.xwady.twitterclient.models.User.AccountStatus.LOGGED_IN;}
        else {
            return false;
        }
    }


}
