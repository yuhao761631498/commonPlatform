package com.gdu.mqttchat.chat.presenter;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.gdu.baselibrary.base.BasePresenter;
import com.gdu.baselibrary.network.BaseBean;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.utils.MMKVUtil;
import com.gdu.baselibrary.utils.MyConstants;
import com.gdu.baselibrary.utils.SPStringUtils;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.model.cases.IssueBean;
import com.gdu.model.config.UrlConfig;
import com.gdu.mqttchat.ChatRecord;
import com.gdu.mqttchat.ChatRecordInfo;
import com.gdu.mqttchat.MqttAppState;
import com.gdu.mqttchat.MqttMessage;
import com.gdu.mqttchat.MqttService;
import com.gdu.mqttchat.chat.view.CaseChatDetailBean;
import com.gdu.mqttchat.chat.view.IChatView;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.rxjava.rxlife.RxLife;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;


public class ChatPresenter extends BasePresenter {

    private static final int FILE_UPLOAD_SUCCEED = 0x01;
    private static final int FILE_UPLOAD_FAILED = 0x10;
    private static final int CHAT_RECORD_SUCCEED = 0x02;
    private static final int CHAT_RECORD_FAILED = 0x03;
    private static final int CASE_SUCCEED = 0x04;
    private static final int CASE_FAILED = 0x05;
    private static final int CASE_SUCCEED_NEW = 0x06;

    private IChatView mChatView;
    private Handler mHandler;
    private Gson gson;
    private ChatService mChatService;
    private ChatRecordInfo mChatRecordInfo;
    private OnChatListener mOnChatListener;
    private String mCaseChatId;

    private String mUserName = "";
    private int mUserID;
    private String mDeptName = "";
    private IssueBean mCurrentIssueBean;

    public ChatPresenter() {
        gson = new Gson();
        mChatService = RetrofitClient.getAPIService(ChatService.class);
        mDeptName = (String) MMKVUtil.getString(SPStringUtils.DEPT_NAME, "NULL");
        mUserName = (String) MMKVUtil.getString(SPStringUtils.USER_NAME, "NULL");
        mUserID = (Integer) MMKVUtil.getInt(SPStringUtils.USER_ID, 0);
        initHandler();
    }

    public void setView(IChatView chatView){
        mChatView = chatView;
    }

    /**
     * 设置当前案件
     * @param caseId
     */
    public void setCaseChat(String caseId){
        MyLogUtils.d("setCaseChat() caseId = " + caseId);
        mCaseChatId = caseId;
        if (caseId != null && !caseId.isEmpty() && caseId.trim().length() != 0) {
            MqttService.subTopic(UrlConfig.MQTT_TOPIC  + "/" + caseId);
        }
    }

    @SuppressLint("HandlerLeak")
    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case FILE_UPLOAD_SUCCEED:
                        String path = (String) msg.obj;
                        int type = msg.arg1;
                        String fileType;
                        if (type == com.gdu.model.base.MediaType.IMAGE.getKey()) {
                            fileType = "img";
                        } else {
                            fileType = "video";
                        }
                        sendChatMessage(fileType, UrlConfig.ImgIp + path);
                        break;
                    case CHAT_RECORD_SUCCEED:
                        if (mOnChatListener != null) {
                            mOnChatListener.onChatHistoryGot(mChatRecordInfo);
                        }
//                        mView.hideProgressDialog();
                        break;
                    case CHAT_RECORD_FAILED:
                        if (mView == null) {
                            return;
                        }
                        mView.onRequestFinish();
                        break;

                    case CASE_SUCCEED:
                        if (mChatView == null) {
                            return;
                        }
                        mChatView.onCaseGot(mCurrentIssueBean);
                        break;

                    case CASE_SUCCEED_NEW:
                        CaseChatDetailBean.DataBean mDataBean = (CaseChatDetailBean.DataBean) msg.obj;
                        if (mChatView == null) {
                            return;
                        }
                        mChatView.onCaseDetailGet(mDataBean);
                        break;

                    case CASE_FAILED:

