package com.bjcathay.woqu.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.woqu.constant.ApiUrl;

import java.io.Serializable;

/**
 * Created by jiagnm on 15-10-20.
 */
public class OrderModel implements Serializable{
    private long id;
    private String status;//状态
    private String name;//标题
    private String imageUrl;
    private long imageId;
    private long createdAt;
    private String orderId;
    private String expressOrderId;
    @JSONCollection(type = ConsigneeModel.class)
    private ConsigneeModel consignee;
    @JSONCollection(type = ExpressModel.class)
    private ExpressModel express;
    private String expressData;
//    @JSONCollection(type = ExpressDataModel.class)
//    private ExpressDataModel expressData;

//    public ExpressDataModel getExpressData() {
//        return expressData;
//    }
//
//    public void setExpressData(ExpressDataModel expressData) {
//        this.expressData = expressData;
//    }


    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public String getExpressOrderId() {
        return expressOrderId;
    }

    public void setExpressOrderId(String expressOrderId) {
        this.expressOrderId = expressOrderId;
    }

    public String getExpressData() {
        return expressData;
    }

    public void setExpressData(String expressData) {
        this.expressData = expressData;
    }

    public ConsigneeModel getConsignee() {
        return consignee;
    }

    public void setConsignee(ConsigneeModel consignee) {
        this.consignee = consignee;
    }

    public ExpressModel getExpress() {
        return express;
    }

    public void setExpress(ExpressModel express) {
        this.express = express;
    }



    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private static IContentDecoder<OrderModel> decoder = new IContentDecoder.BeanDecoder<OrderModel>(
            OrderModel.class, "order");
    // 获取订单详情
    public static IPromise orderDetails(Long id) {
        return Http.instance().get(ApiUrl.getOrderDetails(id)).contentDecoder(decoder).isCache(true).run();
    }
}
