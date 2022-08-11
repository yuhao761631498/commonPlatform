package com.gdu.model.resource;

import java.io.Serializable;

/**
 * 组织架构
 */
public class ResOrganizationBean implements Serializable {
    private int id;
    private String deptCode;
    private String deptName;
    private String deptAddress;
    private String deptTel;
    private double deptLongitude;
    private double deptLatitude;
    private String deptIcon;
    private String iconSize;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDeptIcon() {
        return deptIcon;
    }

    public void setDeptIcon(String deptIcon) {
        this.deptIcon = deptIcon;
    }

    public String getIconSize() {
        return iconSize;
    }

    public void setIconSize(String iconSize) {
        this.iconSize = iconSize;
    }
}
