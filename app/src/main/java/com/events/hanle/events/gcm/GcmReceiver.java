package com.events.hanle.events.gcm;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

import com.events.hanle.events.Activity.ListOfEvent1;
import com.events.hanle.events.app.Config;


/**
 * Created by Hanle on 9/26/2016.
 */

public class GcmReceiver extends WakefulBroadcastReceiver {
    ListOfEvent1 listOfEvent1 = new ListOfEvent1();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
            // notification received
            // checking for type intent filter
           if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                // new push notification is received
                listOfEvent1.handlePushNotification(intent);
                Toast.makeText(context, "Push recieved", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
