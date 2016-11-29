package com.bjcathay.woqu.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.woqu.constant.ApiUrl;

/**
 * Created by zhouh on 15-10-21.
 */
public class SunAwardDetailsTalkModel {
    private Boolean success;
    @JSONCollection(type = SunTalkModel.class)
    private SunTalkModel talk;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public SunTalkModel getTalk() {
        return talk;
    }

    public void setTalk(SunTalkModel talk) {
        this.talk = talk;
    }

    private static IContentDecoder<SunAwardDetailsTalkModel> decoder = new IContentDecoder.BeanDecoder<SunAwardDetailsTalkModel>(
            SunAwardDetailsTalkModel.class);
    public static IPromise talks(String id) {
        return Http.instance().get(ApiUrl.talks(id)).param("id",id).contentDecoder(decoder).isCache(true).run();

    }

}
