package com.gdu.command.ui.alarm.center;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.gdu.baselibrary.base.BasePresenter;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.ui.alarm.AlarmListBean;
import com.gdu.command.ui.alarm.AlarmService;
import com.gdu.command.views.drop.AlarmDeviceBean;
import com.gdu.model.alarm.AlarmInfo;
import com.gdu.utils.MyVariables;
import com.google.gson.Gson;
import com.rxjava.rxlife.RxLife;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 告警中心
 */
public class AlarmCenterPresenter extends BasePresenter {

    private final int ALARM_SUCCEED = 0x01;
    private final int ALARM_FAILED = 0x02;
    private final int ALARM_DEV_LIST_SUCCEED = 0x03;
    private final int ALARM_DEV_LIST_FAILED = 0x04;

    private AlarmListBean.DataBean mAlarmListBean1;
    /** 待核实数据 */
    private List<AlarmInfo> mAlarmInfoList1;

    private AlarmListBean.DataBean mAlarmListBean2;
    /** 待接警数据 */
    private List<AlarmInfo> mAlarmInfoList2;

    private AlarmListBean.DataBean mAlarmListBean3;
    /** 待处理数据 */
    private List<AlarmInfo> mAlarmInfoList3;

    private AlarmListBean.DataBean mAlarmListBean4;
    /** 已处理数据 */
    private List<AlarmInfo> mAlarmInfoList4;

    /** 事件类型过滤选项 */
    private Map<Integer, Boolean> mEventTypeMap;
    /** 事件状态过滤选项 */
    private Map<Integer, Boolean> mStatusMap;
    /** 事件等级过滤选项 */
    private Map<Integer, Boolean> mLevelMap;
    /** 记录开始时间 */
    private String mStartTime = "";
    /** 记录结束时间 */
    private String mEndTime = "";
    /** 筛选选中的设备ID */
    private List<String> selectDevIds = new ArrayList();
    /** 每页请求数据数量 */
    private int PAGE_COUNT = 10;
    /**
     * (待核实: 1; 误报: 2; 待接警: 3; 待处理:	4; 已处理: 5)
     */
    private int mHandleType;
    /**
     * 当前是否是加载更多数据
     */
    private boolean curIsLoadMoreData;

    private AlarmService mAlarmService;

    private IAlarmCenterView mAlarmCenterView;
    private Handler mHandler;
//    private List<AlarmInfo> mAlarmInfoList = new ArrayList<>();
//    private String mStartTime;
//    private String mEndTime;
//    /** 是否下载网络过滤数据 */
//    private boolean isLoadFilterData = false;

    public AlarmCenterPresenter() {
        mAlarmService = RetrofitClient.getAPIService(AlarmService.class);
//        long endTime = System.currentTimeMillis();
//        mEndTime = TimeUtils.getTime(endTime, "yyyy/MM/dd HH:mm:ss");
//        long startTime = endTime - 24 * 60 * 60 * 1000;
//        mStartTime = TimeUtils.getTime(startTime, "yyyy/MM/dd HH:mm:ss");
    }

    public void setView(IAlarmCenterView alarmCenterView) {
        mAlarmCenterView = alarmCenterView;
        initHandler();
    }

    private void initHandler() {
        mHandler = new Handler(msg -> {
            switch (msg.what) {
                case ALARM_SUCCEED:
//                    if (isLoadFilterData) {
////                            filterDatas(mStatusMap, mLevelMap, mStartTime, mEndTime);
////                        filterDatas(mEventTypeMap, mStatusMap, mLevelMap, mStartTime, mEndTime);
//                    } else {
//                        mAlarmCenterView.showAlarmList(mAlarmInfoList);
//                    }
                    break;

                case ALARM_FAILED:
//                    mAlarmInfoList.clear();
//                    mAlarmCenterView.showAlarmList(mAlarmInfoList);
                    break;

                case ALARM_DEV_LIST_SUCCEED:
                    mAlarmCenterView.showDeviceList((List<AlarmDeviceBean.DataBean.RowsBean>) msg.obj);
                    break;

                case ALARM_DEV_LIST_FAILED:
                    mAlarmCenterView.showDeviceList(null);
                    break;

                default:
                    break;
            }
            return false;
        });
    }

