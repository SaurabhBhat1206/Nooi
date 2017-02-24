package com.events.hanle.events.Constants;

/**
 * Created by Hanle on 4/28/2016.
 */
public class WebUrl {

    //production url
    //public final static String BASE_URL = "http://hanlesolutions.com/nooi/event_mobile_webservices/phpscript";
    public final static String BASE_URL = "http://uat.hanlesolutions.com/hanle-test/mobiletest/event_mobile_webservices/phpscript";
    //public final static String BASE_URL = "http://www.hanlesolutions.com/nooi/test/event_mobile_webservices/phpscript";

    public final static String USER_LOGIN_URL = BASE_URL + "/otp/login_smscountry.php";
    public final static String CHECK_OTP = BASE_URL + "/otp/confirm.php";
    public final static String USER_CHNAGE_DECISION = BASE_URL + "/userchange_decision.php";
    public final static String USER_EVENT_URL = BASE_URL + "/ur-v2.php?eventid=EVENT_ID&phone=MOBILE_NO&countrycode=";
    public final static String LIST_EVENT_URL = BASE_URL + "/list_event.php?user_id=";
    public final static String USER_ATTENDING = BASE_URL + "/user_attending_status_yes.php";
    public final static String USER_NOTATTENDING = BASE_URL + "/user_attending_status_no.php";
    public final static String LIST_CONFIRMATION = BASE_URL + "/list_event_attending_not_attending.php?eventid=";
    public final static String LIST_ATTENDING_CONTACT = BASE_URL + "/list_attending.php?eventid=";
    public final static String ORGANISER_ARTWORK = BASE_URL+"/organiser_art_work.php?id=";
    public final static String ORGANISER_LOGIN = "http://uat.hanlesolutions.com/hanle-test/mobiletest/mobile/Login/admin_login";

    public static final int MY_SOCKET_TIMEOUT_MS = 30000;

}
