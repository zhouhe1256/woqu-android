package com.bjcathay.woqu.model;

import com.bjcathay.android.json.annotation.JSONCollection;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhouh on 15-10-19.
 */
public class PointPraiseGiftsModel implements Serializable {
    private String friendlyLink;
    private double price;
    private String giftDetail;
    @JSONCollection(type = String.class)
    private List<String> imageUrls;
    private String name;
    private String id;

    public String getFriendlyLink() {
        return friendlyLink;
    }

    public void setFriendlyLink(String friendlyLink) {
        this.friendlyLink = friendlyLink;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getGiftDetail() {
        return giftDetail;
    }

    public void setGiftDetail(String giftDetail) {
        this.giftDetail = giftDetail;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
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
}
