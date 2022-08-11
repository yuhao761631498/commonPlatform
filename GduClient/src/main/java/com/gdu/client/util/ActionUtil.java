package com.gdu.client.util;

import com.gdu.client.base.Action;

/**
 * Created by zhangzhilai on 2017/11/24.
 */

public class ActionUtil {

    public static void changeEnvironment(boolean isTest){
//        System.out.println("test isTest: " + isTest);
        if (isTest) {
            Action.BASE_URL = "http://120.24.12.64:8088";//测试
        } else {
            Action.BASE_URL = "http://api.prodrone-tech.com";//shang-20170904-正式
        }
    }

    public static void changeApOrStaURL(boolean isAp){
        if (isAp) {
            Action.AP_STA_URL = "http://192.168.11.123";  //直连飞机获取飞机的wifi相关信息
        } else {
            Action.AP_STA_URL = "http://192.168.100.1";   //连接遥控器获取遥控周边wifi（8002）和飞机wifi信息（8001），
        }
    }

    public static void setUrlAndPort(String url, int port){
        Action.AP_STA_URL = "http://" + url;
        Action.AP_STA_PORT = port;
    }


}
