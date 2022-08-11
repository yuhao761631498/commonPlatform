package com.gdu.utils;

/**
 * 防止快速点击工具类
 * create by zyf
 */
public class FastClickUtils {
    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime = 0;
    private static int lastViewId = -1;

    /**
     * 默认间隔时间为1000毫秒
     * @param viewId 点击控件的id
     * @return
     */
    public static boolean isFastClick(int viewId) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (lastViewId == viewId && lastClickTime > 0 && timeD < MIN_CLICK_DELAY_TIME) {
            return true;
        }
        lastClickTime = time;
        lastViewId = viewId;
        return false;
    }

    /**
     * 传入连续点击间隔时间
     * @param viewId 点击的id
     * @param diff 单位：毫秒
     * @return
     */
    public static boolean isFastClick(int viewId, long diff) {
        long time = System.currentTimeMillis();
        long timeD = lastClickTime;
        if (lastViewId == viewId && lastClickTime > 0 && timeD < diff) {
            return true;
        }
        lastClickTime = time;
        lastViewId = viewId;
        return false;
    }
}
