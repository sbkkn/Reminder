package com.bakaikin.sergey.reminder;

import android.app.Application;

/**
 * Created by Sergey on 27.09.2015.
 */
public class MyApplication extends Application {

    private static boolean activityVisible;

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed(){
        activityVisible = true;
    }

    public static void activityPaused(){
        activityVisible = false;
    }
}
