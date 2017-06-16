package com.events.hanle.events.Model;

/**
 * Created by Hanle on 10/18/2016.
 */

public class CompletedEvent {
    private String id, event_title, user_status, invitername, lastMessage, timestamp, event_status, random_no, share_detail, artwork, event_type, chat_window, countrycode, phone, organiserId;
    private int unreadCount;


    public CompletedEvent() {

    }

    public CompletedEvent(String id, String event_title, String lastMessage, String timestamp, int unreadCount) {
        this.id = id;
        this.event_title = event_title;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.unreadCount = unreadCount;
    }

    public CompletedEvent(String id, String event_title, String user_status, String invitername, String event_status, String lastMessage, String share_detail, String artwork, String event_type, String chat_window, String countrycode, String phone, String organiserId) {
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
        this.organiserId = organiserId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getShare_detail() {
        return share_detail;
    }

    public void setShare_detail(String share_detail) {
        this.share_detail = share_detail;
    }

    public String getRandom_no() {
        return random_no;
    }

    public void setRandom_no(String random_no) {
        this.random_no = random_no;
    }

    public String getEvent_status() {
        return event_status;
    }

    public void setEvent_status(String event_status) {
        this.event_status = event_status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getInvitername() {
        return invitername;
    }

    public void setInvitername(String invitername) {
        this.invitername = invitername;
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
}
