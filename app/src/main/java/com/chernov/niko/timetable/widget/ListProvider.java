package com.chernov.niko.timetable.widget;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.chernov.niko.timetable.R;
import com.chernov.niko.timetable.StaticVariable;
import com.chernov.niko.timetable.UpdateCurrentWeek;
import com.chernov.niko.timetable.XMLParser;
import com.chernov.niko.timetable.customList.ItemOfSchedule;

public class ListProvider implements RemoteViewsFactory {
    private String[] dayRus = {"понедельник","вторник","среда","четверг","пятница","суббота"};
    private String[] dayEng = {"monday","tuesday","wednesday","thursday","friday","saturday"};
    private List<ItemOfSchedule> listItemList;// = new ArrayList<ItemOfSchedule>();
    private Context context = null;
    private int appWidgetId;

    public ListProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        listItemList = new ArrayList<ItemOfSchedule>();
        boolean flag = false;
        for(int i = 0; i < dayEng.length; i++) {
            if (new SimpleDateFormat("EEEE").format(new Date()).equals(dayRus[i])) {
                listItemList = new XMLParser().parseScheduleForDay(dayEng[i], new UpdateCurrentWeek().parityOfWeek(context) + "");
                flag = true;
            }

        }
        if(!flag)
            listItemList = new XMLParser().parseScheduleForDay(dayEng[0], new UpdateCurrentWeek().parityOfWeek(context) + "");
    }


    @Override
    public int getCount() {
        return listItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.item_table);
        ItemOfSchedule listItem = listItemList.get(position);
        remoteView.setTextViewText(R.id.time, listItem.getTime());
        remoteView.setTextViewText(R.id.teacher, listItem.getTeacher());
        remoteView.setTextViewText(R.id.audience, listItem.getAudience());
        remoteView.setTextViewText(R.id.subject, listItem.getSubject());

        return remoteView;
    }


    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Log.d("LISTPROVIDER","CHANGE");
        listItemList.clear();
        boolean flag = false;
        for(int i = 0; i < dayEng.length; i++) {
            if (new SimpleDateFormat("EEEE").format(new Date()).equals(dayRus[i])) {
                listItemList = new XMLParser().parseScheduleForDay(dayEng[i], new UpdateCurrentWeek().parityOfWeek(context) + "");
                flag = true;
            }

        }
        if(!flag) {
            //listItemList = new XMLParser().parseScheduleForDay(dayEng[0], new UpdateCurrentWeek().parityOfWeek(context) + "");
            if(sharedPreferences.getInt(StaticVariable.WEEK, 0) == 0)
                listItemList = new XMLParser().parseScheduleForDay(dayEng[0], "1");
            else
                listItemList = new XMLParser().parseScheduleForDay(dayEng[0], "0");
        }
    }

    @Override
    public void onDestroy() {
    }

}
