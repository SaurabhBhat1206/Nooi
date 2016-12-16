package com.events.hanle.events.SqlliteDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Hanle on 10/20/2016.
 */

public class DBController extends SQLiteOpenHelper {
    private static final String LOGCAT = null;

    public DBController(Context applicationcontext) {
        super(applicationcontext, "nooisqllite.db", null, 2);
        Log.d(LOGCAT, "Created");
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;
        query = "CREATE TABLE events (eID INTEGER PRIMARY KEY, event_title TEXT, event_status TEXT, share_detial INT, user_attending_status INT, inviter_name TEXT, dat Text, tim TEXT)";
        database.execSQL(query);
        Log.d(LOGCAT, "events Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        String query;
        query = "DROP TABLE IF EXISTS events";
        database.execSQL(query);
        onCreate(database);
        Log.d(LOGCAT, "events Dropped");

    }

    public void insertEvent(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("eID", Integer.parseInt(queryValues.get("eID")));
        values.put("event_title", queryValues.get("event_title"));
        values.put("event_status", queryValues.get("event_status"));
        values.put("share_detial", Integer.parseInt(queryValues.get("share_detial")));
        values.put("user_attending_status", Integer.parseInt(queryValues.get("user_attending_status")));
        values.put("inviter_name", queryValues.get("inviter_name"));
        values.put("dat", queryValues.get("date"));
        values.put("tim", queryValues.get("time"));
        database.insert("events", null, values);
        database.close();

    }

    public ArrayList<HashMap<String, String>> getallEvents() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM events";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("eID", cursor.getString(0));
                map.put("event_title", cursor.getString(1));
                map.put("event_status", cursor.getString(2));
                map.put("share_detial", cursor.getString(3));
                map.put("user_attending_status", cursor.getString(4));
                map.put("inviter_name", cursor.getString(5));
                map.put("dat", cursor.getString(6));
                map.put("tim", cursor.getString(7));
                wordList.add(map);
            } while (cursor.moveToNext());
        }

        // return contact list
        return wordList;


    }



}
