package com.events.hanle.events.Model;

/**
 * Created by Hanle on 8/30/2016.
 */
public class Attending {
    private String nsme,status,mobile,alreadyinvited;
    private int id;

    public String getAlreadyinvited() {
        return alreadyinvited;
    }

    public void setAlreadyinvited(String alreadyinvited) {
        this.alreadyinvited = alreadyinvited;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNsme() {
        return nsme;
    }

    public void setNsme(String nsme) {
        this.nsme = nsme;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
