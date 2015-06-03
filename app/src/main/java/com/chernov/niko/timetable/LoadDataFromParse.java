package com.chernov.niko.timetable;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Arrays;
import java.util.List;


public class LoadDataFromParse {

    private ProgressDialog dialog;
    Activity mActivity;
    Spinner spinnerGroup;
    private String[] listGroup;
    public LoadDataFromParse(Settings activity){
        mActivity = activity;
        dialog = new ProgressDialog((mActivity));
        spinnerGroup = (Spinner)mActivity.findViewById(R.id.spinerGroup);
    }

    protected void startShowDialogLoading( String mess){
        dialog = new ProgressDialog(mActivity);
        dialog.setMessage(mess);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        dialog.show();
    }

    protected void stopShowDialogLoading(){

        dialog.dismiss();
        dialog = null;
    }

    public void getListOfGroups(Context ctx){
        final SharedPreferences sPref;
        sPref = PreferenceManager.getDefaultSharedPreferences(ctx);
        startShowDialogLoading("Загрузка списка групп...");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {

                    listGroup = new String[parseObjects.size()];
                    for (int i = 0; i < parseObjects.size(); i++) {
                        listGroup[i] = parseObjects.get(i).get("nameGroup").toString();
                        Log.d("LDFP", listGroup[i] + "   ");
                    }
                    Arrays.sort(listGroup);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, listGroup);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerGroup.setAdapter(adapter);
                    for(int i = 0;i<listGroup.length;i++) {
                        if(listGroup[i].equals(sPref.getString(StaticVariable.GROUP_NAME,null))) {
                            spinnerGroup.setSelection(i);
                            break;
                        }
                    }

                    stopShowDialogLoading();
                }else{
                    stopShowDialogLoading();
                }
            }
        });
    }

    public void getIdGroup(final String name){
        startShowDialogLoading("Загрузка расписания группы: "+name);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < parseObjects.size(); i++) {
                        if(parseObjects.get(i).get("nameGroup").toString().equals(name)) {
                            Log.d("LDFP", parseObjects.get(i).get("idGroup").toString() + "   ");
                            getFileOfGroup(parseObjects.get(i).get("idGroup").toString());
                        }
                    }
                }
            }
        });
    }
    public void getFileOfGroup(String idGroup){
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Schedule");
        query.getInBackground(idGroup,new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                final ParseFile file = (ParseFile) parseObject.get("schedule");
                file.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, ParseException e) {
                        if(e == null){
                            try {
                                String internalStorage = System.getenv(StaticVariable.EXTERNAL_STORAGE);
                                Log.d("LDFP",internalStorage+"     storage");
                                File dir = new File (internalStorage + "/TimeTable");
                                dir.mkdirs();
                                File file = new File(dir, "MySchedule.xml");
                                FileOutputStream f = new FileOutputStream(file);
                                f.write(bytes);
                                f.close();
                                Log.d("LDFP","     GOOD FILE");
                            } catch (FileNotFoundException e1) {
                                System.err.println("ERROR 1");
                                e1.printStackTrace();
                            } catch (IOException e1) {
                                System.err.println("ERROR 2");
                                e1.printStackTrace();
                            }
                            stopShowDialogLoading();
                        }else{
                            System.err.println("ERROR");
                            stopShowDialogLoading();
                        }
                    }
                });
            }
        });
    }
}
