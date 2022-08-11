package com.gdu.utils;

import com.gdu.model.config.UrlConfig;

public class ToolManager {

    /**
     * <P>shang</P>
     * <P>切换APP 测试和正式的环境</P>
     * <P>debug 环境是 true 。正式环境是 false </P>
     */
    public static void SwitchAppEnvironment(boolean isTest) {
        if (isTest) {
            UrlConfig.BASE_IP = UrlConfig.TEST_IP;
            UrlConfig.MQTT_IP = UrlConfig.MQTT_TEST_IP;
        } else {
            UrlConfig.BASE_IP = UrlConfig.PRODUCTION_IP;
            UrlConfig.MQTT_IP = UrlConfig.MQTT_PRODUCTION_IP;
        }
    }
}
