package com.example.pr1.Users;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;

public class UsersContext {
    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_CURRENT_USER = "CurrentUser";
    public static ArrayList<UsersModel> All() {
        ArrayList<UsersModel> items = new ArrayList<>();

        return items;
    }


    public static void setCurrentUser(Context context, UsersModel user) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        editor.putString(KEY_CURRENT_USER, userJson);
        editor.apply();
    }

    public static UsersModel getCurrentUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String userJson = prefs.getString(KEY_CURRENT_USER, null);

        if (userJson != null) {
            Gson gson = new Gson();
            return gson.fromJson(userJson, UsersModel.class);
        }
        return null;
    }

    public static void clearCurrentUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_CURRENT_USER);
        editor.apply();
    }
}