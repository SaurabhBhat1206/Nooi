package com.events.hanle.events.app;

import android.graphics.Typeface;

/**
 * Created by Lincoln on 05/01/16.
 */
public class Config {

    // flag to identify whether to show single line
    // or multi line test push notification tray
    public static boolean appendNotificationMessages = true;

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    public static final String SCHEDULEDPUSH = "alarmnotification";
    public static final String PUSH_NOTIFICATION_FROM_PARTNER = "pushNotificationfrompartner";
    public static final String PUSH_NOTIFICATION_FROM_ORGANISER = "pushNotificationfromorganiser";
    public static final String PUSH_NOTIFICATION_FROM_NOTATTEDNING = "pushNotificationfromnotattending";
    public static final String PUSH_NOTIFICATION_FROM_LARGE_EVENT = "pushNotificationfromlargeevent";
    public static final String PUSH_NOTIFICATION_INVITATION = "pushNotificationInvitation";
    public static final long TWO_MINUTES = 2 * 60 * 1000;

    // type of push messages
    public static final int PUSH_TYPE_CHATROOM = 1;
    public static final int PUSH_TYPE_USER = 2;
    public static final int PUSH_TYPE_PARTNER = 3;
    public static final int PUSH_TYPE_ORGANISER = 4;
    public static final int PUSH_TYPE_LARGE_EVENT = 5;
    public static final int PUSH_TYPE_NOT_ATTENDING = 6;
    public static final int PUSH_TYPE_INVITEE = 7;

    // id to handle the notification in the notification try
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static Typeface typeface;

}
