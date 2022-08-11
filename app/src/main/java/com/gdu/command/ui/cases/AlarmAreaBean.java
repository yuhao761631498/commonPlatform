package com.gdu.command.ui.cases;

import java.util.List;

public class AlarmAreaBean {


    /**
     * msg : success
     * code : 0
     * data : [{"dept_code":"department_fire_815049","dept_name":"中心城区"},{"dept_code":"department_fire_300596","dept_name":"江陵县"},{"dept_code":"department_fire_688220","dept_name":"松滋市"},{"dept_code":"department_fire_201791","dept_name":"公安县"},{"dept_code":"department_fire_994555","dept_name":"石首市"},{"dept_code":"department_fire_914703","dept_name":"监利县"},{"dept_code":"department_fire_200576","dept_name":"洪湖市"},{"dept_code":"department_fire_183655","dept_name":"长湖"},{"dept_code":"department_fire_712621","dept_name":"洪湖"},{"dept_code":"department_fire_301482","dept_name":"洪湖大湖"}]
     */

    private String msg;
    private int code;
    private List<AreaBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<AreaBean> getData() {
        return data;
    }

    public void setData(List<AreaBean> data) {
        this.data = data;
    }

    public static class AreaBean {
        /**
         * dept_code : department_fire_815049
         * dept_name : 中心城区
         */

        private String dept_code;
        private String dept_name;

        public String getDept_code() {
            return dept_code;
        }

        public void setDept_code(String dept_code) {
            this.dept_code = dept_code;
        }

        public String getDept_name() {
            return dept_name;
        }

        public void setDept_name(String dept_name) {
            this.dept_name = dept_name;
        }
    }
}
