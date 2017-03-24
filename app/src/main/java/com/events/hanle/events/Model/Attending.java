package com.events.hanle.events.Model;

/**
 * Created by Hanle on 8/30/2016.
 */
public class Attending {
    String id, nsme,status,mobile;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
