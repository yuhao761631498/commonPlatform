package com.gdu.mqttchat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gdu.baselibrary.utils.MMKVUtil;
import com.gdu.baselibrary.utils.NotificationUtils;
import com.gdu.baselibrary.utils.SPStringUtils;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.baselibrary.utils.schedule.ScheduleTool;
import com.gdu.model.chat.ReceiveChatEvent;
import com.gdu.model.config.GlobalVariable;
import com.gdu.model.config.StorageConfig;
import com.gdu.model.config.UrlConfig;
import com.gdu.mqttchat.mqtt.MqttConfig;
import com.gdu.mqttchat.mqtt.MqttListener;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by fc on 2018/12/18.
 */

public class MqttService extends Service implements MqttListener {
    private static final int MESSAGE_CHECK = 0;
    private static MqttConfig sMqttConfig;
    private static List<MqttListener> mMqttListenerList = new ArrayList<>();
    private CheckMqttThread mCheckMqttThread;
    private static List<String> mTopicList = new ArrayList<>();
    private Gson mGson = new Gson();
    private ScheduledExecutorService mExecutorService;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull @NotNull Message msg) {
//            MyLogUtils.d("handleMessage() msgWhat = " + msg.what);
            if (msg.what != MESSAGE_CHECK) {
                return false;
            }
            if (sMqttConfig != null && !sMqttConfig.isConnect()) {
                sMqttConfig.connectMqtt();
            }
            return false;
        }
    });

    @Override
    public void onCreate() {
        super.onCreate();
        MyLogUtils.d("onCreate()");
        sMqttConfig = new MqttConfig(this, this);
        sMqttConfig.connectMqtt();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyLogUtils.d("onStartCommand() flags = " + flags + "; startId = " + startId);
        if (mCheckMqttThread == null) {
            // 定时检查mqtt服务是否连接
            mCheckMqttThread = new CheckMqttThread();
        }
        if (mExecutorService == null) {
            mExecutorService = ScheduleTool.getInstance().getTimerSchedule();
        }
        if (!mExecutorService.isShutdown()) {
            mExecutorService.scheduleAtFixedRate(mCheckMqttThread, 2000, 1000,
                    TimeUnit.MILLISECONDS);
        }
        // 启动前台服务
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(1, getNotification());
        }
        return Service.START_STICKY;
    }

    private Notification getNotification() {
        MyLogUtils.d("getNotification()");
        Notification notification = null;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //设置Notification的ChannelID,否则不能正常显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel("mqtt", getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(getApplicationContext(), "mqtt").build();
        } else {
            Notification.Builder builder = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setContentText("");
            notification = builder.build();
        }
        return notification;
    }

    private class CheckMqttThread extends TimerTask {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(MESSAGE_CHECK);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyLogUtils.d("onDestroy()");
        if (sMqttConfig != null) {
            sMqttConfig.destroyClient();
        }
    }

    public static void addMqttListener(MqttListener listener) {
        MyLogUtils.d("addMqttListener()");
        if (!mMqttListenerList.contains(listener)) {
            mMqttListenerList.add(listener);
        }
    }

    public static void removeMqttListener(MqttListener listener) {
        MyLogUtils.d("removeMqttListener()");
        mMqttListenerList.remove(listener);
    }

    public static MqttConfig getMqttConfig() {
        return sMqttConfig;
    }

    @Override
    public void onConnected() {
        MyLogUtils.d("onConnected()");
        try {
            if (sMqttConfig != null && GlobalVariable.sPersonInfoBean != null && GlobalVariable.sPersonInfoBean.getData() != null) {
//            PersonInfoBean.DataBean data = GlobalVariable.sPersonInfoBean.getData();
                sMqttConfig.subTopic(UrlConfig.MQTT_MESSAGE_TOPIC + "#", 0);
            }
            for (MqttListener mqttListener : mMqttListenerList) {
                mqttListener.onConnected();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 订阅主题
     */
    public static void subTopic(String topic){
        MyLogUtils.w("onConnected() topic = " + topic);
        try {
            MyLogUtils.w("sMqttConfig = " + sMqttConfig);
            if (sMqttConfig != null) {
                sMqttConfig.subTopic(topic, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFail() {
        MyLogUtils.d("onFail()");
        if (sMqttConfig != null) {
            sMqttConfig.reStartMqtt();
        }
        for (MqttListener mqttListener : mMqttListenerList) {
            mqttListener.onFail();
        }
    }

    @Override
    public void onLost() {
        MyLogUtils.d("onLost()");
        if (sMqttConfig != null) {
            sMqttConfig.reStartMqtt();
        }
        for (MqttListener mqttListener : mMqttListenerList) {
            mqttListener.onLost();
        }
    }

    @Override
    public void onReceive(String topic, String message) {
        MyLogUtils.d("onReceive() topic = " + topic + "; message = " + message);
        EventBus.getDefault().post(new ReceiveChatEvent(topic, message));
        parseMessage(topic, message);
        for (MqttListener mqttListener : mMqttListenerList) {
            mqttListener.onReceive(topic, message);
        }
    }

    private void parseMessage(String topic, String message) {
//        MyLogUtils.d("parseMessage() topic = " + topic + "; message = " + message);
        if (StorageConfig.isOnChatPage) {
            return;
        }
        if (topic.contains(UrlConfig.MQTT_TOPIC)) {
            MqttMessage mqttMessage = mGson.fromJson(message, MqttMessage.class);
            notifyMessage(mqttMessage.getUsername(), mqttMessage.getMsg());
        } else if(topic.contains(UrlConfig.MQTT_MESSAGE_TOPIC)){
            int index = topic.lastIndexOf("/");
            String name = topic.substring(index, topic.length());
            notifyMessage(name, "新消息");
        }
    }

    private void notifyMessage(String mesUserName, String message){
        MyLogUtils.d("notifyMessage() mesUserName = " + mesUserName + "; message = " + message);
        String deptName = (String) MMKVUtil.getString(SPStringUtils.DEPT_NAME, "NULL");
        String userName = (String) MMKVUtil.getString(SPStringUtils.USER_NAME, "NULL");
        String name = deptName + " " + userName;
        if (!name.equals(mesUserName)) {
            NotificationUtils notificationUtils = new NotificationUtils(this);
            notificationUtils.setDefaults(1);
            notificationUtils.sendNotification(1, deptName, message, R.mipmap.ic_launcher);
        }
    }

    @Override
    public void onSendSucc() {
        MyLogUtils.d("onSendSucc()");
        for (MqttListener mqttListener : mMqttListenerList) {
            mqttListener.onSendSucc();
        }
    }
}
