package com.bjcathay.woqu.model;

import java.io.Serializable;

/**
 * Created by jiangm on 15-10-22.
 */
public class DataModel implements Serializable{
    private  String time;
    private  String location;
    private  String context;

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
