package com.events.hanle.events.app;

/**
 * Created by Saurabh on 06/01/16.
 */
public class EndPoints {

    // localhost url
    //production url
   // public static final String BASE_URL = "http://hanlesolutions.com/nooi/newchat1.05/gcm_chat/v1/index.php";

    public static final String BASE_URL = "http://uat.hanlesolutions.com/hanle-test/14-09-2016-live/newchat/gcm_chat/v1/index.php";
    public static final String LOGIN = BASE_URL + "/user/login";
    public static final String USER = BASE_URL + "/user/_ID_";
    public static final String CHAT_ROOMS = BASE_URL + "/chat_rooms";
    public static String CHAT_THREAD = BASE_URL + "/chat_rooms/_ID_&_PAGECOUNT_";
    public static final String CHAT_ROOM_MESSAGE = BASE_URL + "/chat_rooms/_ID_/message";
    public static final String CHAT_ROOMS_LIST = BASE_URL + "/chat_rooms_list/COUNTRY_CODE&_USERID_";
    public final static String LIST_COMPLETED = BASE_URL + "/completed_event/COUNTRY_CODE&_USERID_";
    public final static String LIST_CANCELLED = BASE_URL + "/canceled_event/COUNTRY_CODE&_USERID_";

}