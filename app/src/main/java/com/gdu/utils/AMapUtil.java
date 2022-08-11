package com.gdu.utils;

import android.content.Context;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;

import java.util.List;

public class AMapUtil {
    private AMapUtil() {
    }

    /**
     * 把LatLng对象转化为LatLonPoint对象
     */
    public static LatLonPoint convertToLatLonPoint(LatLng latlon) {
        return new LatLonPoint(latlon.latitude, latlon.longitude);
    }

    /**
     * 把LatLonPoint对象转化为LatLon对象
     */
    public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

    /**
     * 获取当前经纬度的具体位置
     * @param context
     * @param latLonPoint
     * @return
     */
    public static RegeocodeAddress getCurrentLocationDetails(Context context, LatLonPoint latLonPoint){
        RegeocodeAddress regeocodeAddress = null;
        try {
            // 地址逆解析
            GeocodeSearch geocoderSearch = new GeocodeSearch(context);
            // 第一个参数表示一个Latlng(经纬度)，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
            RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 25, GeocodeSearch.AMAP);
            regeocodeAddress = geocoderSearch.getFromLocation(query);
        } catch (AMapException e) {
            e.printStackTrace();
        }
        return regeocodeAddress;
    }

    /**
     * 获取当前经纬度的具体位置
     * @param context
     * @param latLonPoint
     * @return
     */
    public static void getCurrentLocationDetailsNew(Context context, LatLonPoint latLonPoint,
                                                    GeocodeSearch.OnGeocodeSearchListener listener){
        try {
            // 地址逆解析
            final GeocodeSearch geocoderSearch = new GeocodeSearch(context);
            geocoderSearch.setOnGeocodeSearchListener(listener);
            // 第一个参数表示一个Latlng(经纬度)，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
            final RegeocodeQuery mRegeocodeQuery = new RegeocodeQuery(latLonPoint, 100, "");
            geocoderSearch.getFromLocationAsyn(mRegeocodeQuery);
        } catch (AMapException e) {
            e.printStackTrace();
        }
    }

    public static float getPatrolDistance(List<LatLng> points) {
        int startIndex = 0;
        int secondIndex = 1;
        float distance = 0;
        for (int i = 0; i < points.size(); i++) {
            if (secondIndex <= points.size() - 1) {
                distance += AMapUtils.calculateLineDistance(points.get(startIndex),
                        points.get(secondIndex));
                startIndex++;
                secondIndex++;
            }
        }
        return distance;
    }
}
