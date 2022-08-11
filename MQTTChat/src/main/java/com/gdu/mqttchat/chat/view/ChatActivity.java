package com.gdu.mqttchat.chat.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.donkingliang.imageselector.utils.ImageSelector;
import com.gdu.baselibrary.base.RefreshActivity;
import com.gdu.baselibrary.base.route.Transfer;
import com.gdu.baselibrary.utils.FileUtil;
import com.gdu.baselibrary.utils.MMKVUtil;
import com.gdu.baselibrary.utils.SPStringUtils;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.baselibrary.utils.imageloade.MyImageLoadUtils;
import com.gdu.model.base.MediaType;
import com.gdu.model.cases.CaseStatus;
import com.gdu.model.cases.IssueBean;
import com.gdu.model.config.GlobalVariable;
import com.gdu.model.config.StorageConfig;
import com.gdu.model.config.UrlConfig;
import com.gdu.mqttchat.ChatRecord;
import com.gdu.mqttchat.ChatRecordInfo;
import com.gdu.mqttchat.MqttAdapter;
import com.gdu.mqttchat.MqttAppState;
import com.gdu.mqttchat.MqttMessage;
import com.gdu.mqttchat.MqttService;
import com.gdu.mqttchat.R;
import com.gdu.mqttchat.chat.presenter.ChatPresenter;
import com.gdu.mqttchat.message.MessageSendView;
import com.gdu.mqttchat.mqtt.MqttListener;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 聊天界面
 */
public class ChatActivity extends RefreshActivity<ChatPresenter> implements MqttListener, View.OnClickListener, IChatView {

    private static final int REQUEST_PIC_CODE = 100;  //图片请求
    private EditText mMessageET;
    private ImageView mBackImageView;
    private Button mSendBtn;
    private Gson gson = new Gson();
    private List<MqttMessage> messageList = new ArrayList<>();
    private String mCaseId;
    private MqttAdapter mAdapter;
    private MessageSendView mMessageSendView;

    private TextView mCaseTitleTextView;
    private TextView mCaseContentTextView;
    private TextView mCaseAddressTextView;
    private TextView mCaseTimeTextView;
    private TextView mCaseChatTextView;
    private LinearLayout mDealCaseLayout;
    private RelativeLayout mVideoLayout;
    private LinearLayout mLoadingLayout;
    private VideoView mVideoView;
    private ImageView mImageView;
    private ImageView mVideoBackImageView;
    private ImageView mChatGroupImageView;

