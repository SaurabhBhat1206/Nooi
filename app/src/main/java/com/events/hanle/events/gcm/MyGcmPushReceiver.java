///**
// * Copyright 2015 Google Inc. All Rights Reserved.
// * <p/>
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// * <p/>
// * http://www.apache.org/licenses/LICENSE-2.0
// * <p/>
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.events.hanle.events.gcm;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.content.LocalBroadcastManager;
//import android.text.TextUtils;
//import android.util.Log;
//import android.widget.Toast;
//
//
//import com.events.hanle.events.Activity.ListOfEvent1;
//import com.events.hanle.events.Model.Message;
//import com.events.hanle.events.Model.User;
//import com.events.hanle.events.app.Config;
//import com.events.hanle.events.app.MyApplication;
//import com.google.firebase.messaging.FirebaseMessagingService;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//
//public class MyGcmPushReceiver extends FirebaseMessagingService {
//
//    private static final String TAG = MyGcmPushReceiver.class.getSimpleName();
//
//    private NotificationUtils notificationUtils;
////    public static final String MyPREFERENCES = "MyPrefs";
////    public static final String CRID = "crid";
////    public static final String MSG = "msg";
//
//
//    /**
//     * Called when message is received.
//     *
//     * @param from   SenderID of the sender.
//     * @param bundle Data bundle containing message data as key/value pairs.
//     *               For Set of keys use data.keySet().
//     */
//
//    @Override
//    public void onMessageReceived(String from, Bundle bundle) {
//        String title = bundle.getString("title");
//        Boolean isBackground = Boolean.valueOf(bundle.getString("is_background"));
//        String flag = bundle.getString("flag");
//        String data = bundle.getString("data");
//        Log.d(TAG, "From: " + from);
//        Log.d(TAG, "title: " + title);
//        Log.d(TAG, "isBackground: " + isBackground);
//        Log.d(TAG, "flag: " + flag);
//        Log.d(TAG, "data: " + data);
//
//        if (flag == null)
//            return;
//
//        if (MyApplication.getInstance().getPrefManager().getUser() == null) {
//            // user is not logged in, skipping push notification
//            Log.e(TAG, "user is not logged in, skipping push notification");
//            return;
//        }
//
//        if (from.startsWith("/topics/")) {
//            // message received from some topic.
//        } else {
//            // normal downstream message.
//        }
//
//        switch (Integer.parseInt(flag)) {
//            case Config.PUSH_TYPE_CHATROOM:
//                // push notification belongs to a chat room
//                processChatRoomPush(title, isBackground, data);
//                break;
//            case Config.PUSH_TYPE_USER:
//                // push notification is specific to user
//                processUserMessage(title, isBackground, data);
//                break;
//        }
//    }
//
//    /**
//     * Processing chat room push message
//     * this message will be broadcasts to all the activities registered
//     */
//    private void processChatRoomPush(String title, boolean isBackground, String data) {
//        if (!isBackground) {
//
//            try {
//                JSONObject datObj = new JSONObject(data);
//
//                String chatRoomId = datObj.getString("chat_room_id");
//
//                JSONObject mObj = datObj.getJSONObject("message");
//                Message message = new Message();
//                message.setMessage(mObj.getString("message"));
//                message.setId(mObj.getString("message_id"));
//                message.setCreatedAt(mObj.getString("created_at"));
//                message.setName(mObj.getString("name"));
//                String s = message.setMessage(mObj.getString("message"));
//                Log.e("crid", chatRoomId);
//
//                JSONObject uObj = datObj.getJSONObject("user");
//
//                // skip the message if the message belongs to same user as
//                // the user would be having the same message when he was sending
//                // but it might differs in your scenario
//                if (uObj.getString("user_id").equals(MyApplication.getInstance().getPrefManager().getUser().getId())) {
//                    Log.e(TAG, "Skipping the push message as it belongs to same user");
//                    return;
//                }
//
//                User user = new User();
//                user.setId(uObj.getString("user_id"));
//                user.setMobile(uObj.getString("phone"));
//                user.setName(uObj.getString("name"));
//                message.setUser(user);
//
////                sharedpreferences = getApplicationContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
////                SharedPreferences.Editor editor = sharedpreferences.edit();
////                editor.putString(CRID,chatRoomId);
////                editor.putString(MSG,s);
////                editor.putBoolean("pushcount", true);
////                editor.commit();
//
//                // verifying whether the app is in background or foreground
//                if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
//
//                    // app is in foreground, broadcast the push message
//                    Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
//                    pushNotification.putExtra("type", Config.PUSH_TYPE_CHATROOM);
//                    pushNotification.putExtra("message", message);
//                    pushNotification.putExtra("chat_room_id", chatRoomId);
//                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
//
//                    // play notification sound
//                    NotificationUtils notificationUtils = new NotificationUtils();
//                    notificationUtils.playNotificationSound();
//
//                } else {
//
//                    // app is in background. show the message in notification try
//                    Intent resultIntent = new Intent(getApplicationContext(), ListOfEvent1.class);
//                    resultIntent.putExtra("chat_room_id", chatRoomId);
//                    resultIntent.putExtra("me", message);
//                    showNotificationMessage(getApplicationContext(), title, message.getName() + " : " + "message", message.getCreatedAt(), resultIntent);
//                }
//
//            } catch (JSONException e) {
//                Log.e(TAG, "json parsing error: " + e.getMessage());
//                Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//            }
//
//        } else {
//            // the push notification is silent, may be other operations needed
//            // like inserting it in to SQLite
//        }
//    }
//
//    /**
//     * Processing user specific push message
//     * It will be displayed with / without image in push notification tray
//     */
//    private void processUserMessage(String title, boolean isBackground, String data) {
//        if (!isBackground) {
//
//            try {
//                JSONObject datObj = new JSONObject(data);
//
//                String imageUrl = datObj.getString("image");
//
//                JSONObject mObj = datObj.getJSONObject("message");
//                Message message = new Message();
//                message.setMessage(mObj.getString("message"));
//                message.setId(mObj.getString("message_id"));
//                message.setCreatedAt(mObj.getString("created_at"));
//
//                JSONObject uObj = datObj.getJSONObject("user");
//                User user = new User();
//                user.setId(uObj.getString("user_id"));
//                user.setMobile(uObj.getString("phone"));
//                user.setName(uObj.getString("name"));
//                message.setUser(user);
//
//                // verifying whether the app is in background or foreground
//                if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
//
//                    // app is in foreground, broadcast the push message
//                    Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
//                    pushNotification.putExtra("type", Config.PUSH_TYPE_USER);
//                    pushNotification.putExtra("message", message);
//                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
//
//                    // play notification sound
//                    NotificationUtils notificationUtils = new NotificationUtils();
//                    notificationUtils.playNotificationSound();
//                } else {
//
//                    // app is in background. show the message in notification try
//                    Intent resultIntent = new Intent(getApplicationContext(), ListOfEvent1.class);
//
//                    // check for push notification image attachment
//                    if (TextUtils.isEmpty(imageUrl)) {
//                        showNotificationMessage(getApplicationContext(), title, message.getName() + " : " + "message", message.getCreatedAt(), resultIntent);
//                    } else {
//                        // push notification contains image
//                        // show it with the image
//                        showNotificationMessageWithBigImage(getApplicationContext(), title, message.getName(), message.getCreatedAt(), resultIntent, imageUrl);
//                    }
//
//                }
//            } catch (JSONException e) {
//                Log.e(TAG, "json parsing error: " + e.getMessage());
//                Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//            }
//
//        } else {
//            // the push notification is silent, may be other operations needed
//            // like inserting it in to SQLite
//        }
//    }
//
//    /**
//     * Showing notification with text only
//     */
//    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
//        notificationUtils = new NotificationUtils(context);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
//    }
//
//    /**
//     * Showing notification with text and image
//     */
//    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
//        notificationUtils = new NotificationUtils(context);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
//    }
//}

