package com.gdu.command.ui.video.model;

import com.gdu.model.device.DeviceInfo;

import java.io.Serializable;
import java.util.List;

public class DevicesBean implements Serializable {

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
        private String deviceTypeCode;
        private String deviceTypeName;
        private List<DeviceListBean> deviceList;

        public String getDeviceTypeCode() {
            return deviceTypeCode;
        }

        public void setDeviceTypeCode(String deviceTypeCode) {
            this.deviceTypeCode = deviceTypeCode;
        }

        public String getDeviceTypeName() {
            return deviceTypeName;
        }

        public void setDeviceTypeName(String deviceTypeName) {
            this.deviceTypeName = deviceTypeName;
        }

        public List<DeviceListBean> getDeviceList() {
            return deviceList;
        }

        public void setDeviceList(List<DeviceListBean> deviceList) {
            this.deviceList = deviceList;
        }

        public static class DeviceListBean {
            private String deptTypeCode;
            private String parentDeptCode;
            private String deptCode;
            private String deptName;
            private int orderNum;
            private int deviceCountTotal;
            private int deviceCountOnline;
            private List<SubDeptBean> subDept;
            private List<DeviceInfo> deviceList;

            public String getDeptTypeCode() {
                return deptTypeCode;
            }

            public void setDeptTypeCode(String deptTypeCode) {
                this.deptTypeCode = deptTypeCode;
            }

            public String getParentDeptCode() {
                return parentDeptCode;
            }

            public void setParentDeptCode(String parentDeptCode) {
                this.parentDeptCode = parentDeptCode;
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

            public int getOrderNum() {
                return orderNum;
            }

            public void setOrderNum(int orderNum) {
                this.orderNum = orderNum;
            }

            public int getDeviceCountTotal() {
                return deviceCountTotal;
            }

            public void setDeviceCountTotal(int deviceCountTotal) {
                this.deviceCountTotal = deviceCountTotal;
            }

            public int getDeviceCountOnline() {
                return deviceCountOnline;
            }

            public void setDeviceCountOnline(int deviceCountOnline) {
                this.deviceCountOnline = deviceCountOnline;
            }

            public List<SubDeptBean> getSubDept() {
                return subDept;
            }

            public void setSubDept(List<SubDeptBean> subDept) {
                this.subDept = subDept;
            }

            public List<DeviceInfo> getDeviceListX() {
                return deviceList;
            }

            public void setDeviceListX(List<DeviceInfo> deviceList) {
                this.deviceList = deviceList;
            }

            public static class SubDeptBean {
                private String deptTypeCode;
                private String parentDeptCode;
                private String deptCode;
                private String deptName;
                private int orderNum;
                private int deviceCountTotal;
                private int deviceCountOnline;
                private Object subDept;
                private List<DeviceInfo> deviceList;

                public String getDeptTypeCode() {
                    return deptTypeCode;
                }

                public void setDeptTypeCode(String deptTypeCode) {
                    this.deptTypeCode = deptTypeCode;
                }

                public String getParentDeptCode() {
                    return parentDeptCode;
                }

                public void setParentDeptCode(String parentDeptCode) {
                    this.parentDeptCode = parentDeptCode;
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

                public int getOrderNum() {
                    return orderNum;
                }

                public void setOrderNum(int orderNum) {
                    this.orderNum = orderNum;
                }

                public int getDeviceCountTotal() {
                    return deviceCountTotal;
                }

                public void setDeviceCountTotal(int deviceCountTotal) {
                    this.deviceCountTotal = deviceCountTotal;
                }

                public int getDeviceCountOnline() {
                    return deviceCountOnline;
                }

                public void setDeviceCountOnline(int deviceCountOnline) {
                    this.deviceCountOnline = deviceCountOnline;
                }

                public Object getSubDept() {
                    return subDept;
                }

                public void setSubDept(Object subDept) {
                    this.subDept = subDept;
                }

                public List<DeviceInfo> getDeviceList() {
                    return deviceList;
                }

                public void setDeviceList(List<DeviceInfo> deviceList) {
                    this.deviceList = deviceList;
                }
            }

            public static class DeviceListBeanX {
                private String deviceTypeCode;
                private String deviceName;
                private String deviceCode;
                private String deviceBrand;
                private String deviceAddress;
                private double deviceLatitude;
                private double deviceLongitude;
                private String onlineStatus;
                private Object deviceUserName;
                private Object devicePassword;
                private Object deviceExpirationDate;
                private Object deviceHeight;
                private Object deviceSeatAz;
                private long onlineTime;
                private long offlineTime;
                private long createTime;
                private List<?> streamList;

                public String getDeviceTypeCode() {
                    return deviceTypeCode;
                }

                public void setDeviceTypeCode(String deviceTypeCode) {
                    this.deviceTypeCode = deviceTypeCode;
                }

                public String getDeviceName() {
                    return deviceName;
                }

                public void setDeviceName(String deviceName) {
                    this.deviceName = deviceName;
                }

                public String getDeviceCode() {
                    return deviceCode;
                }

                public void setDeviceCode(String deviceCode) {
                    this.deviceCode = deviceCode;
                }

                public String getDeviceBrand() {
                    return deviceBrand;
                }

                public void setDeviceBrand(String deviceBrand) {
                    this.deviceBrand = deviceBrand;
                }

                public String getDeviceAddress() {
                    return deviceAddress;
                }

                public void setDeviceAddress(String deviceAddress) {
                    this.deviceAddress = deviceAddress;
                }

                public double getDeviceLatitude() {
                    return deviceLatitude;
                }

                public void setDeviceLatitude(double deviceLatitude) {
                    this.deviceLatitude = deviceLatitude;
                }

                public double getDeviceLongitude() {
                    return deviceLongitude;
                }

                public void setDeviceLongitude(double deviceLongitude) {
                    this.deviceLongitude = deviceLongitude;
                }

                public String getOnlineStatus() {
                    return onlineStatus;
                }

                public void setOnlineStatus(String onlineStatus) {
                    this.onlineStatus = onlineStatus;
                }

                public Object getDeviceUserName() {
                    return deviceUserName;
                }

                public void setDeviceUserName(Object deviceUserName) {
                    this.deviceUserName = deviceUserName;
                }

                public Object getDevicePassword() {
                    return devicePassword;
                }

                public void setDevicePassword(Object devicePassword) {
                    this.devicePassword = devicePassword;
                }

                public Object getDeviceExpirationDate() {
                    return deviceExpirationDate;
                }

                public void setDeviceExpirationDate(Object deviceExpirationDate) {
                    this.deviceExpirationDate = deviceExpirationDate;
                }

                public Object getDeviceHeight() {
                    return deviceHeight;
                }

                public void setDeviceHeight(Object deviceHeight) {
                    this.deviceHeight = deviceHeight;
                }

                public Object getDeviceSeatAz() {
                    return deviceSeatAz;
                }

                public void setDeviceSeatAz(Object deviceSeatAz) {
                    this.deviceSeatAz = deviceSeatAz;
                }

                public long getOnlineTime() {
                    return onlineTime;
                }

                public void setOnlineTime(long onlineTime) {
                    this.onlineTime = onlineTime;
                }

                public long getOfflineTime() {
                    return offlineTime;
                }

                public void setOfflineTime(long offlineTime) {
                    this.offlineTime = offlineTime;
                }

                public long getCreateTime() {
                    return createTime;
                }

                public void setCreateTime(long createTime) {
                    this.createTime = createTime;
                }

                public List<?> getStreamList() {
                    return streamList;
                }

                public void setStreamList(List<?> streamList) {
                    this.streamList = streamList;
                }
            }
        }
    }
}
