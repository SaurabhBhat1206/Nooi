package com.events.hanle.events.BroadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.widget.Toast;

import com.events.hanle.events.gcm.GcmIntentService;

/**
 * Created by Hanle on 5/30/2017.
 */

public class Offline extends BroadcastReceiver {

    public static final int REQUEST_CODE = 131231;

    @Override
    public void onReceive(Context context, Intent intent) {

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        // Put here YOUR code.
        Intent i = new Intent(context, GcmIntentService.class);
        i.putExtra(GcmIntentService.KEY, GcmIntentService.OFFLINE);
        context.startService(i);
        //Toast.makeText(context, "Alarm from offline !!!!!!!!!!", Toast.LENGTH_LONG).show(); // For example

        wl.release();

    }
}
