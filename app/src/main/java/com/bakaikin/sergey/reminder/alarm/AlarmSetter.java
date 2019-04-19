package com.bakaikin.sergey.reminder.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bakaikin.sergey.reminder.database.DBHelper;
import com.bakaikin.sergey.reminder.model.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergey on 27.09.2015.
 */
public class AlarmSetter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DBHelper dbHelper = new DBHelper(context);

        AlarmHelper.getInstance().init(context);
        AlarmHelper alarmHelper = AlarmHelper.getInstance();

        List<Task> tasks = new ArrayList<>();
//        tasks.addAll(dbHelper.query().getTasks(DBHelper.SELECTION_STATUS + " OR "
//                + DBHelper.SELECTION_STATUS, new String[]{Integer.toString(Task.STATUS_CURRENT),
//                Integer.toString(Task.STATUS_OVERDUE)}, DBHelper.TASK_DATE_COLUMN));

        for (Task task : tasks) {
            if (task.date != 0) {
                alarmHelper.setAlarm(task);
            }
        }

    }
}
