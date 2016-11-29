package com.bjcathay.woqu.model;



import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.IContentDecoder;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jiangm on 15-10-22.
 */
public class ExpressDataModel implements Serializable{
    private String nu;
    private String state;

    private List<DataModel> data;

    public List<DataModel> getData() {
        return data;
    }

    public void setData(List<DataModel> data) {
        this.data = data;
    }

    public String getNu() {
        return nu;
    }

    public void setNu(String nu) {
        this.nu = nu;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
   private static IContentDecoder<ExpressDataModel> decoder = new IContentDecoder.BeanDecoder<ExpressDataModel>(
            ExpressDataModel.class,"data");

}
