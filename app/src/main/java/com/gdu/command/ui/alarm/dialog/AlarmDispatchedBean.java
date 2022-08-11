package com.gdu.command.ui.alarm.dialog;

import java.io.Serializable;
import java.util.List;

public class AlarmDispatchedBean implements Serializable {

    private int code;
    private String msg;
    private List<DataBean> data;
    private Object extra;

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

    public Object getExtra() {
        return extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }

    public static class DataBean implements Serializable {
        private String username;
        private String orgName;
        private int handleType;
        private String handleTypeName;
        private String createTime;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }

        public int getHandleType() {
            return handleType;
        }

        public void setHandleType(int handleType) {
            this.handleType = handleType;
        }

        public String getHandleTypeName() {
            return handleTypeName;
        }

        public void setHandleTypeName(String handleTypeName) {
            this.handleTypeName = handleTypeName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
