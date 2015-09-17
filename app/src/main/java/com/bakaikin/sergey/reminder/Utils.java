package com.bakaikin.sergey.reminder;

import java.text.SimpleDateFormat;

/**
 * Created by Sergey on 16.09.2015.
 */
public class Utils {

    public static String getDate(long date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
        return dateFormat.format(date);
    }

    public static String getTime(long time){
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH.mm");
        return timeFormat.format(time);
    }


    public static String getFullDate(long date){
        SimpleDateFormat fullTimeFormat = new SimpleDateFormat("dd.MM.yy  HH.mm");
        return fullTimeFormat.format(date);
    }
}
