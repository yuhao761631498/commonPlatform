package com.gdu.app;

import android.content.Context;

import com.gdu.baselibrary.utils.MMKVUtil;
import com.gdu.model.config.StorageConfig;
import com.gdu.model.config.UrlConfig;

public class GduCPInit {

    private static Context mContext;

    public static void init(Context context){
        mContext = context;
        initUrl();
    }

    private static void initUrl() {
      boolean  isTest = MMKVUtil.getBoolean(StorageConfig.TestServerMode, false);
        if (isTest) {
            UrlConfig.BASE_IP = UrlConfig.TEST_IP;
            UrlConfig.MQTT_IP = UrlConfig.MQTT_TEST_IP;
        } else {
            UrlConfig.BASE_IP = UrlConfig.PRODUCTION_IP;
            UrlConfig.MQTT_IP = UrlConfig.MQTT_PRODUCTION_IP;
        }
        UrlConfig.HttpCP = UrlConfig.BASE_IP  + ":" + UrlConfig.BASE_PORT;
        UrlConfig.ImgIp = UrlConfig.BASE_IP  + ":" + UrlConfig.IMG_PORT;
    }

}
