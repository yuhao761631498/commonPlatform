package com.gdu.command.ui.data;

import com.gdu.model.config.UrlConfig;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Copyright (C), 2020-2030, 普宙飞行器科技有限公司
 * <p>
 * 功能描述:
 * <p>
 * 创建时间: 2022/5/7 17:16
 *
 * @author yuhao
 */
public interface ForeWarnService {


    /**
     * 告警事件类型分布，按告警等级、告警类型进行统计
     * @return
     */
    @GET(UrlConfig.getAlarmResInfo)
    Observable<AlarmResInfoBean> getAlarmResInfo();


    /**
     * 告警设备排名
     * @return
     */
    @GET(UrlConfig.deviceRank)
    Observable<DeviceRankBean> getRankInfo();


    /**
     * 出发结果
     * @return
     */
    @GET(UrlConfig.dealResult)
    Observable<DealResultBean> getDealResult(@Query("startTime") String startTime,@Query("endTime") String endTime);
}
