package com.bakaikin.sergey.reminder.database;


import com.bakaikin.sergey.reminder.model.Task;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import static com.bakaikin.sergey.reminder.database.DBHelper.TASK_TABLE;
import static com.bakaikin.sergey.reminder.database.DBHelper.TASK_TIME_STAMP_COLUMN;

@Dao
public interface TaskDao {
    @Query("SELECT * FROM " + TASK_TABLE)
    List<Task> getAll();

    @Query("SELECT * FROM " + TASK_TABLE +
           " WHERE " + TASK_TIME_STAMP_COLUMN + " = :timeStamp")
    Task getTask(long timeStamp);

    @Insert
    void insertTask(Task task);

    @Delete
    void deleteByTimeStamp(Task task);

    @Update
    void updateTask(Task updatedTask);
}
