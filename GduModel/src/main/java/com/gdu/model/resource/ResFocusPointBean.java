package com.gdu.model.resource;

import java.io.Serializable;
import java.util.List;

/**
 * 关注点位
 */
public class ResFocusPointBean implements Serializable {

    private String contactTel;
    private String iconSize;
    private String id;
    private List<PointControlScopeVOSDTO> pointControlScopeVOS;
    private String resourcesAddr;
    private String resourcesIcon;
    private double resourcesLatitude;
    private double resourcesLongitude;
    private String resourcesName;
    private String resourcesType;
    private String resourcesTypeName;

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public String getIconSize() {
        return iconSize;
    }

    public void setIconSize(String iconSize) {
        this.iconSize = iconSize;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<PointControlScopeVOSDTO> getPointControlScopeVOS() {
        return pointControlScopeVOS;
    }

    public void setPointControlScopeVOS(List<PointControlScopeVOSDTO> pointControlScopeVOS) {
        this.pointControlScopeVOS = pointControlScopeVOS;
    }

    public String getResourcesAddr() {
        return resourcesAddr;
    }

    public void setResourcesAddr(String resourcesAddr) {
        this.resourcesAddr = resourcesAddr;
    }

    public String getResourcesIcon() {
        return resourcesIcon;
    }

    public void setResourcesIcon(String resourcesIcon) {
        this.resourcesIcon = resourcesIcon;
    }

    public double getResourcesLatitude() {
        return resourcesLatitude;
    }

    public void setResourcesLatitude(double resourcesLatitude) {
        this.resourcesLatitude = resourcesLatitude;
    }

    public double getResourcesLongitude() {
        return resourcesLongitude;
    }

    public void setResourcesLongitude(double resourcesLongitude) {
        this.resourcesLongitude = resourcesLongitude;
    }

    public String getResourcesName() {
        return resourcesName;
    }

    public void setResourcesName(String resourcesName) {
        this.resourcesName = resourcesName;
    }

    public String getResourcesType() {
        return resourcesType;
    }

    public void setResourcesType(String resourcesType) {
        this.resourcesType = resourcesType;
    }

    public String getResourcesTypeName() {
        return resourcesTypeName;
    }

    public void setResourcesTypeName(String resourcesTypeName) {
        this.resourcesTypeName = resourcesTypeName;
    }

    public static class PointControlScopeVOSDTO {
        private String belongOrg;
        private String fillColor;
        private String id;
        private String lineColor;
        private int lineWidth;
        private String longitudeLatitudeArray;
        private String pointAddr;
        private String pointName;
        private String pointRemark;
        private String pointType;
        private String resourcesId;

        public String getBelongOrg() {
            return belongOrg;
        }

        public void setBelongOrg(String belongOrg) {
            this.belongOrg = belongOrg;
        }

        public String getFillColor() {
            return fillColor;
        }

        public void setFillColor(String fillColor) {
            this.fillColor = fillColor;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLineColor() {
            return lineColor;
        }

        public void setLineColor(String lineColor) {
            this.lineColor = lineColor;
        }

        public int getLineWidth() {
            return lineWidth;
        }

        public void setLineWidth(int lineWidth) {
            this.lineWidth = lineWidth;
        }

        public String getLongitudeLatitudeArray() {
            return longitudeLatitudeArray;
        }

        public void setLongitudeLatitudeArray(String longitudeLatitudeArray) {
            this.longitudeLatitudeArray = longitudeLatitudeArray;
        }

        public String getPointAddr() {
            return pointAddr;
        }

        public void setPointAddr(String pointAddr) {
            this.pointAddr = pointAddr;
        }

        public String getPointName() {
            return pointName;
        }

        public void setPointName(String pointName) {
            this.pointName = pointName;
        }

        public String getPointRemark() {
            return pointRemark;
        }

        public void setPointRemark(String pointRemark) {
            this.pointRemark = pointRemark;
        }

        public String getPointType() {
            return pointType;
        }

        public void setPointType(String pointType) {
            this.pointType = pointType;
        }

        public String getResourcesId() {
            return resourcesId;
        }

        public void setResourcesId(String resourcesId) {
            this.resourcesId = resourcesId;
        }
    }
}
