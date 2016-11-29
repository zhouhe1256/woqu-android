
package com.bjcathay.woqu.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.woqu.constant.ApiUrl;


import java.util.List;

/**
 * Created by jiangm on 15-10-10.
 */
public class ProvinceListModel {
    @JSONCollection(type = ProvinceModel.class)
    private List<ProvinceModel> provinces;

    public List<ProvinceModel> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<ProvinceModel> provinces) {
        this.provinces = provinces;
    }

    private static IContentDecoder<ProvinceListModel> decoder = new IContentDecoder.BeanDecoder<ProvinceListModel>(
            ProvinceListModel.class);

    public static IPromise Province() {
        return Http.instance().get(ApiUrl.PROVINCE).
                contentDecoder(decoder).isCache(true).run();
    }
}
