package com.gdu.model.user;

import java.io.Serializable;
import java.util.List;

public class PersonInfoBean implements Serializable {

    private int code;
    private String msg;
    private DataBean data;
    private ExtraBean extra;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String sg) {
        msg = sg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public ExtraBean getExtra() {
        return extra;
    }

    public void setExtra(ExtraBean extra) {
        this.extra = extra;
    }

    public static class DataBean {
        private int id;
        private int customerId;
        private String useraccount;
        private String password;
        private String username;
        private int status;
        private List<RolesBean> roles;
        private String deptCode;
        private String deptName;
        private String roleCode;
        private String mobile;
        private int createUser;
        private int updateUser;
        private String createTime;
        private String updateTime;
        private String expireTime;
        private int employeeId;
        private String employeeName;
        private List<ServerApiVoListBean> serverApiVoList;
        private int userGender;
        private String headImg;
        private String idCard;
        private String officeMobile;

        public String getOfficeMobile() {
            return officeMobile;
        }

        public void setOfficeMobile(String officeMobile) {
            this.officeMobile = officeMobile;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

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

        public List<RolesBean> getRoles() {
            return roles;
        }

        public void setRoles(List<RolesBean> roles) {
            this.roles = roles;
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

        public String getRoleCode() {
            return roleCode;
        }

        public void setRoleCode(String roleCode) {
            this.roleCode = roleCode;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobileParam) {
            mobile = mobileParam;
        }

        public int getCreateUser() {
            return createUser;
        }

        public void setCreateUser(int createUser) {
            this.createUser = createUser;
        }

        public int getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(int updateUser) {
            this.updateUser = updateUser;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
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

        public String getEmployeeName() {
            return employeeName;
        }

        public void setEmployeeName(String employeeName) {
            this.employeeName = employeeName;
        }

        public List<ServerApiVoListBean> getServerApiVoList() {
            return serverApiVoList;
        }

        public void setServerApiVoList(List<ServerApiVoListBean> serverApiVoList) {
            this.serverApiVoList = serverApiVoList;
        }

        public int getUserGender() {
            return userGender;
        }

        public void setUserGender(int userGenderParam) {
            userGender = userGenderParam;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImgParam) {
            headImg = headImgParam;
        }

        public static class RolesBean {
            private int id;
            private int roleCode;
            private String roleName;
            private String roleDescribe;
            private int roleSort;
            private Object isAuth;
            private String createTime;
            private Object createUser;
            private String updateTime;
            private Object updateUser;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getRoleCode() {
                return roleCode;
            }

            public void setRoleCode(int roleCode) {
                this.roleCode = roleCode;
            }

            public String getRoleName() {
                return roleName;
            }

            public void setRoleName(String roleName) {
                this.roleName = roleName;
            }

            public String getRoleDescribe() {
                return roleDescribe;
            }

            public void setRoleDescribe(String roleDescribe) {
                this.roleDescribe = roleDescribe;
            }

            public int getRoleSort() {
                return roleSort;
            }

            public void setRoleSort(int roleSort) {
                this.roleSort = roleSort;
            }

            public Object getIsAuth() {
                return isAuth;
            }

            public void setIsAuth(Object isAuth) {
                this.isAuth = isAuth;
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

            public void setCreateUser(Object createUser) {
                this.createUser = createUser;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }

            public Object getUpdateUser() {
                return updateUser;
            }

            public void setUpdateUser(Object updateUser) {
                this.updateUser = updateUser;
            }
        }

        public static class ServerApiVoListBean {
            /**
             * id : 0224e31a11ea491c391428437193b822
             * apiName : test
             * apiPath : /test
             * apiHash : null
             * serverId : test
             * serverName : test
             * createTime : null
             * menuId : 1
             */

            private String id;
            private String apiName;
            private String apiPath;
            private Object apiHash;
            private String serverId;
            private String serverName;
            private String createTime;
            private int menuId;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getApiName() {
                return apiName;
            }

            public void setApiName(String apiName) {
                this.apiName = apiName;
            }

            public String getApiPath() {
                return apiPath;
            }

            public void setApiPath(String apiPath) {
                this.apiPath = apiPath;
            }

            public Object getApiHash() {
                return apiHash;
            }

            public void setApiHash(Object apiHash) {
                this.apiHash = apiHash;
            }

            public String getServerId() {
                return serverId;
            }

            public void setServerId(String serverId) {
                this.serverId = serverId;
            }

            public String getServerName() {
                return serverName;
            }

            public void setServerName(String serverName) {
                this.serverName = serverName;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public int getMenuId() {
                return menuId;
            }

            public void setMenuId(int menuId) {
                this.menuId = menuId;
            }
        }
    }

    public static class ExtraBean {
        /**
         * id : 1
         * customerId : 1001
         * customerName : 普宙无人机
         * signKey : 233eb86b09324f3fa4ecf7f8deaa13fb
         * status : 1
         * startTime : 2019-04-11 06:06:43
         * endTime : 2021-04-02 06:06:49
         * remark : 可用于测试
         */

        private int id;
        private int customerId;
        private String customerName;
        private String signKey;
        private int status;
        private String startTime;
        private String endTime;
        private String remark;

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

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getSignKey() {
            return signKey;
        }

        public void setSignKey(String signKey) {
            this.signKey = signKey;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
