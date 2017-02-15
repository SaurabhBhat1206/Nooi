package com.events.hanle.events.gcm;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.events.hanle.events.Activity.ListOfEvent1;
import com.events.hanle.events.Activity.UserTabView;
import com.events.hanle.events.Model.Message;
import com.events.hanle.events.Model.User;
import com.events.hanle.events.R;
import com.events.hanle.events.app.Config;
import com.events.hanle.events.app.MyApplication;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by Hanle on 9/30/2016.
 */

public class MyFcmListenerService extends FirebaseMessagingService {
    private NotificationUtils notificationUtils;
    private static final String TAG = MyFcmListenerService.class.getSimpleName();
    public static final String MyPREFERENCES = "PrefChat";
    SharedPreferences sharedpreferences;

    @Override
    public void onMessageReceived(RemoteMessage message) {
        String from = message.getFrom();
        Map dat = message.getData();

        String title = (String) dat.get("title");
        Boolean isBackground = Boolean.valueOf((String) dat.get("is_background"));
        String flag = (String) dat.get("flag");
        String data = (String) dat.get("data");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "title: " + title);
        Log.d(TAG, "isBackground: " + isBackground);
        Log.d(TAG, "flag: " + flag);
        Log.d(TAG, "data: " + data);
        System.out.println("Check app status:" + NotificationUtils.isAppIsInBackground(getApplicationContext()));

        if (flag == null)
            return;

