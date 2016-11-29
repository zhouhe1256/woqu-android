package com.bjcathay.woqu.model;


import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.woqu.constant.ApiUrl;

/**
 * Created by zhouh on 15-10-9.
 */
public class TalkListImagesModel {
    private String success;
    private String imageId;
    private String imageUri;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
    private static IContentDecoder<TalkListImagesModel> decoder = new IContentDecoder.BeanDecoder<TalkListImagesModel>(
            TalkListImagesModel.class);

    public static IPromise setAvatar(byte[] data) {

        return Http.instance().post(ApiUrl.UPLOAD_IMAGE).
                param("type","talks").data(data).contentDecoder(decoder).isCache(true).run();
    }
}
