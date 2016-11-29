package com.bjcathay.woqu.model;

import com.bjcathay.android.json.annotation.JSONCollection;

/**
 * Created by zhouh on 15-10-29.
 */
public class WinnerListModel {
    private int id;
    @JSONCollection(type = UserModel.class)
    private UserModel user;

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
