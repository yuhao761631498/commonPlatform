package com.gdu.baselibrary.network;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/11 0011.
 */

public class AlarmTypeStatictisBean implements Serializable {

    private int code;
    private String msg;
    private List<DataDTO> data;
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

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public Object getExtra() {
        return extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }

    public static class DataDTO implements Serializable {
        private int count;
        private String alarmHandleTypeCode;
        private int alarmHandleTypeValue;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getAlarmHandleTypeCode() {
            return alarmHandleTypeCode;
        }

        public void setAlarmHandleTypeCode(String alarmHandleTypeCode) {
            this.alarmHandleTypeCode = alarmHandleTypeCode;
        }

        public int getAlarmHandleTypeValue() {
            return alarmHandleTypeValue;
        }

        public void setAlarmHandleTypeValue(int alarmHandleTypeValue) {
            this.alarmHandleTypeValue = alarmHandleTypeValue;
        }
    }
}
