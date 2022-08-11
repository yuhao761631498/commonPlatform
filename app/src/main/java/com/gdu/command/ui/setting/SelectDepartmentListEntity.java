package com.gdu.command.ui.setting;

import java.io.Serializable;
import java.util.List;

public class SelectDepartmentListEntity implements Serializable {

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
        private int id;
        private int customerId;
        private String deptTypeCode;
        private String deptCode;
        private String deptName;
        private String parentDeptCode;
        private int orderNum;
        private String createTime;
        private String createUser;
        private String createUserName;
        private String updateTime;
        private int updateUser;
        private String updateUserName;
        private String deptAddress;
        private Object deptTel;
        private Object deptLongitude;
        private Object deptLatitude;
        private Object deptShortName;
        private String deptIcon;
        private Object deptRemark;
        private String deptStatus;
        private List<DataDTO> children;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCustomerId() {
            return customerId;
        }

        public void setCustomerId(int customerId) {
            this.customerId = customerId;
        }

        public String getDeptTypeCode() {
            return deptTypeCode;
        }

        public void setDeptTypeCode(String deptTypeCode) {
            this.deptTypeCode = deptTypeCode;
        }

        public String getDeptCode() {
            return deptCode;
        }

        public void setDeptCode(String deptCode) {
            this.deptCode = deptCode;
        }

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public String getParentDeptCode() {
            return parentDeptCode;
        }

        public void setParentDeptCode(String parentDeptCode) {
            this.parentDeptCode = parentDeptCode;
        }

        public int getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(int orderNum) {
            this.orderNum = orderNum;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public Object getCreateUser() {
            return createUser;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public Object getCreateUserName() {
            return createUserName;
        }

        public void setCreateUserName(String createUserName) {
            this.createUserName = createUserName;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public int getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(int updateUser) {
            this.updateUser = updateUser;
        }

        public String getUpdateUserName() {
            return updateUserName;
        }

        public void setUpdateUserName(String updateUserName) {
            this.updateUserName = updateUserName;
        }

        public String getDeptAddress() {
            return deptAddress;
        }

        public void setDeptAddress(String deptAddress) {
            this.deptAddress = deptAddress;
        }

        public Object getDeptTel() {
            return deptTel;
        }

        public void setDeptTel(Object deptTel) {
            this.deptTel = deptTel;
        }

        public Object getDeptLongitude() {
            return deptLongitude;
        }

        public void setDeptLongitude(Object deptLongitude) {
            this.deptLongitude = deptLongitude;
        }

        public Object getDeptLatitude() {
            return deptLatitude;
        }

        public void setDeptLatitude(Object deptLatitude) {
            this.deptLatitude = deptLatitude;
        }

        public Object getDeptShortName() {
            return deptShortName;
        }

        public void setDeptShortName(Object deptShortName) {
            this.deptShortName = deptShortName;
        }

        public String getDeptIcon() {
            return deptIcon;
        }

        public void setDeptIcon(String deptIcon) {
            this.deptIcon = deptIcon;
        }

        public Object getDeptRemark() {
            return deptRemark;
        }

        public void setDeptRemark(Object deptRemark) {
            this.deptRemark = deptRemark;
        }

        public String getDeptStatus() {
            return deptStatus;
        }

        public void setDeptStatus(String deptStatus) {
            this.deptStatus = deptStatus;
        }

        public List<DataDTO> getChildren() {
            return children;
        }

        public void setChildren(List<DataDTO> children) {
            this.children = children;
        }
    }
}
