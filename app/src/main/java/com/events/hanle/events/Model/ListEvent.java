package com.events.hanle.events.Model;

/**
 * Created by Hanle on 6/15/2016.
 */
public class ListEvent {
    String id, event_title, user_status, invitername, lastMessage, timestamp, event_status, random_no, share_detail;
    int unreadCount;


    public ListEvent() {

    }

    public ListEvent(String id, String event_title, String lastMessage, String timestamp, int unreadCount) {
        this.id = id;
        this.event_title = event_title;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.unreadCount = unreadCount;
    }

    public ListEvent(String id, String event_title, String user_status, String invitername, String event_status, String lastMessage, String share_detail) {
        this.id = id;
        this.event_title = event_title;
        this.user_status = user_status;
        this.invitername = invitername;
        this.event_status = event_status;
        this.lastMessage = lastMessage;
        this.share_detail = share_detail;
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

}
