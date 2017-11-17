package com.events.hanle.events.BroadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.events.hanle.events.Fragments.OtpFragment;


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
                        if (senderNum.substring(3).equalsIgnoreCase("INVITE")) {
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
