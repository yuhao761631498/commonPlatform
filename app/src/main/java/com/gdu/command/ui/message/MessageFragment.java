package com.gdu.command.ui.message;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gdu.baselibrary.base.BaseRVAdapter;
import com.gdu.baselibrary.base.RefreshFragment;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.R;
import com.gdu.model.chat.ReceiveChatEvent;
import com.gdu.model.config.GlobalVariable;
import com.gdu.model.config.UrlConfig;
import com.gdu.mqttchat.MqttMessage;
import com.gdu.mqttchat.chat.bean.CaseToastBean;
import com.gdu.mqttchat.chat.presenter.ChatService;
import com.gdu.mqttchat.chat.presenter.MessageBean;
import com.gdu.mqttchat.chat.view.ChatActivity;
import com.google.gson.Gson;
import com.rxjava.rxlife.RxLife;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 消息列表页
 */
public class MessageFragment extends RefreshFragment {

    private final int CASE_CHAT_GOT = 1;
    private final int CASE_CHAT_FAILED = 2;

    private BaseRVAdapter<MessageBean.DataBean> mChatAdapter;
    private BaseRVAdapter<CaseToastBean.DataBean> mMsgAdapter;
    private List<MessageBean.DataBean> mCaseChatList;
    private List<CaseToastBean.DataBean> mCaseMsgList = new ArrayList<>();
    private Handler mHandler;
    private Gson mGson;

    private TextView chatTitleTv, msgTitleTv;
    private TextView chatIndicatorLine, msgIndicatorLine;
    private boolean isShowChatList = true;
    private int msgOptPosition = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    private void initHandler() {
        mHandler = new Handler(msg -> {
            switch (msg.what) {
                case CASE_CHAT_GOT:
                    if (mCaseChatList == null || mCaseChatList.size() == 0) {
                        return false;
                    }
                    chatTitleTv.setText("即时消息(" + mCaseChatList.size() + ")");
                    msgTitleTv.setText("通知消息(" + mCaseMsgList.size() + ")");
                    setLoadData(mChatAdapter, mCaseChatList);
                    break;
                default:
                    break;
            }
            return false;
        });
    }

    @Override
    protected void initEventAndData() {
        mGson = new Gson();
        if (GlobalVariable.mLastMessageMap == null) {
            GlobalVariable.mLastMessageMap = new HashMap<>();
        }
        EventBus.getDefault().register(this);
        chatTitleTv = getView().findViewById(R.id.tv_caseTitle);
        msgTitleTv = getView().findViewById(R.id.tv_messageTitle);
        chatIndicatorLine = getView().findViewById(R.id.view_line1);
        msgIndicatorLine = getView().findViewById(R.id.view_line2);

        chatTitleTv.setText("即时消息(0)");
        msgTitleTv.setText("通知消息(0)");

        initHandler();
        initRefreshLayout();
        initRecyclerView();
        initChatAdapter();
        initMsgAdapter();
        initListener();
        isShowChatList = false;
        isShowChatListView(isShowChatList);
        lazyLoad();
    }

    private void initListener() {
        chatTitleTv.setOnClickListener(v -> {
            isShowChatList = true;
            setLoadData(mChatAdapter, mCaseChatList);
            isShowChatListView(isShowChatList);
        });
        msgTitleTv.setOnClickListener(v -> {
            isShowChatList = false;
            setLoadData(mMsgAdapter, mCaseMsgList);
            isShowChatListView(isShowChatList);
        });
    }

    private void isShowChatListView(boolean isShow) {
        chatTitleTv.setSelected(isShow);
        chatIndicatorLine.setSelected(isShow);
        msgTitleTv.setSelected(!isShow);
        msgIndicatorLine.setSelected(!isShow);
        mRecyclerView.setAdapter(isShow ? mChatAdapter : mMsgAdapter);
    }

    private void initChatAdapter() {
        mChatAdapter = new BaseRVAdapter<MessageBean.DataBean>(R.layout.item_case_chat) {
            @Override
            public void onBindVH(BaseViewHolder holder, MessageBean.DataBean data, int position) {
//                String caseId = data.get("msgTopic");
                holder.setText(R.id.name_textview, data.getCaseName());
                holder.setText(R.id.time_textview, data.getCreateTime());
                holder.setVisible(R.id.new_message_imageview, false);
                final String content = getContentFromMessage(data.getMsgContent());
                holder.setText(R.id.last_message_textview, content);
                holder.setVisible(R.id.new_message_imageview, data.isNewMsg());
            }
        };

        mChatAdapter.setOnItemClickListener(((adapter, view, position) -> {
            final MessageBean.DataBean data = (MessageBean.DataBean)adapter.getData().get(position);
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.putExtra(GlobalVariable.EXTRA_CASE_ID, data.getMsgTopic());
            GlobalVariable.mLastMessageMap.put(data.getMsgTopic(), getSendTimeFromMessage(data.getMsgContent()));
            startActivity(intent);
            if (data.isNewMsg()) {
                data.setNewMsg(false);
                mChatAdapter.notifyItemChanged(position);
            }
        }));
    }

