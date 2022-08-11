package com.gdu.command.ui.resource;

import java.io.Serializable;

/**
 * 坐标转化实体类
 */
public class LocateInfo implements Serializable {
    private double longitude;
    private double Latitude;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }
}
