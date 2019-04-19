package com.bakaikin.sergey.reminder.database;



import com.bakaikin.sergey.reminder.model.Task;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Task.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}
