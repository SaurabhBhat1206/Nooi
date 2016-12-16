package com.events.hanle.events.Model;

import java.io.Serializable;


public class User implements Serializable {

    String id, name, mobile, countrycode;


    public User() {
    }

    public User(String id, String countrycode) {
        this.id = id;
        this.countrycode = countrycode;
    }


    public User(String id, String name, String mobile, String countrycode) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.countrycode = countrycode;
    }

    public User(String id, String name, String mobile) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

}
