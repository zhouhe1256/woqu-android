package com.bjcathay.woqu.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.woqu.constant.ApiUrl;

import java.util.List;

/**
 * Created by zhouh on 15-10-19.
 */
public class WoQuListModel {
    private Boolean success;
    @JSONCollection(type = WoQuActivitysModel.class)
    private List<WoQuActivitysModel> activities;
    private Boolean hasNext;
    private int page;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<WoQuActivitysModel> getActivities() {
        return activities;
    }

    public void setActivities(List<WoQuActivitysModel> activities) {
        this.activities = activities;
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
    private static IContentDecoder<WoQuListModel> decoder = new IContentDecoder.BeanDecoder<WoQuListModel>(
            WoQuListModel.class);
    public static IPromise getWoQuList(String page){
        return Http.instance().get(ApiUrl.ACTIVITYS).param("page", page).contentDecoder(decoder).isCache(true).run();
    }

    public static IPromise getWishList(String id){
        return Http.instance().get(ApiUrl.getWishDetails(id)).contentDecoder(decoder).isCache(true).run();
    }
}
