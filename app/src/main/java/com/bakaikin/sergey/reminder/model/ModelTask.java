package com.bakaikin.sergey.reminder.model;

import android.view.MenuItem;

/**
 * Created by Sergey on 17.09.2015.
 */
public class ModelTask implements Item {

    private String title;
    private long date;

    public ModelTask(){

    }

    public ModelTask(String title, long date){
        this.date = date;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public boolean isTask() {
        return false;
    }
}