    @Override
    public void loadData() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getPresenter().getChatHistory();
    }

    private void initData() {
        getPresenter().setView(this);
        MqttService.addMqttListener(this);
        mCaseId = (String) getIntent().getSerializableExtra(GlobalVariable.EXTRA_CASE_ID);
        getPresenter().setCaseChat(mCaseId);
        getPresenter().getAlarmByAlarmId();
//        String deptName = (String) MMKVUtil.getString(SPStringUtils.DEPT_NAME, "NULL");
        String userName = (String) MMKVUtil.getString(SPStringUtils.USER_NAME, "NULL");
        mAdapter.setUserName(userName);
//        mAdapter.setUserName(deptName + " " + userName);
    }

    private void initView() {
        mBackImageView = findViewById(R.id.back_imageview);
        mCaseChatTextView = findViewById(R.id.case_chat_textview);
        mCaseTitleTextView = findViewById(R.id.case_title_textview);
        mCaseContentTextView = findViewById(R.id.case_content_textview);
        mCaseAddressTextView = findViewById(R.id.case_position_textview);
        mCaseTimeTextView = findViewById(R.id.case_time_textview);
        mDealCaseLayout = findViewById(R.id.deal_case_layout);
        mVideoLayout = findViewById(R.id.video_layout);
        mLoadingLayout = findViewById(R.id.loading_layout);
        mVideoView = findViewById(R.id.video_view);
        mImageView = findViewById(R.id.image_view);
        mVideoBackImageView = findViewById(R.id.video_back_imageview);
        mMessageET = findViewById(R.id.send_edit);
        mSendBtn = findViewById(R.id.send_button);
        mChatGroupImageView = findViewById(R.id.chat_group_imageview);
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mMessageET.getText().toString().trim();
                sendMessage(message);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mMessageSendView = findViewById(R.id.chat_editor);
        setMessageSendView();
    }

    private void setMessageSendView() {
        View explandableView = getLayoutInflater().inflate(R.layout.expand_view, null);
//        mMessageSendView.setExpandView(explandableView);
        mMessageSendView.setExpandView(null);
        mMessageSendView.setOnSendClickListener(view -> {
            String text = view.getEditText();
            sendMessage(text);
            // 发送文字消息请求
            mMessageSendView.clearEditText();
//                textView.setVisibility(View.VISIBLE);
//                textView.setText(text);
        });

        mMessageSendView.setOnExpandListener(new MessageSendView.OnExpandListener() {
            @Override
            public void onExpand() {
                hideInputMethod(mMessageSendView);
            }

            @Override
            public void onCollapse() {
            }

            @Override
            public void onFocus() {

            }
        });

        mMessageSendView.setOnRecordListener(new MessageSendView.OnRecordVoiceListener() {
            @Override
            public void onStart(MessageSendView view) {
//                recordVoiceView.setVisibility(View.VISIBLE);
//                recordVoiceView.setState(false);
            }

            @Override
            public void onFinish(MessageSendView view, String fileName, boolean cancelled) {
//                recordVoiceView.setVisibility(View.GONE);
//                if (cancelled)
//                    return;
//
//                long amrDuration = MediaUtil.getAmrDuration(fileName);
//                voiceLength = amrDuration / 1000;
//                if (voiceLength < Constans.MIN_CHAT_VOICE_SECONDS)
//                    return;
//                //TODO 发送语音消息请求
//                messageSendView.clearEditText();
            }

            @Override
            public void onInfo(MessageSendView view, int remainSeconds) {
//                recordVoiceView.setRemainIcons(remainSeconds);
            }

            @Override
            public void willCancel(boolean willCancel) {
//                recordVoiceView.setState(willCancel);
            }
        });

        View takePhotoView = explandableView.findViewById(R.id.chat_editor_take_photo);
        takePhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //仅拍照
                ImageSelector.builder()
                        .onlyTakePhoto(true)  // 仅拍照，不打开相册
                        .setTakePhoto(true)
                        .setTakeVideo(false)
                        .start((Activity) mContext, REQUEST_PIC_CODE);
            }
        });

        View takeVideoView = explandableView.findViewById(R.id.chat_editor_take_video);
        takeVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //仅录像
                ImageSelector.builder()
                        .onlyTakePhoto(true)  // 仅录像，不打开相册
                        .setTakeVideo(true)
                        .setTakePhoto(false)
                        .start((Activity) mContext, REQUEST_PIC_CODE);
            }
        });

        View pickImageView = explandableView.findViewById(R.id.chat_editor_more_gallary);
        pickImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //单选
                ImageSelector.builder()
                        .useCamera(true) // 设置是否使用拍照
                        .setSingle(true)  //设置是否单选
                        .canPreview(true) //是否可以预览图片，默认为true
                        .setContainPhoto(true)
                        .setContainVideo(true)  //是否包含视频，默认为false
                        .start((Activity) mContext, REQUEST_PIC_CODE); // 打开相册
            }
        });
    }

    private void sendMessage(String message) {
        if (!TextUtils.isEmpty(message)) {
            getPresenter().sendChatMessage("txt", message);
        } else {
            MqttAppState.getInstance().showToast("不可发送为空的消息");
        }
    }


    public void hideInputMethod(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MqttService.removeMqttListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StorageConfig.isOnChatPage = false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true).statusBarColor(
                R.color.color_224CD0).init();
        StorageConfig.isOnChatPage = true;
        initRefreshLayout();
        initRecyclerView();
        initView();
        mAdapter = new MqttAdapter(this, messageList);
        mRecyclerView.setAdapter(mAdapter);
        initData();
        initListener();
    }

    private void initListener() {
        mBackImageView.setOnClickListener(this);
        mDealCaseLayout.setOnClickListener(this);
        mVideoBackImageView.setOnClickListener(this);
        mChatGroupImageView.setOnClickListener(this);
        mAdapter.setOnMessageListener(new MqttAdapter.OnMessageListener() {
            @Override
            public void onMessageClick(MqttMessage mqttMessage) {
                if (mqttMessage.getType().contains("video")) {
                    hideSoftInput();
                    mLoadingLayout.setVisibility(View.VISIBLE);
                    mVideoLayout.setVisibility(View.VISIBLE);
                    mVideoView.setVisibility(View.VISIBLE);
                    mImageView.setVisibility(View.GONE);
                    mVideoView.setVideoPath(mqttMessage.getMsg());
                    mVideoView.start();
                } else if(mqttMessage.getType().contains("img")){
                    hideSoftInput();
                    mLoadingLayout.setVisibility(View.GONE);
                    mVideoLayout.setVisibility(View.VISIBLE);
                    mVideoView.setVisibility(View.GONE);
                    mImageView.setVisibility(View.VISIBLE);
                    MyImageLoadUtils.loadImage(ChatActivity.this, mqttMessage.getMsg(),
                            0, mImageView);
                }
            }
        });
        mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mediaPlayer, int what, int extra) {
                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {

                } else {
                    mLoadingLayout.setVisibility(View.GONE);
                }
                return true;
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
//                mVideoLayout.setVisibility(View.GONE);
            }
        });
        getPresenter().setOnChatListener(new ChatPresenter.OnChatListener() {
            @Override
            public void onChatHistoryGot(ChatRecordInfo chatRecordInfo) {
                finishRefresh();
                List<ChatRecord> records = chatRecordInfo.getRecords();
                List<MqttMessage> messages = null;
                if (records != null) {
                    messages = new ArrayList<>();
                    for (ChatRecord record : records) {
                        MqttMessage mqttMessage = gson.fromJson(record.getMsgContent(), MqttMessage.class);
                        mqttMessage.setId((int) record.getId());
                        messages.add(mqttMessage);
                    }
                }
                if (messages != null) {
                    mergeMessage(messages);
                    mAdapter.setList(messageList);
                }
            }
        });
    }

    private void mergeMessage(List<MqttMessage> messages) {
        List<MqttMessage> tmpMessages = new ArrayList<>();
        for (MqttMessage message : messages) {
            boolean isAdd = false;
            for (MqttMessage mqttMessage : messageList) {
                if (message.getId() == mqttMessage.getId()) {
                    isAdd = true;
                    continue;
                }
            }
            if (!isAdd) {
                tmpMessages.add(message);
            }
        }
        messageList.addAll(tmpMessages);
    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onFail() {

    }

    @Override
    public void onLost() {

    }

    @Override
    public void onReceive(String topic, String message) {
        if (topic.equals(UrlConfig.MQTT_TOPIC + "/" + mCaseId)) {
            MqttMessage mqttMessage = gson.fromJson(message, MqttMessage.class);
                messageList.add(mqttMessage);
            mAdapter.setList(messageList);
            int latestPosition = messageList.size() - 1;
            mRecyclerView.scrollToPosition(latestPosition);
            mMessageET.setText("");
            mMessageET.clearFocus();
        }
    }


    @Override
    public void onSendSucc() {
        MqttAppState.getInstance().showToast("消息发送成功");
        mMessageET.setText("");
        mMessageET.clearFocus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PIC_CODE && data != null) {
            //获取选择器返回的数据
            ArrayList<String> images = data.getStringArrayListExtra(
                    ImageSelector.SELECT_RESULT);
            String path = images.get(0);
            int type = MediaType.IMAGE.getKey();
            if (path.endsWith("mp4")) {
                type = MediaType.VIDEO.getKey();
            }
            long size = FileUtil.getFileLength(new File(path));
            double realSize = size / (1024 * 1024.0);
            if (realSize > 10) {
                ToastUtil.s("文件过大，发送失败");
            } else {
                getPresenter().uploadFile(path, "test.png", type);
            }
            /**
             * 是否是来自于相机拍照的图片，
             * 只有本次调用相机拍出来的照片，返回时才为true。
             * 当为true时，图片返回的结果有且只有一张图片。
             */
            boolean isCameraImage = data.getBooleanExtra(ImageSelector.IS_CAMERA_IMAGE, false);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_imageview) {
            finish();
        } else if(view.getId() == R.id.deal_case_layout){
            //这个就是刚刚前面在AndroidManManifest中设置的，?后面是需要传去的参数，但是不要太长
//            final String url = "scheme://dealCaseHost/dealCase?caseStatus=" + CaseStatus.HANDLED.getKey();
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//            startActivity(intent);
            final Intent mIntent = new Intent();
            mIntent.putExtra("caseStatus", CaseStatus.HANDLED.getKey());
            Transfer.startActivity(this, "app/CaseClosedActivity", mIntent);
        } else if(view.getId() == R.id.video_back_imageview){
            mVideoLayout.setVisibility(View.GONE);
        } else if(view.getId() == R.id.chat_group_imageview){
            //这个就是刚刚前面在AndroidManManifest中设置的，?后面是需要传去的参数，但是不要太长
//            String url = "scheme://chatGroupHost/chatGroup?caseId=" + mCaseId;
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//            startActivity(intent);
            final Intent mIntent = new Intent();
            mIntent.putExtra("caseId", mCaseId);
            Transfer.startActivity(this, "app/ChatGroupActivity", mIntent);
        }
    }

    @Override
    public void onCaseGot(IssueBean issueBean) {
        GlobalVariable.sCurrentIssueBean = issueBean;
        if (issueBean.getCaseStatus() == 2) {  //案件状态 0未处置 1已归档 2处置中
            mDealCaseLayout.setVisibility(View.VISIBLE);
        }
        mCaseChatTextView.setText(issueBean.getCaseName() + "执法小组");
        mCaseTitleTextView.setText(issueBean.getCaseName());
        mCaseContentTextView.setText(issueBean.getCaseDesc());
        mCaseAddressTextView.setText(issueBean.getReportAddr());
        mCaseTimeTextView.setText(issueBean.getReportTime());
    }

    @Override
    public void onCaseDetailGet(CaseChatDetailBean.DataBean data) {
//        mCaseChatTextView.setText(data.getname() + "执法小组");
        mCaseTitleTextView.setText(data.getAlarmTypeName());
        mCaseContentTextView.setText(data.getCreateTime());
        mCaseAddressTextView.setText(data.getAlarmAddress());
//        mCaseTimeTextView.setText(data.getCreateTime());
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftInput(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }
}
