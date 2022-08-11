package com.gdu.command.ui.cases;

import android.os.Handler;
import android.os.Message;

import com.gdu.baselibrary.base.BasePresenter;
import com.gdu.baselibrary.network.BaseBean;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.MMKVUtil;
import com.gdu.baselibrary.utils.SPStringUtils;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.event.RefreshHomeEvent;
import com.gdu.command.event.RefreshMyCaseEvent;
import com.gdu.command.ui.home.HomePresenter;
import com.gdu.model.cases.CaseStatus;
import com.gdu.model.cases.IssueBean;
import com.gdu.model.cases.MyCaseInfo;
import com.gdu.model.config.GlobalVariable;
import com.gdu.util.TimeUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * @author by DELL
 * @date on 2018/4/12
 */
public class MyCasesPresenter extends BasePresenter {

    private static final int CASE_SUCCEED = 1;
    private static final int CASE_FAILED = 2 ;
    private static final int UPDATE_CASE_STATUS_SUCCEED = 3;
    private static final int UPDATE_CASE_STATUS_FAILED = 4;

    /** 案件批示获取成功 */
    private final int WAIT_COMMENT_CASE_SUCCEED = 6;
    /** 案件批示获取失败 */
    private final int WAIT_COMMENT_CASE_FAILED = 7;

    private final int FINISH_CASE_SUCCEED = 81;
    private final int FINISH_CASE_FAILED = 82;

    /** 当前未处理的案件 */
    private MyCaseInfo mCurrentNoHandleCaseInfo;
    private List<IssueBean> mCurrentNoHandleIssueBeans;
    /** 当前待处理的案件 */
    private MyCaseInfo mCurrentWaitCaseInfo;
    private List<IssueBean> mCurrentWaitIssueBeans;
    /** 当前处理中的案件 */
    private MyCaseInfo mCurrentHandlingCaseInfo;
    private List<IssueBean> mCurrentHandlingIssueBeans;
    /** 当前已处理的案件 */
    private MyCaseInfo mCurrentHandledCaseInfo;
    private List<IssueBean> mCurrentHandledIssueBeans;

    private Handler mHandler;
    private Gson mGson;
    private IMyCaseView mCaseView;
    /** 是否未加载更多数据 */
    private boolean isLoadMoreData;

    private Map<String, Double> mCaseCountMap;
    /** 当前待批示案件列表 */
    private List<IssueBean> mCurrentWaitCommentIssueBeans;
    private HomePresenter mHomePresenter;

    public MyCasesPresenter(){
        mHomePresenter = new HomePresenter();
        mGson = new Gson();
        initData();
        initHandler();
    }

    private void initData() {
        mCurrentNoHandleIssueBeans = new CopyOnWriteArrayList<>();
        mCurrentHandlingIssueBeans = new CopyOnWriteArrayList<>();
        mCurrentWaitIssueBeans = new CopyOnWriteArrayList<>();
        mCurrentHandledIssueBeans = new CopyOnWriteArrayList<>();
    }

    public void initView(IMyCaseView myCaseView) {
        mCaseView = myCaseView;
    }

