package com.gdu.baselibrary.utils;

import com.gdu.baselibrary.BaseApplication;

import java.util.Locale;

/**
 * @author by DELL
 * @date on 2017/11/29
 * @describe SharedPreferences 数据保存管理类
 */

public class BaseSPManager {
    public static final String FOLLOW_SYSTEM_LANGUAGE = "follow_system_language";//语言
    public static final String LANGUAGE = "language";//语言
    public static final String LANGUAGE_COUNTRY = "language_country";//语言_国家地区
    public static final String DAY_NIGHT_MODE = "day_night_mode";//日夜模式
    public static final String IS_FIRST_LAUNCH = "is_first_launch";//是否第一次启动
    public static final String VERSION_CODE = "version_code";//版本号


    /*** 是否跟随系统 */
    public static boolean isFollowSystemLanguage() {
        return (boolean) MMKVUtil.getBoolean(FOLLOW_SYSTEM_LANGUAGE, true);
    }

    public static void setFollowSystemLanguage(boolean isFollow) {
        MMKVUtil.putBoolean(FOLLOW_SYSTEM_LANGUAGE, isFollow);
    }

    /***  语言 默认系统语言*/
    public static Locale getLanguage() {
        boolean isSystem = isFollowSystemLanguage();
        if (isSystem) {
            return Locale.getDefault();
        }
        //默认简体中文
        String language = (String) MMKVUtil.getString(LANGUAGE, Locale.getDefault().getLanguage());
        //默认简体中文
        String country = (String) MMKVUtil.getString(LANGUAGE_COUNTRY, Locale.getDefault().getCountry());
        return new Locale(language, country);
    }


    public static void setLanguage(Locale language) {
        MMKVUtil.putString(LANGUAGE, language.getLanguage());
        MMKVUtil.putString(LANGUAGE_COUNTRY, language.getCountry());
    }


    /*** 日夜模式 */
    public static boolean isNightMode() {
        return (boolean) MMKVUtil.getBoolean(DAY_NIGHT_MODE, false);
    }

    public static void setNightMode(boolean nightMode) {
        MMKVUtil.putBoolean(DAY_NIGHT_MODE, nightMode);
    }

    /*** 是否第一次启动 */
    public static boolean isFirstLaunch() {
        return (boolean) MMKVUtil.getBoolean(IS_FIRST_LAUNCH, true);
    }

    public static void setIsFirstLaunch(boolean isFirstLunch) {
        MMKVUtil.putBoolean(IS_FIRST_LAUNCH, isFirstLunch);
    }
}
