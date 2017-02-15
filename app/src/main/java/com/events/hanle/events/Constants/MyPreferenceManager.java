package com.events.hanle.events.Constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.events.hanle.events.Model.CanceledEvent;
import com.events.hanle.events.Model.CompletedEvent;
import com.events.hanle.events.Model.ConcludedEventView;
import com.events.hanle.events.Model.FeedItem;
import com.events.hanle.events.Model.ListEvent;
import com.events.hanle.events.Model.User;


public class MyPreferenceManager {

    private String TAG = MyPreferenceManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "androidhive_gcm";

    // All Shared Preferences Keys
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_NOTIFICATIONS = "notifications";
    private static final String KEY_USER_MOBILE = "user_mobile";
    private static final String KEY_COUNTRY_CODE = "country_code";
    private static final String KEY_EVENT_ID = "event_id";
    private static final String KEY_EVENT_ID1 = "event_id1";
    private static final String KEY_EVENT_TILTLE1 = "event_title1";
    private static final String KEY_EVENT_TILTLE2 = "event_title2";
    private static final String KEY_EVENT_TILTLE = "event_title";
    private static final String TEMP_USER_ID = "USERID";
    private static final String EVENT_INFO_ID = "EVENTINFOID";
    private static final String EVENT_INFO_ID1 = "EVENTINFOID1";
    private static final String EVENT_INFO_ID2 = "EVENTINFOID2";
    private static final String KEY_USER_PHONE = "user_phone";
    private static final String KEY_EVENT_STATUS = "event_status";
    private static final String KEY_EVENT_STATUS1 = "event_status1";
    private static final String KEY_EVENT_STATUS2 = "event_status2";
    private static final String KEY_INVITER_NAME = "inviter_name";
    private static final String KEY_INVITER_NAME1 = "inviter_name1";
    private static final String KEY_INVITER_NAME2 = "inviter_name2";
    private static final String KEY_USER_STATUS = "user_Status";
    private static final String KEY_USER_STATUS1 = "user_Status1";
    private static final String KEY_USER_STATUS2 = "user_Status2";
    private static final String TEMP_USER_MOB = "USERPHONE";
    private static final String TEMP_COUNTRY_CODE = "COUNTRYCODE";
    private static final String SHARE_DETAIL = "SHAREDETAILS";
    private static final String SHARE_DETAIL1 = "SHAREDETAILS1";
    private static final String SHARE_DETAIL2 = "SHAREDETAILS2";
    private static final String ARTWORK = "artwork";
    private static final String ARTWORK1 = "artwork1";
    private static final String ARTWORK2 = "artwork2";
    private static final String UNREADCOUNT = "unreadcount";
    private static final String BACKGROUND_CHATROOMID = "background_chatroomID";




    // Constructor
    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void storeUser(User user) {
        editor.putString(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_MOBILE, user.getMobile());
        editor.putString(KEY_COUNTRY_CODE, user.getCountrycode());
        editor.commit();

        Log.e(TAG, "User is stored in shared preferences. " + user.getName() + ", " + user.getId() + "," + user.getMobile() + "," + user.getCountrycode());
    }

    public void storeUserID(User user) {
        editor.putString(KEY_USER_ID, user.getId());
        editor.commit();

        Log.e(TAG, "User is stored in shared preferences. " + user.getId());
    }

    public void storetempuserID(User user) {
        editor.putString(TEMP_USER_ID, user.getId());
        editor.putString(TEMP_COUNTRY_CODE, user.getCountrycode());
        editor.putString(TEMP_USER_MOB, user.getMobile());
        editor.commit();

        Log.e(TAG, "temporary User is stored in shared preferences. " + user.getId() + "," + user.getCountrycode() + "," + user.getMobile());
    }

    public void storeEventId(ListEvent listEvent) {
        editor.putString(KEY_EVENT_ID, listEvent.getId());
        editor.putString(KEY_EVENT_TILTLE, listEvent.getEvent_title());
        editor.putString(KEY_EVENT_STATUS, listEvent.getEvent_status());
        editor.putString(KEY_INVITER_NAME, listEvent.getInvitername());
        editor.putString(SHARE_DETAIL, listEvent.getShare_detail());
        editor.putString(ARTWORK, listEvent.getArtwork());
        editor.commit();

    }

    public void storeEventIdCompletedEvent(CompletedEvent completedEvent) {
        editor.putString(KEY_EVENT_ID1, completedEvent.getId());
        editor.putString(KEY_EVENT_TILTLE1, completedEvent.getEvent_title());
        editor.putString(KEY_EVENT_STATUS1, completedEvent.getEvent_status());
        editor.putString(KEY_INVITER_NAME1, completedEvent.getInvitername());
        editor.putString(SHARE_DETAIL1, completedEvent.getShare_detail());
        editor.putString(ARTWORK1, completedEvent.getArtwork());
        editor.commit();

    }

    public void storeEventIdCanceledEvent(CanceledEvent canceledEvent) {
        editor.putString(EVENT_INFO_ID2, canceledEvent.getId());
        editor.putString(KEY_EVENT_TILTLE2, canceledEvent.getEvent_title());
        editor.putString(KEY_EVENT_STATUS2, canceledEvent.getEvent_status());
        editor.putString(KEY_INVITER_NAME2, canceledEvent.getInvitername());
        editor.putString(SHARE_DETAIL2, canceledEvent.getShare_detail());
        editor.putString(ARTWORK2, canceledEvent.getArtwork());
        editor.commit();

    }


