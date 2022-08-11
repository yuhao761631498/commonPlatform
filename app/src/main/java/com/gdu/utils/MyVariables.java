package com.gdu.utils;

import com.amap.api.maps2d.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MyVariables {
    /** 记录底部弹窗item选项. */
    public static List<String> SELECTITEMS = new ArrayList<>();
    /** 记录当前定位的城市. */
    public static String CUR_LOCATION_CITY = "";
    /** 缓存当前坐标. */
    public static LatLng curCoordinate;
    /** 预警中心是否只看关注选项(默认true). */
    public static boolean isLockAtAttention = false;
    /** 水印内容. */
    public static String waterContentStr = "哼哈哈哈";
    /** 用户个人信息 */
    public static String UPDATE_PERSON_INFO = "person_info";
    /** 标题名称 */
    public static String UPDATE_TITLE_NAME = "update_title_name";
    /** 组织机构人员信息实体类 */
    public static String ORG_EMPLOYEE_BEAN = "org_employee_bean";
    /** 修改个人信息跳转*/
    public static String UPDATE_INFO_RESULT_CODE = "update_info_result_code";

    public static int UPDATE_USER_NAME_RESULT_CODE = 201;

    public static int UPDATE_ID_CARD_RESULT_CODE = 203;

    public static int UPDATE_OFFICE_PHONE_RESULT_CODE = 204;

    public static int UPDATE_PHONE_NO_RESULT_CODE = 205;

    public static int UPDATE_USER_SEX_RESULT_CODE = 206;

    public static String CURRENT_LOCATION = "current_location";

    public static String CURRENT_WEATHER = "current_weather";

}
