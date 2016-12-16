package com.events.hanle.events.Model;

/**
 * Created by Hanle on 11/8/2016.
 */

public class ListEventCopy {

    String id, user_status, lastMessage;
    int unreadCount;

    public ListEventCopy() {

    }

    public ListEventCopy(String id, String lastMessage, int unreadCount) {
        this.id = id;
        this.lastMessage = lastMessage;
        this.unreadCount = unreadCount;
    }

    public ListEventCopy(String id, String user_status, String lastMessage) {
        this.id = id;
        this.user_status = user_status;
        this.lastMessage = lastMessage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
}
