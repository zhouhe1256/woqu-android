
package com.bjcathay.woqu.model;

import com.bjcathay.android.remote.IContentDecoder;

import java.io.Serializable;

/**
 * Created by dengt on 15-10-19.
 */
public class ExpressModel implements Serializable {
    private double price;
    private String name;
    private long id;
    private String content;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    private static IContentDecoder<ExpressModel> decoder = new IContentDecoder.BeanDecoder<ExpressModel>(
            ExpressModel.class);
}
