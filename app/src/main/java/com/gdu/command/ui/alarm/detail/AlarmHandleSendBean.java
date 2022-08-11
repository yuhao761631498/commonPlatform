package com.gdu.command.ui.alarm.detail;

import java.io.Serializable;
import java.util.List;

public class AlarmHandleSendBean implements Serializable {
    /** 预警Id */
    private Integer alarmId;
    /** 预警地址 */
    private String alarmAddress;
    /** 文件对象集合 */
    private List<AlarmHandleFileBean> fileList;
    /** 处理内容 */
    private String handleContent;
    /** 处理结果类型(1 行政处罚; 2 行政拘留; 3 口头警告; 4 其他处罚) */
    private Integer handleResultType;
    /** 告警记录id（分派过的告警必传） */
    private Integer id;
    /** 告警纬度 */
    private Double lat;
    /** 告警经度 */
    private Double lon;
    /** 补充处理的处理内容 */
    private String supplementHandleContent;

    public Integer getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(Integer alarmId) {
        this.alarmId = alarmId;
    }

    public String getAlarmAddress() {
        return alarmAddress;
    }

    public void setAlarmAddress(String alarmAddress) {
        this.alarmAddress = alarmAddress;
    }

    public List<AlarmHandleFileBean> getFileList() {
        return fileList;
    }

    public void setFileList(List<AlarmHandleFileBean> fileList) {
        this.fileList = fileList;
    }

    public String getHandleContent() {
        return handleContent;
    }

    public void setHandleContent(String handleContent) {
        this.handleContent = handleContent;
    }

    public Integer getHandleResultType() {
        return handleResultType;
    }

    public void setHandleResultType(Integer handleResultType) {
        this.handleResultType = handleResultType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getSupplementHandleContent() {
        return supplementHandleContent;
    }

    public void setSupplementHandleContent(String supplementHandleContent) {
        this.supplementHandleContent = supplementHandleContent;
    }
}