    private void initHandler() {
        mHandler = new Handler(msg -> {
            switch (msg.what){
                case CASE_SUCCEED:
                    int caseStatus = msg.arg1;
                    if (caseStatus == CaseStatus.HANDLED.getKey()) {
                        mCaseView.issueHandled(mCurrentHandledIssueBeans, isLoadMoreData);
                    } else if(caseStatus == CaseStatus.HANDLING.getKey()){
                        mCaseView.issueHandling(mCurrentHandlingIssueBeans, isLoadMoreData);
                    } else if (caseStatus == CaseStatus.WAIT_COMMENT.getKey()) {
                        mCaseView.issueWaitHandled(mCurrentWaitIssueBeans, isLoadMoreData);
                    } else {
                        mCaseView.issueNoHandle(mCurrentNoHandleIssueBeans, isLoadMoreData);
                    }
                    isLoadMoreData = false;
                    break;

                case CASE_FAILED:
                    if (msg.arg1 == 401) {
                        mCaseView.toast("账号已过期, 请重新登录");
                    } else {
                        mCaseView.toast("获取列表数据失败");
                    }
                    isLoadMoreData = false;
                    break;

                case UPDATE_CASE_STATUS_SUCCEED:
                    mCaseView.toast("接警成功");
                    EventBus.getDefault().post(new RefreshHomeEvent(CaseStatus.NO_HANDLE.getKey(), ""));
                    loadData();
                    getWaitCommentCase();
                    break;

                case UPDATE_CASE_STATUS_FAILED:
                    mCaseView.toast("接警失败");
                    break;

                case WAIT_COMMENT_CASE_SUCCEED:
                    mCaseView.isShowWaitView(true);
                    mCaseView.setCommentsCount(mCurrentWaitCommentIssueBeans);
                    break;

                case WAIT_COMMENT_CASE_FAILED:
                    if (msg.arg1 == 401) {
                        mCaseView.toast("账号已过期, 请重新登录");
                    } else {
                        mCaseView.isShowWaitView(false);
                        mCaseView.toast("获取列表数据失败");
                    }
                    break;

                case FINISH_CASE_SUCCEED:
                    mCaseView.showOrHidePb(false, "");
                    EventBus.getDefault().post(new RefreshHomeEvent(CaseStatus.HANDLING.getKey(), GlobalVariable.sCurrentIssueBean.getId()));
                    EventBus.getDefault().post(new RefreshMyCaseEvent(CaseStatus.HANDLING.getKey(), GlobalVariable.sCurrentIssueBean.getId()));
                    break;

                case FINISH_CASE_FAILED:
                    mCaseView.showOrHidePb(false, "");
                    mCaseView.toast("处置任务失败");
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    public void loadData(){
        MyLogUtils.i("loadData()");
        isLoadMoreData = false;
//        getCaseCount();

        if (mCurrentNoHandleCaseInfo != null) {
            mCurrentNoHandleCaseInfo.setLoadFinish(false);
        }
        if (mCurrentHandlingCaseInfo != null) {
            mCurrentHandlingCaseInfo.setLoadFinish(false);
        }
        if (mCurrentHandledCaseInfo != null) {
            mCurrentHandledCaseInfo.setLoadFinish(false);
        }

        getCaseByStatus(CaseStatus.NO_HANDLE.getKey(), 1);
//        getCaseByStatus(CaseStatus.WAIT_COMMENT.getKey(), 1);
        getCaseByStatus(CaseStatus.HANDLING.getKey(), 1);
        getCaseByStatus(CaseStatus.HANDLED.getKey(), 1);
    }

    /**
     * 加载更多数据
     * @param designateStatus 处置状态: 0 待处置; 1 处置中; 2 已归档
     */
    public void loadMoreData(int designateStatus) {
        MyLogUtils.i("loadMoreData() designateStatus = " + designateStatus);
        int page = -1;
        isLoadMoreData = true;
        if (designateStatus == CaseStatus.NO_HANDLE.getKey()) {
            if (mCurrentNoHandleCaseInfo != null) {
                page = mCurrentNoHandleCaseInfo.getCurrent() + 1;
            }
        } else if (designateStatus == CaseStatus.HANDLING.getKey()) {
            if (mCurrentHandlingCaseInfo != null) {
                page = mCurrentHandlingCaseInfo.getCurrent() + 1;
            }
        } else if (designateStatus == CaseStatus.WAIT_COMMENT.getKey()) {
            if (mCurrentWaitCaseInfo != null) {
                page = mCurrentWaitCaseInfo.getCurrent() + 1;
            }
        } else {
            if (mCurrentHandledCaseInfo != null) {
                page = mCurrentHandledCaseInfo.getCurrent() + 1;
            }
        }
        if (page != -1) {
            if (designateStatus == CaseStatus.WAIT_COMMENT.getKey()) {
                getWaitCommentCase();
            } else {
                getCaseByStatus(designateStatus, page);
            }
        } else {
            mCaseView.loadMoreCompleted();
        }
    }

    /**
     * 根据类型获取案件列表
     * @param designateStatus 处置状态: 0 待处置; 1 处置中; 2 已归档
     */
    public void getCaseByStatus(int designateStatus, int page) {
        MyLogUtils.i("getCaseByStatus() designateStatus = " + designateStatus + "; page = " + page);
        final Map<String, Object> map  = new HashMap<>();
        map.put("designateStatus", designateStatus);
        map.put("currentPage", page);
        map.put("pageSize", 10);
        final String strEntity = mGson.toJson(map);
        final RequestBody body = RequestBody.create(strEntity, okhttp3.MediaType.parse("application/json;" +
                "charset=UTF-8"));
        final CaseService caseService = RetrofitClient.getAPIService(CaseService.class);
        caseService.getData(body).enqueue(new Callback<BaseBean<MyCaseInfo>>() {
            @Override
            public void onResponse(Call<BaseBean<MyCaseInfo>> call, Response<BaseBean<MyCaseInfo>> response) {
                MyLogUtils.i("getData onResponse()");
                BaseBean listBaseBean = response.body();
                final boolean isUnEmptyData =
                        listBaseBean != null && listBaseBean.code == 0 && listBaseBean.data != null;
                if (isUnEmptyData) {
                    dealCaseInfo(listBaseBean, designateStatus, page);
                } else {
                    Message message = new Message();
                    if (listBaseBean != null && listBaseBean.code == 401) {
                        message.arg1 = 401;
                    }
                    message.what = CASE_FAILED;
                    mHandler.sendMessage(message);
                }
            }

            @Override
            public void onFailure(Call<BaseBean<MyCaseInfo>> call, Throwable e) {
                MyLogUtils.i("getData onFailure()");
                MyLogUtils.e("获取案件数据出错", e);
                if(e instanceof HttpException){
                    HttpException exception = (HttpException) e;
                    dealError(CASE_FAILED, exception.response());
                }
            }
        });
    }

    /**
     * 处理网络错误
     * @param what
     * @param response
     */
    private void dealError(int what, Response response){
        MyLogUtils.i("dealError() what = " + what);
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

    private void dealCaseInfo(BaseBean<MyCaseInfo> listBaseBean, int caseStatus, int page){
        MyLogUtils.i("dealCaseInfo() caseStatus = " + caseStatus + "; page = " + page);
        Message message = new Message();
        if (listBaseBean.code == 0 && listBaseBean.data != null) {
            final MyCaseInfo caseInfo = listBaseBean.data;
            final List<IssueBean> data = caseInfo.getRecords();
            if (caseStatus == CaseStatus.HANDLED.getKey()) {
                mCurrentHandledCaseInfo = caseInfo;
                resultHandle(mCurrentHandledCaseInfo, page, mCurrentHandledIssueBeans, data, caseStatus);
            } else if(caseStatus == CaseStatus.HANDLING.getKey()){
                mCurrentHandlingCaseInfo = caseInfo;
                resultHandle(mCurrentHandlingCaseInfo, page, mCurrentHandlingIssueBeans, data, caseStatus);
            } else if(caseStatus == CaseStatus.WAIT_COMMENT.getKey()){
                mCurrentWaitCaseInfo = caseInfo;
                resultHandle(mCurrentWaitCaseInfo, page, mCurrentWaitIssueBeans, data, caseStatus);
            } else {
                mCurrentNoHandleCaseInfo = caseInfo;
                resultHandle(mCurrentNoHandleCaseInfo, page, mCurrentNoHandleIssueBeans, data, caseStatus);
            }
            message.arg1 = caseStatus;
            message.what = CASE_SUCCEED;
        } else {
            message.what = CASE_FAILED;
        }
        mHandler.sendMessage(message);
    }

    private void resultHandle(MyCaseInfo mCaseInfo, int page, List<IssueBean> oldData, List<IssueBean> newData,
                              int caseStatus) {
        if (CommonUtils.isEmptyList(newData)) {
            mCaseInfo.setLoadFinish(true);
        } else {
            mCaseInfo.setCurrent(page);
        }
        if (page == 1) {
            oldData.clear();
        }
        addNewCaseToList(oldData, newData, caseStatus);
    }

    private synchronized void addNewCaseToList(List<IssueBean> currentBeans, List<IssueBean> newBeans, int caseStatus){
        MyLogUtils.i("addNewCaseToList() curDataSize = " + currentBeans.size() + "; caseStatus = " + caseStatus);
        if (newBeans == null) {
            return;
        }
        MyLogUtils.i("addNewCaseToList() newDataSize = " + newBeans.size() + "; caseStatus = " + caseStatus);
        for (IssueBean newBean : newBeans) {
            boolean isExist = false;
            newBean.setCaseStatus(caseStatus);
            for (IssueBean currentBean : currentBeans) {
                if (newBean.getId().equals(currentBean.getId())) {
                    isExist = true;
                }
            }
            if (!isExist) {
                currentBeans.add(newBean);
            }
        }
        sortCaseByCreateTime(currentBeans);
    }

    /**
     *
     * @param currentBeans
     */
    private synchronized void sortCaseByCreateTime(List<IssueBean> currentBeans){
        MyLogUtils.i("sortCaseByCreateTime() curDataSize = " + currentBeans.size());
        ArrayList<IssueBean> arrayList = new ArrayList<IssueBean>(currentBeans);
        Collections.sort(arrayList, (issueBean, issueBean1) -> {
            if (issueBean == null || issueBean1 == null) {
                return 0;
            }
            String format = "yyyy-MM-dd HH:mm:ss";
            Long time = TimeUtils.getTimeStamp(issueBean.getCreateTime(), format) / (1000);
            Long time1 = TimeUtils.getTimeStamp(issueBean1.getCreateTime(), format) / (1000);
            return -time.compareTo(time1);
        });
        currentBeans = arrayList;
    }

    public void updateCaseStatus(String caseId, int designateId){
        updateCaseStatusDo(caseId, designateId);
    }

    /**
     * 接警
     * @param caseId
     * @param designateId
     */
    public void updateCaseStatusDo(String caseId, int designateId) {
        MyLogUtils.i("updateCaseStatusDo() caseId = " + caseId + "; designateId = " + designateId);
        Map<String, Object> map  = new HashMap<>();
        map.put("caseId", caseId);
        map.put("designateId", designateId);
        String strEntity = mGson.toJson(map);
        RequestBody body = RequestBody.create(strEntity, okhttp3.MediaType.parse("application/json;charset=UTF-8"));
        CaseService caseService = RetrofitClient.getAPIService(CaseService.class);
        caseService.updateCaseStatus(body).enqueue(new Callback<BaseBean<Object>>() {
            @Override
            public void onResponse(Call<BaseBean<Object>> call, Response<BaseBean<Object>> response) {
                MyLogUtils.i("updateCaseStatus onResponse()");
                BaseBean myCaseInfoBaseBean = response.body();
                final boolean isUnEmptyData =
                        myCaseInfoBaseBean != null && myCaseInfoBaseBean.code == 0 && myCaseInfoBaseBean.data != null;
                if (isUnEmptyData) {
                    removeCaseFromNoHandle(caseId);
                    mHandler.sendEmptyMessage(UPDATE_CASE_STATUS_SUCCEED);
                } else {
                    mHandler.sendEmptyMessage(UPDATE_CASE_STATUS_FAILED);
                }
            }

            @Override
            public void onFailure(Call<BaseBean<Object>> call, Throwable t) {
                MyLogUtils.i("updateCaseStatus onFailure()");
                MyLogUtils.e("更新案件状态出错", t);
                mHandler.sendEmptyMessage(UPDATE_CASE_STATUS_FAILED);
            }
        });

    }

    /**
     * 接警成功删除未处置中的案件
     */
    private void removeCaseFromNoHandle(String caseId){
        MyLogUtils.i("removeCaseFromNoHandle() caseId = " + caseId);
        int removeIndex = -1;
        for (int i = 0; i < mCurrentNoHandleIssueBeans.size(); i++) {
            IssueBean issueBean = mCurrentNoHandleIssueBeans.get(i);
            if (issueBean.getId().equals(caseId)) {
                removeIndex = i;
            }
        }
        if (removeIndex != -1) {
            mCurrentNoHandleIssueBeans.remove(removeIndex);
        }
    }

    /**
     * 从处理中移除案件
     * @param caseId
     */
    public void removeCaseFromHandling(String caseId){
        MyLogUtils.i("removeCaseFromHandling() caseId = " + caseId);
        int removeIndex = -1;
        for (int i = 0; i < mCurrentHandlingIssueBeans.size(); i++) {
            IssueBean issueBean = mCurrentHandlingIssueBeans.get(i);
            if (issueBean.getId().equals(caseId)) {
                removeIndex = i;
            }
        }
        if (removeIndex != -1) {
            mCurrentHandlingIssueBeans.remove(removeIndex);
        }
    }

    /**
     * 获取待批示案件
     */
    public void getWaitCommentCase() {
        MyLogUtils.i("getWaitCommentCase()");
        CaseService caseService = RetrofitClient.getAPIService(CaseService.class);
        caseService.getWaitCommentCaseList().enqueue(new Callback<BaseBean<List<IssueBean>>>() {
            @Override
            public void onResponse(Call<BaseBean<List<IssueBean>>> call, Response<BaseBean<List<IssueBean>>> response) {
                MyLogUtils.i("getWaitCommentCaseList onResponse()");
                if (response == null || response.body() == null) {
                    mHandler.sendEmptyMessage(WAIT_COMMENT_CASE_FAILED);
                    return;
                }
                final BaseBean<List<IssueBean>> listBaseBean = response.body();
                final boolean isUnEmptyData =
                        listBaseBean != null && listBaseBean.code == 0 && listBaseBean.data != null;
                if (!isUnEmptyData) {
                    Message message = new Message();
                    if (listBaseBean.code == 401) {
                        message.arg1 = 401;
                    }
                    message.what = WAIT_COMMENT_CASE_FAILED;
                    mHandler.sendMessage(message);
                    return;
                }
                mCurrentWaitCommentIssueBeans = listBaseBean.data;
                if (mCurrentWaitCommentIssueBeans != null) {
                    for (IssueBean currentWaitCommentIssueBean : mCurrentWaitCommentIssueBeans) {
                        currentWaitCommentIssueBean.setCaseStatus(CaseStatus.WAIT_COMMENT.getKey());
                    }
                }
                sortCaseByCreateTime(mCurrentWaitCommentIssueBeans);
                mHandler.sendEmptyMessage(WAIT_COMMENT_CASE_SUCCEED);
            }

            @Override
            public void onFailure(Call<BaseBean<List<IssueBean>>> call, Throwable t) {
                MyLogUtils.i("getWaitCommentCaseList onFailure()");
                MyLogUtils.e("获取待批示案件数据出错", t);
                mHandler.sendEmptyMessage(WAIT_COMMENT_CASE_FAILED);
            }
        });
    }

    /**
     * 结案
     */
    public void finishCase(String record) {
        MyLogUtils.i("finishCase() record = " + record);
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
                MyLogUtils.i("finishCase onResponse()");
                BaseBean myCaseInfoBaseBean = response.body();
                final boolean isUnEmptyData =
                        myCaseInfoBaseBean != null && myCaseInfoBaseBean.code == 0 && myCaseInfoBaseBean.data != null;
                if (isUnEmptyData) {
                    mHandler.sendEmptyMessage(FINISH_CASE_SUCCEED);
                } else {
                    if (myCaseInfoBaseBean != null) {
                        if (myCaseInfoBaseBean.code == 88531003 || myCaseInfoBaseBean.data.equals("该案件已结案")) {
                            mCaseView.showOrHidePb(false, "");
                            mCaseView.toast("该案件已结案");
                        }
                    } else {
                        mHandler.sendEmptyMessage(FINISH_CASE_FAILED);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseBean<Object>> call, Throwable t) {
                MyLogUtils.i("finishCase onFailure()");
                MyLogUtils.e("结案出错", t);
                mHandler.sendEmptyMessage(FINISH_CASE_FAILED);
            }
        });
    }

    public MyCaseInfo getCurrentNoHandleCaseInfo() {
        return mCurrentNoHandleCaseInfo;
    }

    public MyCaseInfo getCurrentWaitCaseInfo() {
        return mCurrentWaitCaseInfo;
    }

    public MyCaseInfo getCurrentHandlingCaseInfo() {
        return mCurrentHandlingCaseInfo;
    }

    public MyCaseInfo getCurrentHandledCaseInfo() {
        return mCurrentHandledCaseInfo;
    }
}
