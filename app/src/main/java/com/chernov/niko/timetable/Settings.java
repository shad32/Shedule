package com.chernov.niko.timetable;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chernov.niko.timetable.widget.WidgetTimeTable;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class Settings extends ActionBarActivity{


    Button btnSave;
    SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3b5998")));


        final Spinner spiner = (Spinner)findViewById(R.id.spinerGroup);
        final Spinner spinnerWeek = (Spinner)findViewById(R.id.spinerWeek);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, StaticVariable.NUMBER_OF_WEEK);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWeek.setAdapter(adapter);
        spinnerWeek.setSelection(loadCurrentWeek());

        final TextView tVFrequency = (TextView)findViewById(R.id.tVFrequency);
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBarFrequency);

        final LoadDataFromParse loadDataFromParse = new LoadDataFromParse(this);
        if(checkInternetConnection()) {

            loadDataFromParse.getListOfGroups(this);
        }




        sPref = PreferenceManager.getDefaultSharedPreferences(this);//getPreferences(MODE_PRIVATE);
        if(sPref.getInt(StaticVariable.FREQUENCY,0) == 0)
            tVFrequency.setText("1 раз(а)");
        else
            tVFrequency.setText(sPref.getInt(StaticVariable.FREQUENCY, 0) + " раз(а)");
        seekBar.setProgress(sPref.getInt(StaticVariable.FREQUENCY,0)-1);
        btnSave = (Button)findViewById(R.id.btnSava);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkInternetConnection()) {
                    saveCurrentWeek((int) spinnerWeek.getSelectedItemId());
                    if(spiner.getSelectedItem() != null) {
                        Editor editor = sPref.edit();
                        editor.putString(StaticVariable.GROUP_NAME, spiner.getSelectedItem().toString());
                        editor.commit();
                        int tmp = sPref.getInt(StaticVariable.FREQUENCY, 0);
                        editor.putInt(StaticVariable.FREQUENCY, 0);
                        editor.commit();
                        editor.putInt(StaticVariable.FREQUENCY, tmp);
                        editor.commit();
                        loadDataFromParse.getIdGroup(spiner.getSelectedItem().toString());
                    }
                }else{
                    alertDialog();
                }


            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tVFrequency.setText(progress+1+" раз(а)");
                Editor editor = sPref.edit();
                editor.putInt(StaticVariable.FREQUENCY, progress + 1);
                editor.commit();
                new WidgetTimeTable().onDisabled(getApplication());
                new WidgetTimeTable().onEnabled(getApplication());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        final LoadDataFromParse loadDataFromParse = new LoadDataFromParse(this);
        if(checkInternetConnection()) {

            loadDataFromParse.getListOfGroups(this);
        }else{
            alertDialog();
        }
    }

    public void saveCurrentWeek(int numWeek){
        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        Editor editor = sPref.edit();
        editor.putInt(StaticVariable.WEEK, numWeek);
        editor.commit();
    }
    public int loadCurrentWeek(){
        sPref = PreferenceManager.getDefaultSharedPreferences(this);
        if(sPref.getInt(StaticVariable.WEEK,0) == 1) {
            return 1;
        }
        else {
            return 0;
        }
    }
    public boolean checkInternetConnection(){
        ConnectivityManager check = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = check.getAllNetworkInfo();
        for(int i = 0;i<info.length;i++)
            if(info[i].getState() == NetworkInfo.State.CONNECTED){
                return true;
            }
        return false;
    }
    void alertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this);
        builder.setTitle("Внимание!")
                .setMessage("Отсутствует интернет соединение! \nПроверьте состояние сети.")
                .setIcon(R.mipmap.varning)
                .setCancelable(false)
                .setNegativeButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
