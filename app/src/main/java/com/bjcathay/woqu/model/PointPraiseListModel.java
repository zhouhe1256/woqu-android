package com.bjcathay.woqu.model;

import com.bjcathay.android.json.annotation.JSONCollection;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhouh on 15-10-19.
 */
public class PointPraiseListModel implements Serializable{
    private Boolean attend;
    private String rule;
    private String title;
    private long endAt;
    private String statusLabel;
    private int number;
    private double price;
    private long now;
    private String id;
    private long startAt;
    private int status;
    private Boolean remind;
    private String attendResult;
    private String resultModel;
    private String luckyType;
    private String tagUrl;
    private String attendResultUrl;
    private String imageUrl;
    @JSONCollection(type = PointPraiseGiftsModel.class)
    private PointPraiseGiftsModel gift;

    public Boolean getAttend() {
        return attend;
    }

    public void setAttend(Boolean attend) {
        this.attend = attend;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getEndAt() {
        return endAt;
    }

    public void setEndAt(long endAt) {
        this.endAt = endAt;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
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

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public PointPraiseGiftsModel getGift() {
        return gift;
    }

    public void setGift(PointPraiseGiftsModel gift) {
        this.gift = gift;
    }

    public Boolean getRemind() {
        return remind;
    }

    public void setRemind(Boolean remind) {
        this.remind = remind;
    }

    public String getAttendResult() {
        return attendResult;
    }

    public void setAttendResult(String attendResult) {
        this.attendResult = attendResult;
    }

    public String getResultModel() {
        return resultModel;
    }

    public void setResultModel(String resultModel) {
        this.resultModel = resultModel;
    }

    public String getLuckyType() {
        return luckyType;
    }

    public void setLuckyType(String luckyType) {
        this.luckyType = luckyType;
    }

    public String getTagUrl() {
        return tagUrl;
    }

    public void setTagUrl(String tagUrl) {
        this.tagUrl = tagUrl;
    }

    public String getAttendResultUrl() {
        return attendResultUrl;
    }

    public void setAttendResultUrl(String attendResultUrl) {
        this.attendResultUrl = attendResultUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
