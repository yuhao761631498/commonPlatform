package com.gdu.command.ui.video.model;

import java.io.Serializable;
import java.util.List;

public class ChildrenBean implements Serializable {
    private String isFolder;
    private List<ChildrenBean> children;
    private CountBean count;
    private int weight;
    private long id;
    private String label;
    private String parentId;
    private boolean isVisible = true;  //是否是可见光
    /**
     * 设备类型：2: 球机(QJ)； 3: 高点监控(GDJK)；4: 地点监控(DDJK)； 5: 无人机(WRJ)； 6: 手持设备(SCSB)
     */
    private String deviceType;
    private String deviceTypeLabel;
    private String onlineStatus;
    private long customerId;
    private String lon;
    private String lat;
    private String deptName; // 所属单位名
    private int iconId;
    private int safetyWay;
    private String streamId;
    private double latitude;
    private int channelType;
    private String deviceId;
    private String password;
    private int registerWay;
    private String secrecy;
    private String updateBy;
    private String model;
    private String block;
    private boolean hasPlayAuthority;
    private int ptzType;
    private String certNum;
    private String channelId;
    private String parental;
    private double longitude;
    private String owner;
    private boolean hasAudio;
    private String address;
    private String ipAddress;
    private String updateTime;
    private ParamsBean params;
    private String civilCode;
    private String manufacture;
    private String createBy;
    private boolean hasPtzAuthority;
    private String createTime;
    private int port;
    private int errCode;
    private String name;
    private String deviceBrandLabel;
    private String endTime;
    private int certifiable;
    private String searchValue;
    private String deviceBrand;
    private int status;


    public boolean isVisible() {
        return isVisible;
    }

    public void setIsVisible(boolean visible) {
        isVisible = visible;
    }

    public String getIsFolder() {
        return isFolder;
    }

    public void setIsFolder(String isFolder) {
        this.isFolder = isFolder;
    }

    public List<ChildrenBean> getChildren() {
        return children;
    }

    public void setChildren(List<ChildrenBean> children) {
        this.children = children;
    }

    public CountBean getCount() {
        return count;
    }

    public void setCount(CountBean count) {
        this.count = count;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceTypeLabel() {
        return deviceTypeLabel;
    }

    public void setDeviceTypeLabel(String deviceTypeLabel) {
        this.deviceTypeLabel = deviceTypeLabel;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public int getSafetyWay() {
        return safetyWay;
    }

    public void setSafetyWay(int safetyWay) {
        this.safetyWay = safetyWay;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getChannelType() {
        return channelType;
    }

    public void setChannelType(int channelType) {
        this.channelType = channelType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRegisterWay() {
        return registerWay;
    }

    public void setRegisterWay(int registerWay) {
        this.registerWay = registerWay;
    }

    public String getSecrecy() {
        return secrecy;
    }

    public void setSecrecy(String secrecy) {
        this.secrecy = secrecy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public boolean isHasPlayAuthority() {
        return hasPlayAuthority;
    }

    public void setHasPlayAuthority(boolean hasPlayAuthority) {
        this.hasPlayAuthority = hasPlayAuthority;
    }

    public int getPtzType() {
        return ptzType;
    }

    public void setPtzType(int ptzType) {
        this.ptzType = ptzType;
    }

    public String getCertNum() {
        return certNum;
    }

    public void setCertNum(String certNum) {
        this.certNum = certNum;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getParental() {
        return parental;
    }

    public void setParental(String parental) {
        this.parental = parental;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isHasAudio() {
        return hasAudio;
    }

    public void setHasAudio(boolean hasAudio) {
        this.hasAudio = hasAudio;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public ParamsBean getParams() {
        return params;
    }

    public void setParams(ParamsBean params) {
        this.params = params;
    }

    public String getCivilCode() {
        return civilCode;
    }

    public void setCivilCode(String civilCode) {
        this.civilCode = civilCode;
    }

    public String getManufacture() {
        return manufacture;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public boolean isHasPtzAuthority() {
        return hasPtzAuthority;
    }

    public void setHasPtzAuthority(boolean hasPtzAuthority) {
        this.hasPtzAuthority = hasPtzAuthority;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceBrandLabel() {
        return deviceBrandLabel;
    }

    public void setDeviceBrandLabel(String deviceBrandLabel) {
        this.deviceBrandLabel = deviceBrandLabel;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getCertifiable() {
        return certifiable;
    }

    public void setCertifiable(int certifiable) {
        this.certifiable = certifiable;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public String getDeviceBrand() {
        return deviceBrand;
    }

    public void setDeviceBrand(String deviceBrand) {
        this.deviceBrand = deviceBrand;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class CountBean implements Serializable {
        private int onlineCount;
        private int offlineCount;
        private int totalCount;

        public int getOnlineCount() {
            return onlineCount;
        }

        public void setOnlineCount(int onlineCount) {
            this.onlineCount = onlineCount;
        }

        public int getOfflineCount() {
            return offlineCount;
        }

        public void setOfflineCount(int offlineCount) {
            this.offlineCount = offlineCount;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }
    }

    public static class ParamsBean implements Serializable {
    }
}