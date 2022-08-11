package com.gdu.command.views.drop;

import java.io.Serializable;
import java.util.List;

public class AlarmDeviceBean implements Serializable {

    private int code;
    private String msg;
    private DataBean data;

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

    public static class DataBean implements Serializable {
        private String msg;
        private int total;
        private int code;
        private List<RowsBean> rows;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public List<RowsBean> getRows() {
            return rows;
        }

        public void setRows(List<RowsBean> rows) {
            this.rows = rows;
        }

        public static class RowsBean implements Serializable {
            private String deviceType;
            private String deviceIp;
            private String onlineStatus;
            private int orderNum;
            private String deviceCode;
            private String protocolType;
            private String deviceName;
            private int parentId;
            private String isFolder;
            private String deviceLatitude;
            private Object deviceAddress;
            private List<ChannelsBean> channels;
            private int customerId;
            private String deviceBrandLabel;
            private int id;
            private String ancestors;
            private String deviceLongitude;
            private String deviceBrand;
            private boolean isSelected;

            public String getDeviceType() {
                return deviceType;
            }

            public void setDeviceType(String deviceType) {
                this.deviceType = deviceType;
            }

            public String getDeviceIp() {
                return deviceIp;
            }

            public void setDeviceIp(String deviceIp) {
                this.deviceIp = deviceIp;
            }

            public String getOnlineStatus() {
                return onlineStatus;
            }

            public void setOnlineStatus(String onlineStatus) {
                this.onlineStatus = onlineStatus;
            }

            public int getOrderNum() {
                return orderNum;
            }

            public void setOrderNum(int orderNum) {
                this.orderNum = orderNum;
            }

            public String getDeviceCode() {
                return deviceCode;
            }

            public void setDeviceCode(String deviceCode) {
                this.deviceCode = deviceCode;
            }

            public String getProtocolType() {
                return protocolType;
            }

            public void setProtocolType(String protocolType) {
                this.protocolType = protocolType;
            }

            public String getDeviceName() {
                return deviceName;
            }

            public void setDeviceName(String deviceName) {
                this.deviceName = deviceName;
            }

            public int getParentId() {
                return parentId;
            }

            public void setParentId(int parentId) {
                this.parentId = parentId;
            }

            public String getIsFolder() {
                return isFolder;
            }

            public void setIsFolder(String isFolder) {
                this.isFolder = isFolder;
            }

            public String getDeviceLatitude() {
                return deviceLatitude;
            }

            public void setDeviceLatitude(String deviceLatitude) {
                this.deviceLatitude = deviceLatitude;
            }

            public Object getDeviceAddress() {
                return deviceAddress;
            }

            public void setDeviceAddress(Object deviceAddress) {
                this.deviceAddress = deviceAddress;
            }

            public List<ChannelsBean> getChannels() {
                return channels;
            }

            public void setChannels(List<ChannelsBean> channels) {
                this.channels = channels;
            }

            public int getCustomerId() {
                return customerId;
            }

            public void setCustomerId(int customerId) {
                this.customerId = customerId;
            }

            public String getDeviceBrandLabel() {
                return deviceBrandLabel;
            }

            public void setDeviceBrandLabel(String deviceBrandLabel) {
                this.deviceBrandLabel = deviceBrandLabel;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getAncestors() {
                return ancestors;
            }

            public void setAncestors(String ancestors) {
                this.ancestors = ancestors;
            }

            public String getDeviceLongitude() {
                return deviceLongitude;
            }

            public void setDeviceLongitude(String deviceLongitude) {
                this.deviceLongitude = deviceLongitude;
            }

            public String getDeviceBrand() {
                return deviceBrand;
            }

            public void setDeviceBrand(String deviceBrand) {
                this.deviceBrand = deviceBrand;
            }

            public boolean isSelected() {
                return isSelected;
            }

            public void setSelected(boolean selected) {
                isSelected = selected;
            }

            public static class ChannelsBean implements Serializable {
                private int channelType;
                private String channelId;

                public int getChannelType() {
                    return channelType;
                }

                public void setChannelType(int channelType) {
                    this.channelType = channelType;
                }

                public String getChannelId() {
                    return channelId;
                }

                public void setChannelId(String channelId) {
                    this.channelId = channelId;
                }
            }
        }
    }
}
