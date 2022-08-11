package com.gdu.command.ui.home;

import java.io.Serializable;
import java.util.List;

public class RoleInfoBean implements Serializable {
    private int code;
    private String msg;
    private List<List<DataBean>> data;
    private ExtraBean extra;

    public int getCode() {
        return code;
    }

    public void setCode(int codeParam) {
        code = codeParam;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msgParam) {
        msg = msgParam;
    }

    public List<List<DataBean>> getDataX() {
        return data;
    }

    public void setDataX(List<List<DataBean>> dataX) {
        this.data = dataX;
    }

    public ExtraBean getExtraX() {
        return extra;
    }

    public void setExtraX(ExtraBean extraX) {
        this.extra = extraX;
    }

    public static class DataBean {
        private int id;
        private int roleCode;
        private int menuId;
        private String menuName;
        private int superId;
        private List<RoleMenuListBean> roleMenuList;

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

        public int getMenuId() {
            return menuId;
        }

        public void setMenuId(int menuId) {
            this.menuId = menuId;
        }

        public String getMenuName() {
            return menuName;
        }

        public void setMenuName(String menuName) {
            this.menuName = menuName;
        }

        public int getSuperId() {
            return superId;
        }

        public void setSuperId(int superId) {
            this.superId = superId;
        }

        public List<RoleMenuListBean> getRoleMenuList() {
            return roleMenuList;
        }

        public void setRoleMenuList(List<RoleMenuListBean> roleMenuList) {
            this.roleMenuList = roleMenuList;
        }

        public static class RoleMenuListBean {
            private int id;
            private int roleCode;
            private int menuId;
            private String menuName;
            private int superId;
            private List<RoleMenuListBeanX> roleMenuList;

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

            public int getMenuId() {
                return menuId;
            }

            public void setMenuId(int menuId) {
                this.menuId = menuId;
            }

            public String getMenuName() {
                return menuName;
            }

            public void setMenuName(String menuName) {
                this.menuName = menuName;
            }

            public int getSuperId() {
                return superId;
            }

            public void setSuperId(int superId) {
                this.superId = superId;
            }

            public List<RoleMenuListBeanX> getRoleMenuXList() {
                return roleMenuList;
            }

            public void setRoleMenuXList(List<RoleMenuListBeanX> roleMenuList) {
                this.roleMenuList = roleMenuList;
            }

            public static class RoleMenuListBeanX {
                private int id;
                private int roleCode;
                private int menuId;
                private String menuName;
                private int superId;
                private List<Object> roleMenuList;

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

                public int getMenuId() {
                    return menuId;
                }

                public void setMenuId(int menuId) {
                    this.menuId = menuId;
                }

                public String getMenuName() {
                    return menuName;
                }

                public void setMenuName(String menuName) {
                    this.menuName = menuName;
                }

                public int getSuperId() {
                    return superId;
                }

                public void setSuperId(int superId) {
                    this.superId = superId;
                }

                public List<Object> getRoleMenuList() {
                    return roleMenuList;
                }

                public void setRoleMenuList(List<Object> roleMenuList) {
                    this.roleMenuList = roleMenuList;
                }
            }
        }
    }
    public static class ExtraBean {

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
