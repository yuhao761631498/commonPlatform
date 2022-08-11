package com.gdu.command.ui.alarm.detail;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.gdu.baselibrary.base.BasePresenter;
import com.gdu.baselibrary.base.IBaseDisplay;
import com.gdu.baselibrary.network.HostUrl;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.MMKVUtil;
import com.gdu.baselibrary.utils.SPStringUtils;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.client.action.GetAlarmRemarksAction;
import com.gdu.client.action.RemarkAlarmAction;
import com.gdu.client.handler.ActionHandler;
import com.gdu.command.event.RefreshAlarmEvent;
import com.gdu.command.ui.alarm.AlarmService;
import com.gdu.command.ui.alarm.dialog.AlarmDispatchedBean;
import com.gdu.command.uploadpic.IUploadSeparateBackView;
import com.gdu.command.uploadpic.UploadSeparateBackPresenter;
import com.gdu.model.config.GlobalVariable;
import com.gdu.model.config.UrlConfig;
import com.gdu.model.organization.OrganizationInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rxjava.rxlife.RxLife;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 告警详情
 */
public class AlarmDetailPresenter extends BasePresenter {

    private final int GET_ALARM_REMARKS_SUCCEED = 0x01;
    private final int GET_ALARM_REMARKS_FAILED = 0x02;
    private final int UPDATE_ALARM_SUCCEED = 0x03;
    private final int UPDATE_ALARM_FAILED = 0x04;
    private final int REMARK_ALARM_SUCCEED = 0x05;
    private final int REMARK_ALARM_FAILED = 0x06;
    private IAlarmDetailView mAlarmDetailView;
    private Handler mHandler;
    private List<Map<String, String>> mAlarmRemarkList;
    private int mAlarmId;

    private AlarmService mAlarmService;

    private UploadSeparateBackPresenter mUploadPicPresenter;
    private List<OrganizationInfo> organizationList;
    private List<AlarmDispatchedBean.DataBean> assignmentData;

    public AlarmDetailPresenter() {
        mAlarmService = RetrofitClient.getAPIService(AlarmService.class);
        mUploadPicPresenter = new UploadSeparateBackPresenter();
        initHandler();
    }

    @Override
    public void attachView(IBaseDisplay display) {
        super.attachView(display);
        if (mUploadPicPresenter != null) {
            mUploadPicPresenter.attachView(display);
        }
    }

    public void setView(IAlarmDetailView alarmDetailView) {
        mAlarmDetailView = alarmDetailView;
    }

    public void setUploadCallbackView(IUploadSeparateBackView uploadCallback) {
        if (mUploadPicPresenter == null) {
            return;
        }
        mUploadPicPresenter.setIView(uploadCallback);
    }

