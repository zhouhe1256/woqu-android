
package com.bjcathay.woqu.model;


import java.io.Serializable;

/**
 * Created by dengt on 15-5-22.
 */
public class PushModel implements Serializable {
    private String t;
    private String id;
    private String m;

    public String getT() {
            return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }
}
