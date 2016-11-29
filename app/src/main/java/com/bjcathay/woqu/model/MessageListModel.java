
package com.bjcathay.woqu.model;

import com.bjcathay.android.async.IPromise;
import com.bjcathay.android.json.annotation.JSONCollection;
import com.bjcathay.android.remote.Http;
import com.bjcathay.android.remote.IContentDecoder;
import com.bjcathay.woqu.constant.ApiUrl;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jiangm on 15-10-13.
 */
public class MessageListModel implements Serializable {
    @JSONCollection(type = MessageModel.class)
    private List<MessageModel> messages;
    private boolean hasNext;
    private int page;

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<MessageModel> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageModel> messages) {
        this.messages = messages;
    }

    private static IContentDecoder<MessageListModel> decoder = new IContentDecoder.BeanDecoder<MessageListModel>(
            MessageListModel.class);

    // 消息列表
    public static IPromise msgList(String type, int page) {
        return Http.instance().get(ApiUrl.MESSAGE).param("type", type).param("page", page)
                .contentDecoder(decoder).isCache(true).run();
    }

    public static IPromise msgList(int page) {
        return Http.instance().get(ApiUrl.MESSAGE).param("page", page).contentDecoder(decoder)
                .isCache(true)
                .run();
    }

    // 消息标记为已读
    public static IPromise readMsg(long from) {
        return Http.instance().put(ApiUrl.MESSAGE_STATE).param("from", from).isCache(true)
                .run();
    }

    // 删除消息
    public static IPromise deleteMsg(long from) {
        return Http.instance().post(ApiUrl.MESSAGE_DELETE).param("_method","DELETE").isCache(false)
                .param("from", from)
                .run();
    }

    // 清空消息
    public static IPromise emptyMsg() {
        return Http.instance().post(ApiUrl.MESSAGE_EMPTY).param("_method", "DELETE").isCache(false).run();
    }
}
