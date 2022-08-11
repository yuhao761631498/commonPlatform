package com.gdu.command.ui.resource;

public class ResourceConfig {

    /**组织机构marker是否选中*/
    public static boolean isOrganizationChecked = true;
    /**组织机构marker是否选中*/
    public static boolean isPersonChecked = true;
    /**组织机构marker是否选中*/
    public static boolean isAirplaneChecked = true;
    /**组织机构marker是否选中*/
    public static boolean isMonitorChecked = true;
    /**组织机构marker是否选中*/
    public static boolean isRegionChecked = true;
    /**组织机构marker是否选中*/
    public static boolean isCircuitChecked = true;
    /**组织机构marker是否选中*/
    public static boolean isCaseChecked = true;
    /**组织机构marker是否选中*/
    public static boolean isFocusChecked = true;

    /** 重置选中状态 **/
    public static void resetCheckState() {
        isOrganizationChecked = true;
        isPersonChecked = true;
        isAirplaneChecked = true;
        isMonitorChecked = true;
        isRegionChecked = true;
        isCircuitChecked = true;
        isCaseChecked = true;
        isFocusChecked = true;
    }

}
