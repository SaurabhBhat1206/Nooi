package com.events.hanle.events.BroadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.events.hanle.events.SqlliteDB.DBController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * Created by Hanle on 7/20/2017.
 */

public class SchedulePushfortodayy extends BroadcastReceiver {
    Context ctx;
    public static final int REQUEST_CODE1 = 54321;
    ArrayList<String> mylist = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();
        // new push notification is received
        DBController dbController = new DBController(context);

        //Toast.makeText(context, "Schedled push from broadcast", Toast.LENGTH_SHORT).show();
        ArrayList<HashMap<String, String>> animalList = dbController.getallEvents();

        if (animalList.size() != 0) {
            animalList = dbController.getallEvents();
            for (HashMap<String, String> entry : animalList) {
                Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date currentLocalTime = cal.getTime();
                String localTime2 = formatter.format(currentLocalTime);
                try {
                    Date currentD = formatter.parse(localTime2);
                    Date date1 = formatter.parse(entry.get("dat"));
                    if (date1.equals(currentD)) {
                        System.out.println("today" + entry.get("event_title"));
                        mylist.add(entry.get("event_title"));
                        Intent service1 = new Intent(context, SceduledPushNotification.class);
                        service1.putStringArrayListExtra("ar", mylist);
                        service1.putExtra("check_flag", "today");
                        context.startService(service1);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
