package com.bjcathay.woqu.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.woqu.constant.ApiUrl;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jiangm on 15-10-10.
 */
public class DistrictListModel implements Serializable{
    @JSONCollection(type = DistrictModel.class)
    private List<DistrictModel> counties;

    public List<DistrictModel> getCounties() {
        return counties;
    }

    public void setCounties(List<DistrictModel> counties) {
        this.counties = counties;
    }

    private static IContentDecoder<DistrictListModel> decoder = new IContentDecoder.BeanDecoder<DistrictListModel>(
            DistrictListModel.class);

    public static IPromise Districts() {
        return Http.instance().get(ApiUrl.COUNTIES).
                contentDecoder(decoder).isCache(true).run();
    }
}
