package com.chernov.niko.timetable;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;


import com.chernov.niko.timetable.day._0Monday;
import com.chernov.niko.timetable.day._1tuesday;
import com.chernov.niko.timetable.day._2wednesday;
import com.chernov.niko.timetable.day._3thursday;
import com.chernov.niko.timetable.day._4friday;
import com.chernov.niko.timetable.day._5saturday;


public class FragmentPageAdapter extends FragmentStatePagerAdapter {


    public FragmentPageAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0: {

                return new _0Monday();
            }
            case 1: {
                return new _1tuesday();
            }
            case 2: {
                return new _2wednesday();
            }
            case 3: {
                return new _3thursday();
            }
            case 4: {
                return new _4friday();
            }
            case 5: {
                return new _5saturday();
            }
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 6;
    }


}