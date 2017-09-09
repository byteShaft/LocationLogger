package com.byteshaft.locationlogger.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppGlobals extends Application {

    private static final String APP_STATUS = "app_status";
    private static final String UNIQUE_DEVICE_ID = "unique_device_id";
    private static final String USERNAME = "username";
    private static final String AGE = "age";
    private static final String GENDER = "gender";
    private static final String ADVERSARY_ADDED = "adversary_added";
    private static final String ADVERSARY_NAME = "adversary_name";
    private static final String USER_TEST_RESULTS = "user_test_results";
    private static final String ADVERSARY_TEST_RESULTS = "adversary_test_results";
    private static final String RELATION_WITH_ADVERSARY = "relation_with_adversary";
    private static final String NOTIFICATION_TIME_IN_MILLIS = "notification_time_in_millis";
    private static final String TEST_TAKEN_BY_ADVERSARY = "test_taken_by_adversary";
    private static final String TIME_TAKEN_FOR_TEST_BY_USER = "time_taken_for_test_by_user";
    private static final String TIME_TAKEN_FOR_TEST_BY_ADVERSARY = "time_taken_for_test_by_adversary";
    private static final String LOCATION_SERVICE = "location_service";
    private static final String FULL_NAME = "full_name";
    private static final String LOCATION_SERVICE_PAUSED = "location_service_paused";
    public static final String TIME_TAKEN_FOR_EACH_QUESTION_BY_USER = "time_taken_user";
    public static final String TIME_TAKEN_FOR_EACH_QUESTION_BY_ADVERSARY = "time_taken_adversary";
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

    public static long getNotificationTime() {
        return sPreferences.getLong(NOTIFICATION_TIME_IN_MILLIS, 0);
    }

    public static void putNotificationTime(long time) {
        sPreferences.edit().putLong(NOTIFICATION_TIME_IN_MILLIS, time).apply();
    }

    public static void saveTimeTakenForEachQuestionByUser(StringBuilder time) {
        sPreferences.edit().putString(TIME_TAKEN_FOR_EACH_QUESTION_BY_USER, time.toString()).apply();
    }

    public static String getTimeTakenForEachQuestionByUser() {
        return sPreferences.getString(TIME_TAKEN_FOR_EACH_QUESTION_BY_USER, null);
    }

    public static void saveTimeTakenForEachQuestionByAdversary(StringBuilder time) {
        sPreferences.edit().putString(TIME_TAKEN_FOR_EACH_QUESTION_BY_ADVERSARY, time.toString()).apply();
    }

    public static String getTimeTakenForEachQuestionByAdversary() {
        return sPreferences.getString(TIME_TAKEN_FOR_EACH_QUESTION_BY_ADVERSARY, null);
    }

    public static boolean isAdversaryAdded() {
        return sPreferences.getBoolean(ADVERSARY_ADDED, true);
    }

    public static void testTakenByAdversary(boolean testTakenByAdversary) {
        sPreferences.edit().putBoolean(TEST_TAKEN_BY_ADVERSARY, testTakenByAdversary).apply();
    }

    public static boolean isTestTakenByAdversary() {
        return sPreferences.getBoolean(TEST_TAKEN_BY_ADVERSARY, false);
    }

    public static void putAdversaryAdded(boolean adversaryAdded) {
        sPreferences.edit().putBoolean(ADVERSARY_ADDED, adversaryAdded).apply();
    }
    
    public static void putUserTestResults(String results) {
        sPreferences.edit().putString(USER_TEST_RESULTS, results).apply();
    }

    public static void putAdversaryTestResults(String adversaryResults) {
        sPreferences.edit().putString(ADVERSARY_TEST_RESULTS, adversaryResults).apply();
    }

    public static void putTimeTakenForTestByUser(String time) {
        sPreferences.edit().putString(TIME_TAKEN_FOR_TEST_BY_USER, time).apply();
    }

    public static void setLocationServiceStarted(boolean locationService) {
        sPreferences.edit().putBoolean(LOCATION_SERVICE, locationService).apply();
    }

    public static boolean isLocationServiceEnabled() {
        return sPreferences.getBoolean(LOCATION_SERVICE, false);
    }

    public static void putFullName(String fullName) {
        sPreferences.edit().putString(FULL_NAME, fullName).apply();
    }

    public static String getTimeTakenForTestByUser() {
        return sPreferences.getString(TIME_TAKEN_FOR_TEST_BY_USER, null);
    }

    public static void setLocationServicePaused(boolean locationService) {
        sPreferences.edit().putBoolean(LOCATION_SERVICE_PAUSED, locationService).apply();
    }

    public static boolean isLocationServicePaused() {
        return sPreferences.getBoolean(LOCATION_SERVICE_PAUSED, false);
    }

    public static String getFullName() {
        return sPreferences.getString(FULL_NAME, null);
    }

    public static String getAdversaryTestResults() {
        return sPreferences.getString(ADVERSARY_TEST_RESULTS, null);
    }
    
    public static String getUserTestResults() {
        return sPreferences.getString(USER_TEST_RESULTS, null);
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

    public static void putTimeTakenForTestByAdversary(String time) {
        sPreferences.edit().putString(TIME_TAKEN_FOR_TEST_BY_ADVERSARY, time).apply();
    }

    public static String getTimeTakenForTestByAdversary() {
        return sPreferences.getString(TIME_TAKEN_FOR_TEST_BY_ADVERSARY, null);
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
