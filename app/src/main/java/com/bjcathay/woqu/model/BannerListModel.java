
package com.bjcathay.woqu.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.woqu.constant.ApiUrl;

import java.io.Serializable;
import java.util.List;


public class BannerListModel implements Serializable {
    @JSONCollection(type = BannerModel.class)
    private List<BannerModel> banners;
    private Boolean success;
    public List<BannerModel> getBanners() {
        return banners;
    }

    public void setBanners(List<BannerModel> banners) {
        this.banners = banners;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    private static IContentDecoder<BannerListModel> decoder = new IContentDecoder.BeanDecoder<BannerListModel>(
            BannerListModel.class);


    public static IPromise getHomeBanners() {
        return Http.instance().get(ApiUrl.HOME_BANNER).
                contentDecoder(decoder).isCache(true).run();
    }
}
