package com.gdu.model.resource;

import java.io.Serializable;

/**
 * 高点监控资源
 */
public class ResHighPointDeviceBean implements Serializable {

    private String accessType;
    private String channelInfo;
    private String deviceAddress;
    private String deviceBrand;
    private String deviceCode;
    private double deviceLatitude;
    private double deviceLongitude;
    private String deviceName;
    private String deviceTypeCode;
    private String iconSize;
    private String iconUrl;
    private int id;
    private String onlineStatus;
    private boolean isShow = true;

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getChannelInfo() {
        return channelInfo;
    }

    public void setChannelInfo(String channelInfo) {
        this.channelInfo = channelInfo;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public String getDeviceBrand() {
        return deviceBrand;
    }

    public void setDeviceBrand(String deviceBrand) {
        this.deviceBrand = deviceBrand;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
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

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceTypeCode() {
        return deviceTypeCode;
    }

    public void setDeviceTypeCode(String deviceTypeCode) {
        this.deviceTypeCode = deviceTypeCode;
    }

    public String getIconSize() {
        return iconSize;
    }

    public void setIconSize(String iconSize) {
        this.iconSize = iconSize;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }
}
