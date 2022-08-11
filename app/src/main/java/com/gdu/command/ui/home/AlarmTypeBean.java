package com.gdu.command.ui.home;

import java.util.List;

/**
 * Copyright (C), 2020-2030, 普宙飞行器科技有限公司
 * <p>
 * 功能描述:
 * <p>
 * 创建时间: 2022/5/6 13:28
 *
 * @author yuhao
 */

public class AlarmTypeBean {

    private int code;

    private String msg;

    private Object extra;

    private List<TypeBean> data;

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

    public List<TypeBean> getData() {
        return data;
    }

    public void setData(List<TypeBean> data) {
        this.data = data;
    }


    public static class TypeBean {

        private int count;

        private int alarmTypeValue;

        private String alarmTypeCode;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getAlarmTypeValue() {
            return alarmTypeValue;
        }

        public void setAlarmTypeValue(int alarmTypeValue) {
            this.alarmTypeValue = alarmTypeValue;
        }

        public String getAlarmTypeCode() {
            return alarmTypeCode;
        }

        public void setAlarmTypeCode(String alarmTypeCode) {
            this.alarmTypeCode = alarmTypeCode;
        }
    }
}
