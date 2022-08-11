package com.gdu.command.ui.message;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gdu.baselibrary.base.BaseFragment;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.utils.MMKVUtil;
import com.gdu.baselibrary.utils.SPStringUtils;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.R;
import com.gdu.command.ui.my.LoginActivity;
import com.gdu.command.ui.splash.LoginService;
import com.gdu.model.chat.ReceiveChatEvent;
import com.gdu.model.config.GlobalVariable;
import com.gdu.model.config.UrlConfig;
import com.gdu.model.user.PersonInfoBean;
import com.gdu.mqttchat.MqttService;
import com.google.gson.Gson;
import com.rxjava.rxlife.RxLife;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gdu.model.config.UrlConfig.MQTT_ALARM_NOTICE;
import static com.gdu.model.config.UrlConfig.MQTT_DUTY_NOTICE;

public class NoticeMessageFragment extends BaseFragment {

    private ListView ll_notice_list;
    private NoticeMessageAdapter noticeMessageAdapter;
    public static ArrayList<String> messageList = new ArrayList<>();
    private Gson gson;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_notice_message;
    }

    @Override
    protected void initialize() {
        initView();
        initListener();
        initData();
    }

    private void initData() {
        noticeMessageAdapter = new NoticeMessageAdapter(getContext(), messageList);
        ll_notice_list.setAdapter(noticeMessageAdapter);
    }

    private void initListener() {
        EventBus.getDefault().register(this);

//        Observable.just("").delay(1, TimeUnit.SECONDS)
//                .to(RxLife.to(this))
//                .subscribe(s -> {
//                    int userId = MMKVUtil.getInt(SPStringUtils.USER_ID);
//                    MqttService.subTopic(MQTT_DUTY_NOTICE + 410);
//                }, throwable -> {
//                }, () -> {
//                });

        ll_notice_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteDialog(messageList.get(position));
            }
        });
    }


    private void showDeleteDialog(String message) {
        Dialog alertDialog = new AlertDialog.Builder(getContext()).
                setTitle("确定删除？").
                setMessage("您确定删除该通知消息吗？").
                setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        messageList.remove(message);
                        noticeMessageAdapter.setData(messageList);
                        dialog.dismiss();
                    }
                }).
                setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        alertDialog.show();
    }

    private void initView() {
        ll_notice_list = ((ListView) getView().findViewById(R.id.lv_notice_list));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshMessage(NoticeMessageEvent noticeMessageEvent) {
        if (noticeMessageEvent.getTopic().contains(UrlConfig.MQTT_DUTY_NOTICE)) {
            //收到通知消息
            Log.e("yuhao", "onRefreshMessage: " + noticeMessageEvent.getMessage());
            messageList.add(noticeMessageEvent.getMessage());
            noticeMessageAdapter.setData(messageList);
        } else if (noticeMessageEvent.getTopic().contains(UrlConfig.MQTT_ALARM_NOTICE)) {
            Log.e("yuhao", "onRefreshMessage: " + noticeMessageEvent.getMessage());
            if (gson == null) {
                gson = new Gson();
            }
            NoticeAlarmBean noticeAlarmBean = gson.fromJson(noticeMessageEvent.getMessage(), NoticeAlarmBean.class);
            messageList.add(noticeAlarmBean.getMsgVo().getMsgContent());
            noticeMessageAdapter.setData(messageList);
        }
    }


//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onRefreshMessage(ReceiveChatEvent receiveChatEvent) {
//        String topic = receiveChatEvent.getTopic();
//        if (!TextUtils.isEmpty(topic)) {
//            if (topic.contains(UrlConfig.MQTT_DUTY_NOTICE)) {
//                //收到通知消息
//                Log.e("yuhao", "onRefreshMessage: " + receiveChatEvent.getMessage());
//                messageList.add(receiveChatEvent.getMessage());
//                noticeMessageAdapter.setData(messageList);
//            } else if (topic.contains(UrlConfig.MQTT_ALARM_NOTICE)) {
//                Log.e("yuhao", "onRefreshMessage: " + receiveChatEvent.getMessage());
//                if (gson == null) {
//                    gson = new Gson();
//                }
//                NoticeAlarmBean noticeAlarmBean = gson.fromJson(receiveChatEvent.getMessage(), NoticeAlarmBean.class);
//                messageList.add(noticeAlarmBean.getMsgVo().getMsgContent());
//                noticeMessageAdapter.setData(messageList);
//            }
//        }
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}