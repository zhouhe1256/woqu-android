package com.bjcathay.woqu.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.woqu.constant.ApiUrl;

import java.io.Serializable;

/**
 * Created by zhouh on 15-10-19.
 */
public class PointPraiseModel implements Serializable{
    private Boolean success;
    @JSONCollection(type = PointPraiseListModel.class)
    private PointPraiseListModel activity;
    private String message;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public PointPraiseListModel getActivity() {
        return activity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setActivity(PointPraiseListModel activity) {
        this.activity = activity;
    }
    private static IContentDecoder<PointPraiseModel> decoder = new IContentDecoder.BeanDecoder<PointPraiseModel>(
            PointPraiseModel.class);
    public static IPromise getActivitys(String id){
        return Http.instance().get(ApiUrl.getActivitys(id)).contentDecoder(decoder).isCache(true).run();
    }
    public static IPromise getWishList(String id){
        //return Http.instance().get(ApiUrl.getWishDetails(id)).contentDecoder(decoder).run();
        return Http.instance().get(ApiUrl.getActivitys(id)).contentDecoder(decoder).isCache(true).run();
    }
}
