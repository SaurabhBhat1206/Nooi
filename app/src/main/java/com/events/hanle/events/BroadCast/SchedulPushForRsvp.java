package com.events.hanle.events.BroadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.events.hanle.events.SqlliteDB.DBController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * Created by Hanle on 8/31/2017.
 */

public class SchedulPushForRsvp extends BroadcastReceiver {


    public static final int REQUEST_CODE_RSVP = 783;
    ArrayList<String> mylist = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {

        //Toast.makeText(context, "Alarm triggered!!!", Toast.LENGTH_SHORT).show();
        Log.d("Alarm","Alarmtriggered");
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();
        // new push notification is received
        DBController dbController = new DBController(context);
        ArrayList<HashMap<String, String>> eventlist = dbController.getallEvents();

        if (eventlist.size() != 0) {
            eventlist = dbController.getallEvents();
            for (HashMap<String, String> entry : eventlist) {
                Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date currentLocalTime = cal.getTime();
                String localTime2 = formatter.format(currentLocalTime);
                try {
                    Date currentD = formatter.parse(localTime2);
                    System.out.println("rsvpdate" + entry.get("rsvpdate"));

                    if ((entry.get("rsvpdate") != null) && (!entry.get("rsvpdate").equals("")) && (String.valueOf(entry.get("user_attending_status")).equals("1"))) {
                        Date date = formatter.parse(entry.get("rsvpdate"));
                        if (date.equals(currentD)) {
                            System.out.println("today" + entry.get("event_title"));
                            mylist.add(entry.get("event_title"));
                            Intent service1 = new Intent(context, RsvpPush.class);
                            service1.putStringArrayListExtra("ar", mylist);
                            context.startService(service1);
                        }
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
