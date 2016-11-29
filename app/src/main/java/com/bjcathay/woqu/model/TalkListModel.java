package com.bjcathay.woqu.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.woqu.constant.ApiUrl;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhouh on 15-10-8.
 */
public class TalkListModel implements Serializable{
    private Boolean success;
    @JSONCollection(type = TalkModel.class)
    private List<TalkModel> talks;
    private Boolean hasNext;
    private int page;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }



    public List<TalkModel> getTalks() {
        return talks;
    }

    public void setTalks(List<TalkModel> talks) {
        this.talks = talks;
    }

    private static IContentDecoder<TalkListModel> decoder = new IContentDecoder.BeanDecoder<TalkListModel>(
            TalkListModel.class);
    public static IPromise talks(String content, String deviceName,String location,String imageIds) {

        return Http.instance().post(ApiUrl.TALKS)
                .param("content", content).param("deviceName", deviceName)
                .param("location",location).param("imageIds",imageIds).contentDecoder(decoder).isCache(true).run();

    }

    public static IPromise talks(String page) {

        return Http.instance().get(ApiUrl.TALKS).param("page",page).contentDecoder(decoder).isCache(true).run();

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
}
