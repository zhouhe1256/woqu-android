package com.bjcathay.woqu.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.woqu.constant.ApiUrl;

import java.util.List;

/**
 * Created by zhouh on 15-10-16.
 */
public class CampaignModel {
    private int expect;
    private String attend;
    private String now;
    private String id;
    private String title;
    private String endAt;
    private String tagUrl;
    private String startAt;
    private String statusLabel;
    @JSONCollection(type = UserModel.class)
    private UserModel user;
    @JSONCollection(type = WoQuGiftModel.class)
    private List<WoQuGiftModel> gifts;
    private String status;

    public int getExpect() {
        return expect;
    }

    public void setExpect(int expect) {
        this.expect = expect;
    }

    public String getAttend() {
        return attend;
    }

    public void setAttend(String attend) {
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

    public String getTagUrl() {
        return tagUrl;
    }

    public void setTagUrl(String tagUrl) {
        this.tagUrl = tagUrl;
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

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public List<WoQuGiftModel> getGifts() {
        return gifts;
    }

    public void setGifts(List<WoQuGiftModel> gifts) {
        this.gifts = gifts;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    private static IContentDecoder<CampaignModel> decoder = new IContentDecoder.BeanDecoder<CampaignModel>(
            CampaignModel.class);
    public static IPromise campaignsDetails(String id) {
        return Http.instance().get(ApiUrl.getWishDetails(id)).contentDecoder(decoder).isCache(true).run();
    }
}
