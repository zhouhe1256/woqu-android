package com.bjcathay.woqu.model;

/**
 * Created by zhouh on 15-10-16.
 */
public class WoQuGiftModel {
    private double number;
    private String imageUrl;
    private String name;
    private String id;
    private String nickname;
    private Boolean selected;

    public double getNumber() {
        return number;
    }

    public void setNumber(double number) {
        this.number = number;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
