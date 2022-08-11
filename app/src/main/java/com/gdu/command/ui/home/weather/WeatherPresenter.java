package com.gdu.command.ui.home.weather;

import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.model.config.StorageConfig;
import com.gdu.model.config.UrlConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yuhao on 2017/3/21.
 */
public class WeatherPresenter {

    public WeatherPresenter(){
    }

    /**
     * 获取天气信息
     */
    public void getWeatherInfo(OnWeatherListener onWeatherListener) {
        WeatherService mWeatherService = RetrofitClient.getAPIService(WeatherService.class);
        if (StorageConfig.sGPSLat == 0 || StorageConfig.sGPSLng == 0) {
            return;
        }
        mWeatherService.getWeatherByLocation(UrlConfig.Weather_map, StorageConfig.sGPSLat + "",
                StorageConfig.sGPSLng + "", "d1dfc4be5c774b4f2ad89fb19bca0361")
                .enqueue(new Callback<WeatherBeans>() {
            @Override
            public void onResponse(Call<WeatherBeans> call, Response<WeatherBeans> response) {
                final WeatherBeans weatherBeans = response.body();
                if (weatherBeans == null) {
                    return;
                }
                onWeatherListener.onOnWeatherGot(weatherBeans);
            }

            @Override
            public void onFailure(Call<WeatherBeans> call, Throwable t) {

            }
        });
    }

    public interface OnWeatherListener{
        void onOnWeatherGot(WeatherBeans weatherBeans);
    }

}