    private void initHandler() {
        mHandler = new Handler(msg -> {
            switch (msg.what){
                case GET_ALARM_REMARKS_SUCCEED:
                    mAlarmDetailView.showAlarmRemarkList(mAlarmRemarkList);
                    break;
                case UPDATE_ALARM_SUCCEED:
                    ToastUtil.s("案件状态更新成功");
                    EventBus.getDefault().post(new RefreshAlarmEvent(true));
                    mAlarmDetailView.showAlarmStatus(msg.arg1);
                    break;
                case UPDATE_ALARM_FAILED:
                    ToastUtil.s("案件状态更新失败");
                    break;
                case REMARK_ALARM_SUCCEED:
//                    loadRemarks(mAlarmId);
                    mAlarmDetailView.remarkCallback(0);
                    ToastUtil.s("案件备注更新成功");
                    break;
                case REMARK_ALARM_FAILED:
                    mAlarmDetailView.remarkCallback(-1);
                    ToastUtil.s("案件备注提交失败");
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    /**
     * 更新告警状态
     * @param id
     * @param status   2确认  3误报; 4转案件
     */
    public void updateAlarm(int id, int status){
        MyLogUtils.i("remarkAlarm() id = " + id + "; status = " + status);
        Map<String, String> map = new HashMap<>();
        map.put("id", id + "");
        map.put("status", status + "");
        mAlarmService.updateAlarm(map).subscribeOn(Schedulers.io())
                .to(RxLife.toMain(getBaseActivity()))
                .subscribe(data -> {
                    final boolean isSuc = data != null && data.code == 0 && data.data;
                    if (isSuc) {
                        Message message1 = new Message();
                        message1.what = UPDATE_ALARM_SUCCEED;
                        message1.arg1 = status;
                        mHandler.sendMessage(message1);
                    } else {
                        mHandler.sendEmptyMessage(UPDATE_ALARM_FAILED);
                    }
                }, throwable -> {
                    MyLogUtils.e("更新告警状态出错", throwable);
                    mHandler.sendEmptyMessage(UPDATE_ALARM_FAILED);
                });
    }

    /**
     * 加载告警数据
     */
    public void loadRemarks(int id) {
        MyLogUtils.i("loadRemarks() alarmId = " + id);
        final Map<String, String> map = new HashMap<>();
        mAlarmId = id;
        map.put("alarmId", mAlarmId + "");
        map.put("page",  "1");
        map.put("sizeOfPage",  "100");
        GetAlarmRemarksAction updateAlarmAction = new GetAlarmRemarksAction(HostUrl.GET_ALARM_REMARK, UrlConfig.HttpCP, map);
        updateAlarmAction.setHeader("Authorization", "Bearer " + MMKVUtil.getString(SPStringUtils.TOKEN, "NULL"));
        updateAlarmAction.execute(true, new ActionHandler() {
            @Override
            public void doActionStart() {

            }

            @Override
            public void doActionEnd() {

            }

            @Override
            public void doActionResponse(int status, Serializable message) {
                final String result = (String) message;
                if (!CommonUtils.isEmptyString(result)) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int code = jsonObject.getInt("code");
                        if (code == 0) {
                            String data = jsonObject.getString("data");
                            JSONObject jsonData = new JSONObject(data);
                            String alarmList = jsonData.getString("data");
                            Gson gson = new Gson();
                            mAlarmRemarkList = gson.fromJson(alarmList, new TypeToken<ArrayList<Map<String, String>>>() {
                            }.getType());
                            mHandler.sendEmptyMessage(GET_ALARM_REMARKS_SUCCEED);
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mHandler.sendEmptyMessage(GET_ALARM_REMARKS_FAILED);
            }

            @Override
            public void doActionRawData(Serializable data) {

            }
        });
    }

    /**
     * 备注告警
     */
    public void remarkAlarm(int alarmId, String remark) {
        MyLogUtils.i("remarkAlarm() alarmId = " + alarmId + "; remark = " + remark);
        if (TextUtils.isEmpty(remark)) {
            ToastUtil.s("备注不能为空");
            return;
        }
        String authorId = GlobalVariable.sPersonInfoBean.getData().getId() + "";
        String authName =  GlobalVariable.sPersonInfoBean.getData().getUsername();
        Map<String, String> map = new HashMap<>();
        map.put("alarmId", alarmId + "");
        map.put("authorId", authorId + "");
        map.put("authName", authName + "");
        map.put("remark", remark + "");
        RemarkAlarmAction remarkAlarmAction = new RemarkAlarmAction(HostUrl.REMARK_ALARM, UrlConfig.HttpCP, map);
        remarkAlarmAction.setHeader("Authorization", "Bearer " + MMKVUtil.getString(SPStringUtils.TOKEN, "NULL"));
        remarkAlarmAction.execute(true, new ActionHandler() {
            @Override
            public void doActionStart() {

            }

            @Override
            public void doActionEnd() {

            }

            @Override
            public void doActionResponse(int status, Serializable message) {
                final String result = (String) message;
                if (!CommonUtils.isEmptyString(result)) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        int code = jsonObject.getInt("code");
                        if (code == 0) {
                            mHandler.sendEmptyMessage(REMARK_ALARM_SUCCEED);
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mHandler.sendEmptyMessage(REMARK_ALARM_FAILED);
            }

            @Override
            public void doActionRawData(Serializable data) {

            }
        });
    }

    /**
     * 查询分派和处理信息(获取案件详情信息)
     * @param alarmId 预警Id
     * @param handleType 预警类型
     */
    public void getCaseDetailInfo(int alarmId, int handleType) {
        MyLogUtils.i("getCaseDetailInfo() alarmId = " + alarmId + "; handleType = " + handleType);
        final HashMap<String, Integer> paramMap = new HashMap<>();
        paramMap.put("alarmId", alarmId);
        paramMap.put("handleType", handleType);
        mAlarmService.getCaseDetailInfo(paramMap)
                .subscribeOn(Schedulers.io())
                .to(RxLife.toMain(getBaseActivity()))
                .subscribe(data -> {
                    final boolean isSuc = data != null && data.getCode() == 0 && data.getData() != null;
                    if (!isSuc) {
                        ToastUtil.s("获取预警信息详情失败");
                        return;
                    }
                    mAlarmDetailView.showDetailInfo(data.getData());
                }, throwable -> {
                    MyLogUtils.e("获取预警详情出错", throwable);
                });
    }

    /**
     * 查询分派记录
     * @param alarmId 预警Id
     * @param handleType 预警类型
     */
    public void queryAssignment(int alarmId, int handleType) {
        MyLogUtils.i("queryAssignment() alarmId = " + alarmId + "; handleType = " + handleType);
        final HashMap<String, Integer> paramMap = new HashMap<>();
        paramMap.put("alarmId", alarmId);
        paramMap.put("handleType", handleType);
        mAlarmService.queryAssignment(paramMap)
                .subscribeOn(Schedulers.io())
                .to(RxLife.toMain(getBaseActivity()))
                .subscribe(data -> {
//                    final boolean isSuc = data != null && data.getCode() == 0 && data.getData() != null;
//                    if (!isSuc) {
//                        ToastUtil.s("获取预警信息详情失败");
//                        return;
//                    }

                }, throwable -> {
                    MyLogUtils.e("查询分派记录出错", throwable);
                });
    }

    /**
     * 分派预警
     * @param sendBean 预警Id
     */
    public void assignment(AlarmDispatchSendBean sendBean) {
        MyLogUtils.i("assignment()");
        final String paramStr = new Gson().toJson(sendBean);
        final RequestBody mBody = RequestBody.create(paramStr,
                MediaType.parse("application/json;charset=UTF-8"));
        mAlarmService.assignment(mBody)
                .subscribeOn(Schedulers.io())
                .to(RxLife.toMain(getBaseActivity()))
                .subscribe(data -> {
                    final boolean isSuc = data != null && data.code == 0;
                    if (isSuc) {
                        mAlarmDetailView.dispatchedResult(true);
                    } else {
                        mAlarmDetailView.dispatchedResult(false);
                    }
                }, throwable -> {
                    mAlarmDetailView.dispatchedResult(false);
                    MyLogUtils.e("分派预警出错", throwable);
                });
    }

    /**
     * 预警处理
     * @param dataBean 封装所有参数的对象
     */
    public void alarmHandle(AlarmHandleSendBean dataBean) {
        MyLogUtils.i("alarmHandle()");
        final String paramStr = new Gson().toJson(dataBean);
        final RequestBody mBody = RequestBody.create(paramStr,
                MediaType.parse("application/json;charset=UTF-8"));
        mAlarmService.alarmHandle(mBody)
                .subscribeOn(Schedulers.io())
                .to(RxLife.toMain(getBaseActivity()))
                .subscribe(data -> {
                    final boolean isSuc = data != null && data.code == 0;
                    if (isSuc) {
                        mAlarmDetailView.caseHandleResult(true);
                    } else {
                        mAlarmDetailView.caseHandleResult(false);
                    }
                }, throwable -> {
                    mAlarmDetailView.caseHandleResult(false);
                    MyLogUtils.e("预警处理出错", throwable);
                });
    }

    /**
     * 处理信息列表
     * @param alarmId 预警Id
     * @param handleType 预警处理类型
     * @param userId 当前登陆人id
     */
    public void queryAlarmHandleRecord(int alarmId, int handleType, int userId) {
        MyLogUtils.i("queryAlarmHandleRecord() alarmId = " + alarmId + "; handleType = " + handleType
                + "; userId = " + userId);
        final HashMap<String, Integer> paramMap = new HashMap<>();
        paramMap.put("alarmId", alarmId);
        paramMap.put("handleType", handleType);
        paramMap.put("userId", userId);
        mAlarmService.queryAlarmHandleRecord(paramMap)
                .subscribeOn(Schedulers.io())
                .to(RxLife.toMain(getBaseActivity()))
                .subscribe(data -> {
//                    final boolean isSuc = data != null && data.getCode() == 0 && data.getData() != null;
//                    if (!isSuc) {
//                        ToastUtil.s("获取预警信息详情失败");
//                        return;
//                    }

                }, throwable -> {
                    MyLogUtils.e("获取处理信息列表出错", throwable);
                });
    }

    /**
     * 补充处理
     * @param dataBean 封装所有参数的对象
     */
    public void supplementHandle(AlarmHandleSendBean dataBean) {
        MyLogUtils.i("supplementHandle()");
        final String paramStr = new Gson().toJson(dataBean);
        final RequestBody mBody = RequestBody.create(paramStr,
                MediaType.parse("application/json;charset=UTF-8"));
        mAlarmService.supplementHandle(mBody)
                .subscribeOn(Schedulers.io())
                .to(RxLife.toMain(getBaseActivity()))
                .subscribe(data -> {
                    final boolean isSuc = data != null && data.code == 0;
                    if (isSuc) {
                        mAlarmDetailView.supplementHandleResult(true);
                    } else {
                        mAlarmDetailView.supplementHandleResult(false);
                    }
                }, throwable -> {
                    mAlarmDetailView.supplementHandleResult(false);
                    MyLogUtils.e("补充处理出错", throwable);
                });
    }

    /**
     * 预警接警
     * @param alarmId 预警Id
     */
    public void answerPolice(int alarmId) {
        MyLogUtils.i("answerPolice() alarmId = " + alarmId);
        final HashMap<String, Integer> paramMap = new HashMap<>();
        paramMap.put("alarmId", alarmId);
        mAlarmService.answerPolice(paramMap)
                .subscribeOn(Schedulers.io())
                .to(RxLife.toMain(getBaseActivity()))
                .subscribe(data -> {
                    final boolean isSuc = data != null && data.code == 0;
                    if (isSuc) {
                        mAlarmDetailView.answerPoliceResult(true);
                    } else {
                        mAlarmDetailView.answerPoliceResult(false);
                    }
                }, throwable -> {
                    mAlarmDetailView.answerPoliceResult(false);
                    MyLogUtils.e("预警接警出错", throwable);
                });
    }

    /**
     * 查询告警被分派列表
     * @param alarmId 预警Id
     * @param handleType 预警处理类型
     * @param userId 当前登陆人id
     */
    public void queryAssignmentList(int alarmId, int handleType, int userId) {
        MyLogUtils.i("queryAssignmentList() alarmId = " + alarmId + "; handleType = " + handleType
                + "; userId = " + userId);
        final HashMap<String, Integer> paramMap = new HashMap<>();
        paramMap.put("alarmId", alarmId);
        if (handleType != 0) {
            paramMap.put("handleType", handleType);
        }
        paramMap.put("userId", userId);
        mAlarmService.queryAssignmentList(paramMap)
                .subscribeOn(Schedulers.io())
                .to(RxLife.toMain(getBaseActivity()))
                .subscribe(data -> {
                    final boolean isSuc =
                            data != null && data.getCode() == 0 && data.getData() != null && data.getData().size() > 0;
                    if (isSuc) {
                        assignmentData = data.getData();
                        mAlarmDetailView.getDispatchedListResult(true, assignmentData);
                    } else {
                        mAlarmDetailView.getDispatchedListResult(true, null);
                    }
                }, throwable -> {
                    mAlarmDetailView.getDispatchedListResult(false, null);
                    MyLogUtils.e("查询告警被分派列表出错", throwable);
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
        paramMap.put("handleType", handleType);
        paramMap.put("userId", userId);
        mAlarmService.getAlarmHandleRecord(paramMap)
                .subscribeOn(Schedulers.io())
                .to(RxLife.toMain(getBaseActivity()))
                .subscribe(data -> {
//                    final boolean isSuc = data != null && data.getCode() == 0 && data.getData() != null;
//                    if (!isSuc) {
//                        ToastUtil.s("获取预警信息详情失败");
//                        return;
//                    }

                }, throwable -> {
                    MyLogUtils.e("获取聊天处理信息详情出错", throwable);
                });
    }

    /**
     * 查询组织树
     */
    public void getDeptTree() {
        MyLogUtils.i("getDeptTree()");
        mAlarmService.getDeptTree()
                .subscribeOn(Schedulers.io())
                .to(RxLife.toMain(getBaseActivity()))
                .subscribe(data -> {
                    final boolean isSuc = data != null && data.code == 0 && data.data != null
                            && data.data.get(0) != null;
                    if (isSuc) {
                        organizationList = data.data.get(0).getChildren();
                        mAlarmDetailView.callBackOrganizationList(true, organizationList);
                    } else {
                        mAlarmDetailView.callBackOrganizationList(false, null);
                    }
                }, throwable -> {
                    mAlarmDetailView.callBackOrganizationList(false, null);
                    MyLogUtils.e("查询组织树出错", throwable);
                });
    }

    public void uploadFiles(List<String> images, List<String> videos) {
        if (mUploadPicPresenter == null) {
            return;
        }
        mUploadPicPresenter.uploadFiles(images, videos);
    }

    public List<OrganizationInfo> getOrganizationList() {
        return organizationList;
    }

    public List<AlarmDispatchedBean.DataBean> getAssignmentData() {
        return assignmentData;
    }
}
