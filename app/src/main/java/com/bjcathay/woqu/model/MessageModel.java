
package com.bjcathay.woqu.model;

import java.io.Serializable;

/**
 * Created by jiangm on 15-10-13.
 */
public class MessageModel implements Serializable {
    // "target" : "41",
    // "status" : "READ",
    // "content" : "尊敬的用户，您好。您订...",
    // "title" : "标题",
    // "type" : ”待定",ACTIVITY  AD   SYSTEM_MESSAGE
    // "id" : 55,
    // "humanDate" : "29天前",
    // "created":"2015-10-10 10:10:10"
    private long id;
    private String target;
    private String status;
    private String content;
    private String title;
    private String type;// 待定
    private String humanDate;
    private String created;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getHumanDate() {
        return humanDate;
    }

    public void setHumanDate(String humanDate) {
        this.humanDate = humanDate;
    }

    public long getId() {
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

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
