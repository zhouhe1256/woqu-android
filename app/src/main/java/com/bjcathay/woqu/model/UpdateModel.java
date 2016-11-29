
package com.bjcathay.woqu.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.woqu.constant.ApiUrl;


import java.io.Serializable;

/**
 * Created by dengt on 15-1-4.
 */
public class UpdateModel implements Serializable {

    private double version;
    private String url;
    private String description;
    private double minVersion;

    public double getMinVersion() {
        return minVersion;
    }

    public void setMinVersion(double minVersion) {
        this.minVersion = minVersion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private static IContentDecoder<UpdateModel> decoder = new IContentDecoder.BeanDecoder<UpdateModel>(
            UpdateModel.class, "version");

    public static IPromise sendVersion() {
        return Http.instance().get(ApiUrl.UPDATE).
                contentDecoder(decoder).isCache(false).run();
    }
}
