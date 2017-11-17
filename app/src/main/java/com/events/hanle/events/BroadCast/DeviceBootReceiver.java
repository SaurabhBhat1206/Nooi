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

            Intent service = new Intent(context, ScheduledPush.class);
            final PendingIntent pIntent = PendingIntent.getBroadcast(context, ScheduledPush.REQUEST_CODE,
                    service, 0);
            Calendar alarmStartTime = Calendar.getInstance();
            alarmStartTime.set(Calendar.HOUR_OF_DAY, 18);
            alarmStartTime.set(Calendar.MINUTE, 00);
            alarmStartTime.set(Calendar.SECOND, 0);
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pIntent);


            Intent service1 = new Intent(context, SchedulePushfortodayy.class);
            final PendingIntent pIntent1 = PendingIntent.getBroadcast(context, SchedulePushfortodayy.REQUEST_CODE1,
                    service1,0);
            Calendar alarmStartTime1 = Calendar.getInstance();
            alarmStartTime1.set(Calendar.HOUR_OF_DAY, 8);
            alarmStartTime1.set(Calendar.MINUTE, 00);
            alarmStartTime1.set(Calendar.SECOND, 0);
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime1.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pIntent1);


            Intent service2 = new Intent(context, SchedulPushForRsvp.class);
            final PendingIntent pIntent2 = PendingIntent.getBroadcast(context, SchedulPushForRsvp.REQUEST_CODE_RSVP,
                    service2, 0);
            Calendar alarmStartTime2 = Calendar.getInstance();
            alarmStartTime2.setTimeInMillis(System.currentTimeMillis());
            alarmStartTime2.set(Calendar.HOUR_OF_DAY, 13);
            alarmStartTime2.set(Calendar.MINUTE, 30);
            alarmStartTime2.set(Calendar.SECOND, 0);
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime2.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pIntent2);

        }
    }
}
