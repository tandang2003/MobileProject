package com.example.mobileproject.sharedPreference;

import android.content.Context;

public class GetData {
    private MySharedPreference mySharedPreference;
    private static GetData instance;
    public static void init(Context context) {
        instance = new GetData();
        instance.mySharedPreference = new MySharedPreference(context);
    }
    public static GetData getInstance() {
        if (instance == null) {
            instance = new GetData();
        }
        return instance;
    }
    public String getString(String key) {
        return mySharedPreference.getString(key);
    }
    public void setString(String key, String value) {
        mySharedPreference.setString(key, value);
    }
    public void putBoolean(String key, boolean value) {
        mySharedPreference.putBoolean(key, value);
    }
    public boolean getBoolean(String key) {
        return mySharedPreference.getBoolean(key);
    }
}
