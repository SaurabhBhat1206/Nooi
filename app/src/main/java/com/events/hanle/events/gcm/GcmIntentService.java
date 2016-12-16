package com.events.hanle.events.gcm;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.tech.NfcBarcode;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.events.hanle.events.Activity.ListOfEvent1;
import com.events.hanle.events.BroadCast.ScheduledPush;
import com.events.hanle.events.Constants.WebUrl;
import com.events.hanle.events.Model.ListEvent;
import com.events.hanle.events.Model.User;
import com.events.hanle.events.R;
import com.events.hanle.events.SqlliteDB.DBController;
import com.events.hanle.events.app.Config;
import com.events.hanle.events.app.EndPoints;
import com.events.hanle.events.app.MyApplication;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.joda.time.DateTimeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

import static android.os.Build.VERSION.SDK_INT;


public class GcmIntentService extends IntentService {

    private static final String TAG = GcmIntentService.class.getSimpleName();

    public GcmIntentService() {
        super(TAG);
    }

    public static final String KEY = "key";
    public static final String TOPIC = "topic";
    public static final String SUBSCRIBE = "subscribe";
    public static final String UNSUBSCRIBE = "unsubscribe";
    String user_id, mobileno, countrycode;

    DBController dbController;

    @Override
    protected void onHandleIntent(Intent intent) {
        String key = intent.getStringExtra(KEY);
        if (key != null) {
            switch (key) {
                case SUBSCRIBE:
                    // subscribe to a topic
                    String topic = intent.getStringExtra(TOPIC);
                    subscribeToTopic(topic);
                    break;
                case UNSUBSCRIBE:
                    break;

                default:
                    // if key is specified, register with GCM
                    registerGCM();
                    storeIntoSqllite();
                    //show();
            }
        }

    }

//    private void show() {
//
//        DBController dbController = new DBController(getApplicationContext());
//
//        //Toast.makeText(context, "Schedled push from broadcast", Toast.LENGTH_SHORT).show();
//        ArrayList<HashMap<String, String>> animalList = dbController.getallEvents();
//
//
//        System.out.println("op from sqllit:"+ animalList);
//    }


    /**
     * Registering with GCM and obtaining the gcm registration id
     */
    private void registerGCM() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
//            InstanceID instanceID = InstanceID.getInstance(this);
//            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
//                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            String token = FirebaseInstanceId.getInstance().getToken();


            Log.e(TAG, "GCM Registration Token: " + token);

            // sending the registration id to our server
            sendRegistrationToServer(token);

