package com.example.pa4al;

import android.app.Application;
import android.content.Context;

public class AndroidApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        AndroidApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return AndroidApplication.context;
    }
}