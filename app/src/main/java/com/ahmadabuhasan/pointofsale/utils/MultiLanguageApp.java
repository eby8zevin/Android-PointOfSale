package com.ahmadabuhasan.pointofsale.utils;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

public class MultiLanguageApp extends Application {

    public void attachBaseContext(Context context) {
        super.attachBaseContext(LocaleManager.setLocale(context));
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        LocaleManager.setLocale(this);
    }
}