
package com.bjcathay.woqu.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.remote.Http;
import com.bjcathay.woqu.constant.ApiUrl;

import java.io.Serializable;

/**
 * Created by dengt on 15-10-20.
 */
public class PayModel implements Serializable {

    public static IPromise pay(long id, Long consigneeId, String type) {
        return Http.instance().get(ApiUrl.pay(id))
                .param("consigneeId", consigneeId).param("type", type).isCache(false).run();
    }
}
