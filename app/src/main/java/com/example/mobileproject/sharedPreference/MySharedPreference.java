package com.example.mobileproject.sharedPreference;

import android.content.Context;

public class SharedPreference {
    private static final String SHARED_PREF_NAME = "ebook";
    private Context context;
    public SharedPreference(Context context) {
        this.context=context;
    }
    public void setString (String key, String value){
        android.content.SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
}
