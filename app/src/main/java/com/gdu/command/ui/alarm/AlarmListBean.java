package com.gdu.command.ui.alarm;

import com.gdu.model.alarm.AlarmInfo;

import java.io.Serializable;
import java.util.List;

public class AlarmListBean implements Serializable {

    private int code;
    private String msg;
    private DataBean data;
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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public Object getExtra() {
        return extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }

    public static class DataBean implements Serializable {
        private int currentPage;
        private int allPage;
        private int sizeOfPage;
        private int total;
        private List<AlarmInfo> data;
        private boolean isLoadFinish;

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getAllPage() {
            return allPage;
        }

        public void setAllPage(int allPage) {
            this.allPage = allPage;
        }

        public int getSizeOfPage() {
            return sizeOfPage;
        }

        public void setSizeOfPage(int sizeOfPage) {
            this.sizeOfPage = sizeOfPage;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<AlarmInfo> getData() {
            return data;
        }

        public void setData(List<AlarmInfo> data) {
            this.data = data;
        }

        public boolean isLoadFinish() {
            return isLoadFinish;
        }

        public void setLoadFinish(boolean loadFinish) {
            isLoadFinish = loadFinish;
        }
    }
}
