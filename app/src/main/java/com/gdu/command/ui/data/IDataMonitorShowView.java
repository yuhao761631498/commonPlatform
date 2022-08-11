package com.gdu.command.ui.data;

import com.gdu.model.data.DataAlarmTotalBean;
import com.gdu.model.data.DataCaseTotalBean;
import com.gdu.model.data.DataHighTotal;

import java.util.List;

public interface IDataMonitorShowView {
    /** 获取案件总数统计*/
    void getCaseTotalCount(DataCaseTotalBean bean);
    /** 获取告警总数*/
    void getAlarmTotalCount(DataAlarmTotalBean bean);
    /** 获取高点监控总数*/
    void getHighTotalCount(DataHighTotal bean);
    /** 获取今年案件来源分布*/
    void getCaseFromByYear(List<DataCaseResFromBean.CaseInfoSourceCountVOSBean> bean);
    /** 获取案件处置排名*/
    void getCaseDealRank(List<DataCaseRankBean.DataBean> bean);
    /** 告警事件类型分布，按告警等级、告警类型进行统计*/
    void getAlarmResInfo(AlarmResInfoBean.DataBean bean);
    /** 告警数据时间分布趋势*/
    void getAlarmByYearDistribute(List<DataAlarmByYearBean.DataBean> bean);

}
