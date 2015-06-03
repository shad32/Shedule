package com.chernov.niko.timetable;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends  ActionBarActivity implements ActionBar.TabListener {


    ActionBar actionBar;
    public ViewPager  viewPager;
    FragmentPageAdapter ft;
    StartParse startParse = new StartParse();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startParse.ParseInit(this);

        viewPager = (ViewPager)findViewById(R.id.pager);
        ft = new FragmentPageAdapter(getSupportFragmentManager());

        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(StaticVariable.COLOR)));

        viewPager.setAdapter(ft);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.addTab(actionBar.newTab().setText("Понедельник").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("Вторник").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("Среда").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("Четверг").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("Пятница").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("Суббота").setTabListener(this));

        actionBar.setSelectedNavigationItem(selectDayTab(new SimpleDateFormat( "EEEE" ).format ( new Date() )));
        Log.d("DAY",new SimpleDateFormat( "EEEE" ).format ( new Date() ));

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                actionBar.setSelectedNavigationItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        viewPager.getAdapter().notifyDataSetChanged();
        viewPager.setCurrentItem(tab.getPosition());
        Log.d("VIEWPAGER","VIEWPAGER");


    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    public int selectDayTab(String day){
        if(day.equals("понедельник")){
            return 0;
        }
        if(day.equals("вторник")){
            return 1;
        }
        if(day.equals("среда")){
            return 2;
        }
        if(day.equals("четверг")){
            return 3;
        }
        if(day.equals("пятница")){
            return 4;
        }
        if(day.equals("суббота")){
            return 5;
        }
        return 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this,Settings.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.select_num_week ){
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
