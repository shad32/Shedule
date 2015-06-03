package com.chernov.niko.timetable.day;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.chernov.niko.timetable.R;
import com.chernov.niko.timetable.UpdateCurrentWeek;
import com.chernov.niko.timetable.XMLParser;
import com.chernov.niko.timetable.customList.CustomListAdapter;


public class _0Monday extends Fragment{




    private CustomListAdapter adapter;
   // FragmentPageAdapter ft;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.day,container,false);

        ListView listViewItems = (ListView) rootView.findViewById(R.id.listViewItemsSchedule);
        XMLParser xmlParser = new XMLParser();
        if(xmlParser.checkExistFile()) {

            Log.d("0mon", "    1");
            //ServiceOfWeek serviceOfWeek = new ServiceOfWeek();
            ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(new UpdateCurrentWeek().titleActivity(getActivity()));
            adapter = new CustomListAdapter(getActivity(), xmlParser.parseScheduleForDay("monday", new UpdateCurrentWeek().parityOfWeek(getActivity())+""));
            //xmlParser.parseScheduleForDay("monday");



            rootView.setTag("0");
            listViewItems.setAdapter(adapter);
            listViewItems.setEmptyView(rootView.findViewById(R.id.empty_view));

            Log.d("0mon", "    2");
        }else{
            listViewItems.setEmptyView(rootView.findViewById(R.id.fileExist));
        }

        return  rootView;
    }



}
