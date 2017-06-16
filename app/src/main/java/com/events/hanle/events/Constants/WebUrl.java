package com.events.hanle.events.Constants;

/**
 * Created by Hanle on 4/28/2016.
 */
public class WebUrl {

    //production url
    //public final static String BASE_URL = "http://nooitheinviteapp.com/mobile/event_mobile_webservices/phpscript";
   // public final static String ORGANISER_URL = "http://nooitheinviteapp.com/mobile/organiser_mobile/mobile";

    //me loki testing
    public final static String BASE_URL = "http://uat.hanlesolutions.com/hanle-test/mobiletest/event_mobile_webservices/phpscript";
    public final static String ORGANISER_URL = "http://uat.hanlesolutions.com/hanle-test/mobiletest/mobile";

    //beta testing
    //public static final String BASE_URL = "http://uat.hanlesolutions.com/hanle-test/beta/mobiletest/event_mobile_webservices/phpscript";
    //public final static String ORGANISER_URL = "http://uat.hanlesolutions.com/hanle-test/beta/mobiletest/mobile";

    public final static String USER_LOGIN_URL = BASE_URL + "/otp/login_smscountry.php";
    //public final static String USER_LOGIN_URL = ORGANISER_URL + "/Login/mobile_login";
    public final static String CHECK_OTP = BASE_URL + "/otp/confirm.php";
    public final static String USER_CHNAGE_DECISION = BASE_URL + "/userchange_decision.php";
    public final static String USER_EVENT_URL = BASE_URL + "/ur-v2.php?eventid=EVENT_ID&phone=MOBILE_NO&countrycode=";
    public final static String LIST_EVENT_URL = BASE_URL + "/list_event.php?user_id=";
    public final static String USER_ATTENDING = BASE_URL + "/user_attending_status_yes.php";
    public final static String USER_NOTATTENDING = BASE_URL + "/user_attending_status_no.php";
   // public final static String LIST_CONFIRMATION = BASE_URL + "/list_event_attending_not_attending.php?eventid=";
    public final static String LIST_ATTENDING_CONTACT = BASE_URL + "/list_attending.php?eventid=";
    public final static String ORGANISER_ARTWORK = BASE_URL + "/organiser_art_work.php?id=";
    public final static String ORGANISER_LOGIN = ORGANISER_URL + "/Login/admin_login";
    public final static String ORGANISER_LOGIN1 = ORGANISER_URL + "/Login/admin_login_active_events";
    //public final static String ORGANISER_LOGIN = ORGANISER_URL + "/Login/auto_admin_login";
    public final static String ORGANISER_CREATE_NEW_USER = ORGANISER_URL + "/User/createNewUser";
    public final static String ORGANISER_Invitee_list = ORGANISER_URL + "/event/getinviteedata?organiser_id=ORGANISER_ID&eventId=EVENT_ID";
    public final static String INVITE_USER = ORGANISER_URL + "/event/inviteMultipleUsers";
    public final static String PUSH_MESSAGE = ORGANISER_URL + "/event/pushMessage";
    public final static String CANCEL_MESSAGE = ORGANISER_URL + "/event/CancelEventDataById";
    public final static String GET_COUPON_DETIALS = ORGANISER_URL + "/event/get_coupon_detials?eventID=EVENTID";
    public final static String ACTIVE_EVENTS = ORGANISER_URL + "/event/list_of_events?countrycode=COUNTRY_CODE&phone=_USERID_";
    public final static String EVENT_DETAILS = ORGANISER_URL + "/event/event_detials?eventid=EVENT_ID&phone=MOBILE_NO&countrycode=";
    public final static String ATTENDING_OR_NOT = ORGANISER_URL + "/event/get_data_attending_not_atending?eventid=";
    public final static String EVENT_DETIALS_OFFLINE = ORGANISER_URL + "/event/list_of_events_for_offline?countrycode=COUNTRY_CODE&phone=_USERID_";
    public final static String EVENT_DETIALS_OFFLINE_CANCELLED = ORGANISER_URL + "/event/list_of_events_cancelled_for_offline?countrycode=COUNTRY_CODE&phone=_USERID_";
    public final static String EVENT_DETIALS_OFFLINE_CONCLUDED = ORGANISER_URL + "/event/list_of_events_concluded_for_offline?countrycode=COUNTRY_CODE&phone=_USERID_";
    public static final int MY_SOCKET_TIMEOUT_MS = 30000;
}
