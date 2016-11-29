package com.bjcathay.woqu.model;

/**
 * Created by zhouh on 15-10-21.
 */
public class CommentListModel {
    private String created;
    private String imageUrl;
    private String nickname;
    private String humanDate;
    private String id;
    private String content;

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

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

    public String getHumanDate() {
        return humanDate;
    }

    public void setHumanDate(String humanDate) {
        this.humanDate = humanDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
