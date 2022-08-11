package com.gdu.model.data;

import java.io.Serializable;

/**
 * 数据监测 - 告警总数
 */
public class DataAlarmTotalBean implements Serializable {

    /**
     * code : 0
     * msg : 成功
     * data : 11912
     * extra : null
     */

    private int code;
    private String msg;
    private int data;
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

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public Object getExtra() {
        return extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }
}
