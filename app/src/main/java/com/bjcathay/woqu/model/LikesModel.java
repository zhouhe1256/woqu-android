package com.bjcathay.woqu.model;

/**
 * Created by zhouh on 15-10-21.
 */
public class LikesModel {
    private String imageUrl;
    private String nickname;
    private long id;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
