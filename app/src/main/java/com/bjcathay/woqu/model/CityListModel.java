
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
public class CityListModel implements Serializable {
    @JSONCollection(type = GetCitysModel.class)
    private List<GetCitysModel> cities;

    public void setCities(List<GetCitysModel> cities) {
        this.cities = cities;
    }

    public List<GetCitysModel> getCities() {
        return cities;
    }

    private static IContentDecoder<CityListModel> decoder = new IContentDecoder.BeanDecoder<CityListModel>(
            CityListModel.class);

    public static IPromise Cities() {
        return Http.instance().get(ApiUrl.CITIES).
                contentDecoder(decoder).isCache(true).run();
    }
}
