package com.events.hanle.events.Model;

/**
 * Created by Hanle on 10/28/2016.
 */


/**
 * Created by Hanle on 10/26/2016.
 */

public class CancelledEventView {

    private String event_creator_name;
    private String eventname;
    private String eventdesc;
    private String date;
    private String time;
    private String address;
    private Double latitude;
    private Double longitude;
    private String eventinfoID;
    private String payment;
    private String dresscode;
    private String timezone;
    private String orgnaserphone;
    private String weekday;

    public CancelledEventView() {

    }

    public CancelledEventView(String eventinfoID) {
        this.eventinfoID = eventinfoID;
    }

    public String getEvent_creator_name() {
        return event_creator_name;
    }

    public void setEvent_creator_name(String event_creator_name) {
        this.event_creator_name = event_creator_name;
    }

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public String getEventdesc() {
        return eventdesc;
    }

    public void setEventdesc(String eventdesc) {
        this.eventdesc = eventdesc;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getEventinfoID() {
        return eventinfoID;
    }

    public void setEventinfoID(String eventinfoID) {
        this.eventinfoID = eventinfoID;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getDresscode() {
        return dresscode;
    }

    public void setDresscode(String dresscode) {
        this.dresscode = dresscode;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getOrgnaserphone() {
        return orgnaserphone;
    }

    public void setOrgnaserphone(String orgnaserphone) {
        this.orgnaserphone = orgnaserphone;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }
}
