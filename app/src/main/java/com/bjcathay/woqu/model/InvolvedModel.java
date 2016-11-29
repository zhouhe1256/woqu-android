package com.bjcathay.woqu.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.woqu.constant.ApiUrl;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bjcathay on 15-10-22.
 */
public class InvolvedModel implements Serializable{
    private boolean hasNext;
    private int page;
    @JSONCollection(type = ActivityModel.class)
    private List<ActivityModel> activities;

    public List<ActivityModel> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityModel> activities) {
        this.activities = activities;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
    private static IContentDecoder<InvolvedModel> decoder = new IContentDecoder.BeanDecoder<InvolvedModel>(
            InvolvedModel.class);
    public static IPromise getActivityList(int page){
        return Http.instance().get(ApiUrl.MY_INVOLVED).param("page",page).contentDecoder(decoder).isCache(true).run();
    }
}
