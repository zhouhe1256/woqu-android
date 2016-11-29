package com.bjcathay.woqu.model;

import com.bjcathay.android.json.annotation.JSONCollection;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jiangm on 15-10-22.
 */
public class ActivityModel implements Serializable{
    private Boolean attend;
    private String title;
    private long endAt;
    private String tagUrl;
    private String statusLabel;
    private int number;
    private double price;
    private long now;
    private String imageUrl;
    private String id;
    private long startAt;
    private int status;

    public Boolean getAttend() {
        return attend;
    }

    public void setAttend(Boolean attend) {
        this.attend = attend;
    }

    public long getEndAt() {
        return endAt;
    }

    public void setEndAt(long endAt) {
        this.endAt = endAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getStartAt() {
        return startAt;
    }

    public void setStartAt(long startAt) {
        this.startAt = startAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public String getTagUrl() {
        return tagUrl;
    }

    public void setTagUrl(String tagUrl) {
        this.tagUrl = tagUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
