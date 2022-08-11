package com.gdu.model.organization;

import java.io.Serializable;
import java.util.List;

/**
 * 组织数据结构
 */
public class OrganizationInfo implements Serializable {
            private int id;
            private int customerId;
            private String deptTypeCode;
            private String deptCode;
            private String deptName;
            private String parentDeptCode;
            private int orderNum;
            private String createTime;
            private int createUser;
            private String createUserName;
            private String updateTime;
            private int updateUser;
            private String updateUserName;
            private String deptAddress;
            private String deptTel;
            private double deptLongitude;
            private double deptLatitude;
            private String deptShortName;
            private String deptIcon;
            private String deptRemark;
            private String deptStatus;
            private List<OrganizationInfo> children;
            private boolean isSelected;
            private boolean isOrganization;
            private List<UseBean> users;

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

    public int getCreateUser() {
        return createUser;
    }

    public void setCreateUser(int createUser) {
        this.createUser = createUser;
    }

    public String getCreateUserName() {
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

    public String getDeptTel() {
        return deptTel;
    }

    public void setDeptTel(String deptTel) {
        this.deptTel = deptTel;
    }

    public double getDeptLongitude() {
        return deptLongitude;
    }

    public void setDeptLongitude(double deptLongitude) {
        this.deptLongitude = deptLongitude;
    }

    public double getDeptLatitude() {
        return deptLatitude;
    }

    public void setDeptLatitude(double deptLatitude) {
        this.deptLatitude = deptLatitude;
    }

    public String getDeptShortName() {
        return deptShortName;
    }

    public void setDeptShortName(String deptShortName) {
        this.deptShortName = deptShortName;
    }

    public String getDeptIcon() {
        return deptIcon;
    }

    public void setDeptIcon(String deptIcon) {
        this.deptIcon = deptIcon;
    }

    public String getDeptRemark() {
        return deptRemark;
    }

    public void setDeptRemark(String deptRemark) {
        this.deptRemark = deptRemark;
    }

    public String getDeptStatus() {
        return deptStatus;
    }

    public void setDeptStatus(String deptStatus) {
        this.deptStatus = deptStatus;
    }

    public List<OrganizationInfo> getChildren() {
        return children;
    }

    public void setChildren(List<OrganizationInfo> children) {
        this.children = children;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isOrganization() {
        return isOrganization;
    }

    public void setOrganization(boolean organization) {
        isOrganization = organization;
    }

    public List<UseBean> getUsers() {
        return users;
    }

    public void setUsers(List<UseBean> users) {
        this.users = users;
    }

    public class UseBean implements Serializable {
        private int id;
        private int customerId;
        private String useraccount;
        private String password;
        private String username;
        private int status;
        private int teamid;
        private String roleCode;
        private String deptCode;
        private String mobile;
        private String email;
        private String createtTime;
        private int createUser;
        private String updateTime;
        private int updateUser;
        private String lastLonginTime;
        private String disableReason;
        private String country;
        private String headImg;
        private String orgName;
        private String jobDesc;
        private int userNameVisible;
        private String expireTime;
        private int employeeId;
        private String userLongitude;
        private String userLatitude;
        private String userSort;
        private String idCard;
        private String officeMobile;
        private boolean isSelected;

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

        public String getUseraccount() {
            return useraccount;
        }

        public void setUseraccount(String useraccount) {
            this.useraccount = useraccount;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getTeamid() {
            return teamid;
        }

        public void setTeamid(int teamid) {
            this.teamid = teamid;
        }

        public String getRoleCode() {
            return roleCode;
        }

        public void setRoleCode(String roleCode) {
            this.roleCode = roleCode;
        }

        public String getDeptCode() {
            return deptCode;
        }

        public void setDeptCode(String deptCode) {
            this.deptCode = deptCode;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCreatetTime() {
            return createtTime;
        }

        public void setCreatetTime(String createtTime) {
            this.createtTime = createtTime;
        }

        public int getCreateUser() {
            return createUser;
        }

        public void setCreateUser(int createUser) {
            this.createUser = createUser;
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

        public String getLastLonginTime() {
            return lastLonginTime;
        }

        public void setLastLonginTime(String lastLonginTime) {
            this.lastLonginTime = lastLonginTime;
        }

        public String getDisableReason() {
            return disableReason;
        }

        public void setDisableReason(String disableReason) {
            this.disableReason = disableReason;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }

        public String getJobDesc() {
            return jobDesc;
        }

        public void setJobDesc(String jobDesc) {
            this.jobDesc = jobDesc;
        }

        public int getUserNameVisible() {
            return userNameVisible;
        }

        public void setUserNameVisible(int userNameVisible) {
            this.userNameVisible = userNameVisible;
        }

        public String getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(String expireTime) {
            this.expireTime = expireTime;
        }

        public int getEmployeeId() {
            return employeeId;
        }

        public void setEmployeeId(int employeeId) {
            this.employeeId = employeeId;
        }

        public String getUserLongitude() {
            return userLongitude;
        }

        public void setUserLongitude(String userLongitude) {
            this.userLongitude = userLongitude;
        }

        public String getUserLatitude() {
            return userLatitude;
        }

        public void setUserLatitude(String userLatitude) {
            this.userLatitude = userLatitude;
        }

        public String getUserSort() {
            return userSort;
        }

        public void setUserSort(String userSort) {
            this.userSort = userSort;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public String getOfficeMobile() {
            return officeMobile;
        }

        public void setOfficeMobile(String officeMobile) {
            this.officeMobile = officeMobile;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }
}
