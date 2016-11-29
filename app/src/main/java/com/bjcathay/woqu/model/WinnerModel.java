package com.bjcathay.woqu.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.woqu.constant.ApiUrl;

import java.util.List;

/**
 * Created by zhouh on 15-10-29.
 */
public class WinnerModel {
    private Boolean success;
    private Boolean hasNext;
    private int page;
    @JSONCollection(type = WinnerListModel.class)
    private List<WinnerListModel> winners;

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

    public List<WinnerListModel> getWinners() {
        return winners;
    }

    public void setWinners(List<WinnerListModel> winners) {
        this.winners = winners;
    }
    private static IContentDecoder<WinnerModel> decoder = new IContentDecoder.BeanDecoder<WinnerModel>(
            WinnerModel.class);
    public static IPromise getWinner(String id) {
        return Http.instance().get(ApiUrl.getWinner(id)).
                contentDecoder(decoder).isCache(true).run();
    }
}
