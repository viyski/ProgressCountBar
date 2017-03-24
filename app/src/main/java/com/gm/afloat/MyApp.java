package com.gm.afloat;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by lgm on 2017/3/21.
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
