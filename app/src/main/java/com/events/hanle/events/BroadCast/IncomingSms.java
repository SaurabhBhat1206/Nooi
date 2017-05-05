package com.events.hanle.events.BroadCast;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.events.hanle.events.Activity.ListOfEvent1;
import com.events.hanle.events.Activity.LoginActivity;
import com.events.hanle.events.Activity.UserTabView;
import com.events.hanle.events.Constants.WebUrl;
import com.events.hanle.events.Fragments.OtpFragment;
import com.events.hanle.events.Model.User;
import com.events.hanle.events.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.events.hanle.events.Fragments.OtpFragment.MyPREFERENCES;
import static com.events.hanle.events.Fragments.OtpFragment.USER_INPUT;

/**
 * Created by Hanle on 3/22/2017.
 */

public class IncomingSms extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                assert pdusObj != null;
                for (Object aPdusObj : pdusObj) {
                    SmsMessage currentMessage;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        String format = bundle.getString("format");
                        currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj, format);

                    } else {
                        currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                    }

                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    System.out.println("message" + phoneNumber + senderNum + message);
                    try {
                        if (senderNum.substring(3).equalsIgnoreCase("HANSLN")) {
                            OtpFragment Sms = new OtpFragment();
                            Sms.recivedSms(message.substring(36, 42));
                        }
                    } catch (Exception e) {
                    }

                }
            }

        } catch (Exception e) {

        }
    }


}
