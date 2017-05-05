package com.events.hanle.events.Model;

/**
 * Created by Hanle on 6/15/2016.
 */
public class ListEvent {
    private String id, event_title, user_status, invitername, lastMessage, timestamp, event_status, share_detail, artwork, event_type, chat_window,date,time,monthno,weekday,countrycode,phone;
    private int unreadCount, unreadcount1;

    public ListEvent() {

    }
    public ListEvent(String id, String event_title, String lastMessage, String timestamp, int unreadCount) {
        this.id = id;
        this.event_title = event_title;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.unreadCount = unreadCount;
    }

    public ListEvent(String id, String event_title, String user_status, String invitername, String event_status, String lastMessage, String share_detail, String artwork, String event_type, String chat_window,String countrycode,String phone) {
        this.id = id;
        this.event_title = event_title;
        this.user_status = user_status;
        this.invitername = invitername;
        this.event_status = event_status;
        this.lastMessage = lastMessage;
        this.share_detail = share_detail;
        this.artwork = artwork;
        this.event_type = event_type;
        this.chat_window = chat_window;
        this.countrycode = countrycode;
        this.phone = phone;
    }

    public ListEvent(String countrycode,String phone){
        this.countrycode = countrycode;
        this.phone = phone;
    }

    public String getShare_detail() {
        return share_detail;
    }

    public void setShare_detail(String share_detail) {
        this.share_detail = share_detail;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }


    public String getInvitername() {
        return invitername;
    }

    public void setInvitername(String invitername) {
        this.invitername = invitername;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEvent_title() {
        return event_title;
    }

    public void setEvent_title(String event_title) {
        this.event_title = event_title;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getEvent_status() {
        return event_status;
    }

    public void setEvent_status(String event_status) {
        this.event_status = event_status;
    }

    public int getUnreadcount1() {
        return unreadcount1;
    }

    public void setUnreadcount1(int unreadcount1) {
        this.unreadcount1 = unreadcount1;
    }

    public String getArtwork() {
        return artwork;
    }

    public void setArtwork(String artwork) {
        this.artwork = artwork;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getChat_window() {
        return chat_window;
    }

    public void setChat_window(String chat_window) {
        this.chat_window = chat_window;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMonthno() {
        return monthno;
    }

    public void setMonthno(String monthno) {
        this.monthno = monthno;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }
    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
