package com.gdu.command.ui.home;

import com.gdu.baselibrary.network.BaseBean;
import com.gdu.model.config.UrlConfig;

import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Copyright (C), 2020-2030, 普宙飞行器科技有限公司
 * <p>
 * 功能描述:
 * <p>
 * 创建时间: 2022/5/6 11:20
 *
 * @author yuhao
 */
public interface HomeService {

    /**
     * 获取某天告警类型统计
     */
    @GET(UrlConfig.getAlarmTypeForDay)
    Observable<AlarmTypeBean> getAlarmTypeForDay(@Query("thisDay") String thisDay);

    /**
     * 获取某天告警类型统计
     */
    @GET(UrlConfig.getAlarmDealForDay)
    Observable<AlarmDealBean> getAlarmDealForDay(@Query("thisDay") String thisDay);

}
