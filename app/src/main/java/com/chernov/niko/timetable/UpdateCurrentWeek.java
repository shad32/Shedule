package com.chernov.niko.timetable;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class UpdateCurrentWeek {

    public int parityOfWeek(Context ctx){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        Calendar calendar = Calendar.getInstance();

        if(calendar.get(Calendar.WEEK_OF_YEAR) != sharedPreferences.getInt(StaticVariable.WEEK_OF_YEAR, 0)){// StaticVariable.weekOfYear){
            int difference = Math.abs(calendar.get(Calendar.WEEK_OF_YEAR) - sharedPreferences.getInt(StaticVariable.WEEK_OF_YEAR, 0));
            for(int i = 0; i < difference; i++){
                if(sharedPreferences.getInt(StaticVariable.WEEK, 0) == 0) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(StaticVariable.WEEK, 1);
                    editor.commit();
                }
                else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(StaticVariable.WEEK, 0);
                    editor.commit();
                }
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(StaticVariable.WEEK_OF_YEAR, calendar.get(Calendar.WEEK_OF_YEAR));
            editor.commit();
            Log.d("SERVICE", calendar.get(Calendar.WEEK_OF_YEAR) + "   week    " + sharedPreferences.getInt(StaticVariable.WEEK, 0));
        }
        return sharedPreferences.getInt(StaticVariable.WEEK, 0);
    }

    public String titleActivity(Context ctx){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        String day = new SimpleDateFormat( "EEEE" ).format ( new Date() );
        if(day.equals("воскресенье")){
          //  day = "понедельник";
            if(sharedPreferences.getInt(StaticVariable.WEEK, 0) == 0)
                return   sharedPreferences.getString(StaticVariable.GROUP_NAME,null)+" ("+ StaticVariable.NUMBER_OF_WEEK[1]+")";
            else
                return   sharedPreferences.getString(StaticVariable.GROUP_NAME,null)+" ("+ StaticVariable.NUMBER_OF_WEEK[0]+")";
        }
        return   sharedPreferences.getString(StaticVariable.GROUP_NAME,null)+" ("+ StaticVariable.NUMBER_OF_WEEK[sharedPreferences.getInt(StaticVariable.WEEK, 0)]+")";
    }
}
