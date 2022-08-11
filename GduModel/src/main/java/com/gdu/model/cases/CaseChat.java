package com.gdu.model.cases;

import java.io.Serializable;

/**
 * 案件讨论数据结构
 */
public class CaseChat implements Serializable {

    private long id;
    private String msgTopic;
    private String msgContent;
    private String createTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMsgTopic() {
        return msgTopic;
    }

    public void setMsgTopic(String msgTopic) {
        this.msgTopic = msgTopic;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
