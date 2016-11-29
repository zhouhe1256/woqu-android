package com.bjcathay.woqu.model;

import java.io.Serializable;

/**
 * Created by dengt on 15-10-19.
 */
public class ImageModel implements Serializable {
    private long id;
    private String imageUrl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
