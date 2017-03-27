package com.byteshaft.locationlogger.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    public void createNewEntry(String username, String latitude, String longitude) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseConstants.USERNAME, username);
        values.put(DatabaseConstants.LATITUDE, latitude);
        values.put(DatabaseConstants.LONGITUDE, longitude);
        db.insert(DatabaseConstants.TABLE_NAME, null, values);
        db.close();
    }

    public HashMap<String, String> getAllRecords() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM "
                + DatabaseConstants.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        HashMap<String, String> hashMap = new HashMap<>();
        while (cursor.moveToNext()) {
            int unique_id = cursor.getInt(
                    cursor.getColumnIndex(DatabaseConstants.ID_COLUMN));
            String name = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.USERNAME));
            String latitude = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.LATITUDE));
            String longitude = cursor.getString(
                    cursor.getColumnIndex(DatabaseConstants.LONGITUDE));
            hashMap.put("unique_id", String.valueOf(unique_id));
            hashMap.put("username", name);
            hashMap.put("latitude", latitude);
            hashMap.put("longitude", longitude);
        }
        db.close();
        cursor.close();
        return hashMap;
    }
}