//  ---------->>>>GCM INTENT SERVICE CODE<<<<<-------


//package com.events.hanle.events.gcm;
//
//import android.app.AlarmManager;
//import android.app.IntentService;
//import android.app.PendingIntent;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.nfc.tech.NfcBarcode;
//import android.os.Build;
//import android.os.Bundle;
//import android.preference.PreferenceManager;
//import android.support.v4.content.LocalBroadcastManager;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.NetworkResponse;
//import com.android.volley.NoConnectionError;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.ServerError;
//import com.android.volley.TimeoutError;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.events.hanle.events.Activity.ListOfEvent1;
//import com.events.hanle.events.BroadCast.ScheduledPush;
//import com.events.hanle.events.Constants.WebUrl;
//import com.events.hanle.events.Model.ListEvent;
//import com.events.hanle.events.Model.User;
//import com.events.hanle.events.R;
//import com.events.hanle.events.SqlliteDB.DBController;
//import com.events.hanle.events.app.Config;
//import com.events.hanle.events.app.EndPoints;
//import com.events.hanle.events.app.MyApplication;
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.messaging.FirebaseMessaging;
//
//import org.joda.time.DateTimeUtils;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.TimeZone;
//
//import static android.os.Build.VERSION.SDK_INT;
//
//
//public class GcmIntentService extends IntentService {
//
//    private static final String TAG = GcmIntentService.class.getSimpleName();
//
//    public GcmIntentService() {
//        super(TAG);
//    }
//
//    public static final String KEY = "key";
//    public static final String TOPIC = "topic";
//    public static final String SUBSCRIBE = "subscribe";
//    public static final String UNSUBSCRIBE = "unsubscribe";
//    public static final String PUSHNOTIFICATION = "pushnotification";
//
//    public static final long INTERVALONEMINUTE = 1 * 60 * 1000;
//    DBController dbController;
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//        String key = intent.getStringExtra(KEY);
//        if (key != null) {
//            switch (key) {
//                case SUBSCRIBE:
//                    // subscribe to a topic
//                    String topic = intent.getStringExtra(TOPIC);
//                    subscribeToTopic(topic);
//                    break;
//                case UNSUBSCRIBE:
//                    break;
//                case PUSHNOTIFICATION:
//                    callBroda(intent);
//                    break;
//                default:
//                    // if key is specified, register with GCM
//                    registerGCM();
//                    fetchChatRooms();
//            }
//        }
//
//    }
//
//    private void callBroda(Intent intent) {
//
//        System.out.println("Broda op:" + intent.getStringExtra("title") + "," + intent.getStringExtra("") + "," + intent.getSerializableExtra("data"));
//    }
//
//    /**
//     * Registering with GCM and obtaining the gcm registration id
//     */
//    private void registerGCM() {
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//
//        try {
////            InstanceID instanceID = InstanceID.getInstance(this);
////            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
////                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
//
//            String token = FirebaseInstanceId.getInstance().getToken();
//
//
//            Log.e(TAG, "GCM Registration Token: " + token);
//
//            // sending the registration id to our server
//            sendRegistrationToServer(token);
//
//            sharedPreferences.edit().putBoolean(Config.SENT_TOKEN_TO_SERVER, true).apply();
//        } catch (Exception e) {
//            Log.e(TAG, "Failed to complete token refresh", e);
//
//            sharedPreferences.edit().putBoolean(Config.SENT_TOKEN_TO_SERVER, false).apply();
//        }
//        // Notify UI that registration has completed, so the progress indicator can be hidden.
//        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
//    }
//
//    private void fetchChatRooms() {
//        String mobileno = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUser().getMobile();
//        String countrycode = com.events.hanle.events.app.MyApplication.getInstance().getPrefManager().getUser().getCountrycode();
//
//        String endpoint = EndPoints.CHAT_ROOMS_LIST.replace("COUNTRY_CODE", countrycode);
//        String endpoint1 = endpoint.replace("_USERID_", mobileno);
//
//        Log.e(TAG, "end point: " + endpoint);
//
//
//        StringRequest strReq = new StringRequest(Request.Method.GET,
//                endpoint1, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                Log.e(TAG, "response: " + response);
//
//                try {
//                    JSONObject obj = new JSONObject(response);
//
//                    if (obj.getBoolean("error") == false) {
//                        JSONArray chatRoomsArray = obj.getJSONArray("chat_rooms");
//                        Log.d("Array length", String.valueOf(chatRoomsArray.length()));
//                        for (int i = 0; i < chatRoomsArray.length(); i++) {
//                            JSONObject chatRoomsObj = (JSONObject) chatRoomsArray.get(i);
//
//                            dbController = new DBController(getApplicationContext());
//                            HashMap<String, String> queryValues = new HashMap<String, String>();
//                            queryValues.put("eID", chatRoomsObj.getString("event_id"));
//                            queryValues.put("event_title", chatRoomsObj.getString("event_title"));
//                            queryValues.put("event_status", chatRoomsObj.getString("event_status"));
//                            queryValues.put("share_detial", chatRoomsObj.getString("share_detail"));
//                            queryValues.put("user_attending_status", chatRoomsObj.getString("user_attending_status"));
//                            queryValues.put("inviter_name", chatRoomsObj.getString("inviter_name"));
//                            queryValues.put("date", chatRoomsObj.getString("date"));
//                            queryValues.put("time", chatRoomsObj.getString("time"));
//                            dbController.insertEvent(queryValues);
//                        }
//
//                        ArrayList<HashMap<String, String>> animalList = dbController.getallEvents();
//                        for (HashMap<String, String> entry : animalList) {
//                            System.out.println("SQL OP from new loop:" + entry.get("dat"));
//
//
//                            //System.out.println("SQL OP:" + animalList.get(j).get("dat"));
//
//                            Calendar cal = Calendar.getInstance(TimeZone.getDefault());
//                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//                            SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
//
//                            Date currentLocalTime = cal.getTime();
//                            formatter.setTimeZone(TimeZone.getDefault());
//                            String localTime2 = formatter.format(currentLocalTime);
//                            String localTime3 = formatter1.format(currentLocalTime);
//                            Date currentD = formatter.parse(localTime2);
//
//                            // Date d1 = formatter1.parse(animalList.get(j).get("dat") + " " + animalList.get(j).get("tim"));
//                            //Date d2 = formatter1.parse(localTime3);
//                            //Date d2 = formatter1.parse("10/10/2013 11:30");
//
//                            //Interval interval =new Interval(d2.getTime(), d1.getTime());
//                            //Period period = interval.toPeriod();
//                            //System.out.printf("%d years, %d months, %d days, %d hours, %d minutes, %d seconds%n", period.getYears(), period.getMonths(), period.getDays(), period.getHours(), period.getMinutes(), period.getSeconds());
//
//
//                            Date date1 = formatter.parse(entry.get("dat"));
//                            System.out.println("currentD" + localTime2);
//                            System.out.println("date" + date1);
//                            System.out.println("Total op" + date1.equals(currentD));
//                            System.out.println("Total op1" + date1.after(currentD));
//                            System.out.println("Total op2" + date1.before(currentD));


