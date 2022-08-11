package com.gdu.command.ui.data;

import com.gdu.baselibrary.network.BaseBean;
import com.gdu.model.config.UrlConfig;
import com.gdu.model.data.DataAlarmTotalBean;
import com.gdu.model.data.DataCaseTotalBean;
import com.gdu.model.data.DataHighTotal;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * 数据监测接口请求
 */
public interface DataMonitorService {

    /**
     * 获取案件总数
     * @return
     */
    @GET(UrlConfig.getDataCaseTotal)
    Observable<BaseBean<DataCaseTotalBean>> getDataCaseTotal();

    /**
     * 获取告警总数
     * @return
     */
    @GET(UrlConfig.getDataAlarmTotal)
    Observable<DataAlarmTotalBean> getDataAlarmTotal();

    /**
     * 获取高点监控总数
     * @return
     */
    @GET(UrlConfig.getDataHighTotal)
    Observable<DataHighTotal> getDataHighTotal();

    /**
     * 获取今年案件来源分布
     * @return
     */
    @GET(UrlConfig.getCaseResourceFrom)
    Observable<BaseBean<DataCaseResFromBean>> getCaseResFrom();

    /**
     * 获取案件处置排名
     * @return
     */
    @GET(UrlConfig.getCaseRankProgress)
    Observable<DataCaseRankBean> getDataCaseRank();

    /**
     * 告警事件类型分布，按告警等级、告警类型进行统计
     * @return
     */
    @GET(UrlConfig.getAlarmResInfo)
    Observable<AlarmResInfoBean> getAlarmResInfo();

    /**
     * 告警数据时间分布趋势
     * @return
     */
    @GET(UrlConfig.getAlarmTimeByYear)
    Observable<DataAlarmByYearBean> getAlarmByYearDistribute();

}