        if (MyApplication.getInstance().getPrefManager().getUser() == null) {
            // user is not logged in, skipping push notification
            Log.e(TAG, "user is not logged in, skipping push notification");
            return;
        }
        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }
        switch (Integer.parseInt(flag)) {
            case Config.PUSH_TYPE_CHATROOM:
                // push notification belongs to a chat room
                processChatRoomPush(title, isBackground, data);
                break;
            case Config.PUSH_TYPE_USER:
                // push notification is specific to user
                processUserMessage(title, isBackground, data);
                break;
            case Config.PUSH_TYPE_PARTNER:
                // push notification is specific to user
                ///code added for general push

                if (message.getNotification() != null) {
                    Log.e(TAG, "Notification Body partner: " + message.getNotification().getBody());
                    handleNotification(message.getNotification().getBody());
                }

                // Check if message contains a data payload.
                if (message.getData().size() > 0) {
                    Log.e(TAG, "Data Payload partner: " + message.getData().toString());

                    try {
                        JSONObject json = new JSONObject(message.getData().toString());
                        handleDataMessage(json);
                    } catch (Exception e) {
                        Log.e(TAG, "Exception from partner: " + e.getMessage());
                    }
                }
                break;
            case Config.PUSH_TYPE_ORGANISER:
                // push notification is specific to user
                ///code added for general push

                if (message.getNotification() != null) {
                    Log.e(TAG, "Notification Body from organiser: " + message.getNotification().getBody());
                    handleNotificationforOrganiser(message.getNotification().getBody());
                }

                // Check if message contains a data payload.
                if (message.getData().size() > 0) {
                    Log.e(TAG, "Data Payload from organiser: " + message.getData().toString());

                    try {
                        JSONObject json = new JSONObject(message.getData().toString());
                        handleDataMessageforOrganiser(json);
                    } catch (Exception e) {
                        Log.e(TAG, "Exception from organiser: " + e.getMessage());
                    }
                }
                break;
        }


    }

    private void handleDataMessage(JSONObject json) {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            String event_status = data.getString("eventstatus");
            String share_detial = data.getString("sharedetail");
            String eventID = data.getString("eventId");
            String invitername = data.getString("invitername");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            String description = data.getString("description");
            String artwork = data.getString("artwork");
            JSONObject payload = data.getJSONObject("payload");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);

            System.out.println("Check app status:" + NotificationUtils.isAppIsInBackground(getApplicationContext()));
            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION_FROM_PARTNER);
                pushNotification.putExtra("type", Config.PUSH_TYPE_PARTNER);
                pushNotification.putExtra("description", description);
                pushNotification.putExtra("eventId", eventID);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), UserTabView.class);
                resultIntent.putExtra("message", message);
                resultIntent.putExtra("eventId", eventID);
                resultIntent.putExtra("eventstatus", event_status);
                resultIntent.putExtra("classcheck", "from_partner");
                resultIntent.putExtra("event_title", message);
                resultIntent.putExtra("share_detail", share_detial);
                resultIntent.putExtra("artwork", artwork);
                resultIntent.putExtra("invitername", invitername);
                editor.putString("partnereventID" + eventID, eventID);
                editor.commit();
                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, "Mailbag Message : " + message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private void handleDataMessageforOrganiser(JSONObject json) {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            String event_status = data.getString("eventstatus");
            String share_detial = data.getString("sharedetail");
            String invitername = data.getString("invitername");
            String eventID = data.getString("eventId");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            String description = data.getString("description");
            String artwork = data.getString("artwork");
            JSONObject payload = data.getJSONObject("payload");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION_FROM_ORGANISER);
                pushNotification.putExtra("type", Config.PUSH_TYPE_ORGANISER);
                pushNotification.putExtra("description", description);
                pushNotification.putExtra("eventId", eventID);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), UserTabView.class);
                resultIntent.putExtra("message", message);
                resultIntent.putExtra("eventId", eventID);
                resultIntent.putExtra("eventstatus", event_status);
                resultIntent.putExtra("classcheck", "from_organiser");
                resultIntent.putExtra("event_title", message);
                resultIntent.putExtra("share_detail", share_detial);
                resultIntent.putExtra("artwork", artwork);
                resultIntent.putExtra("invitername", invitername);
                editor.putString("organisereventID" + eventID, eventID);
                editor.commit();
                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, "Organiser Message : " + message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private void handleNotificationforOrganiser(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void processChatRoomPush(String title, boolean isBackground, String data) {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if (!isBackground) {

            try {
                JSONObject datObj = new JSONObject(data);

                String chatRoomId = datObj.getString("chat_room_id");

                JSONObject mObj = datObj.getJSONObject("message");

                String event_status = mObj.getString("event_status");
                String share_detial = mObj.getString("share_detail");
                String artwork = mObj.getString("artwork");
                String invitername = mObj.getString("invitername");
                Message message = new Message();
                message.setMessage(mObj.getString("message"));
                message.setId(mObj.getString("message_id"));
                message.setCreatedAt(mObj.getString("created_at"));
                message.setName(mObj.getString("name"));
                String s = message.setMessage(mObj.getString("message"));
                Log.e("crid", chatRoomId);

                JSONObject uObj = datObj.getJSONObject("user");

                if (uObj.getString("user_id").equals(MyApplication.getInstance().getPrefManager().getUser().getId())) {
                    Log.e(TAG, "Skipping the push message as it belongs to same user");
                    return;
                }

                User user = new User();
                user.setId(uObj.getString("user_id"));
                user.setMobile(uObj.getString("phone"));
                user.setName(uObj.getString("name"));
                message.setUser(user);


                if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {

                    // app is in foreground, broadcast the push message
                    Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                    pushNotification.putExtra("type", Config.PUSH_TYPE_CHATROOM);
                    pushNotification.putExtra("message", message);
                    pushNotification.putExtra("chat_room_id", chatRoomId);
                    pushNotification.putExtra("eventstatus", event_status);
                    pushNotification.putExtra("classcheck", "from_notifications");
                    pushNotification.putExtra("event_title", message.getName());
                    pushNotification.putExtra("share_detail", share_detial);
                    pushNotification.putExtra("artwork", artwork);

                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                    // play notification sound
                    NotificationUtils notificationUtils = new NotificationUtils();
                    notificationUtils.playNotificationSound();

                } else {

                    // app is in background. show the message in notification try
                    Intent resultIntent = new Intent(getApplicationContext(), UserTabView.class);
                    resultIntent.putExtra("type", Config.PUSH_TYPE_CHATROOM);
                    resultIntent.putExtra("chat_room_id", chatRoomId);
                    resultIntent.putExtra("eventstatus", event_status);
                    resultIntent.putExtra("classcheck", "from_notifications");
                    resultIntent.putExtra("event_title", message.getName());
                    resultIntent.putExtra("me", message);
                    resultIntent.putExtra("invitername", invitername);
                    resultIntent.putExtra("share_detail", share_detial);
                    resultIntent.putExtra("artwork", artwork);

                    editor.putString("chateventID" + chatRoomId, chatRoomId);
                    editor.commit();

                    showNotificationMessage(getApplicationContext(), title, message.getName() + " : " + "chat message", message.getCreatedAt(), resultIntent);
                }

            } catch (JSONException e) {
                Log.e(TAG, "json parsing error: " + e.getMessage());
               // Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        } else {
            // the push notification is silent, may be other operations needed
            // like inserting it in to SQLite
        }
    }

    private void processUserMessage(String title, boolean isBackground, String data) {
        if (!isBackground) {

            try {
                JSONObject datObj = new JSONObject(data);

                String imageUrl = datObj.getString("image");

                JSONObject mObj = datObj.getJSONObject("message");
                Message message = new Message();
                message.setMessage(mObj.getString("message"));
                message.setId(mObj.getString("message_id"));
                message.setCreatedAt(mObj.getString("created_at"));

                JSONObject uObj = datObj.getJSONObject("user");
                User user = new User();
                user.setId(uObj.getString("user_id"));
                user.setMobile(uObj.getString("phone"));
                user.setName(uObj.getString("name"));
                message.setUser(user);

                // verifying whether the app is in background or foreground
                if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {

                    // app is in foreground, broadcast the push message
                    Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                    pushNotification.putExtra("type", Config.PUSH_TYPE_USER);
                    pushNotification.putExtra("message", message);

                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                    // play notification sound
                    NotificationUtils notificationUtils = new NotificationUtils();
                    notificationUtils.playNotificationSound();
                } else {

                    // app is in background. show the message in notification try
                    Intent resultIntent = new Intent(getApplicationContext(), ListOfEvent1.class);

                    // check for push notification image attachment
                    if (TextUtils.isEmpty(imageUrl)) {
                        showNotificationMessage(getApplicationContext(), title, message.getName() + " : " + "chat message", message.getCreatedAt(), resultIntent);
                    } else {
                        // push notification contains image
                        // show it with the image
                        showNotificationMessageWithBigImage(getApplicationContext(), title, message.getName(), message.getCreatedAt(), resultIntent, imageUrl);
                    }

                }
            } catch (JSONException e) {
                Log.e(TAG, "json parsing error: " + e.getMessage());
                Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        } else {
            // the push notification is silent, may be other operations needed
            // like inserting it in to SQLite
        }
    }

    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

}
