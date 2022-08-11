package com.gdu.utils;


/**
 * Created by yuhao on 2017/3/20.
 */
public class WeatherQueryHelper {

    private static int weatherId;

    /**
     * 获取天气名称
     * @param stateCode
     * @return
     */
    public static String getWeatherName(int stateCode){
        String weather = "";
        String codeStr = stateCode + "";
        if (codeStr.startsWith("7")) {
            stateCode = 700;
        } else if (codeStr.startsWith("3")) {
            stateCode = 300;
        } else if (codeStr.startsWith("5")) {
            stateCode = 500;
        } else if (codeStr.startsWith("2")) {
            stateCode = 200;
        } else if (codeStr.startsWith("6")) {
            stateCode = 600;
        } else if (codeStr.startsWith("90")) {
            stateCode = 900;
        }
        switch (stateCode) {
            case 800:
                weather = "晴天";
                break;

            case 801:
            case 803:
                weather = "多云";
                break;

            case 802:
                weather = "多云转晴";
                break;

            case 804:
                weather = "阴天";
                break;

            case 700:
                weather = "雾霾";
                break;

            case 300:
                weather = "小雨";
                break;

            case 500:
                weather = "暴雨";
                break;

            case 200:
            case 900:
                weather = "雷暴";
                break;

            case 600:
                weather = "暴雪";
                break;

            default:
                weather = "";
                break;
        }
        return weather;
    }

    public static int getWeather(int stateCode) {
        String codeStr = stateCode + "";
        if (codeStr.startsWith("7")) {
            stateCode = 700;
        } else if (codeStr.startsWith("3")) {
            stateCode = 300;
        } else if (codeStr.startsWith("5")) {
            stateCode = 500;
        } else if (codeStr.startsWith("2")) {
            stateCode = 200;
        } else if (codeStr.startsWith("6")) {
            stateCode = 600;
        } else if (codeStr.startsWith("90")) {
            stateCode = 900;
        }
//        switch (stateCode) {
//            case 800:
//                weatherId = R.drawable.icons_qingtian_weather;
//                break;
//
//            case 801:
//                weatherId = R.drawable.icons_duoyun_weather;
//                break;
//
//            case 802:
//                weatherId = R.drawable.icons_duoyunzhuanqing_weather;
//                break;
//
//            case 803:
//                weatherId = R.drawable.icons_duoyun_weather;
//                break;
//
//            case 804:
//                weatherId = R.drawable.icons_yintian_weather;
//                break;
//
//            case 700:
//                weatherId = R.drawable.icons_wumai_weather;
//                break;
//
//            case 300:
//                weatherId = R.drawable.icons_rain_weather;
//                break;
//
//            case 500:
//                weatherId = R.drawable.icons_baoyu_weather;
//                break;
//
//            case 200:
//                weatherId = R.drawable.icons_leibao_weather;
//                break;
//
//            case 600:
//                weatherId = R.drawable.icons_baoxue_weather;
//                break;
//
//            case 900:
//                weatherId = R.drawable.icons_leibao_weather;
//                break;
//            default:
//                weatherId = R.drawable.icons_leibao_weather;
//                break;
//        }
        return weatherId;
    }

}
