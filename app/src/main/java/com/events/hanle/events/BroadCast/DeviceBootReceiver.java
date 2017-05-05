package com.events.hanle.events.BroadCast;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.events.hanle.events.BroadCast.ScheduledPush;

import java.util.Calendar;

/**
 * Created by Hanle on 11/29/2016.
 */

public class DeviceBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent service1 = new Intent(context, ScheduledPush.class);
            final PendingIntent pIntent = PendingIntent.getBroadcast(context, ScheduledPush.REQUEST_CODE,
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
    }
}