    private void initMsgAdapter() {
        mMsgAdapter = new BaseRVAdapter<CaseToastBean.DataBean>(R.layout.item_case_chat_new) {
            @Override
            public void onBindVH(BaseViewHolder holder, CaseToastBean.DataBean data, int position) {
                holder.setText(R.id.tv_timeYearToDay, data.getCreateTime());
                holder.setText(R.id.tv_timeDetail, data.getCreateTime());
                holder.setText(R.id.tv_content, data.getCaseDesc());
                final TextView readIconTv = holder.getView(R.id.tv_newIcon);
                readIconTv.setVisibility(data.getHasBeenRead() == 0 ? View.VISIBLE : View.GONE);
            }
        };
        mMsgAdapter.setOnItemClickListener((adapter, view, position) -> {
            msgOptPosition = position;
            sendReadToastCmd(mCaseMsgList.get(position).getCaseId(), 1);
        });
    }

    /**
     * 判断消息是否未新消息
     *
     * @param data
     * @return
     */
    private boolean isNewMessage(MessageBean.DataBean data) {
        if (data == null) {
            return false;
        }
        String topic = data.getMsgTopic();
        String content = data.getMsgContent();
        if (TextUtils.isEmpty(content)) {
            return false;
        }
        String lastTime = GlobalVariable.mLastMessageMap.get(topic);
        String sendTime = getSendTimeFromMessage(content);
        if (lastTime == null) {
            GlobalVariable.mLastMessageMap.put(topic, sendTime);
            return false;
        } else {
            if (!lastTime.equals(sendTime)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 从消息中获取发送时间
     *
     * @param content
     * @return
     */
    private String getSendTimeFromMessage(String content) {
        MqttMessage mqttMessage = mGson.fromJson(content, MqttMessage.class);
        return mqttMessage.getSendTime();
    }

    @Override
    protected void lazyLoad() {
        downLoadData();
        getCastToastList();
    }

    private void downLoadData() {
        ChatService chatService = RetrofitClient.getAPIService(ChatService.class);
        Call<MessageBean> call = chatService.getChatHisList();
        call.enqueue(new Callback<MessageBean>() {
            @Override
            public void onResponse(Call<MessageBean> call, Response<MessageBean> response) {
                final boolean isUnEmptyData =
                        response.body() != null && response.body().getCode() == 0 && response.body().getData() != null;
                if (isUnEmptyData) {
                    mCaseChatList = response.body().getData();
                    for (int i = 0; i < mCaseChatList.size(); i++) {
                        if (isNewMessage(mCaseChatList.get(i))) {
                            mCaseChatList.get(i).setNewMsg(true);
                        }
                    }
                    mHandler.sendEmptyMessage(CASE_CHAT_GOT);
                } else {
                    mHandler.sendEmptyMessage(CASE_CHAT_GOT);
                }
            }

            @Override
            public void onFailure(Call<MessageBean> call, Throwable t) {
                MyLogUtils.e("获取消息列表出错", t);
                mHandler.sendEmptyMessage(CASE_CHAT_GOT);
            }
        });
    }

    private void getCastToastList() {
        ChatService chatService = RetrofitClient.getAPIService(ChatService.class);
        chatService.getMyCase()
                .subscribeOn(Schedulers.io())
                .to(RxLife.toMain(this))
                .subscribe(bean -> {
                    final boolean isEmptyData = bean == null
                            || bean.getCode() != 0
                            || bean.getData() == null
                            || bean.getData().size() == 0;
                    if (bean != null && bean.getCode() == 401) {
                        mCaseMsgList.clear();
                        mMsgAdapter.notifyDataSetChanged();
                        return;
                    }
                    if (isEmptyData) {
                        return;
                    }
                    mCaseMsgList.clear();
                    CommonUtils.listAddAllAvoidNPE(mCaseMsgList, bean.getData());
                    msgTitleTv.setText("通知消息(" + mCaseMsgList.size() + ")");
                    mMsgAdapter.notifyDataSetChanged();
                }, throwable -> {
                    MyLogUtils.e("获取通知列表出错", throwable);
                    mCaseMsgList.clear();
                    mMsgAdapter.notifyDataSetChanged();
                }, () -> {

                });
    }

    private void sendReadToastCmd(String id, int isRead) {
        Map<String, String> params = new HashMap<>();
        params.put("caseId", id);
        params.put("hasBeenRead", isRead + "");
        final String jsonStr = new Gson().toJson(params);
        RequestBody body = RequestBody.create(jsonStr, MediaType.parse("application/json;charset=UTF-8"));
        ChatService chatService = RetrofitClient.getAPIService(ChatService.class);
        chatService.readCase(body)
                    .subscribeOn(Schedulers.io())
                    .to(RxLife.toMain(this))
                    .subscribe(stringBaseBean -> {
                        mCaseMsgList.get(msgOptPosition).setHasBeenRead(1);
                        mMsgAdapter.notifyItemChanged(msgOptPosition);
                    }, throwable -> {
                        MyLogUtils.e("设置通知已读出错", throwable);
                    }, () -> {

                    });
    }

    private String getContentFromMessage(String content) {
        MqttMessage message = mGson.fromJson(content, MqttMessage.class);
        String contentS = "";
        if (message.getType().equals("txt")) {
            contentS = message.getMsg();
        } else if (message.getType().equals("img")) {
            contentS = "图片";
        } else {
            contentS = "视频";
        }
        return contentS;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}