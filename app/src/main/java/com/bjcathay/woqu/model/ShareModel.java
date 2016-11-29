package com.bjcathay.woqu.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.woqu.constant.ApiUrl;

/**
 * Created by zhouh on 15-10-20.
 */
public class ShareModel {
    private Boolean success;
    private String result;
    private String message;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private static IContentDecoder<ShareModel> decoder = new IContentDecoder.BeanDecoder<ShareModel>(
            ShareModel.class);
    public static IPromise share(String id) {
        return Http.instance().get(ApiUrl.getShare(id)).contentDecoder(decoder).
                run();
    }
    public static IPromise jingXuanShare(String id,String giftId) {
        return Http.instance().get(ApiUrl.getJingXuanShare(id)).param("giftId",giftId).contentDecoder(decoder).
        isCache(false).run();
    }
    public static IPromise participate(String id) {
        return Http.instance().post(ApiUrl.getParticipate(id)).contentDecoder(decoder).isCache(false).run();
    }

}
