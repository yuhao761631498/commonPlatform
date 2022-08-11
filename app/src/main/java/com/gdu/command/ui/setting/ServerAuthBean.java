package com.gdu.command.ui.setting;

import java.io.Serializable;

public class ServerAuthBean implements Serializable {
    private String searchValue;
    private String createBy;
    private String createTime;
    private String updateBy;
    private String updateTime;
    private String remark;
    private ParamsDTO params;
    private int id;
    private String mqttHost;
    private int mqttPort;
    private int organizationPort;
    private int pushPort;
    private String custCode;
    private String sqCustomer;
    private String serverType;
    private String spToken;
    private String httpIp;
    private int spStatus;

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public ParamsDTO getParams() {
        return params;
    }

    public void setParams(ParamsDTO params) {
        this.params = params;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMqttHost() {
        return mqttHost;
    }

    public void setMqttHost(String mqttHost) {
        this.mqttHost = mqttHost;
    }

    public int getMqttPort() {
        return mqttPort;
    }

    public void setMqttPort(int mqttPort) {
        this.mqttPort = mqttPort;
    }

    public int getOrganizationPort() {
        return organizationPort;
    }

    public void setOrganizationPort(int organizationPort) {
        this.organizationPort = organizationPort;
    }

    public int getPushPort() {
        return pushPort;
    }

    public void setPushPort(int pushPort) {
        this.pushPort = pushPort;
    }

    public String getCustCode() {
        return custCode;
    }

    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }

    public String getSqCustomer() {
        return sqCustomer;
    }

    public void setSqCustomer(String sqCustomer) {
        this.sqCustomer = sqCustomer;
    }

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public String getSpToken() {
        return spToken;
    }

    public void setSpToken(String spToken) {
        this.spToken = spToken;
    }

    public String getHttpIp() {
        return httpIp;
    }

    public void setHttpIp(String httpIp) {
        this.httpIp = httpIp;
    }

    public int getSpStatus() {
        return spStatus;
    }

    public void setSpStatus(int spStatus) {
        this.spStatus = spStatus;
    }

    public static class ParamsDTO {
    }
}
