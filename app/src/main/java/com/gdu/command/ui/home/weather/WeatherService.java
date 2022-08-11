package com.gdu.command.ui.home.weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * 获取天气的Service
 * @author wixche
 */
public interface WeatherService {

    /**
     * 获取天气依据坐标
     * @return
     */
    @GET
    Call<WeatherBeans> getWeatherByLocation(@Url String url,
                                                @Query("lat") String lat,
                                                @Query("lon") String lon,
                                                @Query("appid") String appid);
}