    public void storeEventInfoID(FeedItem feedItem) {
        editor.putString(EVENT_INFO_ID, feedItem.getEventinfoID());
        editor.commit();
        Log.e(TAG, "Event Userinfo is stored in shared preferences. " + feedItem.getEventinfoID());

    }

    public void SEID(ConcludedEventView feedItem) {
        editor.putString(EVENT_INFO_ID1, feedItem.getEventinfoID());
        editor.commit();
        Log.e(TAG, "Event Userinfo is stored in shared preferences. " + feedItem.getEventinfoID());

    }

    public ConcludedEventView getSEID() {
        if (pref.getString(EVENT_INFO_ID1, null) != null) {
            String eventinfoId;
            eventinfoId = pref.getString(EVENT_INFO_ID1, null);
            ConcludedEventView feedItem = new ConcludedEventView(eventinfoId);
            return feedItem;
        }
        return null;
    }

    public User getUser() {
        if (pref.getString(KEY_USER_ID, null) != null) {
            String id, name, countrycode, mobile;
            id = pref.getString(KEY_USER_ID, null);
            name = pref.getString(KEY_USER_NAME, null);
            mobile = pref.getString(KEY_USER_MOBILE, null);
            countrycode = pref.getString(KEY_COUNTRY_CODE, null);

            User user = new User(id, name, mobile, countrycode);
            return user;
        }
        return null;
    }

    public User getUserId() {
        if (pref.getString(KEY_USER_ID, null) != null) {
            String userId, countrycode;
            userId = pref.getString(KEY_USER_ID, null);
            countrycode = pref.getString(KEY_COUNTRY_CODE, null);
            User user = new User(userId, countrycode);
            return user;
        }
        return null;
    }


    public User getTempUserId() {
        if (pref.getString(TEMP_USER_MOB, null) != null) {
            String userId, mobile, countrycode;
            userId = pref.getString(TEMP_USER_ID, null);
            mobile = pref.getString(TEMP_USER_MOB, null);
            countrycode = pref.getString(TEMP_COUNTRY_CODE, null);
            User user = new User(userId, null, mobile, countrycode);
            return user;
        }
        return null;
    }

    public FeedItem getEventInfoID() {
        if (pref.getString(EVENT_INFO_ID, null) != null) {
            String eventinfoId;
            eventinfoId = pref.getString(EVENT_INFO_ID, null);
            FeedItem feedItem = new FeedItem(eventinfoId);
            return feedItem;
        }
        return null;
    }

    public ListEvent getEventId() {
        if (pref.getString(KEY_EVENT_ID, null) != null) {
            String eventId, eventTitle, event_Status, inviter_name, user_status, sharedetail,artwork;
            eventId = pref.getString(KEY_EVENT_ID, null);
            eventTitle = pref.getString(KEY_EVENT_TILTLE, null);
            event_Status = pref.getString(KEY_EVENT_STATUS, null);
            inviter_name = pref.getString(KEY_INVITER_NAME, null);
            user_status = pref.getString(KEY_USER_STATUS, null);
            sharedetail = pref.getString(SHARE_DETAIL, null);
            artwork = pref.getString(ARTWORK, null);

            ListEvent listEvent = new ListEvent(eventId, eventTitle, user_status, inviter_name, event_Status, null, sharedetail,artwork);
            return listEvent;

        }
        return null;
    }

    public CompletedEvent getCompletedEventId() {
        if (pref.getString(KEY_EVENT_ID1, null) != null) {
            String eventId, eventTitle, event_Status, inviter_name, user_status, sharedetail,artwork;
            eventId = pref.getString(KEY_EVENT_ID1, null);
            eventTitle = pref.getString(KEY_EVENT_TILTLE1, null);
            event_Status = pref.getString(KEY_EVENT_STATUS1, null);
            inviter_name = pref.getString(KEY_INVITER_NAME1, null);
            user_status = pref.getString(KEY_USER_STATUS1, null);
            sharedetail = pref.getString(SHARE_DETAIL, null);
            artwork = pref.getString(ARTWORK1, null);

            CompletedEvent listEvent = new CompletedEvent(eventId, eventTitle, user_status, inviter_name, event_Status, null, sharedetail,artwork);
            return listEvent;

        }
        return null;
    }

    public CanceledEvent getCancelledEventID() {
        if (pref.getString(EVENT_INFO_ID2, null) != null) {
            String eventId, eventTitle, event_Status, inviter_name, user_status, sharedetail,artwork;
            eventId = pref.getString(EVENT_INFO_ID2, null);
            eventTitle = pref.getString(KEY_EVENT_TILTLE2, null);
            event_Status = pref.getString(KEY_EVENT_STATUS2, null);
            inviter_name = pref.getString(KEY_INVITER_NAME2, null);
            user_status = pref.getString(KEY_USER_STATUS2, null);
            sharedetail = pref.getString(SHARE_DETAIL2, null);
            artwork = pref.getString(ARTWORK2, null);

            CanceledEvent listEvent = new CanceledEvent(eventId, eventTitle, user_status, inviter_name, event_Status, null, sharedetail,artwork);
            return listEvent;

        }
        return null;
    }

    public void addNotification(String notification) {

        // get old notifications
        String oldNotifications = getNotifications();

        if (oldNotifications != null) {
            oldNotifications += "|" + notification;
        } else {
            oldNotifications = notification;
        }

        editor.putString(KEY_NOTIFICATIONS, oldNotifications);
        editor.commit();
    }

    public void clearnotification(){
        editor.remove(KEY_NOTIFICATIONS);
        editor.apply();
    }

    public String getNotifications() {
        return pref.getString(KEY_NOTIFICATIONS, null);
    }



    public void clear() {
        editor.clear();
        editor.commit();
    }
}
