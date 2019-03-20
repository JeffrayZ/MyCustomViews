package com.example.test;

import android.app.Application;

import com.example.alarmManager.CrashHandler;

public class MyApplication extends Application {
    public static MyApplication context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        CrashHandler.getInstance().init(this);
    }
}
