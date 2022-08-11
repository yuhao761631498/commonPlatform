package com.gdu.command.ui.duty;

import java.util.List;

public class DutyForDayBean {


    /**
     * msg : success
     * code : 0
     * data : [{"id":502,"userId":"410","userName":"admin","userPhone":null,"shiftdCode":"08c0197f-bd82-45eb-9003-b43a7babe61d","shiftdName":"夜班","dutyType":"7213a37a-1ee7-43fa-96b4-fdae6a2b32b1","dutyTypeName":"带班领导","forChange":0,"forChangeName":null,"forChangeId":null,"forChangeUserName":null,"forChangePhone":null,"shiftCode":"3668ffaf-f0c4-4c21-aef6-e41a83745415","shiftName":"紧急备战","cause":null,"regionCode":"department_fire_815049","regionName":"中心城区","duty":"2022-05-13","dutyMonth":"2022-05","sort":"1","areaName":"区域名称","areaId":"110"},{"id":503,"userId":"503","userName":"执法员01","userPhone":null,"shiftdCode":"58b81bad-15e9-456d-b73b-fc89d4df3e8d","shiftdName":"白班","dutyType":"91f27a5b-976e-4f89-8bf6-9978306efb71","dutyTypeName":"值班员","forChange":0,"forChangeName":null,"forChangeId":null,"forChangeUserName":null,"forChangePhone":null,"shiftCode":"3668ffaf-f0c4-4c21-aef6-e41a83745415","shiftName":"紧急备战","cause":null,"regionCode":"department_fire_815049","regionName":"中心城区","duty":"2022-05-13","dutyMonth":"2022-05","sort":"4","areaName":"区域名称","areaId":"110"}]
     */

    private String msg;
    private int code;
    private List<DutyMemberBean> data;

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

    public List<DutyMemberBean> getData() {
        return data;
    }

    public void setData(List<DutyMemberBean> data) {
        this.data = data;
    }

    public static class DutyMemberBean {
        /**
         * id : 502
         * userId : 410
         * userName : admin
         * userPhone : null
         * shiftdCode : 08c0197f-bd82-45eb-9003-b43a7babe61d
         * shiftdName : 夜班
         * dutyType : 7213a37a-1ee7-43fa-96b4-fdae6a2b32b1
         * dutyTypeName : 带班领导
         * forChange : 0
         * forChangeName : null
         * forChangeId : null
         * forChangeUserName : null
         * forChangePhone : null
         * shiftCode : 3668ffaf-f0c4-4c21-aef6-e41a83745415
         * shiftName : 紧急备战
         * cause : null
         * regionCode : department_fire_815049
         * regionName : 中心城区
         * duty : 2022-05-13
         * dutyMonth : 2022-05
         * sort : 1
         * areaName : 区域名称
         * areaId : 110
         */

        private int id;
        private String userId;
        private String userName;
        private String userPhone;
        private String shiftdCode;
        private String shiftdName;
        private String dutyType;
        private String dutyTypeName;
        private int forChange;
        private String forChangeName;
        private String forChangeId;
        private String forChangeUserName;
        private String forChangePhone;
        private String shiftCode;
        private String shiftName;
        private String cause;
        private String regionCode;
        private String regionName;
        private String duty;
        private String dutyMonth;
        private String sort;
        private String areaName;
        private String areaId;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public String getShiftdCode() {
            return shiftdCode;
        }

        public void setShiftdCode(String shiftdCode) {
            this.shiftdCode = shiftdCode;
        }

        public String getShiftdName() {
            return shiftdName;
        }

        public void setShiftdName(String shiftdName) {
            this.shiftdName = shiftdName;
        }

        public String getDutyType() {
            return dutyType;
        }

        public void setDutyType(String dutyType) {
            this.dutyType = dutyType;
        }

        public String getDutyTypeName() {
            return dutyTypeName;
        }

        public void setDutyTypeName(String dutyTypeName) {
            this.dutyTypeName = dutyTypeName;
        }

        public int getForChange() {
            return forChange;
        }

        public void setForChange(int forChange) {
            this.forChange = forChange;
        }

        public String getForChangeName() {
            return forChangeName;
        }

        public void setForChangeName(String forChangeName) {
            this.forChangeName = forChangeName;
        }

        public String getForChangeId() {
            return forChangeId;
        }

        public void setForChangeId(String forChangeId) {
            this.forChangeId = forChangeId;
        }

        public String getForChangeUserName() {
            return forChangeUserName;
        }

        public void setForChangeUserName(String forChangeUserName) {
            this.forChangeUserName = forChangeUserName;
        }

        public String getForChangePhone() {
            return forChangePhone;
        }

        public void setForChangePhone(String forChangePhone) {
            this.forChangePhone = forChangePhone;
        }

        public String getShiftCode() {
            return shiftCode;
        }

        public void setShiftCode(String shiftCode) {
            this.shiftCode = shiftCode;
        }

        public String getShiftName() {
            return shiftName;
        }

        public void setShiftName(String shiftName) {
            this.shiftName = shiftName;
        }

        public String getCause() {
            return cause;
        }

        public void setCause(String cause) {
            this.cause = cause;
        }

        public String getRegionCode() {
            return regionCode;
        }

        public void setRegionCode(String regionCode) {
            this.regionCode = regionCode;
        }

        public String getRegionName() {
            return regionName;
        }

        public void setRegionName(String regionName) {
            this.regionName = regionName;
        }

        public String getDuty() {
            return duty;
        }

        public void setDuty(String duty) {
            this.duty = duty;
        }

        public String getDutyMonth() {
            return dutyMonth;
        }

        public void setDutyMonth(String dutyMonth) {
            this.dutyMonth = dutyMonth;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }
    }
}