    public void setFilterDataType(Map<Integer, Boolean> statusMap, Map<Integer, Boolean> levelMap,
                                  String startTime, String endTime) {
        setFilterDataType(mEventTypeMap, statusMap, levelMap, startTime, endTime);
    }

    public void setFilterDataType(Map<Integer, Boolean> eventTypeMap,
                                  Map<Integer, Boolean> statusMap, Map<Integer, Boolean> levelMap,
                                  String startTime, String endTime) {
        mEventTypeMap = eventTypeMap;
        mStatusMap = statusMap;
        mLevelMap = levelMap;
        mStartTime = startTime;
        mEndTime = endTime;
    }

    public void setSelectDevIds(List<String> devIds) {
        CommonUtils.listAddAllAvoidNPE(selectDevIds, devIds);
    }

    /**
     * @param handleType 告警处理状态 (待核实: 1; 误报: 2; 待接警: 3; 待处理:	4; 已处理: 5)
     */
    public void showAlarmDataByType(int handleType) {
        mHandleType = handleType;
        switch (mHandleType) {
            case 1:
                if (mAlarmInfoList1 == null) {
                    loadDataNew(handleType);
                } else {
                    mAlarmCenterView.showAlarmList(mAlarmInfoList1);
                }
                break;
            case 3:
                if (mAlarmInfoList2 == null) {
                    loadDataNew(handleType);
                } else {
                    mAlarmCenterView.showAlarmList(mAlarmInfoList2);
                }
                break;
            case 4:
                if (mAlarmInfoList3 == null) {
                    loadDataNew(handleType);
                } else {
                    mAlarmCenterView.showAlarmList(mAlarmInfoList3);
                }
                break;
            case 5:
                if (mAlarmInfoList4 == null) {
                    loadDataNew(handleType);
                } else {
                    mAlarmCenterView.showAlarmList(mAlarmInfoList4);
                }
                break;

            default:
                break;
        }
    }

    public void loadDataNew(int handleType) {
        clearOldData();

        loadDataNew(handleType, false);
    }

    private void clearOldData() {
        switch (mHandleType) {
            case 1:
                if (mAlarmInfoList1 != null) {
                    mAlarmInfoList1.clear();
                }
                break;
            case 3:
                if (mAlarmInfoList2 != null) {
                    mAlarmInfoList2.clear();
                }
                break;
            case 4:
                if (mAlarmInfoList3 != null) {
                    mAlarmInfoList3.clear();
                }
                break;
            case 5:
                if (mAlarmInfoList4 != null) {
                    mAlarmInfoList4.clear();
                }
                break;

            default:
                break;
        }
    }

    /**
     * @param handleType 告警处理状态 (待核实: 1; 误报: 2; 待接警: 3; 待处理:	4; 已处理: 5)
     */
    public void loadDataNew(int handleType, boolean isLoadMore) {
        mHandleType = handleType;
        curIsLoadMoreData = isLoadMore;
        int page = -1;
        if (curIsLoadMoreData) {
            switch (mHandleType) {
                case 1:
                    if (mAlarmListBean1 == null) {
                        page = 1;
                    } else {
                        page = mAlarmListBean1.getCurrentPage();
                    }
                    break;
                case 3:
                    if (mAlarmListBean2 == null) {
                        page = 1;
                    } else {
                        page = mAlarmListBean2.getCurrentPage();
                    }
                    break;
                case 4:
                    if (mAlarmListBean3 == null) {
                        page = 1;
                    } else {
                        page = mAlarmListBean3.getCurrentPage();
                    }
                    break;
                case 5:
                    if (mAlarmListBean4 == null) {
                        page = 1;
                    } else {
                        page = mAlarmListBean4.getCurrentPage();
                    }
                    break;

                default:
                    break;
            }
        } else {
            page = 1;
            if (mAlarmListBean1 != null) {
                mAlarmListBean1.setCurrentPage(1);
                mAlarmListBean1.setLoadFinish(false);
            }
            if (mAlarmListBean2 != null) {
                mAlarmListBean2.setCurrentPage(1);
                mAlarmListBean2.setLoadFinish(false);
            }
            if (mAlarmListBean3 != null) {
                mAlarmListBean3.setCurrentPage(1);
                mAlarmListBean3.setLoadFinish(false);
            }
            if (mAlarmListBean4 != null) {
                mAlarmListBean4.setCurrentPage(1);
                mAlarmListBean4.setLoadFinish(false);
            }
        }
        loadDataNew(mEventTypeMap, mStatusMap, mLevelMap, selectDevIds,
                MyVariables.isLockAtAttention ? 1 : 0, "", mStartTime, mEndTime,
                "desc", "", handleType + "", page, PAGE_COUNT);
    }

