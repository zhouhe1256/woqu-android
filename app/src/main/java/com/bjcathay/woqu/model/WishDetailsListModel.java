package com.bjcathay.woqu.model;

import com.bjcathay.android.json.annotation.JSONCollection;

import java.util.List;

/**
 * Created by zhouh on 15-10-22.
 */
public class WishDetailsListModel {
    private int expect;
    private Boolean attend;
    private String now;
    private String id;
    private String title;
    private String endAt;
    private String startAt;
    private String statusLabel;
    private String rule;
    @JSONCollection(type = UserModel.class)
    private UserModel user;
    @JSONCollection(type = WishDetailsGiftsModel.class)
    private List<WishDetailsGiftsModel> gifts;
    private int status;

    public int getExpect() {
        return expect;
    }

    public void setExpect(int expect) {
        this.expect = expect;
    }

    public Boolean getAttend() {
        return attend;
    }

    public void setAttend(Boolean attend) {
        this.attend = attend;
    }

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEndAt() {
        return endAt;
    }

    public void setEndAt(String endAt) {
        this.endAt = endAt;
    }

    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public List<WishDetailsGiftsModel> getGifts() {
        return gifts;
    }

    public void setGifts(List<WishDetailsGiftsModel> gifts) {
        this.gifts = gifts;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
