package com.chernov.niko.timetable.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.chernov.niko.timetable.MainActivity;
import com.chernov.niko.timetable.R;
import com.chernov.niko.timetable.StaticVariable;
import com.chernov.niko.timetable.XMLParser;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WidgetTimeTable extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        Intent configIntent = new Intent(context, MainActivity.class);
        PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);

         for (int i = 0; i < N; ++i) {
             RemoteViews remoteViews = updateWidgetListView(context, appWidgetIds[i]);
             remoteViews.setOnClickPendingIntent(R.id.mainID,configPendingIntent);

             appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
             appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[i],R.id.listViewItemsSchedule);
        }
        Log.d("WIDGET","    widget");


    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equalsIgnoreCase(StaticVariable.UPDATE_ALL_WIDGETS)) {
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), getClass().getName());
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
            for (int appWidgetID : ids) {
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetID,R.id.listViewItemsSchedule);
                RemoteViews remoteViews = updateWidgetListView(context, appWidgetID);
                appWidgetManager.updateAppWidget(appWidgetID, remoteViews);
                Log.d("WIDGET","    widget  receive");
            }
        }
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Intent intent = new Intent(context, WidgetTimeTable.class);
        intent.setAction(StaticVariable.UPDATE_ALL_WIDGETS);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
/////////////////////////////////////  ----------360000---------\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
        if(sharedPreferences.getInt(StaticVariable.FREQUENCY,0) == 0) {
            alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 3600000, pIntent);/////3600000
        }
        else{
            alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 3600000*sharedPreferences.getInt(StaticVariable.FREQUENCY,0), pIntent);//3600000
        }
        Log.d("WIDGET", "    widget  enabled");
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Intent intent = new Intent(context, WidgetTimeTable.class);
        intent.setAction(StaticVariable.UPDATE_ALL_WIDGETS);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pIntent);
        Log.d("WIDGET","    widget  disable");
    }

    private RemoteViews updateWidgetListView(Context context, int appWidgetId) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        Intent svcIntent = new Intent(context, WidgetService.class);
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        remoteViews.setRemoteAdapter(appWidgetId, R.id.listViewItemsSchedule, svcIntent);

        SharedPreferences sPref;
        sPref = PreferenceManager.getDefaultSharedPreferences(context);

       // DateFormat format = SimpleDateFormat.getTimeInstance(SimpleDateFormat.MEDIUM, Locale.getDefault());
       // remoteViews.setTextViewText(R.id.widgetText,"Группа: "+sPref.getString("NAME_GROUP",null)+"("+NUMBER_OF_WEEK[sPref.getInt("WEEK",0)]+")"+format.format(new Date()));//format.format(new Date())

        remoteViews.setTextViewText(R.id.group,"Группа: "+sPref.getString(StaticVariable.GROUP_NAME,"Не выбрана"));
        String day = new SimpleDateFormat( "EEEE" ).format ( new Date() );
        if(day.equals("воскресенье")) {
            day = "понедельник";
            XMLParser xmlParser = new XMLParser();
            if(xmlParser.checkExistFile()) {
                if(sPref.getInt(StaticVariable.WEEK, 0) == 0)
                    remoteViews.setTextViewText(R.id.week, StaticVariable.NUMBER_OF_WEEK[1] + " (" + day + ")");
                else
                    remoteViews.setTextViewText(R.id.week, StaticVariable.NUMBER_OF_WEEK[0] + " (" + day + ")");
                remoteViews.setEmptyView(R.id.listViewItemsSchedule, R.id.empty_view);


            }
        }else{
            XMLParser xmlParser = new XMLParser();
            if(xmlParser.checkExistFile()) {
                //if()
                remoteViews.setTextViewText(R.id.week, StaticVariable.NUMBER_OF_WEEK[sPref.getInt(StaticVariable.WEEK, 0)] + " (" + day + ")");
                remoteViews.setEmptyView(R.id.listViewItemsSchedule, R.id.empty_view);


            }
        }
        //setting an empty view in case of no data

        return remoteViews;
    }
}
