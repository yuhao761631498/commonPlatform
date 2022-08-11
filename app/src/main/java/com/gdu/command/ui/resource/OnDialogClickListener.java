package com.gdu.command.ui.resource;

public interface OnDialogClickListener {
    //重点点位到这里去
    void onEmphasisPointGuideListener(double latitude, double longitude, String address);
    //高点监控视频点击
    void onMonitorVideoListener();
    //高点监控云台操作
    void onMonitorDispatchListener();
    //组织机构到这里去
    void onOrganizationGuideListener(double latitude, double longitude, String address);
    //案件信息到这里去
    void onCaseInfoGuideListener(double latitude, double longitude, String address);
}