    /**
     * @isLoadCollection 是否只显示关注数据
     */
    public void isLoadCollectData(boolean isLoadCollection) {
        MyVariables.isLockAtAttention = isLoadCollection;
        if (mHandleType == 0) {
            return;
        }
        switch (mHandleType) {
            case 1:
                if (mAlarmListBean1 != null) {
                    mAlarmListBean1.setCurrentPage(1);
                    mAlarmListBean1.setLoadFinish(false);
                }
                break;

            case 3:
                if (mAlarmListBean2 != null) {
                    mAlarmListBean2.setCurrentPage(1);
                    mAlarmListBean2.setLoadFinish(false);
                }
                break;

            case 4:
                if (mAlarmListBean3 != null) {
                    mAlarmListBean3.setCurrentPage(1);
                    mAlarmListBean3.setLoadFinish(false);
                }
                break;

            case 5:
                if (mAlarmListBean4 != null) {
                    mAlarmListBean4.setCurrentPage(1);
                    mAlarmListBean4.setLoadFinish(false);
                }
                break;

            default:
                break;
        }

        loadDataNew(mEventTypeMap, mStatusMap, mLevelMap, selectDevIds,
                MyVariables.isLockAtAttention ? 1 : 0, "", mStartTime, mEndTime,
                "desc", "", mHandleType + "", 1, PAGE_COUNT);
    }