            sharedPreferences.edit().putBoolean(Config.SENT_TOKEN_TO_SERVER, true).apply();
        } catch (Exception e) {
            Log.e(TAG, "Failed to complete token refresh", e);

            sharedPreferences.edit().putBoolean(Config.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void storeIntoSqllite() {

        user_id = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUserId().getId();
        mobileno = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUser().getMobile();
        countrycode = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUser().getCountrycode();

        String endpoint = EndPoints.CHAT_ROOMS_LIST.replace("COUNTRY_CODE", countrycode);
        String endpoint1 = endpoint.replace("_USERID_", mobileno);
        Log.e(TAG, "end point: " + endpoint1);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                endpoint1, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    if (obj.getBoolean("error") == false) {
                        JSONArray chatRoomsArray = obj.getJSONArray("chat_rooms");
                        Log.d("Array length", String.valueOf(chatRoomsArray.length()));


                        for (int i = 0; i < chatRoomsArray.length(); i++) {
                            JSONObject chatRoomsObj = (JSONObject) chatRoomsArray.get(i);
                            dbController = new DBController(getApplicationContext());
                            HashMap<String, String> queryValues = new HashMap<String, String>();
                            queryValues.put("eID", chatRoomsObj.getString("event_id"));
                            queryValues.put("event_title", chatRoomsObj.getString("event_title"));
                            queryValues.put("event_status", chatRoomsObj.getString("event_status"));
                            queryValues.put("share_detial", chatRoomsObj.getString("share_detail"));
                            queryValues.put("user_attending_status", chatRoomsObj.getString("user_attending_status"));
                            queryValues.put("inviter_name", chatRoomsObj.getString("inviter_name"));
                            queryValues.put("date", chatRoomsObj.getString("date"));
                            queryValues.put("time", chatRoomsObj.getString("time"));
                            dbController.insertEvent(queryValues);

                            //System.out.println("SQL OP from new loop:" + entry.get("event_title"));
                            AlarmManager alarm = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

                            Intent service1 = new Intent(getApplicationContext(), ScheduledPush.class);
                            final PendingIntent pIntent = PendingIntent.getBroadcast(getApplicationContext(), ScheduledPush.REQUEST_CODE,
                                    service1, PendingIntent.FLAG_UPDATE_CURRENT);
                            //long firstMillis = System.currentTimeMillis(); // alarm is set right away
                            alarm.cancel(pIntent);

                            Calendar alarmStartTime = Calendar.getInstance();
                            Calendar now = Calendar.getInstance();
                            alarmStartTime.set(Calendar.HOUR_OF_DAY, 8);
                            alarmStartTime.set(Calendar.MINUTE, 00);
                            alarmStartTime.set(Calendar.SECOND, 0);
                            if (now.after(alarmStartTime)) {
                                Log.d("Hey", "Added a day");
                                alarmStartTime.add(Calendar.DATE, 1);
                            }
                            alarm.setRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(),
                                    AlarmManager.INTERVAL_DAY, pIntent);


                        }


                    } else {
                        // error in fetching chat rooms
                        Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }


                    // check for error flag


                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Server did not respond!!", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                //Toast.makeText(ListOfEvent1.this, "Server did not respond!!", Toast.LENGTH_SHORT).show();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(), "time out error", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Server did not respond!!", Toast.LENGTH_SHORT).show();

                }
            }
        });


        strReq.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    private void sendRegistrationToServer(final String token) {

        // checking for valid login session
        User user = MyApplication.getInstance().getPrefManager().getUser();
        if (user == null) {
            // TODO
            // user not found, redirecting him to login screen
            return;
        }

        String endPoint = EndPoints.USER.replace("_ID_", user.getId());

        Log.e(TAG, "endpoint: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.PUT,
                endPoint, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error
                    if (obj.getBoolean("error") == false) {
                        // broadcasting token sent to server
                        Intent registrationComplete = new Intent(Config.SENT_TOKEN_TO_SERVER);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(registrationComplete);
                    } else {
                        Toast.makeText(getApplicationContext(), "Unable to send gcm registration id to our sever. " + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("gcm_registration_id", token);

                Log.e(TAG, "params: " + params.toString());
                return params;
            }
        };

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    /**
     * Subscribe to a topic
     */
    public static void subscribeToTopic(String topic) {
        FirebaseMessaging pubSub = FirebaseMessaging.getInstance();
        //InstanceID instanceID = InstanceID.getInstance(MyApplication.getInstance().getApplicationContext());
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();


        String token = null;
        token = FirebaseInstanceId.getInstance().getToken();

        if (token != null) {
            FirebaseMessaging.getInstance().subscribeToTopic(topic);
            Log.e(TAG, "Subscribed to topic: " + topic);
        } else {
            Log.e(TAG, "error: gcm registration id is null");
        }
    }

//    public void unsubscribeFromTopic(String topic) {
//        GcmPubSub pubSub = GcmPubSub.getInstance(getApplicationContext());
//        InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
//        String token = null;
//        try {
//            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
//                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
//            if (token != null) {
//                pubSub.unsubscribe(token, "");
//                Log.e(TAG, "Unsubscribed from topic: " + topic);
//            } else {
//                Log.e(TAG, "error: gcm registration id is null");
//            }
//        } catch (IOException e) {
//            Log.e(TAG, "Topic unsubscribe error. Topic: " + topic + ", error: " + e.getMessage());
//            Toast.makeText(getApplicationContext(), "Topic subscribe error. Topic: " + topic + ", error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }
}
