package com.events.hanle.events.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Hanle on 8/4/2017.
 */

public class ImageUpload  {


    @SerializedName("message")
    private String message;
    @SerializedName("count")
    private int count;
    @SerializedName("success")
    private int success;

    public int getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public int getCount() {
        return count;
    }
}
