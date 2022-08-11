package com.gdu.model.data;

import java.io.Serializable;

/**
 * 数据监测 - 高点监控总数统计
 */
public class DataHighTotal implements Serializable {

    /**
     * code : 0
     * msg : success
     * data : {"deviceCount":26}
     */

    private int code;
    private String msg;
    private DataBean data;

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

    public static class DataBean {
        /**
         * deviceCount : 26
         */

        private int deviceCount;

        public int getDeviceCount() {
            return deviceCount;
        }

        public void setDeviceCount(int deviceCount) {
            this.deviceCount = deviceCount;
        }
    }
}
