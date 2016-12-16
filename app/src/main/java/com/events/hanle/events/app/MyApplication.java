package com.events.hanle.events.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.events.hanle.events.Activity.LoginActivity;
import com.events.hanle.events.Constants.MyPreferenceManager;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import io.fabric.sdk.android.Fabric;


public class MyApplication extends Application {

    public static final String TAG = MyApplication.class
            .getSimpleName();

    private RequestQueue mRequestQueue;

    private static MyApplication mInstance;

    private MyPreferenceManager pref;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        OneSignal.startInit(this).init();

//                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//                .init();
        //      OneSignal.sendTag("test12", "test12");
        //       JSONObject tags = new JSONObject();
//        try {
        //           tags.put("event22", "event_value22");


        //OneSignal.sendTags(tags);
        //      } catch (JSONException e) {
        //           e.printStackTrace();
        //       }

        mInstance = this;


    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            Cache cache = new DiskBasedCache(getApplicationContext().getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
        }

        return mRequestQueue;
    }

    public MyPreferenceManager getPrefManager() {
        if (pref == null) {
            pref = new MyPreferenceManager(this);
        }
        return pref;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void logout() {
        pref.clear();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
