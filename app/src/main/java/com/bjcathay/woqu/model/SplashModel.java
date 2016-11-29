package com.bjcathay.woqu.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.woqu.constant.ApiUrl;

import java.io.Serializable;

/**
 * Created by dengt on 15-11-3.
 */
public class SplashModel implements Serializable {
    private  int id;
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    private static IContentDecoder<SplashModel> decoder = new IContentDecoder.BeanDecoder<SplashModel>(
            SplashModel.class, "splash");
    public static IPromise getImg() {
        return Http.instance().get(ApiUrl.SPLASH_IMG).contentDecoder(decoder).isCache(true).run();
    }
}
