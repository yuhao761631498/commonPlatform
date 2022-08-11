package com.gdu.model.device;

import com.gdu.model.LightType;

import java.io.Serializable;
import java.util.List;

/**
 * 高点设备信息
 */
public class DeviceInfo implements Serializable {

    private LightType lightType; //光类型
    private String accessType;   //接入方式(0：SDK接入，1：GB接入)
    private String deviceAddress; //设备地址
    private String deviceBrand; //设备品牌
    private String deviceCode;  //设备ID
    private double deviceLatitude;        //纬度
    private double deviceLongitude;        //经度
    private String deviceName;             //设备名称
    private String deviceTypeCode;         //设备类型
    private String iconSize;  //图标尺寸
    private String iconUrl;  //图标
    private String onlineStatus;  //设备在线状态,empty未注册，online在线，offline离线
    private List<StreamInfo> streamList;  //高点流信息

    private String visibleLightUrl;  //可见光的播放地址
    private String infraredLightUrl; //红外光的播放地址
    private String visibleCoverUrl;  //可见光的封面地址
    private String infraredCoverUrl; //红外光的封面地址
    private String deviceUserName;  // 设备用户名
    private long createTime;  // 创建时间
    private String deptName; // 所属单位名
    private int iconId;


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

    public String getVisibleLightUrl() {
        return visibleLightUrl;
    }

    public void setVisibleLightUrl(String visibleLightUrl) {
        this.visibleLightUrl = visibleLightUrl;
    }

    public String getInfraredLightUrl() {
        return infraredLightUrl;
    }

    public void setInfraredLightUrl(String infraredLightUrl) {
        this.infraredLightUrl = infraredLightUrl;
    }

    public String getVisibleCoverUrl() {
        return visibleCoverUrl;
    }

    public void setVisibleCoverUrl(String visibleCoverUrl) {
        this.visibleCoverUrl = visibleCoverUrl;
    }

    public String getInfraredCoverUrl() {
        return infraredCoverUrl;
    }

    public void setInfraredCoverUrl(String infraredCoverUrl) {
        this.infraredCoverUrl = infraredCoverUrl;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public LightType getLightType() {
        return lightType;
    }

    public void setLightType(LightType lightType) {
        this.lightType = lightType;
    }

    public double getDeviceLongitude() {
        return deviceLongitude;
    }

    public void setDeviceLongitude(double deviceLongitude) {
        this.deviceLongitude = deviceLongitude;
    }

    public double getDeviceLatitude() {
        return deviceLatitude;
    }

    public void setDeviceLatitude(double deviceLatitude) {
        this.deviceLatitude = deviceLatitude;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
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

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public List<StreamInfo> getStreamList() {
        return streamList;
    }

    public void setStreamList(List<StreamInfo> streamList) {
        this.streamList = streamList;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconIdParam) {
        iconId = iconIdParam;
    }

    public String getDeviceUserName() {
        return deviceUserName;
    }

    public void setDeviceUserName(String deviceUserNameParam) {
        deviceUserName = deviceUserNameParam;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTimeParam) {
        createTime = createTimeParam;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptNameParam) {
        deptName = deptNameParam;
    }
}
