package com.bjcathay.woqu.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.woqu.constant.ApiUrl;

import java.util.List;

/**
 * Created by zhouh on 15-10-21.
 */
public class CommentModel {
    private Boolean success;
    private Boolean hasNext;
    private int page;
    @JSONCollection(type = CommentListModel.class)
    private List<CommentListModel> comments;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<CommentListModel> getComments() {
        return comments;
    }

    public void setComments(List<CommentListModel> comments) {
        this.comments = comments;
    }
    private static IContentDecoder<CommentModel> decoder = new IContentDecoder.BeanDecoder<CommentModel>(
            CommentModel.class);
    public static IPromise getComment(String id){
        return Http.instance().get(ApiUrl.talksComment(id)).contentDecoder(decoder).isCache(true).run();
    }
}
