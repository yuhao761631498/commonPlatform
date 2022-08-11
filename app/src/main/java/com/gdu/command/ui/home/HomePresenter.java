package com.gdu.command.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.gdu.baselibrary.base.BasePresenter;
import com.gdu.baselibrary.base.IBaseDisplay;
import com.gdu.baselibrary.network.BaseBean;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.utils.ActivityManager;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.MMKVUtil;
import com.gdu.baselibrary.utils.SPStringUtils;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.config.GlobalCache;
import com.gdu.command.event.LocationEvent;
import com.gdu.command.event.RefreshHomeEvent;
import com.gdu.command.event.RefreshMyCaseEvent;
import com.gdu.command.ui.alarm.center.AlarmCenterPresenter;
import com.gdu.command.ui.alarm.center.IAlarmCenterView;
import com.gdu.command.ui.cases.CaseService;
import com.gdu.command.ui.home.weather.WeatherBeans;
import com.gdu.command.ui.home.weather.WeatherPresenter;
import com.gdu.command.ui.my.LoginActivity;
import com.gdu.model.cases.CaseStatus;
import com.gdu.model.cases.IssueBean;
import com.gdu.model.cases.MyCaseInfo;
import com.gdu.model.config.GlobalVariable;
import com.gdu.model.config.StorageConfig;
import com.gdu.util.TimeUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePresenter extends BasePresenter {

    private final int CASE_SUCCEED = 1;
    private final int CASE_FAILED = 2;

    private final int WEATHER_GOT = 3;
    private final int CASE_COUNT_GOT = 4;

    /** 案件批示获取成功 */
    private final int WAIT_COMMENT_CASE_SUCCEED = 5;
    /** 案件批示获取失败 */
    private final int WAIT_COMMENT_CASE_FAILED = 6;
    /** 获取角色成功 */
    private final int GET_ROLE_SUCCEED = 7;
    /** 获取角色失败 */
    private final int GET_ROLE_FAILED = 8;

    private final int FINISH_CASE_SUCCEED = 18;
    private final int FINISH_CASE_FAILED = 19;

    private Context mContext;
    private IHomeView mHomeView;
    private Gson mGson;
    private Handler mHandler;
    private Map<String, Double> mCaseCountMap;
    private WeatherPresenter mWeatherPresenter;
    private WeatherBeans mWeatherBeans;
    private String mCurrentCity;

//    private MyCaseInfo mCurrentHandlingCaseInfo;
    /** 当前待处置案件列表 */
    private List<IssueBean> mCurrentHandlingIssueBeans;
    /** 当前待批示案件列表 */
    private List<IssueBean> mCurrentWaitCommentIssueBeans;
    /** 所有的案件信息 */
    private List<IssueBean> mAllIssueBeans;
    private long mStartTimeL;
    private long mEndTimeL;
    private String mStartTime;
    private String mEndTime;
    private AlarmCenterPresenter mAlarmCenterPresenter;


    public HomePresenter(){
        mAlarmCenterPresenter = new AlarmCenterPresenter();
        mAlarmCenterPresenter.setPageCount(20);
        mGson = new Gson();
        mCurrentHandlingIssueBeans = new ArrayList<>();
        mAllIssueBeans = new ArrayList<>();
        EventBus.getDefault().register(this);
        mWeatherPresenter = new WeatherPresenter();
        initHandler();
        initTime();
    }

    @Override
    public void attachView(IBaseDisplay display) {
        super.attachView(display);
        mAlarmCenterPresenter.attachView(display);
    }

    private void initTime() {
        mStartTimeL = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
        mEndTimeL = System.currentTimeMillis();
        mStartTime = TimeUtils.getTime(mStartTimeL, "yyyy/MM/dd HH:mm:ss");
        mEndTime = TimeUtils.getTime(mEndTimeL, "yyyy/MM/dd HH:mm:ss");
    }

    public void setView(Context context, IHomeView homeView, IAlarmCenterView alarmCenterView){
        mContext = context;
        mHomeView = homeView;
        mAlarmCenterPresenter.setView(alarmCenterView);
    }

//    /**
//     *  添加处置中案件到总案件列表
//     */
//    private void addIssueBeansToAllList() {
//        MyLogUtils.d("addIssueBeansToAllList()");
//        for (IssueBean currentHandlingIssueBean : mCurrentHandlingIssueBeans) {
//            boolean isExist = false;
//            for (IssueBean allIssueBean : mAllIssueBeans) {
//                if (allIssueBean.getId().equals(currentHandlingIssueBean.getId())) {
//                    isExist = true;
//                    break;
//                }
//            }
//            if (!isExist) {
//                mAllIssueBeans.add(currentHandlingIssueBean);
//            }
//        }
//    }

    private void initHandler() {
        mHandler = new Handler(msg -> {
            MyLogUtils.d("handleMessage() msgWhat = " + msg.what);
            switch (msg.what){
                case CASE_SUCCEED:
//                       addIssueBeansToAllList();
                    mAllIssueBeans.clear();
                    CommonUtils.listAddAllAvoidNPE(mAllIssueBeans, mCurrentHandlingIssueBeans);
                    CommonUtils.listAddAllAvoidNPE(mAllIssueBeans, mCurrentWaitCommentIssueBeans);
//                       mAllIssueBeans.addAll(mCurrentHandlingIssueBeans);
                    if (mHomeView != null) {
                        mHomeView.showData(mAllIssueBeans);
                    }
                    break;
                case CASE_FAILED:
                    if (mHomeView != null) {
                        mHomeView.showData(mAllIssueBeans);
                    }
                    break;
                case WEATHER_GOT:
                    if (mHomeView != null) {
                        mHomeView.setWeatherBeans(mWeatherBeans);
                    }
                    break;
                case CASE_COUNT_GOT:
                    if (mHomeView != null) {
                        mHomeView.setCaseCount(mCaseCountMap);
                    }
                    break;
                case WAIT_COMMENT_CASE_SUCCEED:
                    mAllIssueBeans.clear();
                    CommonUtils.listAddAllAvoidNPE(mAllIssueBeans, mCurrentWaitCommentIssueBeans);
//                    if (mHomeView != null) {
//                        mHomeView.setCommentsCount(mCurrentWaitCommentIssueBeans.size());
//                    }
                    getHandling();
                    break;
                case WAIT_COMMENT_CASE_FAILED:
                    getHandling();
                    break;
                case GET_ROLE_SUCCEED:
                    if (mHomeView != null) {
                        mHomeView.showComments();
                    }
//                    getWaitCommentCase();
                    getCaseCenter();
                    break;
                case GET_ROLE_FAILED:
                    if (msg.arg1 == 401) {
                        MMKVUtil.putString(SPStringUtils.TOKEN, "NULL");
                        Intent intent1 = new Intent(getContext(), LoginActivity.class);
                        getContext().startActivity(intent1);
                        ActivityManager.getInstance().finishAll();
                        if (mHomeView != null) {
                            mHomeView.showToast("账号已过期, 请重新登录");
                        }
                    } else {
                        getHandling();
                    }
                    break;

                case FINISH_CASE_SUCCEED:
                    mHomeView.showOrHidePb(false, "");
                    EventBus.getDefault().post(new RefreshHomeEvent(CaseStatus.HANDLING.getKey(), GlobalVariable.sCurrentIssueBean.getId()));
                    EventBus.getDefault().post(new RefreshMyCaseEvent(CaseStatus.HANDLING.getKey(), GlobalVariable.sCurrentIssueBean.getId()));
                    break;

                case FINISH_CASE_FAILED:
                    mHomeView.showOrHidePb(false, "");
                    mHomeView.showToast("处置任务失败");
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLocationChange(LocationEvent locationEvent) {
        mCurrentCity = locationEvent.city;
        if (mWeatherBeans != null) {
            return;
        }
        mWeatherPresenter.getWeatherInfo(weatherBeans -> {
            if (weatherBeans == null) {
                return;
            }
            mWeatherBeans = weatherBeans;
            mWeatherBeans.setName(mCurrentCity);
            mHandler.sendEmptyMessage(WEATHER_GOT);
        });
    }

    /**
     * 首先获取请批示文件
     */
    public void loadData(){
        MyLogUtils.d("loadData()");
        getCaseCount();
        if (mAlarmCenterPresenter != null) {
            mAlarmCenterPresenter.getUsePermission();
        }
//        getUserRoleTree();
    }

    public void getUserRoleTree(){
        MyLogUtils.d("getUserRoleTree()");
        CaseService caseService = RetrofitClient.getAPIService(CaseService.class);
        Call<RoleInfoBean> call = caseService.getUserRoleTree();
        call.enqueue(new Callback<RoleInfoBean>() {

            @Override
            public void onResponse(Call<RoleInfoBean> call, Response<RoleInfoBean> response) {
                final RoleInfoBean dataBean = response.body();
                final boolean isUnEmptyData =
                        dataBean != null && dataBean.getCode() == 0 && dataBean.getDataX() != null;
                if (isUnEmptyData) {
                    parseUserRole(dataBean);
                } else {
                    dealError(GET_ROLE_FAILED, response);
                }
            }

            @Override
            public void onFailure(Call<RoleInfoBean> call, Throwable e) {
                mHandler.sendEmptyMessage(GET_ROLE_FAILED);
                MyLogUtils.e("获取用户权限出错", e);
            }
        });
    }

    /**
     * 处理网络错误
     * @param what
     * @param response
     */
    private void dealError(int what, Response response){
        Message message = new Message();
        message.what = what ;
        if (response != null) {
            ResponseBody body = response.errorBody();
            try {
                if (body != null && body.string().contains("401")) {
                    message.arg1 = 401;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mHandler.sendMessage(message);
    }

    /**
     *解析用户权限
     */
    private void parseUserRole(RoleInfoBean baseBean) {
        MyLogUtils.d("parseUserRole()");
        List<List<RoleInfoBean.DataBean>> roles =
                (List<List<RoleInfoBean.DataBean>>) baseBean.getDataX();
        if (roles.size() <= 0) {
            mHandler.sendEmptyMessage(GET_ROLE_FAILED);
            return;
        }
        MyLogUtils.d("parseUserRole() enter normal flow");
        for (List<RoleInfoBean.DataBean> roleList : roles) {
            if (roleList == null) {
                continue;
            }
            for (RoleInfoBean.DataBean roleMap : roleList) {
                if (roleMap == null) {
                    continue;
                }
                if (!"APP菜单".equals(roleMap.getMenuName())) {
                    continue;
                }
                final List<RoleInfoBean.DataBean.RoleMenuListBean> realRoleList = roleMap.getRoleMenuList();
                if (realRoleList == null || realRoleList.size() == 0) {
                    mHandler.sendEmptyMessage(GET_ROLE_FAILED);
                    return;
                }
                for (RoleInfoBean.DataBean.RoleMenuListBean role : realRoleList) {
                    if (role.getMenuName().contains("批示")) {
                        mHandler.sendEmptyMessage(GET_ROLE_SUCCEED);
                        GlobalVariable.isAppMenu = true;
                        return;
                    }
                }
            }
        }
        GlobalVariable.isAppMenu = false;
    }

    /**
     * 再获取处置中
     */
    public void getHandling(){
        MyLogUtils.d("getHandling()");
        getCaseByStatus(CaseStatus.NO_HANDLE.getKey());
    }

    /**
     * 获取待批示案件
     */
    public void getWaitCommentCase(){
        MyLogUtils.d("getWaitCommentCase()");
        CaseService caseService = RetrofitClient.getAPIService(CaseService.class);
        caseService.getWaitCommentCaseList().enqueue(new Callback<BaseBean<List<IssueBean>>>() {
            @Override
            public void onResponse(Call<BaseBean<List<IssueBean>>> call, Response<BaseBean<List<IssueBean>>> response) {
                final BaseBean<List<IssueBean>> listBaseBean = response.body();
                final boolean isUnEmptyData =
                        listBaseBean != null && listBaseBean.code == 0 && listBaseBean.data != null;
                if (isUnEmptyData) {
                    mCurrentWaitCommentIssueBeans = listBaseBean.data;
                    for (IssueBean currentWaitCommentIssueBean : mCurrentWaitCommentIssueBeans) {
                        currentWaitCommentIssueBean.setCaseStatus(CaseStatus.WAIT_COMMENT.getKey());
                    }
                    sortCaseByCreateTime(mCurrentWaitCommentIssueBeans);
                    mHandler.sendEmptyMessage(WAIT_COMMENT_CASE_SUCCEED);
                } else {
                    mHandler.sendEmptyMessage(WAIT_COMMENT_CASE_FAILED);
                }
            }

            @Override
            public void onFailure(Call<BaseBean<List<IssueBean>>> call, Throwable t) {
                MyLogUtils.e("获取案件列表出错", t);
                mHandler.sendEmptyMessage(WAIT_COMMENT_CASE_FAILED);
            }
        });
    }

    /**
     * 获取待批示案件
     */
    public void getCaseCenter(){
        MyLogUtils.d("getCaseCenter()");
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("caseStatus", "");
        paramMap.put("caseType", "");
        paramMap.put("content", "");
        paramMap.put("currentPage", 1);
        paramMap.put("pageSize", 1000);
//        paramMap.put("endTime", mStartTime);
//        paramMap.put("startTime", mEndTime);

        CaseService caseService = RetrofitClient.getAPIService(CaseService.class);
        final String param = new Gson().toJson(paramMap);
        RequestBody body = RequestBody.create(param, okhttp3.MediaType.parse("application/json;charset=UTF-8"));
        caseService.getCaseCenter(body).enqueue(new Callback<CaseInfoBean>() {
            @Override
            public void onResponse(Call<CaseInfoBean> call, Response<CaseInfoBean> response) {
                final CaseInfoBean listBaseBean = response.body();
                final boolean isUnEmptyData =
                        listBaseBean != null && listBaseBean.getCode() == 0
                                && listBaseBean.getData() != null
                                && listBaseBean.getData().getRecords() != null
                                && listBaseBean.getData().getRecords().size() != 0;
                if (isUnEmptyData) {
                    mCurrentWaitCommentIssueBeans = listBaseBean.getData().getRecords();
                    for (IssueBean currentWaitCommentIssueBean : mCurrentWaitCommentIssueBeans) {
                        currentWaitCommentIssueBean.setCaseStatus(CaseStatus.WAIT_COMMENT.getKey());
                    }
                    sortCaseByCreateTime(mCurrentWaitCommentIssueBeans);
                    mHandler.sendEmptyMessage(WAIT_COMMENT_CASE_SUCCEED);
                } else {
                    mHandler.sendEmptyMessage(WAIT_COMMENT_CASE_FAILED);
                }
            }

            @Override
            public void onFailure(Call<CaseInfoBean> call, Throwable t) {
                MyLogUtils.e("获取案件列表出错", t);
                mHandler.sendEmptyMessage(WAIT_COMMENT_CASE_FAILED);
            }
        });
    }

    /**
     * 根据类型获取案件列表
     * @param designateStatus
     */
    public void getCaseByStatus(int designateStatus) {
        MyLogUtils.d("getCaseByStatus() designateStatus = " + designateStatus);
        Map<String, Object> map  = new HashMap<>();
        map.put("designateStatus", designateStatus);
        map.put("currentPage", 1);
        map.put("pageSize", 5);
        String strEntity = mGson.toJson(map);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"),strEntity);
        CaseService caseService = RetrofitClient.getAPIService(CaseService.class);
        caseService.getData(body).enqueue(new Callback<BaseBean<MyCaseInfo>>() {
            @Override
            public void onResponse(Call<BaseBean<MyCaseInfo>> call, Response<BaseBean<MyCaseInfo>> response) {
                BaseBean listBaseBean = response.body();
                final boolean isUnEmptyData =
                        listBaseBean != null && listBaseBean.code == 0 && listBaseBean.data != null;
                if (isUnEmptyData) {
                    dealCaseInfo(listBaseBean, designateStatus);
                } else {
                    mHandler.sendEmptyMessage(CASE_FAILED);
                }
            }

            @Override
            public void onFailure(Call<BaseBean<MyCaseInfo>> call, Throwable t) {
                mHandler.sendEmptyMessage(CASE_FAILED);
            }
        });
    }

    private void dealCaseInfo(BaseBean<MyCaseInfo> listBaseBean, int caseStatus){
        MyLogUtils.d("dealCaseInfo() caseStatus = " + caseStatus);
        Message message = new Message();
        if (caseStatus == CaseStatus.NO_HANDLE.getKey()) {
            MyCaseInfo caseInfo = listBaseBean.data;
            if (caseInfo == null) {
                mHandler.sendEmptyMessage(CASE_FAILED);
                return;
            }
//            mCurrentHandlingCaseInfo = caseInfo;
            addNewCaseToList(mCurrentHandlingIssueBeans, caseInfo.getRecords(), caseStatus);
        }
        sortCaseByCreateTime(mCurrentHandlingIssueBeans);
        message.arg1 = caseStatus;
        message.what = CASE_SUCCEED;
        mHandler.sendMessage(message);
    }

    private void addNewCaseToList(List<IssueBean> currentBeans, List<IssueBean> newBeans, int caseStatus){
        MyLogUtils.d("addNewCaseToList() curListSize = " + currentBeans.size() + "; newListSize = "
                + newBeans.size() + "; caseStatus = " + caseStatus);
        for (IssueBean newBean : newBeans) {
            boolean isExist = false;
            newBean.setCaseStatus(caseStatus);
            for (IssueBean currentBean : currentBeans) {
                if (newBean.getId().equals(currentBean.getId())) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                currentBeans.add(newBean);
            }
        }
    }

    /**
     * 查询天气
     */
    public void queryWeather(){
        MyLogUtils.d("queryWeather()");
        if (TextUtils.isEmpty(mCurrentCity)) {
            mCurrentCity = MMKVUtil.getString(StorageConfig.GD_CITY, "");
        }
        if (GlobalCache.sWeatherBeans != null) {
            mWeatherBeans = GlobalCache.sWeatherBeans;
            mHandler.sendEmptyMessage(WEATHER_GOT);
            return;
        }
        mWeatherPresenter.getWeatherInfo(weatherBeans -> {
            if (weatherBeans == null) {
                return;
            }
            mWeatherBeans = weatherBeans;
            mWeatherBeans.setName(mCurrentCity);
            GlobalCache.sWeatherBeans = weatherBeans;
            mHandler.sendEmptyMessage(WEATHER_GOT);
        });
    }

    /**
     * 获取案件统计
     */
    public void getCaseCount(){
        MyLogUtils.d("getCaseCount()");
        CaseService caseService = RetrofitClient.getAPIService(CaseService.class);
        caseService.selectCountCaseAction().enqueue(new Callback<BaseBean<Map<String, Double>>>() {
            @Override
            public void onResponse(Call<BaseBean<Map<String, Double>>> call, Response<BaseBean<Map<String, Double>>> response) {
                BaseBean<Map<String, Double>> baseBean = response.body();
                final boolean isUnEmptyData =
                        baseBean != null && baseBean.code == 0 && baseBean.data != null;
                if (isUnEmptyData) {
                    mCaseCountMap = baseBean.data;
                    mHandler.sendEmptyMessage(CASE_COUNT_GOT);
                }
            }

            @Override
            public void onFailure(Call<BaseBean<Map<String, Double>>> call, Throwable t) {
                mHandler.sendEmptyMessage(CASE_FAILED);
            }
        });
    }

    /**
     * 对案件进行时间排序
     * @param currentBeans
     */
    private  synchronized void sortCaseByCreateTime(List<IssueBean> currentBeans){
        MyLogUtils.d("sortCaseByCreateTime() listSize = " + currentBeans.size());
        if (android.os.Build.VERSION.SDK_INT <= 23 || currentBeans == null) {
            return;
        }
        Collections.sort(currentBeans, (issueBean, issueBean1) -> {
            final boolean emptyData = issueBean == null || issueBean1 == null
                    || CommonUtils.isEmptyString(issueBean.getReportTime()) ||
                    CommonUtils.isEmptyString(issueBean1.getReportTime());
            if (emptyData) {
                return 0;
            }
            String format = "yyyy-MM-dd HH:mm:ss";
            Long time = TimeUtils.getTimeStamp(issueBean.getReportTime(), format) / (1000);
            Long time1 = TimeUtils.getTimeStamp(issueBean1.getReportTime(), format) / (1000);
            return time.compareTo(time1);
        });
    }

    /**
     * 移除已处置的案件
     * @param type
     * @param removeCaseId
     */
    public void removeCase(int type, String removeCaseId) {
        removeCase(mAllIssueBeans, removeCaseId);
        if (type == CaseStatus.HANDLING.getKey()) {
            removeCase(mCurrentHandlingIssueBeans, removeCaseId);
        }
    }

    /**
     * 从列表中移除案件
     * @param issueBeans
     * @param removeCaseId
     */
    public void removeCase(List<IssueBean> issueBeans, String removeCaseId){
        int index = -1;
        for (int i = 0; i < issueBeans.size(); i++) {
            IssueBean issueBean = issueBeans.get(i);
            if (removeCaseId.equals(issueBean.getId())) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            issueBeans.remove(index);
        }
    }

    /**
     * 结案
     */
    public void finishCase(String record) {
        IssueBean bean = GlobalVariable.sCurrentIssueBean;
        Map<String, Object> map  = new HashMap<>();
        map.put("id", bean.getId());
        String time = TimeUtils.getCurrentTime();
        final int mUserID = (int) MMKVUtil.getInt(SPStringUtils.USER_ID, -1);
        map.put("dispositionTime", time);
        map.put("dispositionRecord", record);
        map.put("dispositionMan", mUserID);
        map.put("disFinishAttachment", "");

        String strEntity = mGson.toJson(map);
        RequestBody body = RequestBody.create(strEntity, okhttp3.MediaType.parse("application" +
                "/json;charset=UTF-8"));
        CaseService caseService = RetrofitClient.getAPIService(CaseService.class);
        caseService.finishCase(body).enqueue(new Callback<BaseBean<Object>>() {
            @Override
            public void onResponse(Call<BaseBean<Object>> call, Response<BaseBean<Object>> response) {
                BaseBean myCaseInfoBaseBean = response.body();
                final boolean isUnEmptyData =
                        myCaseInfoBaseBean != null && myCaseInfoBaseBean.code == 0 && myCaseInfoBaseBean.data != null;
                if (isUnEmptyData) {
                    mHandler.sendEmptyMessage(FINISH_CASE_SUCCEED);
                } else {
                    mHandler.sendEmptyMessage(FINISH_CASE_FAILED);
                }
            }

            @Override
            public void onFailure(Call<BaseBean<Object>> call, Throwable t) {
                mHandler.sendEmptyMessage(FINISH_CASE_FAILED);
                MyLogUtils.e("结案出错", t);
            }
        });
    }

    public void loadDataNew(int curAlarmType) {
        mAlarmCenterPresenter.loadDataNew(curAlarmType);
    }

    public boolean getFinishStatus() {
        return mAlarmCenterPresenter.getFinishStatus();
    }

}