                        break;
                    default:
                        break;
                }
            }
        };
    }

    public void sendChatMessage(String type, String message) {
        MyLogUtils.i("sendChatMessage() type = " + type + "; message = " + message);
        if (!TextUtils.isEmpty(message)) {
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setUserId(mUserID + "");
            mqttMessage.setMsg(message);
            mqttMessage.setType(type);
            mqttMessage.setUsername(mUserName);
            mqttMessage.setDeptName(mDeptName);
            mqttMessage.setSendTime(System.currentTimeMillis() + "");
            MqttService.getMqttConfig().pubMsg(UrlConfig.MQTT_TOPIC  + "/" + mCaseChatId, gson.toJson(mqttMessage), 0);
        } else {
            MqttAppState.getInstance().showToast("不可发送为空的消息");
        }
    }

    /**
     * 上传文件
     *
     * @param path
     * @param name
     */
    public void uploadFile(String path, String name, int type) {
        Observable.just(path)
                .flatMapIterable(str ->
                        Luban.with(mView.getContext()).load(str).filter(
                                path1 -> !isEmptyString(path1))
                                .ignoreBy(MyConstants.LUBAN_IGONE_SIZE).setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {
                                MyLogUtils.d("CompressListener onStart()");
                            }

                            @Override
                            public void onSuccess(File file) {
                                MyLogUtils.d("CompressListener onSuccess()");
                            }

                            @Override
                            public void onError(Throwable e) {
                                MyLogUtils.d("CompressListener onError()");
                                MyLogUtils.e("压缩图片出错", e);
                            }
                        }).get())
                .concatMap((Function<File, ObservableSource<BaseBean<String>>>) file -> {
                    RequestBody requestBody = RequestBody.create(file,
                            MediaType.parse("multipart/form-data"));
                    String suff = ".png";
                    if (type == com.gdu.model.base.MediaType.VIDEO.getKey()) {
                        suff = ".mp4";
                    }
                    MultipartBody.Part body = MultipartBody.Part.createFormData("file",
                            "chat" + suff, requestBody);
                    return mChatService.uploadChatFile(body);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .to(RxLife.to(mView.getBaseActivity()))
                .subscribe(stringBaseBean -> {
                            Message message = new Message();
                            final boolean isUnEmptyData =
                                    stringBaseBean != null && stringBaseBean.code == 0 && stringBaseBean.data != null;
                            if (isUnEmptyData) {
                                final String path2 = (String) stringBaseBean.data;
                                message.what = FILE_UPLOAD_SUCCEED;
                                message.arg1 = type;
                                message.obj = path2;
                            } else {
                                message.what = FILE_UPLOAD_FAILED;
                            }
                            mHandler.sendMessage(message);
                        },
                        throwable -> {
                            MyLogUtils.e("上传图片出错", throwable);
                            mHandler.sendEmptyMessage(FILE_UPLOAD_FAILED);
                        },
                        () -> {

                        });

    }

    /**
     * 判断字符串是否为空.
     * @param str str.
     * @return
     */
    public static boolean isEmptyString(String str) {
        return str == null || str.isEmpty() || str.trim().length() == 0;
    }

    private int mCurrentPage = 0;
    private int mTotalPages = 0;

    /**
     * 获取聊天历史记录
     */
    public void getChatHistory() {
        MyLogUtils.d("getChatHistory()");
        if (mCurrentPage == 0) {
            mCurrentPage = 1;
        } else {
            mCurrentPage++;
        }
        if (mCurrentPage > mTotalPages && mTotalPages != 0) {
            mCurrentPage = mTotalPages;
            return;
        }
        Call<BaseBean<Object>> call = mChatService.getChatHistory(mCaseChatId, mCurrentPage + "", "5");
        call.enqueue(new Callback<BaseBean<Object>>() {
            @Override
            public void onResponse(Call<BaseBean<Object>> call, Response<BaseBean<Object>> response) {
                if (response.body() != null && response.body().data != null) {
                    BaseBean baseBean = response.body();
                    parseChatInfo(baseBean);
                    mHandler.sendEmptyMessage(CHAT_RECORD_SUCCEED);
                }  else {
                    mCurrentPage--;
                    mHandler.sendEmptyMessage(CHAT_RECORD_FAILED);
                }
            }

            @Override
            public void onFailure(Call<BaseBean<Object>> call, Throwable t) {
                MyLogUtils.e("获取聊天历史出错", t);
                mCurrentPage--;
                mHandler.sendEmptyMessage(CHAT_RECORD_FAILED);
            }
        });
    }

    /**
     * 解析聊天历史记录
     *
     * @param baseBean
     */
    private void parseChatInfo(BaseBean baseBean) {
        MyLogUtils.d("parseChatInfo()");
        Map<String, Object> data = (LinkedTreeMap<String, Object>) baseBean.data;
        ChatRecordInfo info = new ChatRecordInfo();
        List<Map<String, Object>> objectList = (List<Map<String, Object>>) data.get("records");
        List<ChatRecord> chatRecords = new ArrayList<>();
        for (Map<String, Object> o : objectList) {
            ChatRecord chatRecord = new ChatRecord();
            chatRecord.setId((Double) o.get("id"));
            chatRecord.setUsername((String) o.get("username"));
            chatRecord.setMsgTopic((String) o.get("msgTopic"));
            chatRecord.setMsgType((String) o.get("msgType"));
            chatRecord.setMsgContent((String) o.get("msgContent"));
            chatRecord.setCreateTime((String) o.get("createTime"));
            chatRecords.add(chatRecord);
        }
        info.setRecords(chatRecords);
        info.setTotal((Double) data.get("total"));
        info.setSize((Double) data.get("size"));
        info.setCurrent((Double) data.get("current"));
        info.setSearchCount((Boolean) data.get("searchCount"));
        info.setPages((Double) data.get("pages"));
        mCurrentPage = (int) info.getCurrent();
        mTotalPages = (int) info.getPages();
        mChatRecordInfo = info;
    }

    public void setOnChatListener(OnChatListener onChatListener){
        mOnChatListener = onChatListener;
    }

    public void getCaseDetail() {
        MyLogUtils.d("getCaseDetail() id = " + mCaseChatId);
        Map<String, Object> map  = new HashMap<>();
        map.put("id", mCaseChatId);
        String strEntity = gson.toJson(map);
        RequestBody body = RequestBody.create(strEntity, MediaType.parse("application/json;charset=UTF-8"));
        mChatService.getCaseDetail(body).enqueue(new Callback<BaseBean<IssueBean>>() {
            @Override
            public void onResponse(Call<BaseBean<IssueBean>> call, Response<BaseBean<IssueBean>> response) {
                BaseBean issueBean = response.body();
                if (issueBean != null && issueBean.code == 0) {
                    mCurrentIssueBean = (IssueBean) issueBean.data;
                    mHandler.sendEmptyMessage(CASE_SUCCEED);
                } else {
                    mHandler.sendEmptyMessage(CASE_FAILED);
                }
            }

            @Override
            public void onFailure(Call<BaseBean<IssueBean>> call, Throwable t) {
                MyLogUtils.e("获取案件详情出错", t);
                mHandler.sendEmptyMessage(CASE_FAILED);
            }
        });
    }

    public void getAlarmHandleRecord() {
        getAlarmHandleRecord(Integer.parseInt(mCaseChatId), 0, 0);
    }

    /**
     * 获取聊天列表顶部预计详情
     */
    public void getAlarmByAlarmId() {
        MyLogUtils.i("getAlarmByAlarmId()() alarmId = " + mCaseChatId);
        final HashMap<String, Integer> paramMap = new HashMap<>();
        paramMap.put("alarmId", Integer.parseInt(mCaseChatId));
        mChatService.getAlarmByAlarmId(paramMap)
                .subscribeOn(Schedulers.io())
                .to(RxLife.toMain(getBaseActivity()))
                .subscribe(data -> {
                    final boolean isSuc = data != null && data.getCode() == 0 && data.getData() != null;
                    if (isSuc) {
                        CaseChatDetailBean.DataBean mDetailBean = data.getData();
                        final Message msg = new Message();
                        msg.what = CASE_SUCCEED_NEW;
                        msg.obj = mDetailBean;
                        mHandler.sendMessage(msg);
                    } else {
                        mHandler.sendEmptyMessage(CASE_FAILED);
                    }
                }, throwable -> {
                    mHandler.sendEmptyMessage(CASE_FAILED);
                    MyLogUtils.e("获取聊天处理信息详情出错", throwable);
                });
    }

    /**
     * 聊天处理信息详情
     * @param alarmId 预警Id
     * @param handleType 预警处理类型
     * @param userId 当前登陆人id
     */
    public void getAlarmHandleRecord(int alarmId, int handleType, int userId) {
        MyLogUtils.i("getAlarmHandleRecord() alarmId = " + alarmId + "; handleType = " + handleType
                + "; userId = " + userId);
        final HashMap<String, Integer> paramMap = new HashMap<>();
        paramMap.put("alarmId", alarmId);
        if (handleType != 0) {
            paramMap.put("handleType", handleType);
        }
        if (userId != 0) {
            paramMap.put("userId", userId);
        }
        mChatService.getAlarmHandleRecord(paramMap)
                .subscribeOn(Schedulers.io())
                .to(RxLife.toMain(getBaseActivity()))
                .subscribe(data -> {
                    final boolean isSuc = data != null && data.getCode() == 0 && data.getData() != null;
                    if (isSuc) {
                        CaseChatDetailBean.DataBean mDetailBean = data.getData();
                        final Message msg = new Message();
                        msg.what = CASE_SUCCEED_NEW;
                        msg.obj = mDetailBean;
                        mHandler.sendMessage(msg);
                    } else {
                        mHandler.sendEmptyMessage(CASE_FAILED);
                    }
                }, throwable -> {
                    mHandler.sendEmptyMessage(CASE_FAILED);
                    MyLogUtils.e("获取聊天处理信息详情出错", throwable);
                });
    }


    public interface OnChatListener{
        void onChatHistoryGot(ChatRecordInfo chatRecordInfo);
    }
}
