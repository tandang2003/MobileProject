package com.example.mobileproject;

import android.app.Application;

import com.example.mobileproject.sharedPreference.GetData;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        GetData.init(getApplicationContext());

    }
}
