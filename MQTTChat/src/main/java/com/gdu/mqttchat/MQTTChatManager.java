package com.gdu.mqttchat;

import android.content.Context;

/**
 * MQTT聊天管理类
 */
public class MQTTChatManager {

    public MQTTChatManager(Context context){
        init(context);
    }

    private void init(Context context) {
        MqttAppState.getInstance();
        MqttAppState.setApplication(context);
    }


    public void onDestroy(){
        MqttAppState.getInstance().onTerminate();
    }
}
