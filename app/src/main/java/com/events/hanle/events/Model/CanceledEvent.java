package com.events.hanle.events.Model;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by Hanle on 10/18/2016.
 */

public class CanceledEvent {
    String id, event_title, user_status, invitername, lastMessage, timestamp, event_status, random_no, share_detail, artwork, eventtype, chatw;
    int unreadCount;


    public CanceledEvent() {

    }

    public CanceledEvent(String id, String event_title, String lastMessage, String timestamp, int unreadCount) {
        this.id = id;
        this.event_title = event_title;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.unreadCount = unreadCount;
    }

    public CanceledEvent(String id, String event_title, String user_status, String invitername, String event_status, String lastMessage, String share_detail, String artwork, String eventtype, String chatw) {
        this.id = id;
        this.event_title = event_title;
        this.user_status = user_status;
        this.invitername = invitername;
        this.event_status = event_status;
        this.lastMessage = lastMessage;
        this.share_detail = share_detail;
        this.artwork = artwork;
        this.eventtype = eventtype;
        this.chatw = chatw;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getEvent_status() {
        return event_status;
    }

    public void setEvent_status(String event_status) {
        this.event_status = event_status;
    }

    public String getRandom_no() {
        return random_no;
    }

    public void setRandom_no(String random_no) {
        this.random_no = random_no;
    }

    public String getShare_detail() {
        return share_detail;
    }

    public void setShare_detail(String share_detail) {
        this.share_detail = share_detail;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getArtwork() {
        return artwork;
    }

    public void setArtwork(String artwork) {
        this.artwork = artwork;
    }

    public String getEventtype() {
        return eventtype;
    }

    public void setEventtype(String eventtype) {
        this.eventtype = eventtype;
    }

    public String getChatw() {
        return chatw;
    }

    public void setChatw(String chatw) {
        this.chatw = chatw;
    }
}
