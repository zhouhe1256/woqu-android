package com.bjcathay.woqu.model;

import java.io.Serializable;

/**
 * Created by jiangm on 15-10-10.
 */
public class DistrictModel implements Serializable{
//    id: 1,
//    name: "朝阳区",
//    cityId：1, ///城市id
//    hot:  true|false,
//    firstLetter: "c",
//    shortLetter: "cyq",
//    allLetter: "chaoyangqu"

    private String allLetter;
    private String name;
    private long id;
    private long cityId;
    private boolean hot;
    private String firstLetter;
    private String shortLetter;


    public String getAllLetter() {
        return allLetter;
    }

    public void setAllLetter(String allLetter) {
        this.allLetter = allLetter;
    }

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public boolean isHot() {
        return hot;
    }

    public void setHot(boolean hot) {
        this.hot = hot;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortLetter() {
        return shortLetter;
    }

    public void setShortLetter(String shortLetter) {
        this.shortLetter = shortLetter;
    }
}
