package com.gdu.model.device;

import java.io.Serializable;
import java.util.List;

/**
 * 设备类型对应的组织机构及设备列表
 */
public class DeptAndDevice implements Serializable {

    private String deviceTypeCode;

    private String deviceTypeName;

    private List<DeptListInfo> deviceList;

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

    public List<DeptListInfo> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<DeptListInfo> deviceList) {
        this.deviceList = deviceList;
    }
}
