package com.events.hanle.events.BroadCast;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.events.hanle.events.app.Config;

import java.util.Calendar;

/**
 * Created by Hanle on 5/31/2017.
 */

public class OfflineService extends IntentService {
    private static final String TAG = OfflineService.class.getSimpleName();

    public OfflineService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        AlarmManager alarm = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getApplicationContext(), Offline.class);
        i.putExtra("offline", "offline");
        Calendar alarmStartTime = Calendar.getInstance();
        final PendingIntent pIntent = PendingIntent.getBroadcast(getApplicationContext(), Offline.REQUEST_CODE,
                i, PendingIntent.FLAG_UPDATE_CURRENT);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(),
                Config.TWO_MINUTES, pIntent);
    }
}
