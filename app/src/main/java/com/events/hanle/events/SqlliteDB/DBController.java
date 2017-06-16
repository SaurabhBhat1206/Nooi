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
        super(applicationcontext, "nooisqllite.db", null, 3);
        Log.d(LOGCAT, "Created");
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String event,cancelledEvent,concludedEvent;
        event = "CREATE TABLE events (eID INTEGER PRIMARY KEY,artwork TEXT,chatW TEXT,countrycode TEXT,created_at TEXT,descriptions TEXT,dresscode TEXT,eventaddress TEXT,latitude TEXT,longitude TEXT,payment TEXT,type INT,phone TEXT,organiserId TEXT,timezone TEXT, event_title TEXT, event_status TEXT, share_detial INT, user_attending_status INT, inviter_name TEXT, dat Text,dat1 Text,tim TEXT,weekday TEXT,establishment TEXT)";
        cancelledEvent = "CREATE TABLE cancelledevents (eID INTEGER PRIMARY KEY,artwork TEXT,chatW TEXT,countrycode TEXT,created_at TEXT,descriptions TEXT,dresscode TEXT,eventaddress TEXT,latitude TEXT,longitude TEXT,payment TEXT,type INT,phone TEXT,organiserId TEXT,timezone TEXT, event_title TEXT, event_status TEXT, share_detial INT, user_attending_status INT, inviter_name TEXT, dat Text,dat1 Text,tim TEXT,weekday TEXT,establishment TEXT)";
        concludedEvent = "CREATE TABLE concludedevents (eID INTEGER PRIMARY KEY,artwork TEXT,chatW TEXT,countrycode TEXT,created_at TEXT,descriptions TEXT,dresscode TEXT,eventaddress TEXT,latitude TEXT,longitude TEXT,payment TEXT,type INT,phone TEXT,organiserId TEXT,timezone TEXT, event_title TEXT, event_status TEXT, share_detial INT, user_attending_status INT, inviter_name TEXT, dat Text,dat1 Text,tim TEXT,weekday TEXT,establishment TEXT)";
        database.execSQL(event);
        database.execSQL(cancelledEvent);
        database.execSQL(concludedEvent);
        Log.d(LOGCAT, "events Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        String event,cancelledEvent,concludedEvent;
        event = "DROP TABLE IF EXISTS events";
        cancelledEvent = "DROP TABLE IF EXISTS events";
        concludedEvent = "DROP TABLE IF EXISTS events";
        database.execSQL(event);
        database.execSQL(cancelledEvent);
        database.execSQL(concludedEvent);
        onCreate(database);
        Log.d(LOGCAT, "events Dropped");

    }

    public void insertEvent(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("eID", Integer.parseInt(queryValues.get("eID")));
        values.put("artwork", queryValues.get("artwork"));
        values.put("chatW", queryValues.get("chatW"));
        values.put("countrycode", queryValues.get("countrycode"));
        values.put("created_at", queryValues.get("created_at"));
        values.put("descriptions", queryValues.get("descriptions"));
        values.put("dresscode", queryValues.get("dresscode"));
        values.put("eventaddress", queryValues.get("eventaddress"));
        values.put("latitude", queryValues.get("latitude"));
        values.put("longitude", queryValues.get("longitude"));
        values.put("payment", queryValues.get("payment"));
        values.put("type", queryValues.get("type"));
        values.put("phone", queryValues.get("phone"));
        values.put("organiserId", queryValues.get("organiserId"));
        values.put("timezone", queryValues.get("timezone"));
        values.put("event_title", queryValues.get("event_title"));
        values.put("event_status", queryValues.get("event_status"));
        values.put("share_detial", Integer.parseInt(queryValues.get("share_detial")));
        values.put("user_attending_status", Integer.parseInt(queryValues.get("user_attending_status")));
        values.put("inviter_name", queryValues.get("inviter_name"));
        values.put("dat", queryValues.get("dat"));
        values.put("dat1", queryValues.get("dat1"));
        values.put("tim", queryValues.get("tim"));
        values.put("weekday", queryValues.get("weekday"));
        values.put("establishment", queryValues.get("establishment"));
        database.insert("events", null, values);
        database.close();

    }

    public void insertCanclledEvent(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("eID", Integer.parseInt(queryValues.get("eID")));
        values.put("artwork", queryValues.get("artwork"));
        values.put("chatW", queryValues.get("chatW"));
        values.put("countrycode", queryValues.get("countrycode"));
        values.put("created_at", queryValues.get("created_at"));
        values.put("descriptions", queryValues.get("descriptions"));
        values.put("dresscode", queryValues.get("dresscode"));
        values.put("eventaddress", queryValues.get("eventaddress"));
        values.put("latitude", queryValues.get("latitude"));
        values.put("longitude", queryValues.get("longitude"));
        values.put("payment", queryValues.get("payment"));
        values.put("type", queryValues.get("type"));
        values.put("phone", queryValues.get("phone"));
        values.put("organiserId", queryValues.get("organiserId"));
        values.put("timezone", queryValues.get("timezone"));
        values.put("event_title", queryValues.get("event_title"));
        values.put("event_status", queryValues.get("event_status"));
        values.put("share_detial", Integer.parseInt(queryValues.get("share_detial")));
        values.put("user_attending_status", Integer.parseInt(queryValues.get("user_attending_status")));
        values.put("inviter_name", queryValues.get("inviter_name"));
        values.put("dat", queryValues.get("dat"));
        values.put("dat1", queryValues.get("dat1"));
        values.put("tim", queryValues.get("tim"));
        values.put("weekday", queryValues.get("weekday"));
        values.put("establishment", queryValues.get("establishment"));
        database.insert("cancelledevents", null, values);
        database.close();

    }


    public void insertConcludedEvent(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("eID", Integer.parseInt(queryValues.get("eID")));
        values.put("artwork", queryValues.get("artwork"));
        values.put("chatW", queryValues.get("chatW"));
        values.put("countrycode", queryValues.get("countrycode"));
        values.put("created_at", queryValues.get("created_at"));
        values.put("descriptions", queryValues.get("descriptions"));
        values.put("dresscode", queryValues.get("dresscode"));
        values.put("eventaddress", queryValues.get("eventaddress"));
        values.put("latitude", queryValues.get("latitude"));
        values.put("longitude", queryValues.get("longitude"));
        values.put("payment", queryValues.get("payment"));
        values.put("type", queryValues.get("type"));
        values.put("phone", queryValues.get("phone"));
        values.put("organiserId", queryValues.get("organiserId"));
        values.put("timezone", queryValues.get("timezone"));
        values.put("event_title", queryValues.get("event_title"));
        values.put("event_status", queryValues.get("event_status"));
        values.put("share_detial", Integer.parseInt(queryValues.get("share_detial")));
        values.put("user_attending_status", Integer.parseInt(queryValues.get("user_attending_status")));
        values.put("inviter_name", queryValues.get("inviter_name"));
        values.put("dat", queryValues.get("dat"));
        values.put("dat1", queryValues.get("dat1"));
        values.put("tim", queryValues.get("tim"));
        values.put("weekday", queryValues.get("weekday"));
        values.put("establishment", queryValues.get("establishment"));
        database.insert("concludedevents", null, values);
        database.close();

    }

    public ArrayList<HashMap<String, String>> getallEvents() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM events ORDER BY eID DESC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("eID", cursor.getString(0));
                map.put("artwork",cursor.getString(1));
                map.put("chatW", cursor.getString(2));
                map.put("countrycode", cursor.getString(3));
                map.put("created_at", cursor.getString(4));
                map.put("descriptions", cursor.getString(5));
                map.put("dresscode", cursor.getString(6));
                map.put("eventaddress", cursor.getString(7));
                map.put("latitude", cursor.getString(8));
                map.put("longitude", cursor.getString(9));
                map.put("payment", cursor.getString(10));
                map.put("type", cursor.getString(11));
                map.put("phone", cursor.getString(12));
                map.put("organiserId", cursor.getString(13));
                map.put("timezone",cursor.getString(14));
                map.put("event_title", cursor.getString(15));
                map.put("event_status", cursor.getString(16));
                map.put("share_detial", cursor.getString(17));
                map.put("user_attending_status", cursor.getString(18));
                map.put("inviter_name", cursor.getString(19));
                map.put("dat", cursor.getString(20));
                map.put("dat1", cursor.getString(21));
                map.put("tim", cursor.getString(22));
                map.put("weekday", cursor.getString(23));
                map.put("establishment", cursor.getString(24));


                wordList.add(map);
            } while (cursor.moveToNext());
        }

        // return contact list
        return wordList;


    }
    public ArrayList<HashMap<String, String>> getCancelledEvents() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM cancelledevents ORDER BY eID DESC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("eID", cursor.getString(0));
                map.put("artwork",cursor.getString(1));
                map.put("chatW", cursor.getString(2));
                map.put("countrycode", cursor.getString(3));
                map.put("created_at", cursor.getString(4));
                map.put("descriptions", cursor.getString(5));
                map.put("dresscode", cursor.getString(6));
                map.put("eventaddress", cursor.getString(7));
                map.put("latitude", cursor.getString(8));
                map.put("longitude", cursor.getString(9));
                map.put("payment", cursor.getString(10));
                map.put("type", cursor.getString(11));
                map.put("phone", cursor.getString(12));
                map.put("organiserId", cursor.getString(13));
                map.put("timezone",cursor.getString(14));
                map.put("event_title", cursor.getString(15));
                map.put("event_status", cursor.getString(16));
                map.put("share_detial", cursor.getString(17));
                map.put("user_attending_status", cursor.getString(18));
                map.put("inviter_name", cursor.getString(19));
                map.put("dat", cursor.getString(20));
                map.put("dat1", cursor.getString(21));
                map.put("tim", cursor.getString(22));
                map.put("weekday", cursor.getString(23));
                map.put("establishment", cursor.getString(24));
                wordList.add(map);
            } while (cursor.moveToNext());
        }

        // return contact list
        return wordList;


    }


    public ArrayList<HashMap<String, String>> getAllConcludedEvents() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM concludedevents ORDER BY eID DESC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("eID", cursor.getString(0));
                map.put("artwork",cursor.getString(1));
                map.put("chatW", cursor.getString(2));
                map.put("countrycode", cursor.getString(3));
                map.put("created_at", cursor.getString(4));
                map.put("descriptions", cursor.getString(5));
                map.put("dresscode", cursor.getString(6));
                map.put("eventaddress", cursor.getString(7));
                map.put("latitude", cursor.getString(8));
                map.put("longitude", cursor.getString(9));
                map.put("payment", cursor.getString(10));
                map.put("type", cursor.getString(11));
                map.put("phone", cursor.getString(12));
                map.put("organiserId", cursor.getString(13));
                map.put("timezone",cursor.getString(14));
                map.put("event_title", cursor.getString(15));
                map.put("event_status", cursor.getString(16));
                map.put("share_detial", cursor.getString(17));
                map.put("user_attending_status", cursor.getString(18));
                map.put("inviter_name", cursor.getString(19));
                map.put("dat", cursor.getString(20));
                map.put("dat1", cursor.getString(21));
                map.put("tim", cursor.getString(22));
                map.put("weekday", cursor.getString(23));
                map.put("establishment", cursor.getString(24));
                wordList.add(map);
            } while (cursor.moveToNext());
        }

        // return contact list
        return wordList;


    }

    public HashMap<String, String> getfromEventId(String id) {
        HashMap<String, String> map = new HashMap<String, String>();
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM events where eID='"+id+"'";
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                //HashMap<String, String> map = new HashMap<String, String>();
                map.put("artwork",cursor.getString(1));
                map.put("chatW", cursor.getString(2));
                map.put("countrycode", cursor.getString(3));
                map.put("created_at", cursor.getString(4));
                map.put("descriptions", cursor.getString(5));
                map.put("dresscode", cursor.getString(6));
                map.put("eventaddress", cursor.getString(7));
                map.put("latitude", cursor.getString(8));
                map.put("longitude", cursor.getString(9));
                map.put("payment", cursor.getString(10));
                map.put("type", cursor.getString(11));
                map.put("phone", cursor.getString(12));
                map.put("organiserId", cursor.getString(13));
                map.put("timezone",cursor.getString(14));
                map.put("event_title", cursor.getString(15));
                map.put("event_status", cursor.getString(16));
                map.put("share_detial", cursor.getString(17));
                map.put("user_attending_status", cursor.getString(18));
                map.put("inviter_name", cursor.getString(19));
                map.put("dat", cursor.getString(20));
                map.put("dat1", cursor.getString(21));
                map.put("tim", cursor.getString(22));
                map.put("weekday", cursor.getString(23));
                map.put("establishment", cursor.getString(24));
                //wordList.add(map);
            } while (cursor.moveToNext());
        }
        return map;
    }

    public HashMap<String, String> getfromCancelledEventId(String id) {
        HashMap<String, String> map = new HashMap<String, String>();
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM cancelledevents where eID='"+id+"'";
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                //HashMap<String, String> map = new HashMap<String, String>();
                map.put("artwork",cursor.getString(1));
                map.put("chatW", cursor.getString(2));
                map.put("countrycode", cursor.getString(3));
                map.put("created_at", cursor.getString(4));
                map.put("descriptions", cursor.getString(5));
                map.put("dresscode", cursor.getString(6));
                map.put("eventaddress", cursor.getString(7));
                map.put("latitude", cursor.getString(8));
                map.put("longitude", cursor.getString(9));
                map.put("payment", cursor.getString(10));
                map.put("type", cursor.getString(11));
                map.put("phone", cursor.getString(12));
                map.put("organiserId", cursor.getString(13));
                map.put("timezone",cursor.getString(14));
                map.put("event_title", cursor.getString(15));
                map.put("event_status", cursor.getString(16));
                map.put("share_detial", cursor.getString(17));
                map.put("user_attending_status", cursor.getString(18));
                map.put("inviter_name", cursor.getString(19));
                map.put("dat", cursor.getString(20));
                map.put("dat1", cursor.getString(21));
                map.put("tim", cursor.getString(22));
                map.put("weekday", cursor.getString(23));
                map.put("establishment", cursor.getString(24));
                //wordList.add(map);
            } while (cursor.moveToNext());
        }
        return map;
    }


    public HashMap<String, String> getfromConcludedEventId(String id) {
        HashMap<String, String> map = new HashMap<String, String>();
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM concludedevents where eID='"+id+"'";
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                //HashMap<String, String> map = new HashMap<String, String>();
                map.put("artwork",cursor.getString(1));
                map.put("chatW", cursor.getString(2));
                map.put("countrycode", cursor.getString(3));
                map.put("created_at", cursor.getString(4));
                map.put("descriptions", cursor.getString(5));
                map.put("dresscode", cursor.getString(6));
                map.put("eventaddress", cursor.getString(7));
                map.put("latitude", cursor.getString(8));
                map.put("longitude", cursor.getString(9));
                map.put("payment", cursor.getString(10));
                map.put("type", cursor.getString(11));
                map.put("phone", cursor.getString(12));
                map.put("organiserId", cursor.getString(13));
                map.put("timezone",cursor.getString(14));
                map.put("event_title", cursor.getString(15));
                map.put("event_status", cursor.getString(16));
                map.put("share_detial", cursor.getString(17));
                map.put("user_attending_status", cursor.getString(18));
                map.put("inviter_name", cursor.getString(19));
                map.put("dat", cursor.getString(20));
                map.put("dat1", cursor.getString(21));
                map.put("tim", cursor.getString(22));
                map.put("weekday", cursor.getString(23));
                map.put("establishment", cursor.getString(24));
                //wordList.add(map);
            } while (cursor.moveToNext());
        }
        return map;
    }

    public int updateEvent(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("event_status", queryValues.get("event_status"));
        values.put("user_attending_status", queryValues.get("user_attending_status"));
        System.out.println("Events Table Updated"+queryValues.get("eID"));
        return database.update("events", values, "eID" + " = ?", new String[] { queryValues.get("eID") });
    }

    public void deleteEvent(String id) {
        Log.d(LOGCAT,"delete");
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM  events where eID='"+ id +"'";
        Log.d("query",deleteQuery);
        database.execSQL(deleteQuery);
    }

    public void DeleteEvent(){
        Log.d(LOGCAT,"deleting event*******");

        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM events";
        Log.d("query",deleteQuery);
        database.execSQL(deleteQuery);
    }

    public void DeleteCancellEvent(){
        Log.d(LOGCAT,"deleting cancelled event*******");

        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM cancelledevents";
        Log.d("query",deleteQuery);
        database.execSQL(deleteQuery);
    }

    public void DeleteConcludeEvent(){
        Log.d(LOGCAT,"deleting concluded event*******");

        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM concludedevents";
        Log.d("query",deleteQuery);
        database.execSQL(deleteQuery);
    }

}
