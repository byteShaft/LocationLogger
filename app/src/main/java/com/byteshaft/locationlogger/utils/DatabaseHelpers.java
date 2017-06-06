package com.byteshaft.locationlogger.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;


public class DatabaseHelpers extends SQLiteOpenHelper {

    public DatabaseHelpers(Context context) {
        super(context, DatabaseConstants.DATABASE_NAME, null, DatabaseConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseConstants.TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseConstants.TABLE_NAME);
        onCreate(db);
    }

    public void createNewEntry(String latitude, String longitude, String timestamp,
                               String timeAtOnePlace) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        // putting the values in the database along with the unique keys
        values.put(DatabaseConstants.LATITUDE, latitude);
        values.put(DatabaseConstants.LONGITUDE, longitude);
        values.put(DatabaseConstants.TIMESTAMP, timestamp);
        values.put(DatabaseConstants.TIME_AT_ONE_PLACE, timeAtOnePlace);
        db.insert(DatabaseConstants.TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<HashMap<String, String>> getRandomRecordFromAllRecords() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM "
                + DatabaseConstants.TABLE_NAME
                + " ORDER BY RANDOM()" + " LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<HashMap<String, String>> userRecords = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap<String, String> hashMap = new HashMap<>();
            String latitude = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.LATITUDE));
            String longitude = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.LONGITUDE));
            String timestamp = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.TIMESTAMP));
            String timeAtOnePlace = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.TIME_AT_ONE_PLACE));
            hashMap.put(DatabaseConstants.LATITUDE, latitude);
            hashMap.put(DatabaseConstants.LONGITUDE, longitude);
            hashMap.put(DatabaseConstants.TIMESTAMP, timestamp);
            hashMap.put(DatabaseConstants.TIME_AT_ONE_PLACE, timeAtOnePlace);

            userRecords.add(hashMap);
        }
        db.close();
        cursor.close();
        return userRecords;
    }
}