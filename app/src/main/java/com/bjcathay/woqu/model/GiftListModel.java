package com.bjcathay.woqu.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.woqu.constant.ApiUrl;

import java.util.List;

/**
 * Created by dengt on 15-10-19.
 */
public class GiftListModel {
    @JSONCollection(type = GiftModel.class)
    private List<GiftModel> gifts;
    private boolean hasNext;
    private int page;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<GiftModel> getGifts() {
        return gifts;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public void setGifts(List<GiftModel> gifts) {
        this.gifts = gifts;
    }

    private static IContentDecoder<GiftListModel> decoder = new IContentDecoder.BeanDecoder<GiftListModel>(
            GiftListModel.class);

    public static IPromise getGift(int page) {
        return Http.instance().get(ApiUrl.MY_GIFTS).param("page",page).contentDecoder(decoder).isCache(true).run();
    }
}
