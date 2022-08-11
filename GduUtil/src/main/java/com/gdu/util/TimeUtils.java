package com.gdu.util;

import com.gdu.baselibrary.utils.CommonUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 *
 */
public class TimeUtils {
    /**
     * 格式为 yyyy-MM-dd HH:mm:ss 的SimpleDateFormat
     */
    public static SimpleDateFormat sdf_ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
            Locale.getDefault());


    /**
     * 根据时间字符串获取时间戳
     * @param time
     * @param format
     * @return
     */
    public static long getTimeStamp(String time, String format){
        if (CommonUtils.isEmptyString(time)) {
            return 0;
        }
        SimpleDateFormat sdr = new SimpleDateFormat(format, Locale.CHINA);
        Date date = null;
        try {
            date = sdr.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long timeL = date.getTime();
        return timeL;
    }

    /**
     * 根据时间字符串获取时间戳
     * @param time
     * @param format
     * @param newFormat
     * @return
     */
    public static String convertTimeByFormat(String time, String format, String newFormat){
        if (CommonUtils.isEmptyString(time)) {
            return "";
        }
        final SimpleDateFormat sdr = new SimpleDateFormat(format, Locale.CHINA);
        final SimpleDateFormat newSdf = new SimpleDateFormat(newFormat, Locale.CHINA);
        String timeStr = "";
        Date date = null;
        try {
            date = sdr.parse(time);
            timeStr = newSdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStr;
    }

    /**
     * <p>获取当前时间的string</p>
     * <p>时间戳</p>
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 获取当前时间不带秒
     */
    public static String getCurrentTimeNoSecond() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 获取当前年份
     */
    public static String getCurrentYear() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * <p>获取时间的string</p>
     * <p>时间戳</p>
     *
     * @return
     */
    public static String getTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(time);//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * <p>获取时间的string</p>
     * <p>时间戳</p>
     *
     * @return
     */
    public static String getTime(long time, String  format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date curDate = new Date(time);//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    public static int[] getDateByTimeStamp(long timeStamp) {
        int[] timeArray = new int[7];
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        timeArray[0] = calendar.get(Calendar.YEAR);
        timeArray[1] = calendar.get(Calendar.MONTH);
        timeArray[2] = calendar.get(Calendar.DAY_OF_MONTH);
        timeArray[3] = calendar.get(Calendar.HOUR_OF_DAY);
        timeArray[4] = calendar.get(Calendar.MINUTE);
        timeArray[5] = calendar.get(Calendar.SECOND);
        timeArray[6] = calendar.get(Calendar.MILLISECOND);
        return timeArray;
    }


    public static String getDetailTime(long time){
        long currentTime = System.currentTimeMillis();
        long period = (currentTime - time) / 1000;
        String detailTime;
        if(period < 60){
            detailTime = period + "s前";
        } else if(period < 60 * 60){
            detailTime = (period / 60) + "分钟前";
        } else if(period < 60 * 60 * 24){
            detailTime = (period / (60*24)) + "小时前";
        } else {
            detailTime = getTime(time);
        }
        return detailTime;
    }
}
