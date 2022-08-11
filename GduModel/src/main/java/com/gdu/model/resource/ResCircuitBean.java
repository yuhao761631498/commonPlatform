package com.gdu.model.resource;

import java.io.Serializable;
import java.util.List;

/**
 * 线资源列表
 */
public class ResCircuitBean implements Serializable {

    private String id;
    private String resourcesName;
    private String resourcesType;
    private String resourcesTypeName;
    private double totalLength;
    private String lineOrigin;
    private String lineDestination;
    private String longitudeLatitudeArray;
    private List<LonAndLatListDTO> lonAndLatList;
    private int lineWidth;
    private String lineColor;
    private RgbaDTODTO lineRgbaDTO;
    private List<PointControlScopeVOSDTO> pointControlScopeVOS;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public double getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(double totalLength) {
        this.totalLength = totalLength;
    }

    public String getLineOrigin() {
        return lineOrigin;
    }

    public void setLineOrigin(String lineOrigin) {
        this.lineOrigin = lineOrigin;
    }

    public String getLineDestination() {
        return lineDestination;
    }

    public void setLineDestination(String lineDestination) {
        this.lineDestination = lineDestination;
    }

    public String getLongitudeLatitudeArray() {
        return longitudeLatitudeArray;
    }

    public void setLongitudeLatitudeArray(String longitudeLatitudeArray) {
        this.longitudeLatitudeArray = longitudeLatitudeArray;
    }

    public List<LonAndLatListDTO> getLonAndLatList() {
        return lonAndLatList;
    }

    public void setLonAndLatList(List<LonAndLatListDTO> lonAndLatList) {
        this.lonAndLatList = lonAndLatList;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public String getLineColor() {
        return lineColor;
    }

    public void setLineColor(String lineColor) {
        this.lineColor = lineColor;
    }

    public RgbaDTODTO getLineRgbaDTO() {
        return lineRgbaDTO;
    }

    public void setLineRgbaDTO(RgbaDTODTO lineRgbaDTO) {
        this.lineRgbaDTO = lineRgbaDTO;
    }

    public List<PointControlScopeVOSDTO> getPointControlScopeVOS() {
        return pointControlScopeVOS;
    }

    public void setPointControlScopeVOS(List<PointControlScopeVOSDTO> pointControlScopeVOS) {
        this.pointControlScopeVOS = pointControlScopeVOS;
    }

    public static class RgbaDTODTO implements Serializable {
        private String red;
        private String green;
        private String blue;
        private String alpha;

        public String getRed() {
            return red;
        }

        public void setRed(String red) {
            this.red = red;
        }

        public String getGreen() {
            return green;
        }

        public void setGreen(String green) {
            this.green = green;
        }

        public String getBlue() {
            return blue;
        }

        public void setBlue(String blue) {
            this.blue = blue;
        }

        public String getAlpha() {
            return alpha;
        }

        public void setAlpha(String alpha) {
            this.alpha = alpha;
        }
    }

    public static class LonAndLatListDTO implements Serializable  {
        private double lon;
        private double lat;

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }
    }

    public static class PointControlScopeVOSDTO implements Serializable  {
        private String id;
        private String pointName;
        private String belongOrg;
        private Object belongArea;
        private double longitude;
        private double latitude;
        private String iconUrl;
        private Object pointSort;
        private Object pointAddr;
        private Object pointRemark;
        private String resourcesId;
        private String longitudeLatitudeArray;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPointName() {
            return pointName;
        }

        public void setPointName(String pointName) {
            this.pointName = pointName;
        }

        public String getBelongOrg() {
            return belongOrg;
        }

        public void setBelongOrg(String belongOrg) {
            this.belongOrg = belongOrg;
        }

        public Object getBelongArea() {
            return belongArea;
        }

        public void setBelongArea(Object belongArea) {
            this.belongArea = belongArea;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public Object getPointSort() {
            return pointSort;
        }

        public void setPointSort(Object pointSort) {
            this.pointSort = pointSort;
        }

        public Object getPointAddr() {
            return pointAddr;
        }

        public void setPointAddr(Object pointAddr) {
            this.pointAddr = pointAddr;
        }

        public Object getPointRemark() {
            return pointRemark;
        }

        public void setPointRemark(Object pointRemark) {
            this.pointRemark = pointRemark;
        }

        public String getResourcesId() {
            return resourcesId;
        }

        public void setResourcesId(String resourcesId) {
            this.resourcesId = resourcesId;
        }

        public String getLongitudeLatitudeArray() {
            return longitudeLatitudeArray;
        }

        public void setLongitudeLatitudeArray(String longitudeLatitudeArray) {
            this.longitudeLatitudeArray = longitudeLatitudeArray;
        }
    }
}