    /**
     * 加载告警数据(新-支持筛选)
     *
     * @param alarmLevel  告警等级
     * @param alarmStatus 告警状态
     * @param alarmType   告警类型
     * @param deviceId    设备id
     * @param attention   只看关注，请传1，否则请传0
     * @param orderBy     排序字段，createTime代表按告警时间排序，alarmStatus代表按告警状态排序，
     *                    alarmLevel代表按告警等级排序，为空则默认按照告警时间排序，整体上是降序排序
     * @param collation 排序规则，asc升序，desc降序
     * @param handleResultType 告警处理结果状态 (行政处罚: 1; 行政拘留: 2; 口头警告: 3; 其他: 4)
     * @param handleType 告警处理状态 (待核实: 1; 误报: 2; 待接警: 3; 待处理:	4; 已处理: 5)
     * @param currentPage 当前页码
     * @param sizeOfPage  每页数量大小
     */
    public void loadDataNew(Map<Integer, Boolean> alarmType, Map<Integer, Boolean> alarmStatus,
                            Map<Integer, Boolean> alarmLevel, List<String> deviceId, int attention,
                            String orderBy, String startTime, String endTime, String collation,
                            String handleResultType, String handleType, int currentPage, int sizeOfPage) {
        final String alarmTypeSizeStr = alarmType == null ? "0" : alarmType.size() + "";

        final String alarmStatusSizeStr = alarmStatus == null ? "0" : alarmStatus.size() + "";

        final String alarmLevelSizeStr = alarmLevel == null ? "0" : alarmLevel.size() + "";

        final String devIdSizeStr = deviceId == null ? "0" : deviceId.size() + "";

        MyLogUtils.d("loadDataNew() alarmTypeSize = " + alarmTypeSizeStr
                + "; alarmStatusSize = " + alarmStatusSizeStr + "; alarmLevelSize = " + alarmLevelSizeStr
                + "; deviceIdSize = " + devIdSizeStr + "; attention = " + attention + "; orderBy = " + orderBy
                + "; startTime = " + startTime + "; endTime = " + endTime + "; collation = " + collation
                + "; handleResultType = " + handleResultType + "; handleType = " + handleType
                + "; currentPage = " + currentPage + "; sizeOfPage = " + sizeOfPage);
        final Map<String, Object> map = new HashMap<>();
        String alarmLevelStr = "";
        if (alarmLevel != null && alarmLevel.size() > 0) {
            alarmLevelStr = map2Str(alarmLevel);
        }
        if (!CommonUtils.isEmptyString(alarmLevelStr)) {
            map.put("alarmLevel", alarmLevelStr);
        }
        String alarmStatusStr = "";
        if (alarmStatus != null && alarmStatus.size() > 0) {
            alarmStatusStr = map2Str(alarmStatus);
        }
        if (!CommonUtils.isEmptyString(alarmStatusStr)) {
            map.put("alarmStatus", alarmStatusStr);
        }
        String alarmTypeStr = "";
        if (alarmType != null && alarmType.size() > 0) {
            alarmTypeStr = map2Str(alarmType);
        }
        if (!CommonUtils.isEmptyString(alarmTypeStr)) {
            map.put("alarmType", alarmTypeStr);
        }
        String deviceIdStr = "";
        if (deviceId != null && deviceId.size() > 0) {
            deviceIdStr = stringArray2Str(deviceId);
        }
        if (!CommonUtils.isEmptyString(deviceIdStr)) {
            map.put("deviceId", deviceIdStr);
        }
        map.put("attention", attention);
        if (!CommonUtils.isEmptyString(orderBy)) {
            map.put("orderBy", orderBy);
        }
        if (!CommonUtils.isEmptyString(startTime)) {
            map.put("startTime", startTime);
        }
        if (!CommonUtils.isEmptyString(endTime)) {
            map.put("endTime", endTime);
        }
        if (!CommonUtils.isEmptyString(collation)) {
            map.put("collation", collation);
        }
        if (!CommonUtils.isEmptyString(handleResultType)) {
            map.put("handleResultType", handleResultType);
        }
        if (!CommonUtils.isEmptyString(handleType)) {
            map.put("handleType", handleType);
        }
        if (!CommonUtils.isEmptyString(collation)) {
            map.put("collation", collation);
        }
        if (!CommonUtils.isEmptyString(handleResultType)) {
            map.put("handleResultType", handleResultType);
        }
        if (!CommonUtils.isEmptyString(handleType)) {
            map.put("handleType", handleType);
        }
        map.put("currentPage", currentPage);
        map.put("sizeOfPage", sizeOfPage);

        mAlarmService.getAlarmListNew(map)
                .subscribeOn(Schedulers.io())
                .to(RxLife.toMain(mView.getBaseActivity()))
                .subscribe(bean -> {
                    final boolean isSuc = bean != null && bean.getCode() == 0 && bean.getData() != null;
                    switch (mHandleType) {
                        case 1:
                            mAlarmListBean1 = bean.getData();
                            if (CommonUtils.isEmptyList(bean.getData().getData())) {
                                mAlarmListBean1.setLoadFinish(true);
                            }
                            if (isSuc) {
                                mAlarmListBean1.setCurrentPage(mAlarmListBean1.getCurrentPage() + 1);
                                if (!curIsLoadMoreData) {
                                    if (mAlarmInfoList1 == null) {
                                        mAlarmInfoList1 = bean.getData().getData();
                                    } else {
                                        mAlarmInfoList1.clear();
                                        CommonUtils.listAddAllAvoidNPE(mAlarmInfoList1, bean.getData().getData());
                                    }
                                } else {
                                    CommonUtils.listAddAllAvoidNPE(mAlarmInfoList1, bean.getData().getData());
                                }
                            }
                            mAlarmCenterView.showAlarmList(mAlarmInfoList1);
                            break;

                        case 3:
                            mAlarmListBean2 = bean.getData();
                            if (CommonUtils.isEmptyList(bean.getData().getData())) {
                                mAlarmListBean2.setLoadFinish(true);
                            }
                            if (isSuc) {
                                mAlarmListBean2.setCurrentPage(mAlarmListBean2.getCurrentPage() + 1);
                                if (!curIsLoadMoreData) {
                                    if (mAlarmInfoList2 == null) {
                                        mAlarmInfoList2 = bean.getData().getData();
                                    } else {
                                        mAlarmInfoList2.clear();
                                        CommonUtils.listAddAllAvoidNPE(mAlarmInfoList2, bean.getData().getData());
                                    }
                                } else {
                                    CommonUtils.listAddAllAvoidNPE(mAlarmInfoList2, bean.getData().getData());
                                }
                            }
                            mAlarmCenterView.showAlarmList(mAlarmInfoList2);
                            break;

                        case 4:
                            mAlarmListBean3 = bean.getData();
                            if (CommonUtils.isEmptyList(bean.getData().getData())) {
                                mAlarmListBean3.setLoadFinish(true);
                            }
                            if (isSuc) {
                                mAlarmListBean3.setCurrentPage(mAlarmListBean3.getCurrentPage() + 1);
                                if (!curIsLoadMoreData) {
                                    if (mAlarmInfoList3 == null) {
                                        mAlarmInfoList3 = bean.getData().getData();
                                    } else {
                                        mAlarmInfoList3.clear();
                                        CommonUtils.listAddAllAvoidNPE(mAlarmInfoList3, bean.getData().getData());
                                    }
                                } else {
                                    CommonUtils.listAddAllAvoidNPE(mAlarmInfoList3, bean.getData().getData());
                                }
                            }
                            mAlarmCenterView.showAlarmList(mAlarmInfoList3);
                            break;

                        case 5:
                            mAlarmListBean4 = bean.getData();
                            if (CommonUtils.isEmptyList(bean.getData().getData())) {
                                mAlarmListBean4.setLoadFinish(true);
                            }
                            if (isSuc) {
                                mAlarmListBean4.setCurrentPage(mAlarmListBean4.getCurrentPage() + 1);
                                if (!curIsLoadMoreData) {
                                    if (mAlarmInfoList4 == null) {
                                        mAlarmInfoList4 = bean.getData().getData();
                                    } else {
                                        mAlarmInfoList4.clear();
                                        CommonUtils.listAddAllAvoidNPE(mAlarmInfoList4, bean.getData().getData());
                                    }
                                } else {
                                    CommonUtils.listAddAllAvoidNPE(mAlarmInfoList4, bean.getData().getData());
                                }
                            }
                            mAlarmCenterView.showAlarmList(mAlarmInfoList4);
                            break;

                        default:
                            break;
                    }
                }, throwable -> {
                    MyLogUtils.e("获取预警列(新)表出错", throwable);
                    final List<AlarmInfo> emptyList = new ArrayList<>();
                    mAlarmCenterView.showAlarmList(emptyList);
                });
    }

