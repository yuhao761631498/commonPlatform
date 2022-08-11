package com.gdu.command.ui.home;

import java.util.List;

/**
 * Copyright (C), 2020-2030, 普宙飞行器科技有限公司
 * <p>
 * 功能描述:
 * <p>
 * 创建时间: 2022/5/6 11:41
 *
 * @author yuhao
 */
public interface IHomeView1 {


    void alarmTypeForDay(List<AlarmTypeBean.TypeBean> data);


    void alarmDealResult(List<AlarmDealBean.DealResultBean> data);
}
