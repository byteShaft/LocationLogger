package com.byteshaft.locationlogger.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by fi8er1 on 18/03/2017.
 */

public class AppGlobals extends Application {

    private static final String APP_STATUS = "app_status";
    private static final String UNIQUE_DEVICE_ID = "unique_device_id";
    private static Context sContext;
    private static SharedPreferences sPreferences;

    public static Context getContext() {
        return sContext;
    }

    public static int getAppStatus() {
        return sPreferences.getInt(APP_STATUS, 0);
    }

    public static void putAppStatus(int status) {
        sPreferences.edit().putInt(APP_STATUS, status).apply();
    }

    public static String getUniqueDeviceId() {
        return sPreferences.getString(UNIQUE_DEVICE_ID, null);
    }

    public static void putUniqueDeviceID(String id  ) {
        sPreferences.edit().putString(UNIQUE_DEVICE_ID, id).apply();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        sPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }
}
