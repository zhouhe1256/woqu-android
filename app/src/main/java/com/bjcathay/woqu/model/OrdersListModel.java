package com.bjcathay.woqu.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.woqu.constant.ApiUrl;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jiangm on 15-10-20.
 */
public class OrdersListModel implements Serializable{
    private int page;
    private boolean hasNext;
    @JSONCollection(type = OrderModel.class)
    private List<OrderModel> orders;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<OrderModel> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderModel> orders) {
        this.orders = orders;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }


    private static IContentDecoder<OrdersListModel> decoder = new IContentDecoder.BeanDecoder<OrdersListModel>(
            OrdersListModel.class);

    public static IPromise achieveOrder(int page) {
        return Http.instance().get(ApiUrl.MY_ORDERS).param("page",page).
                contentDecoder(decoder).isCache(true).run();
    }

}