//                                if (date1.equals(currentD)) {
//
//                                    long firstMillis = System.currentTimeMillis(); // alarm is set right away
//
//                                    Toast.makeText(getApplicationContext(), "starting service", Toast.LENGTH_SHORT).show();
//
//                                    // Create a PendingIntent to be triggered when the alarm goes off
//
//                                    // Setup periodic alarm every 5 seconds
//                                    // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
//                                    // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
//                                    AlarmManager alarm = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//                                    Intent service1 = new Intent(getApplicationContext(), ScheduledPush.class);
//                                    final PendingIntent pIntent = PendingIntent.getBroadcast(getApplicationContext(), ScheduledPush.REQUEST_CODE,
//                                            service1, PendingIntent.FLAG_CANCEL_CURRENT);
//                                    alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
//                                            AlarmManager.INTERVAL_HOUR, pIntent);
//
//
//                                } else if (date1.after(currentD)) {
//                                    // Date d1 = fo.parse(animalList.get(j).get("dat") + " " + animalList.get(j).get("tim"));
//
//                                    //Date d11 = fo.parse(String.valueOf(date1));
//                                    String lc = animalList.get(j).get("dat");
//                                    Log.d("Abhi:", lc);
//
//                                    String[] s1 = lc.split("/");
//
//
//                                    String s2 = s1[0];
//                                    String s3 = s1[1];
//                                    String s4 = s1[2];
//
//                                    Log.d("final op:", s2 + "," + s3 + "," + s4);
//
//
//                                    //cl.setTime(d11);
//                                    AlarmManager alarm = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//                                    Intent service1 = new Intent(getApplicationContext(), ScheduledPush.class);
//                                    final PendingIntent pIntent = PendingIntent.getBroadcast(getApplicationContext(), ScheduledPush.REQUEST_CODE,
//                                            service1, PendingIntent.FLAG_CANCEL_CURRENT);
//                                    alarm.cancel(pIntent);
//                                    Calendar cl = Calendar.getInstance();
//                                    cl.clear();
//                                    // cl.set(Integer.parseInt(s4), Integer.parseInt(s3), Integer.parseInt(s2)+1,12,00);
//                                    cl.set(Calendar.MONTH, 11);
//                                    cl.set(Calendar.YEAR, 2016);
//                                    cl.set(Calendar.DAY_OF_MONTH, 9);
//                                    cl.set(Calendar.HOUR_OF_DAY, 10);
//                                    cl.set(Calendar.MINUTE, 0);
//                                    cl.setTimeInMillis(System.currentTimeMillis());
//
//
//                                    alarm.setRepeating(AlarmManager.RTC_WAKEUP, cl.getTimeInMillis(), AlarmManager.INTERVAL_HOUR, pIntent);
//                                } else if (date1.before(currentD)) {
//
//
//                                }
//                            AlarmManager alarm = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//                            Intent service1 = new Intent(getApplicationContext(), ScheduledPush.class);
//                            //final PendingIntent pIntent = PendingIntent.getBroadcast(getApplicationContext(), ScheduledPush.REQUEST_CODE, service1, PendingIntent.FLAG_UPDATE_CURRENT);
//                            PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, service1, PendingIntent.FLAG_ONE_SHOT);
//
//                            cal.set(Calendar.MONTH, 10);
//                            cal.set(Calendar.YEAR, 2016);
//                            cal.set(Calendar.DAY_OF_MONTH, 17);//date of the particular day
//                            cal.set(Calendar.HOUR_OF_DAY, 12);
//                            cal.set(Calendar.MINUTE, 30);
//
//                            cal.get(Calendar.HOUR_OF_DAY);
//                            cal.get(Calendar.MINUTE);
//
//                            cal.get(Calendar.YEAR);
//                            cal.get(Calendar.MONTH);
//                            cal.get(Calendar.DAY_OF_MONTH);
//                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd kk:mm");
//
//                            String dateTime= "2016-11-17 12:30";
//
//
//                            try {
//                                java.util.Date date = format.parse(dateTime);
//                                cal.setTime(date);
//                                alarm.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
//
//                            } catch (java.text.ParseException e) {
//                                Log.e("OnBootReceiver", e.getMessage(), e);
//                            }
//
//
//                            //alarm.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),INTERVALONEMINUTE , pIntent);
//
//
//                        }
//
//
//                    } else {
//                        // error in fetching chat rooms
//                        Toast.makeText(GcmIntentService.this, "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
//                    }
//
//
//                    // check for error flag
//
//
//                } catch (JSONException e) {
//                    Log.e(TAG, "json parsing error: " + e.getMessage());
//                    Toast.makeText(GcmIntentService.this, "Server did not respond!!", Toast.LENGTH_LONG).show();
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                NetworkResponse networkResponse = error.networkResponse;
//                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
//                //Toast.makeText(ListOfEvent1.this, "Server did not respond!!", Toast.LENGTH_SHORT).show();
//                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
//                    Toast.makeText(getApplicationContext(),
//                            getApplicationContext().getString(R.string.error_network_timeout),
//                            Toast.LENGTH_LONG).show();
//                } else if (error instanceof ServerError) {
//                    Toast.makeText(getApplicationContext(),
//                            getApplicationContext().getString(R.string.error_network_server),
//                            Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(GcmIntentService.this, "Server did not respond!!", Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });
//
//
//        strReq.setRetryPolicy(new DefaultRetryPolicy(
//                WebUrl.MY_SOCKET_TIMEOUT_MS,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        //Adding request to request queue
//        MyApplication.getInstance().addToRequestQueue(strReq);
//    }
//
//
//    private void sendRegistrationToServer(final String token) {
//
//        // checking for valid login session
//        User user = MyApplication.getInstance().getPrefManager().getUser();
//        if (user == null) {
//            // TODO
//            // user not found, redirecting him to login screen
//            return;
//        }
//
//        String endPoint = EndPoints.USER.replace("_ID_", user.getId());
//
//        Log.e(TAG, "endpoint: " + endPoint);
//
//        StringRequest strReq = new StringRequest(Request.Method.PUT,
//                endPoint, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                Log.e(TAG, "response: " + response);
//
//                try {
//                    JSONObject obj = new JSONObject(response);
//
//                    // check for error
//                    if (obj.getBoolean("error") == false) {
//                        // broadcasting token sent to server
//                        Intent registrationComplete = new Intent(Config.SENT_TOKEN_TO_SERVER);
//                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(registrationComplete);
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Unable to send gcm registration id to our sever. " + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
//                    }
//
//                } catch (JSONException e) {
//                    Log.e(TAG, "json parsing error: " + e.getMessage());
//                    Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                NetworkResponse networkResponse = error.networkResponse;
//                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
//                Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("gcm_registration_id", token);
//
//                Log.e(TAG, "params: " + params.toString());
//                return params;
//            }
//        };
//
//        //Adding request to request queue
//        MyApplication.getInstance().addToRequestQueue(strReq);
//    }
//
//    /**
//     * Subscribe to a topic
//     */
//    public static void subscribeToTopic(String topic) {
//        FirebaseMessaging pubSub = FirebaseMessaging.getInstance();
//        //InstanceID instanceID = InstanceID.getInstance(MyApplication.getInstance().getApplicationContext());
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//
//
//        String token = null;
//        token = FirebaseInstanceId.getInstance().getToken();
//
//        if (token != null) {
//            FirebaseMessaging.getInstance().subscribeToTopic(topic);
//            Log.e(TAG, "Subscribed to topic: " + topic);
//        } else {
//            Log.e(TAG, "error: gcm registration id is null");
//        }
//    }

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
//}




