package com.bjcathay.woqu.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.woqu.constant.ApiUrl;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jiangm on 15-10-12.
 */
public class ConsigneeListModel implements Serializable {
    @JSONCollection(type = ConsigneeModel.class)
    private List<ConsigneeModel> consignees;

    public List<ConsigneeModel> getConsignees() {
        return consignees;
    }

    public void setConsignees(List<ConsigneeModel> consignees) {
        this.consignees = consignees;
    }
    private static IContentDecoder<ConsigneeListModel> decoder = new IContentDecoder.BeanDecoder<ConsigneeListModel>(
            ConsigneeListModel.class);
    //收货人列表(GET /api/consignees)
    public static IPromise ConsigneeList() {
        return Http.instance().get(ApiUrl.CONSIGINESS_lIST).contentDecoder(decoder).isCache(true).run();
    }
}
