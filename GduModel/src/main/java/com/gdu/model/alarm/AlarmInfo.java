package com.gdu.model.alarm;

import java.io.Serializable;
import java.util.List;

/**
 * 告警信息
 */
public class AlarmInfo implements Serializable {

    private long captureTime;
    private int id;
    private String createTime;
    private String updateTime;
    private int duration;
    private int imgeWidth;
    private int imgeHeight;
    private String imageUrl;
    private String abbrFrameUrl;
    private int alarmStatus;
    private int handleType;
    private String handleTypeName;
    private int handleResultType;
    private String handleResultTypeName;
    private int alarmType;
    private String alarmTypeName;
    private int alarmLevel;
    private String deviceId;
    private String deviceName;
    private String deviceAddress;
    private List<SectionLocationsBean> sectionLocations;
    private double lat;
    private double lon;
    private double presetLat;
    private double presetLon;
    private double ptzPitch;
    private double ptzYaw;
    private double ptzZoom;
    private String rawFrameUrl;
    private String reliability;
    private String videoUrl;
    private String caseFile;
    private RemarkVoBean remarkVo;
    private int attention;
    private int stayTime;
    private int visibleType;
    private List<DesignateManBean> designateMan;
    private String resultFrameUrl1;
    private String resultFrameUrl2;

    public long getCaptureTime() {
        return captureTime;
    }

    public void setCaptureTime(long captureTime) {
        this.captureTime = captureTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getImgeWidth() {
        return imgeWidth;
    }

    public void setImgeWidth(int imgeWidth) {
        this.imgeWidth = imgeWidth;
    }

    public int getImgeHeight() {
        return imgeHeight;
    }

    public void setImgeHeight(int imgeHeight) {
        this.imgeHeight = imgeHeight;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAbbrFrameUrl() {
        return abbrFrameUrl;
    }

    public void setAbbrFrameUrl(String abbrFrameUrl) {
        this.abbrFrameUrl = abbrFrameUrl;
    }

    public int getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(int alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

    public int getHandleType() {
        return handleType;
    }

    public void setHandleType(int handleType) {
        this.handleType = handleType;
    }

    public String getHandleTypeName() {
        return handleTypeName;
    }

    public void setHandleTypeName(String handleTypeName) {
        this.handleTypeName = handleTypeName;
    }

    public int getHandleResultType() {
        return handleResultType;
    }

    public void setHandleResultType(int handleResultType) {
        this.handleResultType = handleResultType;
    }

    public String getHandleResultTypeName() {
        return handleResultTypeName;
    }

    public void setHandleResultTypeName(String handleResultTypeName) {
        this.handleResultTypeName = handleResultTypeName;
    }

    public int getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(int alarmType) {
        this.alarmType = alarmType;
    }

    public String getAlarmTypeName() {
        return alarmTypeName;
    }

    public void setAlarmTypeName(String alarmTypeName) {
        this.alarmTypeName = alarmTypeName;
    }

    public int getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(int alarmLevel) {
        this.alarmLevel = alarmLevel;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public List<SectionLocationsBean> getSectionLocations() {
        return sectionLocations;
    }

    public void setSectionLocations(List<SectionLocationsBean> sectionLocations) {
        this.sectionLocations = sectionLocations;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getPresetLat() {
        return presetLat;
    }

    public void setPresetLat(double presetLat) {
        this.presetLat = presetLat;
    }

    public double getPresetLon() {
        return presetLon;
    }

    public void setPresetLon(double presetLon) {
        this.presetLon = presetLon;
    }

    public double getPtzPitch() {
        return ptzPitch;
    }

    public void setPtzPitch(double ptzPitch) {
        this.ptzPitch = ptzPitch;
    }

    public double getPtzYaw() {
        return ptzYaw;
    }

    public void setPtzYaw(double ptzYaw) {
        this.ptzYaw = ptzYaw;
    }

    public double getPtzZoom() {
        return ptzZoom;
    }

    public void setPtzZoom(double ptzZoom) {
        this.ptzZoom = ptzZoom;
    }

    public String getRawFrameUrl() {
        return rawFrameUrl;
    }

    public void setRawFrameUrl(String rawFrameUrl) {
        this.rawFrameUrl = rawFrameUrl;
    }

    public String getReliability() {
        return reliability;
    }

    public void setReliability(String reliability) {
        this.reliability = reliability;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getCaseFile() {
        return caseFile;
    }

    public void setCaseFile(String caseFile) {
        this.caseFile = caseFile;
    }

    public RemarkVoBean getRemarkVo() {
        return remarkVo;
    }

    public void setRemarkVo(RemarkVoBean remarkVo) {
        this.remarkVo = remarkVo;
    }

    public int getAttention() {
        return attention;
    }

    public void setAttention(int attention) {
        this.attention = attention;
    }

    public int getStayTime() {
        return stayTime;
    }

    public void setStayTime(int stayTime) {
        this.stayTime = stayTime;
    }

    public int getVisibleType() {
        return visibleType;
    }

    public void setVisibleType(int visibleType) {
        this.visibleType = visibleType;
    }

    public List<DesignateManBean> getDesignateMan() {
        return designateMan;
    }

    public void setDesignateMan(List<DesignateManBean> designateMan) {
        this.designateMan = designateMan;
    }

    public String getResultFrameUrl1() {
        return resultFrameUrl1;
    }

    public void setResultFrameUrl1(String resultFrameUrl1) {
        this.resultFrameUrl1 = resultFrameUrl1;
    }

    public String getResultFrameUrl2() {
        return resultFrameUrl2;
    }

    public void setResultFrameUrl2(String resultFrameUrl2) {
        this.resultFrameUrl2 = resultFrameUrl2;
    }

    public static class RemarkVoBean implements Serializable {
        private String remark;
        private String remarkAuthorName;
        private String remarkTime;

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getRemarkAuthorName() {
            return remarkAuthorName;
        }

        public void setRemarkAuthorName(String remarkAuthorName) {
            this.remarkAuthorName = remarkAuthorName;
        }

        public String getRemarkTime() {
            return remarkTime;
        }

        public void setRemarkTime(String remarkTime) {
            this.remarkTime = remarkTime;
        }
    }

    public static class SectionLocationsBean implements Serializable {
        private int top;
        private int left;
        private int width;
        private int height;

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }

        public int getLeft() {
            return left;
        }

        public void setLeft(int left) {
            this.left = left;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }

    public static class DesignateManBean implements Serializable {
        private int employeeId;
        private String employeeName;
        private String orgName;

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

        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }
    }
}
