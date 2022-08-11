package com.gdu.command.ui.message;

import com.google.gson.annotations.SerializedName;

/**
 * Copyright (C), 2020-2030, 武汉中旗生物医疗电子有限公司
 * <p>
 * 功能描述:
 * <p>
 * 创建时间: 2022/6/17 16:41
 *
 * @author yuhao
 */

public class NoticeAlarmBean {

    private MsgVoDTO msgVo;

    private Integer needVoicePlayMsg;

    private Integer unreadMsgCount;


    public static class MsgVoDTO {

        private String businessId;

        private Integer businessType;

        private String createTime;

        private String deviceCode;

        private Integer deviceId;

        private Integer hasRead;

        private Integer id;

        private String msgContent;


        public String getBusinessId() {
            return businessId;
        }

        public void setBusinessId(String businessId) {
            this.businessId = businessId;
        }

        public Integer getBusinessType() {
            return businessType;
        }

        public void setBusinessType(Integer businessType) {
            this.businessType = businessType;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDeviceCode() {
            return deviceCode;
        }

        public void setDeviceCode(String deviceCode) {
            this.deviceCode = deviceCode;
        }

        public Integer getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(Integer deviceId) {
            this.deviceId = deviceId;
        }

        public Integer getHasRead() {
            return hasRead;
        }

        public void setHasRead(Integer hasRead) {
            this.hasRead = hasRead;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getMsgContent() {
            return msgContent;
        }

        public void setMsgContent(String msgContent) {
            this.msgContent = msgContent;
        }
    }


    public MsgVoDTO getMsgVo() {
        return msgVo;
    }

    public void setMsgVo(MsgVoDTO msgVo) {
        this.msgVo = msgVo;
    }

    public Integer getNeedVoicePlayMsg() {
        return needVoicePlayMsg;
    }

    public void setNeedVoicePlayMsg(Integer needVoicePlayMsg) {
        this.needVoicePlayMsg = needVoicePlayMsg;
    }

    public Integer getUnreadMsgCount() {
        return unreadMsgCount;
    }

    public void setUnreadMsgCount(Integer unreadMsgCount) {
        this.unreadMsgCount = unreadMsgCount;
    }
}
