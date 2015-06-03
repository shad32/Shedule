package com.chernov.niko.timetable;

import android.app.Activity;

import com.parse.Parse;


public class StartParse {
    public void ParseInit(Activity activity){
        Parse.initialize(activity, "nGIawHZhgxBmcVEJ2t6Y5QDnh7wwcDdmQ4bqtFz5", "JZL0HXP9s7HcTJxmfUzBVoFlFyyLE0GYAmBke73G");
    }
}
