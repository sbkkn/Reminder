package com.bakaikin.sergey.reminder;

import android.app.Application;
import com.bakaikin.sergey.reminder.database.AppDatabase;
import com.bakaikin.sergey.reminder.database.TaskDao;

import java.util.Collection;

import androidx.room.Room;

import static com.bakaikin.sergey.reminder.database.DBHelper.DATABASE_NAME;

public class MyApplication extends Application {

    AppDatabase appDatabase;

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

    @Override
    public void onCreate() {
        super.onCreate();

        appDatabase = Room.databaseBuilder(this, AppDatabase.class,DATABASE_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
//                .addMigrations(MIGRATION_1_2)
                .build();

    }

    public AppDatabase getDatabase() {
        return appDatabase;
    }
}
