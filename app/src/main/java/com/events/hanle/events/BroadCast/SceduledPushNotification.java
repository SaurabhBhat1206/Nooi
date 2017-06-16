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
    private NotificationManager notificationManager;
    private PendingIntent pendingIntent;
    private static int NOTIFICATION_ID = 1;
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


            Context context = this.getApplicationContext();
            Intent mIntent = new Intent(this, ListOfEvent1.class);

            title = intent.getStringArrayListExtra("ar");
            System.out.println("Array values:" + title);

            pendingIntent = PendingIntent.getActivity(context, ScheduledPush.REQUEST_CODE, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

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
            notificationManager.notify(NOTIFICATION_ID, notification);

    }
}

