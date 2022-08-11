package com.gdu.command.ui.data;

import java.util.List;

/**
 * Copyright (C), 2020-2030, 普宙飞行器科技有限公司
 * <p>
 * 功能描述:
 * <p>
 * 创建时间: 2022/5/7 17:05
 *
 * @author yuhao
 */
public interface IForeWarnView {

    /**
     * 告警事件类型分布，按告警等级、告警类型进行统计
     */
    void getAlarmResInfo(AlarmResInfoBean.DataBean bean);


    /**
     * 处理结果统计
     */
    void getDealResultInfo(List<DealResultBean.DealItemBean> beans);

    void getDeviceRankInfo(List<DeviceRankBean.DeviceItemBean> beans);
}
