package com.events.hanle.events.Model;

/**
 * Created by Hanle on 5/23/2016.
 */
public class EventMessage {
    String id,user_id,title,description,event_status;
    String event_id,push_message,timestamp;
    String pushtype;

    public String getPushtype() {
        return pushtype;
    }

    public void setPushtype(String pushtype) {
        this.pushtype = pushtype;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getPush_message() {
        return push_message;
    }

    public void setPush_message(String push_message) {
        this.push_message = push_message;
    }

    public EventMessage() {
    }

    public EventMessage(String push_message){
        this.push_message = push_message;
    }

    public EventMessage(String id, String user_id, String title, String description) {
        this.id = id;
        this.user_id = user_id;
        this.title = title;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getEvent_status() {
        return event_status;
    }

    public void setEvent_status(String event_status) {
        this.event_status = event_status;
    }
}
