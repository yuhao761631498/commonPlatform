package com.gdu.command.ui.alarm.center;


import com.gdu.baselibrary.network.AlarmTypeStatictisBean;
import com.gdu.command.views.drop.AlarmDeviceBean;
import com.gdu.model.alarm.AlarmInfo;

import java.util.List;
import java.util.Map;

public interface IAlarmCenterView {

    void showAlarmList(List<AlarmInfo> alarmInfos);

    void showDeviceList(List<AlarmDeviceBean.DataBean.RowsBean> devList);

    void attentionResult(int alarmId);

    void showAlarmTypeStatisticsData(List<AlarmTypeStatictisBean.DataDTO> data);

    void callbackIsAppMenu(boolean isAppMenu);
}
