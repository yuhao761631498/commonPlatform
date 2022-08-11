package com.gdu.client.base;

import com.gdu.model.config.UrlConfig;

import java.io.Serializable;

/**
 * Created by zhangzhilai on 2017/11/15.
 */

public class Action {
    public final static    int  STATUS_SUCCESS          = 0;
    public final static    int  STATUS_FAILED           = -1;
    public final static    int  STATUS_REQUIRE_LOGIN    = -2;
    public final static    int  STATUS_FORBIDDEN        = -3;
    public final static    int  STATUS_NET_ERROR        = -100;
    public final static    int  STATUS_IO_ERROR         = -101;
    public final static    int  STATUS_ENCODE_ERROR     = -102;
    public final static    int  STATUS_RESPONSE_FAILED  = -103;
    public final static    int  STATUS_CONNECT_TIME_OUT = -104;
    public final static    int  STATUS_SOCKET_TIME_OUT  = -105;
    public final static    int  STATUS_PARSE_ERROR  = -106;
    public final static    int  STATUS_PROTOCOL_ERROR  = -10;

    public static final int CONNECT_TIMEOUT = 5000; // ms
    public static final int READ_TIMEOUT = 5000; // ms

//    public static volatile String BASE_URL = "http://120.24.12.64:8088";//测试
    public static volatile String BASE_URL = UrlConfig.BASE_IP + ":" + UrlConfig.BASE_PORT;//shang-20170904-正
    public static volatile String AP_STA_URL = "http://192.168.1.123"; //直连飞机和通过遥控器连接飞机的url，供5.8使用
    public static volatile int AP_STA_PORT = 8001; //直连飞机和通过遥控器连接飞机的port，供5.8使用



    private int error;
    private Serializable data;

    public Action(){

    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public Serializable getData() {
        return data;
    }

    public void setData(Serializable data) {
        this.data = data;
    }

}
