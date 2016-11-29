
package com.bjcathay.woqu.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.woqu.constant.ApiUrl;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dengt on 15-10-19.
 */
public class GiftModel implements Serializable {
    private long id;
    private String name;
    private boolean selected;
    private double number;
    private long create;
    private String friendlyLink;
    private double price;
    private String giftDetail;
    private String imageUrl;
    private long imageId;
    @JSONCollection(type = String.class)
    private List<String> imageUrls;

    @JSONCollection(type = ExpressModel.class)
    private ExpressModel express;

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public String getFriendlyLink() {
        return friendlyLink;
    }

    public void setFriendlyLink(String friendlyLink) {
        this.friendlyLink = friendlyLink;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getGiftDetail() {
        return giftDetail;
    }

    public void setGiftDetail(String giftDetail) {
        this.giftDetail = giftDetail;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public ExpressModel getExpress() {
        return express;
    }

    public void setExpress(ExpressModel express) {
        this.express = express;
    }

    public long getCreate() {
        return create;
    }

    public void setCreate(long create) {
        this.create = create;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public double getNumber() {
        return number;
    }

    public void setNumber(double number) {
        this.number = number;
    }

    private static IContentDecoder<GiftModel> decoder = new IContentDecoder.BeanDecoder<GiftModel>(
            GiftModel.class, "gift");

    public static IPromise get(long id) {
        return Http.instance().get(ApiUrl.getgift(id)).contentDecoder(decoder).isCache(true).run();
    }
}
