package com.example.rings;

import android.app.Application;

import com.droidnet.DroidNet;

public class Applications extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DroidNet.init(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        DroidNet.getInstance().removeAllInternetConnectivityChangeListeners();
    }
}
