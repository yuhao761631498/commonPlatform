package com.gdu.command.ui.data;

import java.util.List;

/**
 * Copyright (C), 2020-2030, 普宙飞行器科技有限公司
 * <p>
 * 功能描述:
 * <p>
 * 创建时间: 2022/5/7 17:12
 *
 * @author yuhao
 */
public class DeviceRankBean {

    private int code;
    private String msg;
    private Object extra;
    private List<DeviceItemBean> data;

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

    public List<DeviceItemBean> getData() {
        return data;
    }

    public void setData(List<DeviceItemBean> data) {
        this.data = data;
    }

    public static class DeviceItemBean {

        private String deviceId;

        private String deviceName;

        private int count;

        private String lon;

        private String lat;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }
    }
}
