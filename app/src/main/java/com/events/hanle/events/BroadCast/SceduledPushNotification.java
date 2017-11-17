package com.events.hanle.events.BroadCast;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.events.hanle.events.Activity.ListOfEvent1;
import com.events.hanle.events.R;
import com.events.hanle.events.gcm.GcmIntentService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Hanle on 10/24/2016.
 */

public class SceduledPushNotification extends IntentService {
    Notification notification;
    private static final String TAG = SceduledPushNotification.class.getSimpleName();
    ArrayList<String> title = new ArrayList<>();
    HashSet<String> al = new HashSet<String>();
    Iterator<String> itr;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public SceduledPushNotification() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        startalarmfortoday();

        Context context = this.getApplicationContext();
        Intent mIntent = new Intent(this, ListOfEvent1.class);

        title = intent.getStringArrayListExtra("ar");
        System.out.println("Array values:" + title);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, ScheduledPush.REQUEST_CODE, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Resources res = this.getResources();
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        StringBuilder s1;

        for (int i = 0; i < title.size(); i++) {

            al.add(title.get(i));

        }

        System.out.println("Set:" + al);
        itr = al.iterator();
        System.out.println("Iterator:" + itr);

        s1 = new StringBuilder();
        int k = 0;
        for (String str : al) {
            s1.append(++k);
            s1.append(".");
            s1.append(str);
            s1.append("\n");
        }

        String eventtitle = s1.toString();
        Log.e("eventtile", eventtitle);


        String check = intent.getStringExtra("check_flag");

        NotificationManager notificationManager;
        if (check.equals("today")) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            notification = new NotificationCompat.Builder(this)

                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_directions_walk_white_24dp)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.nooismall))
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(eventtitle))
                    .setSound(soundUri)
                    .setContentTitle("Events scheduled for today:")
                    .setContentText(eventtitle)
                    .build();


            notification.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;
            notification.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
            notification.ledARGB = 0xFFFFA500;
            notification.ledOnMS = 800;
            notification.ledOffMS = 1000;
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            int TODAYNOTIFICATION = 1;
            notificationManager.notify(TODAYNOTIFICATION, notification);

        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            notification = new NotificationCompat.Builder(this)


                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_directions_walk_white_24dp)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.nooismall))
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(eventtitle))
                    .setSound(soundUri)
                    .setContentTitle("Tommorrow's Event/s")
                    .setContentText(eventtitle)
                    .build();


            notification.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;
            notification.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
            notification.ledARGB = 0xFFFFA500;
            notification.ledOnMS = 800;
            notification.ledOffMS = 1000;
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            int TOMMORROWNOTIFICATION = 2;
            notificationManager.notify(TOMMORROWNOTIFICATION, notification);
        }


    }

    private void startalarmfortoday() {
        AlarmManager alarm = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        Intent service1 = new Intent(getApplicationContext(), SchedulePushfortodayy.class);
        service1.putExtra("localpush", "localpush");
        PendingIntent pIntent = PendingIntent.getBroadcast(getApplicationContext(), SchedulePushfortodayy.REQUEST_CODE1,
                service1, 0);

        Calendar alarmStartTime = Calendar.getInstance();
        alarmStartTime.set(Calendar.HOUR_OF_DAY, 8);
        alarmStartTime.set(Calendar.MINUTE, 00);
        alarmStartTime.set(Calendar.SECOND, 0);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pIntent);
    }
}

