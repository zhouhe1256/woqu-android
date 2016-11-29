package com.bjcathay.woqu.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.woqu.constant.ApiUrl;

import java.io.Serializable;

/**
 * Created by zhouh on 15-10-16.
 */
public class UserModel implements Serializable {

    // "apiToken" : "71c6090615fc61512d7cffe4e23547074809f547",
    // "id" : 1
    private long id;
    private String apiToken;
    private String imageUrl;
    private String nickname;
    private String sex;
    private String birthday;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private static IContentDecoder<UserModel> decoder = new IContentDecoder.BeanDecoder<UserModel>(
            UserModel.class, "user");

    public static IPromise register(String mobileNo, String password, String code) {
        return Http.instance().post(ApiUrl.REGISTER).param("name", mobileNo)
                .param("password", password)
                .param("code", code)
                .isCache(false).run();
    }

    public static IPromise sendCheckCode(String phone, String type) {
        return Http.instance().post(ApiUrl.SEND_CAPTCHA).param("phone", phone).param("type", type).isCache(false)
                .run();
    }

    public static IPromise login(String user, String password) {
        return Http.instance().post(ApiUrl.USER_LOGIN).param("name", user)
                .param("password", password).isCache(false).run();
    }

    // 设置用户头像
    public static IPromise setAvatar(byte[] data) {
        return Http.instance().post(ApiUrl.USER_SET_AVATAR).data(data).contentDecoder(decoder)
                .isCache(false).run();
    }

    // 重置密码
    public static IPromise resetPassword(String phone, String password, String code) {
        return Http.instance().put(ApiUrl.RESET_PASSWORD).param("phone", phone)
                .param("password", password).param("code", code).isCache(false)
                .run();
    }

    // 获取用户信息
    public static IPromise get() {
        return Http.instance().get(ApiUrl.USER_INFO).contentDecoder(decoder).isCache(true).run();
    }

    // 用户反馈
    public static IPromise feedBack(String content, String contactWay) {
        return Http.instance().post(ApiUrl.USER_FEEDBACK).param("content", content)
                .param("contactWay", contactWay).isCache(false).run();
    }

    // 修改密码
    public static IPromise changePassword(String oldPassword, String newPassword) {
        return Http.instance().put(ApiUrl.CHANGE_PASSWORD).param("oldPassword", oldPassword)
                .param("password", newPassword).isCache(false).run();
    }

    // 通过第三方平台登陆
    public static IPromise thirdPartyLogin(String platform, String uid, String name, String data,
                                           String accessToken) {
        return Http.instance().post(ApiUrl.BIND_PLATFORM).param("platform", platform)
                .param("uid", uid).param("name", name).param("data", data)
                .param("accessToken", accessToken).isCache(false).run();
    }

    // 第三方平台登陆用户绑定手机号
    public static IPromise thirdPartyBindphone(String platform, String uid, String name, String data,
                                               String accessToken, String phone, String code) {
        return Http.instance().post(ApiUrl.BIND_PHONE).param("platform", platform)
                .param("uid", uid).param("name", name).param("data", data)
                .param("accessToken", accessToken).param("phone", phone).param("code", code).isCache(false).run();
    }
    // 更新用户信息
    // key 为nickname realName set(MALE|FEMALE) birthday

    public static IPromise updateUser(String key, String value) {
        return Http.instance().put(ApiUrl.USER_INFO_UPDATE).param(key, value)
                .isCache(false).run();
    }
}