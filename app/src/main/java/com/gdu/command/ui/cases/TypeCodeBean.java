package com.gdu.command.ui.cases;

import java.util.List;

public class TypeCodeBean {

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

    public static class DataBean {
        private String typeName;
        private String typeCode;
        private int typeValue;
        private int status;

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getTypeCode() {
            return typeCode;
        }

        public void setTypeCode(String typeCode) {
            this.typeCode = typeCode;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getTypeValue() {
            return typeValue;
        }

        public void setTypeValue(int typeValue) {
            this.typeValue = typeValue;
        }
    }
}
