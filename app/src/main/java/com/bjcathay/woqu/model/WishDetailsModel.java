package com.bjcathay.woqu.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.woqu.constant.ApiUrl;

import java.util.List;

/**
 * Created by zhouh on 15-10-22.
 */
public class WishDetailsModel {
    private Boolean success;
    private String message;
    @JSONCollection(type = WishDetailsListModel.class)
    private WishDetailsListModel campaign;


    public WishDetailsListModel getCampaign() {
        return campaign;
    }

    public void setCampaign(WishDetailsListModel campaign) {
        this.campaign = campaign;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private static IContentDecoder<WishDetailsModel> decoder = new IContentDecoder.BeanDecoder<WishDetailsModel>(
            WishDetailsModel.class);
    public static IPromise getWishList(String id){
        return Http.instance().get(ApiUrl.getWishDetails(id)).contentDecoder(decoder).isCache(true).run();
    }
}
