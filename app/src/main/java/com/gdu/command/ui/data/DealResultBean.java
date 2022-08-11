package com.gdu.command.ui.data;

import java.util.List;

/**
 * Copyright (C), 2020-2030, 普宙飞行器科技有限公司
 * <p>
 * 功能描述:
 * <p>
 * 创建时间: 2022/5/7 17:08
 *
 * @author yuhao
 */

public class DealResultBean {

    private int code;

    private String msg;

    private Object extra;

    private List<DealItemBean> data;

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

    public Object getExtra() {
        return extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }

    public List<DealItemBean> getData() {
        return data;
    }

    public void setData(List<DealItemBean> data) {
        this.data = data;
    }


    public static class DealItemBean {

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
