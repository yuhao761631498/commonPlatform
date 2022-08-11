package com.gdu.command.ui.data;

import java.util.List;

public class DataCaseRankBean {

    /**
     * code : 0
     * msg : success
     * data : [{"caseCount":17,"deptName":"武汉市渔政"}]
     */

    private int code;
    private String msg;
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * caseCount : 17
         * deptName : 武汉市渔政
         */

        private int caseCount;
        private String deptName;

        public int getCaseCount() {
            return caseCount;
        }

        public void setCaseCount(int caseCount) {
            this.caseCount = caseCount;
        }

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }
    }
}
