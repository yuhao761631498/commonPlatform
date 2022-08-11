package com.gdu.command.ui.resource;

/**
 * 资源分布marker显示隐藏状态监听接口
 */
public interface OnCheckBoxChangeListener {
    void onOrganizationChanged(boolean isChecked);
    void onPersonChanged(boolean isChecked);
    void onAirplaneChanged(boolean isChecked);
    void onHighPointChanged(boolean isChecked);
    void onRegionChanged(boolean isChecked);
    void onCircuitChanged(boolean isChecked);
    void onFocusChanged(boolean isChecked);
    void onCaseInfoChanged(boolean isChecked);
}
