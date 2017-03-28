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
    private static final String USERNAME = "username";
    private static final String AGE = "age";
    private static final String GENDER = "gender";
    private static final String ADVERSARY_ADDED = "adversary_added";
    private static final String ADVERSARY_NAME = "adversary_name";
    private static final String RELATION_WITH_ADVERSARY = "relation_with_adversary";
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

    public static boolean isAdversaryAdded() {
        return sPreferences.getBoolean(ADVERSARY_ADDED, false);
    }

    public static void putAdversaryAdded(boolean adversaryAdded) {
        sPreferences.edit().putBoolean(ADVERSARY_ADDED, adversaryAdded).apply();
    }

    public static String getUsername() {
        return sPreferences.getString(USERNAME, null);
    }

    public static void putUserName(String username) {
        sPreferences.edit().putString(USERNAME, username).apply();
    }

    public static String getAdversaryName() {
        return sPreferences.getString(ADVERSARY_NAME, null);
    }

    public static void putAdversaryName(String adversaryName) {
        sPreferences.edit().putString(ADVERSARY_NAME, adversaryName).apply();
    }

    public static String getRelationWithAdversary() {
        return sPreferences.getString(RELATION_WITH_ADVERSARY, null);
    }

    public static void putRelationWithAdversary(String relationWithAdversary) {
        sPreferences.edit().putString(RELATION_WITH_ADVERSARY, relationWithAdversary).apply();
    }

    public static String getGender() {
        return sPreferences.getString(GENDER, null);
    }

    public static void putGender(String gender) {
        sPreferences.edit().putString(GENDER, gender).apply();
    }

    public static String getAge() {
        return sPreferences.getString(AGE, null);
    }

    public static void putAge(String age) {
        sPreferences.edit().putString(AGE, age).apply();
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