    private String map2Str(Map<Integer, Boolean> data) {
        Iterator<Map.Entry<Integer, Boolean>> it = data.entrySet().iterator();
        final StringBuffer sb = new StringBuffer();
        while (it.hasNext()) {
            Map.Entry<Integer, Boolean> entry = it.next();
            if (!entry.getValue()) {
                continue;
            }
            if (!CommonUtils.isEmptyString(sb.toString())) {
                sb.append(",");
            }
            sb.append(entry.getKey() + "");
        }
        return sb.toString();
    }

    private String stringArray2Str(List<String> data) {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < data.size(); i++) {
            if (!CommonUtils.isEmptyString(sb.toString())) {
                sb.append(",");
            }
            sb.append(data.get(i));
        }
        return sb.toString();
    }

//    /**
//     * 获取默认排序数据列表
//     */
//    public void loadDataDefault(String startTime, String endTime, int currentPage, int sizeOfPage) {
//        loadDataNew(null, null, null,
//                null, MyVariables.isLockAtAttention ? 1 : 0,
//                "", startTime, endTime, "desc", "", "",
//                currentPage, sizeOfPage);
//    }

    /**
     * 时间由近到远排序
     */
    public List<AlarmInfo> sortByNearTime(List<AlarmInfo> inData) {
        if (CommonUtils.isEmptyList(inData)) {
            return new ArrayList<>();
        }
        Collections.sort(inData, (t0, t1) -> {
            final Long capTime0 = t0.getCaptureTime();
            final Long capTime1 = t1.getCaptureTime();
            return capTime1.compareTo(capTime0);
        });
        return inData;
    }

    /**
     * 时间由远到近排序
     */
    public List<AlarmInfo> sortByFarTime(List<AlarmInfo> inData) {
        if (CommonUtils.isEmptyList(inData)) {
            return new ArrayList<>();
        }
        Collections.sort(inData, (t0, t1) -> {
            final Long capTime0 = t0.getCaptureTime();
            final Long capTime1 = t1.getCaptureTime();
            return capTime0.compareTo(capTime1);
        });
        return inData;
    }

    /**
     * 根据级别由低到高排序
     */
    public List<AlarmInfo> sortByLevelLow(List<AlarmInfo> inData) {
        if (CommonUtils.isEmptyList(inData)) {
            return new ArrayList<>();
        }
        Collections.sort(inData, (t0, t1) -> {
            final Integer level0 = t0.getAlarmLevel();
            final Integer level1 = t1.getAlarmLevel();
            return level0.compareTo(level1);
        });
        return inData;
    }

    /**
     * 根据级别排序
     */
    public List<AlarmInfo> sortByLevelHigh(List<AlarmInfo> inData) {
        if (CommonUtils.isEmptyList(inData)) {
            return new ArrayList<>();
        }
        Collections.sort(inData, (t0, t1) -> {
            final Integer level0 = t0.getAlarmLevel();
            final Integer level1 = t1.getAlarmLevel();
            return level1.compareTo(level0);
        });
        return inData;
    }

    public void getDeviceList() {
        getDeviceList("", "", "", "");
    }


    /**
     * 获取预警设备列表
     */
    public void getDeviceList(String deviceCode, String deviceName,
                              String pageNum, String pageSize) {
        final Map<String, String> params = new HashMap<>();
        if (!CommonUtils.isEmptyString(deviceCode)) {
            params.put("deviceCode", deviceCode);
        }
        if (!CommonUtils.isEmptyString(deviceName)) {
            params.put("deviceName", deviceName);
        }
        if (!CommonUtils.isEmptyString(pageNum)) {
            params.put("pageNum", pageNum);
        }
        if (!CommonUtils.isEmptyString(pageSize)) {
            params.put("pageSize", pageSize);
        }

        mAlarmService.getDeviceList(params)
                .subscribeOn(Schedulers.io())
                .to(RxLife.toMain(mView.getBaseActivity()))
                .subscribe(bean -> {
                    final boolean isSuc = bean != null && bean.getCode() == 0 && bean.getData() != null;
                    final Message msg = new Message();
                    msg.what = isSuc ? ALARM_DEV_LIST_SUCCEED : ALARM_DEV_LIST_FAILED;
                    msg.obj = bean.getData().getRows();
                    mHandler.sendMessage(msg);
                }, throwable -> {
                    MyLogUtils.e("获取预警设备列表出错", throwable);
                    mHandler.sendEmptyMessage(ALARM_DEV_LIST_FAILED);
                });
    }

    /**
     * 关注/取消关注
     */
    public void attention(int alarmId, int attention) {
        MyLogUtils.d("attention() alarmId = " + alarmId + "; attention = " + attention);
        final Map<String, Integer> paramMap = new HashMap<>();
        paramMap.put("alarmId", alarmId);
        paramMap.put("attention", attention);
        final String paramStr = new Gson().toJson(paramMap);
        final RequestBody mBody = RequestBody.create(paramStr,
                MediaType.parse("application/json;charset=UTF-8"));
        mAlarmService.attention(mBody)
                .subscribeOn(Schedulers.io())
                .to(RxLife.toMain(mView.getBaseActivity()))
                .subscribe(bean -> {
                    final boolean isSuc = bean != null && bean.code == 0;
                    if (isSuc) {
                        switch (mHandleType) {
                            case 1:
                                for (int i = 0; i < mAlarmInfoList1.size(); i++) {
                                    if (mAlarmInfoList1.get(i).getId() == alarmId) {
                                        mAlarmInfoList1.get(i).setAttention(mAlarmInfoList1.get(i).getAttention() == 1 ? 0 : 1);
                                        break;
                                    }
                                }
                                mAlarmCenterView.showAlarmList(mAlarmInfoList1);
                                break;

                            case 3:
                                for (int i = 0; i < mAlarmInfoList2.size(); i++) {
                                    if (mAlarmInfoList2.get(i).getId() == alarmId) {
                                        mAlarmInfoList2.get(i).setAttention(mAlarmInfoList2.get(i).getAttention() == 1 ? 0 : 1);
                                        break;
                                    }
                                }
                                mAlarmCenterView.showAlarmList(mAlarmInfoList2);
                                break;

                            case 4:
                                for (int i = 0; i < mAlarmInfoList3.size(); i++) {
                                    if (mAlarmInfoList3.get(i).getId() == alarmId) {
                                        mAlarmInfoList3.get(i).setAttention(mAlarmInfoList3.get(i).getAttention() == 1 ? 0 : 1);
                                        break;
                                    }
                                }
                                mAlarmCenterView.showAlarmList(mAlarmInfoList3);
                                break;

                            case 5:
                                for (int i = 0; i < mAlarmInfoList4.size(); i++) {
                                    if (mAlarmInfoList4.get(i).getId() == alarmId) {
                                        mAlarmInfoList4.get(i).setAttention(mAlarmInfoList4.get(i).getAttention() == 1 ? 0 : 1);
                                        break;
                                    }
                                }
                                mAlarmCenterView.showAlarmList(mAlarmInfoList4);
                                break;

                            default:
                                break;
                        }
                        mAlarmCenterView.attentionResult(alarmId);
                    } else {
                        mAlarmCenterView.attentionResult(-1);
                    }
                }, throwable -> {
                    MyLogUtils.e("关注/取消关注出错", throwable);
                    mAlarmCenterView.attentionResult(-1);
                });
    }

    /**
     * app端预警类型统计
     */
    public void getAlarmTypeStatistics() {
        mAlarmService.appAlarmTypeStatistics()
                .subscribeOn(Schedulers.io())
                .to(RxLife.toMain(mView.getBaseActivity()))
                .subscribe(bean -> {
                    final boolean isSuc = bean != null && bean.getCode() == 0 && bean.getData() != null;
                    if (isSuc) {
                        mAlarmCenterView.showAlarmTypeStatisticsData(bean.getData());
                    } else {
                        mAlarmCenterView.showAlarmTypeStatisticsData(null);
                    }
                }, throwable -> {
                    mAlarmCenterView.showAlarmTypeStatisticsData(null);
                    MyLogUtils.e("获取类型统计数据出错", throwable);
                });
    }

    /**
     * 是否有app菜单权限
     */
    public void getUsePermission() {
        mAlarmService.isAppMenu()
                .subscribeOn(Schedulers.io())
                .to(RxLife.toMain(mView.getBaseActivity()))
                .subscribe(bean -> {
                    final boolean isSuc = bean != null && bean.code == 0 && bean.data;
                    mAlarmCenterView.callbackIsAppMenu(isSuc);
                }, throwable -> {
                    mAlarmCenterView.callbackIsAppMenu(false);
                    MyLogUtils.e("获取是否有app菜单权限出错", throwable);
                });
    }

    public void setPageCount(int pageCount) {
        PAGE_COUNT = pageCount;
    }

    public void resetHandler() {
        selectDevIds.clear();
        mStartTime = "";
        mEndTime = "";
        MyVariables.isLockAtAttention = true;
    }

    public boolean getFinishStatus() {
        boolean result = true;
        switch (mHandleType) {
            case 1:
                if (mAlarmListBean1 != null) {
                    result = mAlarmListBean1.isLoadFinish();
                }
                break;
            case 3:
                if (mAlarmListBean2 != null) {
                    result = mAlarmListBean2.isLoadFinish();
                }
                break;
            case 4:
                if (mAlarmListBean3 != null) {
                    result = mAlarmListBean3.isLoadFinish();
                }
                break;
            case 5:
                if (mAlarmListBean4 != null) {
                    result = mAlarmListBean4.isLoadFinish();
                }
                break;

            default:
                break;
        }
        return result;
    }
}
