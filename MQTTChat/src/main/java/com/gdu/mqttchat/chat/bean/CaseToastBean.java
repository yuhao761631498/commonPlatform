package com.gdu.mqttchat.chat.bean;

import java.util.List;

public class CaseToastBean {

    private int code;
    private List<DataBean> data;
    private ExtraBean extra;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public ExtraBean getExtra() {
        return extra;
    }

    public void setExtra(ExtraBean extra) {
        this.extra = extra;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class ExtraBean {
    }

    public static class DataBean {
        private String caseDesc;
        private String caseId;
        private String createTime;
        private int hasBeenRead;

        public String getCaseDesc() {
            return caseDesc;
        }

        public void setCaseDesc(String caseDesc) {
            this.caseDesc = caseDesc;
        }

        public String getCaseId() {
            return caseId;
        }

        public void setCaseId(String caseId) {
            this.caseId = caseId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getHasBeenRead() {
            return hasBeenRead;
        }

        public void setHasBeenRead(int hasBeenRead) {
            this.hasBeenRead = hasBeenRead;
        }
    }
}
