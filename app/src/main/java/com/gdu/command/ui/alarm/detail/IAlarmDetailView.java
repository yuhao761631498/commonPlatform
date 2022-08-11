package com.gdu.command.ui.alarm.detail;


import com.gdu.command.ui.alarm.dialog.AlarmDispatchedBean;
import com.gdu.model.organization.OrganizationInfo;

import java.util.List;
import java.util.Map;

public interface IAlarmDetailView {

    void showAlarmRemarkList(List<Map<String, String>> alarmRemarks);

    void showAlarmStatus(int status);

    void remarkCallback(int status);

    /**
     * 返回预警详情信息
     * @param bean
     */
    void showDetailInfo(AlarmDetailBean.DataBean bean);

    /**
     * 返回组织架构信息
     * @param organizationList
     */
    void callBackOrganizationList(boolean isSuc, List<OrganizationInfo> organizationList);

    /**
     * 预警处理结果
     * @param isSuc
     */
    void caseHandleResult(boolean isSuc);

    /**
     * 预警补充处理结果
     * @param isSuc
     */
    void supplementHandleResult(boolean isSuc);

    /**
     * 获取被分派列表结果
     * @param isSuc
     */
    void getDispatchedListResult(boolean isSuc, List<AlarmDispatchedBean.DataBean> data);

    /**
     * 分派结果
     * @param isSuc
     */
    void dispatchedResult(boolean isSuc);

    /**
     * 接警结果
     * @param isSuc
     */
    void answerPoliceResult(boolean isSuc);

}
