package com.gaige.mylibrary;

import android.app.Application;
import android.content.Context;

/**
 * Created by gaoqun on 2016/7/20.
 */
public class App extends Application {
    public static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }
}
