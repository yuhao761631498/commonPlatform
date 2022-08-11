package com.gdu.mqttchat.chat.view;

import java.io.Serializable;

public class CaseChatDetailBean implements Serializable {

    private int code;
    private String msg;
    private DataBean data;
    private Object extra;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public Object getExtra() {
        return extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }

    public static class DataBean {
        private int id;
        private int alarmId;
        private String handleUserName;
        private String handleUserOrgName;
        private int handleType;
        private String handleTypeName;
        private String handleResultType;
        private String handleResultTypeName;
        private String handleContent;
        private String handleTime;
        private String answerPoliceTime;
        private String alarmAddress;
        private String fileKeys;
        private String abbrFrameUrl;
        private String alarmTypeName;
        private String createTime;
        private String deviceName;
        private int alarmType;
        private double presetLat;
        private double presetLon;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getAlarmId() {
            return alarmId;
        }

        public void setAlarmId(int alarmId) {
            this.alarmId = alarmId;
        }

        public String getHandleUserName() {
            return handleUserName;
        }

        public void setHandleUserName(String handleUserName) {
            this.handleUserName = handleUserName;
        }

        public String getHandleUserOrgName() {
            return handleUserOrgName;
        }

        public void setHandleUserOrgName(String handleUserOrgName) {
            this.handleUserOrgName = handleUserOrgName;
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

        public String getHandleResultType() {
            return handleResultType;
        }

        public void setHandleResultType(String handleResultType) {
            this.handleResultType = handleResultType;
        }

        public String getHandleResultTypeName() {
            return handleResultTypeName;
        }

        public void setHandleResultTypeName(String handleResultTypeName) {
            this.handleResultTypeName = handleResultTypeName;
        }

        public String getHandleContent() {
            return handleContent;
        }

        public void setHandleContent(String handleContent) {
            this.handleContent = handleContent;
        }

        public String getHandleTime() {
            return handleTime;
        }

        public void setHandleTime(String handleTime) {
            this.handleTime = handleTime;
        }

        public String getAnswerPoliceTime() {
            return answerPoliceTime;
        }

        public void setAnswerPoliceTime(String answerPoliceTime) {
            this.answerPoliceTime = answerPoliceTime;
        }

        public String getAlarmAddress() {
            return alarmAddress;
        }

        public void setAlarmAddress(String alarmAddress) {
            this.alarmAddress = alarmAddress;
        }

        public String getFileKeys() {
            return fileKeys;
        }

        public void setFileKeys(String fileKeys) {
            this.fileKeys = fileKeys;
        }

        public String getAbbrFrameUrl() {
            return abbrFrameUrl;
        }

        public void setAbbrFrameUrl(String abbrFrameUrl) {
            this.abbrFrameUrl = abbrFrameUrl;
        }

        public String getAlarmTypeName() {
            return alarmTypeName;
        }

        public void setAlarmTypeName(String alarmTypeName) {
            this.alarmTypeName = alarmTypeName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public int getAlarmType() {
            return alarmType;
        }

        public void setAlarmType(int alarmType) {
            this.alarmType = alarmType;
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
    }
}
