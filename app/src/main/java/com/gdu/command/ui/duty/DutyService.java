package com.gdu.command.ui.duty;

import com.gdu.model.config.UrlConfig;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DutyService {

    /**
     * 获取某天的值班数据
     */
    @GET(UrlConfig.dutyForDay)
    Call<DutyForDayBean> getDutyForDay(@Query("regionCode") String regionCode, @Query("date") String date);

    /**
     * 获取当月值班信息
     */
    @POST(UrlConfig.dutyForMe)
    @Headers({"Content-type:application/json;charset=UTF-8"})
    Call<DutyForMeBean> getDutyForMe(@Body RequestBody body);
}
