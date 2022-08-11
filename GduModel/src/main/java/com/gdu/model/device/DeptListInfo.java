package com.gdu.model.device;

import java.io.Serializable;
import java.util.List;

public class DeptListInfo implements Serializable {
    private String deptTypeCode;
    private String parentDeptCode;
    private String deptCode;
    private String deptName;
    private String orderNum;
    private String deviceCountTotal;
    private String deviceCountOnline;
    private List<DeptListInfo> subDept;
    private List<DeviceInfo> deviceList;

    public List<DeviceInfo> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<DeviceInfo> deviceList) {
        this.deviceList = deviceList;
    }


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

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getDeviceCountTotal() {
        return deviceCountTotal;
    }

    public void setDeviceCountTotal(String deviceCountTotal) {
        this.deviceCountTotal = deviceCountTotal;
    }

    public String getDeviceCountOnline() {
        return deviceCountOnline;
    }

    public void setDeviceCountOnline(String deviceCountOnline) {
        this.deviceCountOnline = deviceCountOnline;
    }

    public List<DeptListInfo> getSubDept() {
        return subDept;
    }

    public void setSubDept(List<DeptListInfo> subDept) {
        this.subDept = subDept;
    }
}
