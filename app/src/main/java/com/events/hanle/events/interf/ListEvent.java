package com.events.hanle.events.interf;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Hanle on 9/19/2017.
 */

public class ListEvent implements Serializable
{

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("event_response")
    @Expose
    private List<com.events.hanle.events.Model.ListEvent> eventResponse = null;
    private final static long serialVersionUID = 1515685163282765360L;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<com.events.hanle.events.Model.ListEvent> getEventResponse() {
        return eventResponse;
    }

    public void setEventResponse(List<com.events.hanle.events.Model.ListEvent> eventResponse) {
        this.eventResponse = eventResponse;
    }

}
