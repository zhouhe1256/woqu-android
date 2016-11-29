package com.bjcathay.woqu.model;

import com.bjcathay.android.json.annotation.JSONCollection;

import java.util.List;

/**
 * Created by zhouh on 15-10-21.
 */
public class SunTalkModel {
    private String commentNumber;
    private Boolean isLike;
    private String created;
    @JSONCollection(type = String.class)
    private List<String> imageUrls;
    private String humanDate;
    private String location;
    private String id;
    private String userId;
    private String pageview;
    private String deviceName;
    private String content;
    private int likeNumber;
    @JSONCollection(type = LikesModel.class)
    private List<LikesModel> likes;
    @JSONCollection(type = WoQuGiftModel.class)
    private WoQuGiftModel user;

    public String getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(String commentNumber) {
        this.commentNumber = commentNumber;
    }

    public Boolean getIsLike() {
        return isLike;
    }

    public void setIsLike(Boolean isLike) {
        this.isLike = isLike;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getHumanDate() {
        return humanDate;
    }

    public void setHumanDate(String humanDate) {
        this.humanDate = humanDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPageview() {
        return pageview;
    }

    public void setPageview(String pageview) {
        this.pageview = pageview;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikeNumber() {
        return likeNumber;
    }

    public void setLikeNumber(int likeNumber) {
        this.likeNumber = likeNumber;
    }

    public List<LikesModel> getLikes() {
        return likes;
    }

    public void setLikes(List<LikesModel> likes) {
        this.likes = likes;
    }

    public WoQuGiftModel getUser() {
        return user;
    }

    public void setUser(WoQuGiftModel user) {
        this.user = user;
    }
}
