package com.chernov.niko.timetable.customList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chernov.niko.timetable.R;

import java.util.List;


public class CustomListAdapter extends BaseAdapter{
    private Activity activity;
    private LayoutInflater inflater;
    private List<ItemOfSchedule> itemOfScheduleList;

    public CustomListAdapter(Activity activity,List<ItemOfSchedule> itemOfScheduleList){
        this.activity           = activity;
        this.itemOfScheduleList = itemOfScheduleList;
    }


    @Override
    public int getCount() {
        if(itemOfScheduleList.size() <= 0)
            return 0;
        return itemOfScheduleList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(inflater == null)
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null)
            convertView = inflater.inflate(R.layout.item_table,null);

        TextView time = (TextView)convertView.findViewById(R.id.time);
        TextView audience = (TextView)convertView.findViewById(R.id.audience);
        TextView teacher = (TextView)convertView.findViewById(R.id.teacher);
        TextView subject = (TextView)convertView.findViewById(R.id.subject);

        ItemOfSchedule itemOfSchedule = itemOfScheduleList.get(position);

        time.setText(itemOfSchedule.getTime());
        audience.setText(itemOfSchedule.getAudience());
        teacher.setText(itemOfSchedule.getTeacher());
        subject.setText(itemOfSchedule.getSubject());
//        Log.d("CLA", itemOfSchedule.getTime());
//        Log.d("CLA",itemOfSchedule.getAudience());
//        Log.d("CLA",itemOfSchedule.getTeacher());
//        Log.d("CLA",itemOfSchedule.getSubject());
//        Log.d("CLA","--------------------99--------------------");

        return convertView;
    }
}
