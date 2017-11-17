package com.events.hanle.events.BroadCast;

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
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.events.hanle.events.Activity.ListOfEvent1;
import com.events.hanle.events.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Hanle on 8/31/2017.
 */

public class RsvpPush extends IntentService {
    Notification notification;
    private static final String TAG = RsvpPush.class.getSimpleName();
    ArrayList<String> title = new ArrayList<>();
    HashSet<String> al = new HashSet<String>();
    Iterator<String> itr;


    public RsvpPush() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Context context = this.getApplicationContext();
        Intent mIntent = new Intent(this, ListOfEvent1.class);

        title = intent.getStringArrayListExtra("ar");
        System.out.println("Array values:" + title);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, SchedulPushForRsvp.REQUEST_CODE_RSVP, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

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
                    .setContentTitle("You have RSVP to respond!!")
                    .setContentText(eventtitle)
                    .build();


            notification.flags |= Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;
            notification.defaults |= Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
            notification.ledARGB = 0xFFFFA500;
            notification.ledOnMS = 800;
            notification.ledOffMS = 1000;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        int NOTIFICATION_RSVP = 100;
        notificationManager.notify(NOTIFICATION_RSVP, notification);

    }

}
