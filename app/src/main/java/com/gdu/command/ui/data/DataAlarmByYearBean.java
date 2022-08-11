package com.gdu.command.ui.data;

import java.io.Serializable;
import java.util.List;

/**
 * 告警数据时间分布趋势
 */
public class DataAlarmByYearBean implements Serializable{

    /**
     * code : 0
     * msg : 成功
     * data : [{"time":"1月","count":0},{"time":"2月","count":0},{"time":"3月","count":0},{"time":"4月","count":4012},{"time":"5月","count":3569},{"time":"6月","count":3341},{"time":"7月","count":990},{"time":"8月","count":0},{"time":"9月","count":0},{"time":"10月","count":0},{"time":"11月","count":0},{"time":"12月","count":0}]
     * extra : null
     */

    private int code;
    private String msg;
    private Object extra;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * time : 1月
         * count : 0
         */

        private String time;
        private int count;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
