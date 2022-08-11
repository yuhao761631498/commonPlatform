package com.gdu.mqttchat.mqtt;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.baselibrary.utils.schedule.ScheduleTool;
import com.gdu.model.config.UrlConfig;
import com.gdu.mqttchat.BuildConfig;
import com.gdu.mqttchat.MqttAppState;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * Created by fc on 2018/11/13.
 */

public class MqttConfig {

    private String TAG = "mqtt";
    /**
     * MQTT配置参数
     **/
    private static String host = UrlConfig.MQTT_IP;
    private static String port = UrlConfig.MQTT_PORT;
    private static String userID = "admin";
    private static String passWord = "admin";
    private static String clientID = MqttAppState.getInstance().getIMEI();
    private int mqttRetryCount = 0;

    /**
     * MQTT状态信息
     **/
    private boolean isConnect = false;

    /**
     * MQTT支持类
     **/
    private MqttAndroidClient mqttClient;

    private MqttListener mMqttListener;

    private Context mContext;

    private ExecutorService mExecutorService;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
//            MyLogUtils.d("handleMessage() msgWhat = " + message.what);
            switch (message.arg1) {
                case MqttTag.MQTT_STATE_CONNECTED:
                    if (BuildConfig.DEBUG) {
                        MyLogUtils.d("handleMessage: connected");
                    }
                    mMqttListener.onConnected();
                    mqttRetryCount = 0;
                    break;
                case MqttTag.MQTT_STATE_FAIL:
                    if (BuildConfig.DEBUG) {
                        MyLogUtils.d("handleMessage: fail");
                    }
                    mMqttListener.onFail();
                    break;
                case MqttTag.MQTT_STATE_LOST:
                    if (BuildConfig.DEBUG) {
                        MyLogUtils.d("handleMessage: lost");
                    }
                    mMqttListener.onLost();
                    break;
                case MqttTag.MQTT_STATE_RECEIVE:
                    if (BuildConfig.DEBUG) {
//                        MyLogUtils.d("handleMessage: receive");
                    }
                    MqttObject object = (MqttObject) message.obj;
                    mMqttListener.onReceive(object.getTopic(), object.getMessage());
                    break;
                case MqttTag.MQTT_STATE_SEND_SUCC:
                    if (BuildConfig.DEBUG) {
                        MyLogUtils.d("handleMessage: send");
                    }
                    mMqttListener.onSendSucc();
                    break;

                default:
                    break;
            }
            return true;
        }
    });

    /**
     * 自带的监听类，判断Mqtt活动变化
     */
    private IMqttActionListener mIMqttActionListener = new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            MyLogUtils.d("mIMqttActionListener onSuccess()");
            isConnect = true;
            Message msg = new Message();
            msg.arg1 = MqttTag.MQTT_STATE_CONNECTED;
            mHandler.sendMessage(msg);
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            MyLogUtils.d("mIMqttActionListener onFailure()");
            isConnect = false;
            Message msg = new Message();
            msg.arg1 = MqttTag.MQTT_STATE_FAIL;
            mHandler.sendMessage(msg);
        }
    };

    /**
     * 自带的监听回传类
     */
    private MqttCallback mMqttCallback = new MqttCallback() {
        @Override
        public void connectionLost(Throwable cause) {
            MyLogUtils.d("mMqttCallback connectionLost()");
            MyLogUtils.e("connectionLost", cause);
            isConnect = false;
            Message msg = new Message();
            msg.arg1 = MqttTag.MQTT_STATE_LOST;
            mHandler.sendMessage(msg);
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
//            MyLogUtils.d("mMqttCallback messageArrived() topic = " + topic + "; message = " + message);
            Message msg = new Message();
            msg.arg1 = MqttTag.MQTT_STATE_RECEIVE;
            String content = new String(message.getPayload());
            MqttObject object = new MqttObject();
            object.setTopic(topic);
            object.setMessage(content);
            msg.obj = object;
            mHandler.sendMessage(msg);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
//            MyLogUtils.d("mMqttCallback deliveryComplete()");
//            Message msg = new Message();
//            msg.arg1 = MqttTag.MQTT_STATE_SEND_SUCC;
//            mHandler.sendMessage(msg);
        }
    };

    public MqttConfig(Context context, MqttListener lis) {
        MyLogUtils.d("MqttConfig()");
        mContext = context;
        mMqttListener = lis;
    }

    public static void setMqttSetting(String host, String port, String userID, String passWord, String clientID) {
        MyLogUtils.d("setMqttSetting()");
        MqttConfig.host = host;
        MqttConfig.port = port;
        MqttConfig.userID = userID;
        MqttConfig.passWord = passWord;
        MqttConfig.clientID = clientID;
    }

    /**
     * 进行Mqtt连接
     */
    public void connectMqtt() {
        MyLogUtils.d("connectMqtt()");
        if (mExecutorService == null) {
            mExecutorService = ScheduleTool.getInstance().getThread();
        }
        if (mExecutorService.isShutdown()) {
            return;
        }
        mExecutorService.execute(() -> {
            try {
//                    String serverUri = "tcp://" + host + ":" + port + "/gduMqtt";
                String serverUri = "tcp://" + host + ":" + port ;
                String clientId = "app_" + System.currentTimeMillis();
                MyLogUtils.d("connectMqtt() serverUri = " + serverUri + "; clientId = " + clientId);
                mqttClient = new MqttAndroidClient(mContext.getApplicationContext(), serverUri, clientId);
                mqttClient.setCallback(mMqttCallback);
                mqttClient.connect(getOptions(), null, mIMqttActionListener);
            } catch (MqttException e) {
                MyLogUtils.e("mqtt server reconnect error!", e);
            }
        });
    }


    /**
     * 断开Mqtt连接重新连接
     */
    public void reStartMqtt() {
        MyLogUtils.d("reStartMqtt()");
        if (mqttRetryCount < 5) {
            disConnectMqtt();
            connectMqtt();
            mqttRetryCount++;
        } else {
            MyLogUtils.d("mqtt server reconnect error!");
        }
    }

    /**
     * 断开Mqtt连接
     */
    public void disConnectMqtt() {
        MyLogUtils.d("disConnectMqtt()");
        try {
            if (mqttClient != null && mqttClient.isConnected()) {
                mqttClient.disconnect();
            }
            mqttClient = null;
            isConnect = false;
        } catch (MqttException e) {
            MyLogUtils.e("断开连接出错", e);
        }
    }

    /**
     * 向Mqtt服务器发送数据
     */
    public void pubMsg(String Topic, String Msg, int Qos) {
//        MyLogUtils.d("pubMsg() Topic = " + Topic + "; Msg = " + Msg + "; Qos = " + Qos);
        if (!isConnect) {
            MyLogUtils.d("Mqtt连接未打开()");
            return;
        }
        if (mqttClient == null) {
            return;
        }
        try {
            /** Topic,Msg,Qos,Retained**/
            mqttClient.publish(Topic, Msg.getBytes(), Qos, false);
        } catch (MqttException e) {
            MyLogUtils.e("发送数据出错", e);
        }
    }

    /**
     * 向Mqtt服务器发送数据
     */
    public void pubMsg(String Topic, byte[] Msg, int Qos) {
        MyLogUtils.d("pubMsg() Topic = " + Topic + "; Msg = " + Msg.toString() + "; Qos = " + Qos);
        if (!isConnect) {
            MyLogUtils.d("Mqtt连接未打开()");
            return;
        }
        try {
            /** Topic,Msg,Qos,Retained**/
            mqttClient.publish(Topic, Msg, Qos, false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向Mqtt服务器订阅某一个Topic
     */
    public void subTopic(String Topic, int Qos) {
        MyLogUtils.d("subTopic() Topic = " + Topic + "; Qos = " + Qos);
        if (!isConnect) {
            MyLogUtils.d("Mqtt连接未打开");
            return;
        }
        if (mqttClient == null) {
            return;
        }
        try {
            mqttClient.subscribe(Topic, Qos, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    MyLogUtils.d("Mqtt订阅 onSuccess");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    MyLogUtils.d("Mqtt订阅 onFailure");
                }
            });
        } catch (MqttException e) {
            MyLogUtils.e("Mqtt订阅出错", e);
        }
    }

    public void subTopic(String[] Topic, int[] Qos) {
        MyLogUtils.d("subTopic() Topic = " + Topic + "; Qos = " + Qos);
        if (!isConnect) {
            Log.d(TAG, "Mqtt连接未打开");
            return;
        }
        try {
            mqttClient.subscribe(Topic, Qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置Mqtt的连接信息
     */
    private MqttConnectOptions getOptions() {
        MyLogUtils.d("getOptions()");
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);//重连不保持状态
        if (userID != null && userID.length() > 0 && passWord != null && passWord.length() > 0) {
            options.setUserName(userID);//设置服务器账号密码
            options.setPassword(passWord.toCharArray());
        }
        options.setConnectionTimeout(10);//设置连接超时时间
        options.setKeepAliveInterval(20);//设置保持活动时间，超过时间没有消息收发将会触发ping消息确认


        Map<String, String> map = new HashMap<>();
        map.put("snCode", "123");
        map.put("teamId", "121");
        options.setWill("gdu/appOutLine", new JSONObject(map).toString().getBytes(), 0, false);


//        options.setAutomaticReconnect(true);
//        options.setCleanSession(false);
        return options;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public static String getClientID() {
        return clientID;
    }

    public void destroyClient() {
        MyLogUtils.d("destroyClient()");
        if (mqttClient != null && mqttClient.isConnected()) {
            mqttClient.unregisterResources();
            mqttClient.close();
//                mqttClient.disconnect();
            mqttClient.setCallback(null);
            mqttClient = null;
        }

        mqttClient = null;
    }

    class MqttObject {
        String topic;
        String message;

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
