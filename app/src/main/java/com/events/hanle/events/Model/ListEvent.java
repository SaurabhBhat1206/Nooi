package com.events.hanle.events.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Hanle on 6/15/2016.
 */
public class ListEvent implements Serializable {

    @SerializedName("event_status")
    @Expose
    private String eventStatus;

    @SerializedName("event_id")
    @Expose
    private String eventId;

    @SerializedName("event_title")
    @Expose
    private String eventTitle;

    @SerializedName("user_attending_status")
    @Expose
    private String userAttendingStatus;

    @SerializedName("inviter_name")
    @Expose
    private String inviterName;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("share_detail")
    @Expose
    private String shareDetail;

    @SerializedName("artwork")
    @Expose
    private String artwork;

    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("date1")
    @Expose
    private String date1;

    @SerializedName("weekday")
    @Expose
    private String weekday;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("chatW")
    @Expose
    private String chatW;

    @SerializedName("countrycode")
    @Expose
    private String countrycode;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("organiserId")
    @Expose
    private String organiserId;

    @SerializedName("rsvpdate")
    @Expose
    private String rsvpdate;

    @SerializedName("rsvptime")
    @Expose
    private String rsvptime;

    @SerializedName("acknw")
    @Expose
    private String acknw;

    @SerializedName("no_of_days")
    @Expose
    private String no_of_days;

    @SerializedName("enddate")
    @Expose
    private String enddate;

    private final static long serialVersionUID = 7703136315859209845L;
    private int unreadCount, unreadcount1;
    private String lastMessage;


    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getUnreadcount1() {
        return unreadcount1;
    }

    public void setUnreadcount1(int unreadcount1) {
        this.unreadcount1 = unreadcount1;
    }

    public ListEvent() {

    }

    public ListEvent(String id, String event_title, String user_status, String invitername, String event_status, String lastMessage, String share_detail, String artwork, String event_type, String chat_window, String countrycode, String phone, String organiserId, String acknw,String no_of_days) {
        this.eventId = id;
        this.eventTitle = event_title;
        this.userAttendingStatus = user_status;
        this.inviterName = invitername;
        this.eventStatus = event_status;
        this.lastMessage = lastMessage;
        this.shareDetail = share_detail;
        this.artwork = artwork;
        this.type = event_type;
        this.chatW = chat_window;
        this.countrycode = countrycode;
        this.phone = phone;
        this.organiserId = organiserId;
        this.acknw = acknw;
        this.no_of_days = no_of_days;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getUserAttendingStatus() {
        return userAttendingStatus;
    }

    public void setUserAttendingStatus(String userAttendingStatus) {
        this.userAttendingStatus = userAttendingStatus;
    }

    public String getInviterName() {
        return inviterName;
    }

    public void setInviterName(String inviterName) {
        this.inviterName = inviterName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getShareDetail() {
        return shareDetail;
    }

    public void setShareDetail(String shareDetail) {
        this.shareDetail = shareDetail;
    }

    public String getArtwork() {
        return artwork;
    }

    public void setArtwork(String artwork) {
        this.artwork = artwork;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate1() {
        return date1;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChatW() {
        return chatW;
    }

    public void setChatW(String chatW) {
        this.chatW = chatW;
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

    public String getOrganiserId() {
        return organiserId;
    }

    public void setOrganiserId(String organiserId) {
        this.organiserId = organiserId;
    }

    public String getRsvpdate() {
        return rsvpdate;
    }

    public void setRsvpdate(String rsvpdate) {
        this.rsvpdate = rsvpdate;
    }

    public String getRsvptime() {
        return rsvptime;
    }

    public void setRsvptime(String rsvptime) {
        this.rsvptime = rsvptime;
    }

    public String getAcknw() {
        return acknw;
    }

    public void setAcknw(String acknw) {
        this.acknw = acknw;
    }
    public String getNo_of_days() {
        return no_of_days;
    }

    public void setNo_of_days(String no_of_days) {
        this.no_of_days = no_of_days;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }
}
