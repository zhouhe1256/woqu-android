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
public class WoQuModel {
    private Boolean success;
    private Boolean hasNext;
    private String message;
    private int page;
    @JSONCollection(type = CampaignModel.class)
    private List<CampaignModel> campaigns;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<CampaignModel> getCampaigns() {
        return campaigns;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCampaigns(List<CampaignModel> campaigns) {
        this.campaigns = campaigns;
    }
    private static IContentDecoder<WoQuModel> decoder = new IContentDecoder.BeanDecoder<WoQuModel>(
            WoQuModel.class);
    public static IPromise getWoQuList(String page){
        return Http.instance().get(ApiUrl.CAMPAIGNS).param("page",page).contentDecoder(decoder).isCache(true).run();
    }
}
