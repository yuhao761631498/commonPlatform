package com.gdu.mqttchat.chat.presenter;

import java.io.Serializable;
import java.util.List;

public class MessageBean implements Serializable {

    private int code;
    private String msg;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        private int id;
        private String msgTopic;
        private String caseName;
        private String msgContent;
        private String createTime;
        private boolean isNewMsg;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMsgTopic() {
            return msgTopic;
        }

        public void setMsgTopic(String msgTopic) {
            this.msgTopic = msgTopic;
        }

        public String getCaseName() {
            return caseName;
        }

        public void setCaseName(String caseName) {
            this.caseName = caseName;
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

        public boolean isNewMsg() {
            return isNewMsg;
        }

        public void setNewMsg(boolean newMsg) {
            isNewMsg = newMsg;
        }
    }
}
