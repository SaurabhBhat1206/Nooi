package com.events.hanle.events.BroadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.events.hanle.events.SqlliteDB.DBController;
import com.events.hanle.events.app.Config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * Created by Hanle on 10/24/2016.
 */

public class ScheduledPush extends BroadcastReceiver {
    Context ctx;
    public static final int REQUEST_CODE = 12345;
    ArrayList<String> mylist = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {

        // new push notification is received
        DBController dbController = new DBController(context);

        //Toast.makeText(context, "Schedled push from broadcast", Toast.LENGTH_SHORT).show();
        ArrayList<HashMap<String, String>> animalList = dbController.getallEvents();

        if (animalList.size() != 0) {
            animalList = dbController.getallEvents();
            for (HashMap<String, String> entry : animalList) {
                System.out.println("SQL OP from new loop:" + entry.get("event_title"));
                System.out.println("SQL OP from new loop:" + entry.get("dat"));
                Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date currentLocalTime = cal.getTime();
                String localTime2 = formatter.format(currentLocalTime);
                try {
                    Date currentD = formatter.parse(localTime2);
                    Date date1 = formatter.parse(entry.get("dat"));
                    System.out.println("Today op:" + date1.equals(currentD));
                    if (date1.equals(currentD)) {
                        System.out.println("sule" + entry.get("event_title"));
                        mylist.add(entry.get("event_title"));
                        Intent service1 = new Intent(context, SceduledPushNotification.class);
                        service1.putStringArrayListExtra("ar", mylist);
                        context.startService(service1);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }



        }


    }
}
