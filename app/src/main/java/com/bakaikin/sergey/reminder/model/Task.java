package com.bakaikin.sergey.reminder.model;


import com.bakaikin.sergey.reminder.R;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import static com.bakaikin.sergey.reminder.database.DBHelper.TASK_DATE_COLUMN;
import static com.bakaikin.sergey.reminder.database.DBHelper.TASK_PRIORITY_COLUMN;
import static com.bakaikin.sergey.reminder.database.DBHelper.TASK_STATUS_COLUMN;
import static com.bakaikin.sergey.reminder.database.DBHelper.TASK_TABLE;
import static com.bakaikin.sergey.reminder.database.DBHelper.TASK_TIME_STAMP_COLUMN;
import static com.bakaikin.sergey.reminder.database.DBHelper.TASK_TITLE_COLUMN;


@Entity(tableName = TASK_TABLE)
public class Task implements Item{
    @PrimaryKey
    @ColumnInfo(name = TASK_TIME_STAMP_COLUMN)
    public long timeStamp;

    @ColumnInfo(name = TASK_DATE_COLUMN)
    public long date;

    @ColumnInfo(name = TASK_PRIORITY_COLUMN)
    public int priority;

    @ColumnInfo(name = TASK_STATUS_COLUMN)
    public int status;

    @ColumnInfo(name = TASK_TITLE_COLUMN)
    public String title;

    public int dateStatus;

    @Override
    public boolean isTask() {
        return true;
    }

    public static final int PRIORITY_LOW = 0;
    public static final int PRIORITY_NORMAL = 1;
    public static final int PRIORITY_HIGH = 2;

    public static final String[] PRIORITY_LEVELS = {"Low Priority", "Normal Priority", "High priority"};

    public static final int STATUS_OVERDUE = 0;
    public static final int STATUS_CURRENT = 1;
    public static final int STATUS_DONE = 2;


    public int getPriorityColor() {
        switch (priority) {
            case PRIORITY_HIGH:
                if (status == STATUS_CURRENT || status == STATUS_OVERDUE) {
                    return R.color.priority_high;
                } else {
                    return R.color.priority_high_selected;
                }
            case PRIORITY_NORMAL:
                if (status == STATUS_CURRENT || status == STATUS_OVERDUE) {
                    return R.color.priority_normal;
                } else {
                    return R.color.priority_normal_selected;
                }
            case PRIORITY_LOW:
                if (status == STATUS_CURRENT || status == STATUS_OVERDUE) {
                    return R.color.priority_low;
                } else {
                    return R.color.priority_low_selected;
                }
            default:
                return 0;
        }
    }
}
