package com.events.hanle.events.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;
import android.support.v4.util.LruCache;
import android.text.TextUtils;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
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
    private static Context mCtx;
    private ImageLoader imageLoader;

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

    public MyApplication() {

    }

    private MyApplication(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

        imageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static synchronized MyApplication getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyApplication(context);
        }
        return mInstance;
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

    public ImageLoader getImageLoader() {
        return imageLoader;
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
