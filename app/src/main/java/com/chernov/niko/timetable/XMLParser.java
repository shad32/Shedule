package com.chernov.niko.timetable;

import android.util.Log;

import com.chernov.niko.timetable.customList.ItemOfSchedule;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class XMLParser {


    public boolean checkExistFile(){
        String internalStorage = System.getenv(StaticVariable.EXTERNAL_STORAGE);
        File dir = new File (internalStorage + "/TimeTable");
        File file = new File(dir+"/MySchedule.xml");
        if(file.exists())
            return true;
        else
            return false;
    }
    public List<ItemOfSchedule> parseScheduleForDay(String day, String week){
        XmlPullParserFactory factory = null;
        XmlPullParser xmlPullParser = null;
        List<ItemOfSchedule> itemOfScheduleList = new ArrayList<ItemOfSchedule>();
        try {
            String internalStorage = System.getenv(StaticVariable.EXTERNAL_STORAGE);
            File dir = new File (internalStorage + "/TimeTable");
            dir.mkdirs();
            File file = new File(dir, "MySchedule.xml");
            factory = XmlPullParserFactory.newInstance();
            xmlPullParser = factory.newPullParser();
            FileInputStream fis = new FileInputStream(file);//openFileInput("MySchedule.xml");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            xmlPullParser.setInput(reader);
            try {
                while(xmlPullParser.getEventType()!=XmlPullParser.END_DOCUMENT){
                    if(xmlPullParser.getEventType() == XmlPullParser.START_TAG &&
                            xmlPullParser.getName().equals("schedule") &&
                            xmlPullParser.getAttributeValue(0).equals(day)){
                        Log.d("XMLP", xmlPullParser.getAttributeValue(0) + "    day ");
                        xmlPullParser.nextTag();
                        while(!xmlPullParser.getName().equals("schedule")){
                            if(xmlPullParser.getEventType() == XmlPullParser.START_TAG && xmlPullParser.getName().equals("item")){
                                Log.d("XMLP",xmlPullParser.getAttributeValue(0)+"  week  "+ week);
                               // Log.d("XMLP",xmlPullParser.getAttributeValue(1));
                               // Log.d("XMLP",xmlPullParser.getAttributeValue(2));
                               // Log.d("XMLP",xmlPullParser.getAttributeValue(3));
                               // Log.d("XMLP","--------------------99--------------------");
                                if(xmlPullParser.getAttributeValue(0).equals(week) ||xmlPullParser.getAttributeValue(0).equals("2")) {
                                    ItemOfSchedule itemOfSchedule = new ItemOfSchedule(xmlPullParser.getAttributeValue(1),
                                            xmlPullParser.getAttributeValue(2),
                                            xmlPullParser.getAttributeValue(3),
                                            xmlPullParser.getAttributeValue(4));
                                    itemOfScheduleList.add(itemOfSchedule);
                                }
                            }
                            xmlPullParser.nextTag();
                        }
                        break;
                    }
                    xmlPullParser.nextTag();
                }
            } catch (Throwable t) {
                Log.d("XMLP", t+"     ERROR");
            }
        } catch (XmlPullParserException e) {
            Log.d("XMLP", "     EXCEPTION XMLPULL");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            Log.d("XMLP", "     NOT FOUND FILE");
            e.printStackTrace();
        }
        Log.d("XMLP", "     +++++++");
        return itemOfScheduleList;
    }


}
