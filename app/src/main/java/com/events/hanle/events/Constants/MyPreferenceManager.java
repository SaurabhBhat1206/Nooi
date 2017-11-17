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
    private static final String EVENTTYPE = "eventtype";
    private static final String EVENTTYPE1 = "eventtype1";
    private static final String EVENTTYPE2 = "eventtype2";
    private static final String CHATWINDOW = "chat_window";
    private static final String CHATWINDOW1 = "chat_window1";
    private static final String CHATWINDOW2 = "chat_window2";
    private static final String OrganiserId = "organiser_id";
    private static final String OrganiserId_AUTO_LOGIN = "organiser_id_auto_login";
    private static final String COUNTRY_CODE1 = "country_code1";
    private static final String PHONE = "phone";
    private static final String COUNTRY_CODE2 = "country_code2";
    private static final String PHONE1 = "phone1";
    private static final String COUNTRY_CODE3 = "country_code3";
    private static final String PHONE2 = "phone2";
    private static final String LISTEVENTORGANISERID = "list_event_organiser_id";
    private static final String TABMOVEMENT = "moved";
    private static final String KEY_ACKNW = "acknw";
    private static final String KEY_ACKNW1 = "acknw1";
    private static final String KEY_ACKNW2 = "acknw2";
    private static final String ORGANISER_EMAIL = "organiseremail";
    private static final String NOOFDAYS = "noofdays";


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

    public void storetempuserID(User user) {
        editor.putString(TEMP_USER_ID, user.getId());
        editor.putString(TEMP_COUNTRY_CODE, user.getCountrycode());
        editor.putString(TEMP_USER_MOB, user.getMobile());
        editor.commit();

        Log.e(TAG, "temporary User is stored in shared preferences. " + user.getId() + "," + user.getCountrycode() + "," + user.getMobile());
    }

    public void storeorganiseremail(String email){
        editor.putString(ORGANISER_EMAIL, email);
        editor.commit();

    }

    public void storeEventId(ListEvent listEvent) {
        editor.putString(KEY_EVENT_ID, listEvent.getEventId());
        editor.putString(KEY_EVENT_TILTLE, listEvent.getEventTitle());
        editor.putString(KEY_EVENT_STATUS, listEvent.getEventStatus());
        editor.putString(KEY_INVITER_NAME, listEvent.getInviterName());
        editor.putString(SHARE_DETAIL, listEvent.getShareDetail());
        editor.putString(ARTWORK, listEvent.getArtwork());
        editor.putString(EVENTTYPE, listEvent.getType());
        editor.putString(CHATWINDOW, listEvent.getChatW());
        editor.putString(COUNTRY_CODE1, listEvent.getCountrycode());
        editor.putString(PHONE, listEvent.getPhone());
        editor.putString(OrganiserId_AUTO_LOGIN, listEvent.getOrganiserId());
        editor.putString(KEY_ACKNW, listEvent.getAcknw());
        editor.commit();

    }

    public void storeEventIdCompletedEvent(CompletedEvent completedEvent) {
        editor.putString(KEY_EVENT_ID1, completedEvent.getId());
        editor.putString(KEY_EVENT_TILTLE1, completedEvent.getEvent_title());
        editor.putString(KEY_EVENT_STATUS1, completedEvent.getEvent_status());
        editor.putString(KEY_INVITER_NAME1, completedEvent.getInvitername());
        editor.putString(SHARE_DETAIL1, completedEvent.getShare_detail());
        editor.putString(ARTWORK1, completedEvent.getArtwork());
        editor.putString(EVENTTYPE1, completedEvent.getEvent_type());
        editor.putString(CHATWINDOW1, completedEvent.getChat_window());
        editor.putString(COUNTRY_CODE1, completedEvent.getCountrycode());
        editor.putString(PHONE1, completedEvent.getPhone());
        editor.putString(KEY_ACKNW1, completedEvent.getAcknw());
        editor.commit();

    }

    public void storeEventIdCanceledEvent(CanceledEvent canceledEvent) {
        editor.putString(EVENT_INFO_ID2, canceledEvent.getId());
        editor.putString(KEY_EVENT_TILTLE2, canceledEvent.getEvent_title());
        editor.putString(KEY_EVENT_STATUS2, canceledEvent.getEvent_status());
        editor.putString(KEY_INVITER_NAME2, canceledEvent.getInvitername());
        editor.putString(SHARE_DETAIL2, canceledEvent.getShare_detail());
        editor.putString(ARTWORK2, canceledEvent.getArtwork());
        editor.putString(EVENTTYPE2, canceledEvent.getEventtype());
        editor.putString(CHATWINDOW2, canceledEvent.getChatw());
        editor.putString(COUNTRY_CODE2, canceledEvent.getCountrycode());
        editor.putString(PHONE2, canceledEvent.getPhone());
        editor.putString(KEY_ACKNW2, canceledEvent.getAcknw());
        editor.commit();

    }


    public void storeEventInfoID(FeedItem feedItem) {
        editor.putString(EVENT_INFO_ID, feedItem.getEventinfoID());
        editor.commit();
        Log.e(TAG, "Event Userinfo is stored in shared preferences. " + feedItem.getEventinfoID());

    }




    public User getUserId() {
        if (pref.getString(KEY_USER_ID, null) != null) {
            String userId, countrycode,mobileno;
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




    public ListEvent getEventId() {
        if (pref.getString(KEY_EVENT_ID, null) != null) {
            String eventId, eventTitle, event_Status, inviter_name, user_status, sharedetail, artwork, eventtype, chatwindow,countrycode,phone,organiser_id,acknw,noofdays;
            eventId = pref.getString(KEY_EVENT_ID, null);
            eventTitle = pref.getString(KEY_EVENT_TILTLE, null);
            event_Status = pref.getString(KEY_EVENT_STATUS, null);
            inviter_name = pref.getString(KEY_INVITER_NAME, null);
            user_status = pref.getString(KEY_USER_STATUS, null);
            sharedetail = pref.getString(SHARE_DETAIL, null);
            artwork = pref.getString(ARTWORK, null);
            eventtype = pref.getString(EVENTTYPE, null);
            chatwindow = pref.getString(CHATWINDOW, null);
            countrycode = pref.getString(COUNTRY_CODE1, null);
            phone = pref.getString(PHONE, null);
            organiser_id = pref.getString(OrganiserId_AUTO_LOGIN, null);
            acknw = pref.getString(KEY_ACKNW, null);
            noofdays = pref.getString(NOOFDAYS, null);

            ListEvent listEvent = new ListEvent(eventId, eventTitle, user_status, inviter_name, event_Status, null, sharedetail, artwork, eventtype, chatwindow,countrycode,phone,organiser_id,acknw,noofdays);
            return listEvent;

        }
        return null;
    }

    public CompletedEvent getCompletedEventId() {
        if (pref.getString(KEY_EVENT_ID1, null) != null) {
            String eventId, eventTitle, event_Status, inviter_name, user_status, sharedetail, artwork, eventtype, chatw,countrycode,phone,organiser_id,acknw;
            eventId = pref.getString(KEY_EVENT_ID1, null);
            eventTitle = pref.getString(KEY_EVENT_TILTLE1, null);
            event_Status = pref.getString(KEY_EVENT_STATUS1, null);
            inviter_name = pref.getString(KEY_INVITER_NAME1, null);
            user_status = pref.getString(KEY_USER_STATUS1, null);
            sharedetail = pref.getString(SHARE_DETAIL1, null);
            artwork = pref.getString(ARTWORK1, null);
            eventtype = pref.getString(EVENTTYPE1, null);
            chatw = pref.getString(CHATWINDOW1, null);
            countrycode = pref.getString(COUNTRY_CODE1, null);
            phone = pref.getString(PHONE1, null);
            organiser_id = pref.getString(OrganiserId_AUTO_LOGIN, null);
            acknw = pref.getString(KEY_ACKNW1, null);

            CompletedEvent listEvent = new CompletedEvent(eventId, eventTitle, user_status, inviter_name, event_Status, null, sharedetail, artwork, eventtype, chatw,countrycode,phone,organiser_id,acknw);
            return listEvent;

        }
        return null;
    }

    public CanceledEvent getCancelledEventID() {
        if (pref.getString(EVENT_INFO_ID2, null) != null) {
            String eventId, eventTitle, event_Status, inviter_name, user_status, sharedetail, artwork, eventtype, chatw,countrycode,phone,organiser_id,acknw;
            eventId = pref.getString(EVENT_INFO_ID2, null);
            eventTitle = pref.getString(KEY_EVENT_TILTLE2, null);
            event_Status = pref.getString(KEY_EVENT_STATUS2, null);
            inviter_name = pref.getString(KEY_INVITER_NAME2, null);
            user_status = pref.getString(KEY_USER_STATUS2, null);
            sharedetail = pref.getString(SHARE_DETAIL2, null);
            artwork = pref.getString(ARTWORK2, null);
            eventtype = pref.getString(EVENTTYPE2, null);
            chatw = pref.getString(CHATWINDOW2, null);
            countrycode = pref.getString(COUNTRY_CODE2, null);
            phone = pref.getString(PHONE2, null);
            acknw = pref.getString(KEY_ACKNW2, null);

            CanceledEvent listEvent = new CanceledEvent(eventId, eventTitle, user_status, inviter_name, event_Status, null, sharedetail, artwork, eventtype, chatw,countrycode,phone,acknw);
            return listEvent;

        }
        return null;
    }

    public String getOrganiserID() {
        if (pref.getString(OrganiserId, null) != null) {
            String organiserID;
            organiserID = pref.getString(OrganiserId, null);
            return organiserID;
        }

        return null;
    }

    public String getorgnaiserEmail(){
        if (pref.getString(ORGANISER_EMAIL, null) != null) {
            String organiserEMAIL;
            organiserEMAIL = pref.getString(ORGANISER_EMAIL, null);
            return organiserEMAIL;
        }

        return null;
    }

    public String listEventgetorganiserId() {
        if (pref.getString(LISTEVENTORGANISERID, null) != null) {
            String organiserID;
            organiserID = pref.getString(LISTEVENTORGANISERID, null);
            return organiserID;
        }

        return null;
    }


    public void addorganiserId(String Id) {
        editor.putString(OrganiserId, Id);
        editor.commit();
        Log.e(TAG, "Organiser ID stored  " + Id);

    }
    public void addtabmovement(String Id) {
        editor.putString(TABMOVEMENT, Id);
        editor.commit();
        Log.e(TAG, "addtabmovement ID stored  " + Id);

    }
    public void removetabmovement() {
        editor.remove(TABMOVEMENT);
        editor.commit();
    }
    public String getTabmovedId() {
        if (pref.getString(TABMOVEMENT, null) != null) {
            String tabmovedID;
            tabmovedID = pref.getString(TABMOVEMENT, null);
            return tabmovedID;
        }

        return null;
    }


    public void listeventaddorganiserId(String Id) {
        editor.putString(LISTEVENTORGANISERID, Id);
        editor.commit();
        Log.e(TAG, "Auto Organiser ID stored  " + Id);

    }

    public void clearnotification() {
        editor.remove(KEY_NOTIFICATIONS);
        editor.apply();
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }
}
