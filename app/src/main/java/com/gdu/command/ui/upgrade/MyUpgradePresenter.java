package com.gdu.command.ui.upgrade;

import android.os.Handler;
import android.os.Message;

import com.gdu.baselibrary.base.BasePresenter;
import com.gdu.baselibrary.network.BaseBean;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.command.event.RefreshHomeEvent;
import com.gdu.command.ui.cases.CaseService;
import com.gdu.command.ui.cases.IMyCaseView;
import com.gdu.command.ui.home.HomePresenter;
import com.gdu.model.cases.CaseStatus;
import com.gdu.model.cases.IssueBean;
import com.gdu.model.cases.MyCaseInfo;
import com.gdu.util.TimeUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
 *
 */
public class MyUpgradePresenter extends BasePresenter {

    private static final int CASE_SUCCEED = 1;
    private static final int CASE_FAILED = 2 ;
    private static final int UPDATE_CASE_STATUS_SUCCEED = 3;
    private static final int UPDATE_CASE_STATUS_FAILED = 4;

    private final int WAIT_COMMENT_CASE_SUCCEED = 6;  //案件批示获取成功
    private final int WAIT_COMMENT_CASE_FAILED = 7;   //案件批示获取失败

    private MyCaseInfo mCurrentNoHandleCaseInfo;  //当前未处理的案件
    private List<IssueBean> mCurrentNoHandleIssueBeans;
    private MyCaseInfo mCurrentWaitCaseInfo;  //当前待处理的案件
    private List<IssueBean> mCurrentWaitIssueBeans;
    private MyCaseInfo mCurrentHandlingCaseInfo;  //当前处理中的案件
    private List<IssueBean> mCurrentHandlingIssueBeans;
    private MyCaseInfo mCurrentHandledCaseInfo;   //当前已处理的案件
    private List<IssueBean> mCurrentHandledIssueBeans;

    private Handler mHandler;
    private Gson mGson;
    private IMyCaseView mCaseView;
    private boolean isOutTime;
    private boolean isLoadMoreData; //是否未加载更多数据

    private Map<String, Double> mCaseCountMap;
    private List<IssueBean> mCurrentWaitCommentIssueBeans; //当前待批示案件列表
    private HomePresenter mHomePresenter;

    public MyUpgradePresenter(){
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
        mHandler = new Handler(){
            @Override
            public void handleMessage(@androidx.annotation.NonNull Message msg) {
                switch (msg.what){
                    case CASE_SUCCEED:
                        int caseStatus = msg.arg1;
                        if (caseStatus == CaseStatus.HANDLED.getKey()) {
                            mCaseView.issueHandled(mCurrentHandledIssueBeans, isLoadMoreData);
                        } else if(caseStatus == CaseStatus.HANDLING.getKey()){
                            mCaseView.issueHandling(mCurrentHandlingIssueBeans, isLoadMoreData);
                        } else {
                            mCaseView.issueNoHandle(mCurrentNoHandleIssueBeans, isLoadMoreData);
                        }
                        break;

                    case CASE_FAILED:
                        if (msg.arg1 == 401) {
                            isOutTime = true;
                            mCaseView.toast("账号已过期, 请重新登录");
                        }
                        break;

                    case UPDATE_CASE_STATUS_SUCCEED:
                        mCaseView.toast("接警成功");
                        EventBus.getDefault().post(new RefreshHomeEvent(CaseStatus.HANDLING.getKey(), ""));
                        loadData();
                        break;

                    case UPDATE_CASE_STATUS_FAILED:
                        mCaseView.toast("接警失败");
                        break;

                    case WAIT_COMMENT_CASE_SUCCEED:
                        mCaseView.isShowWaitView(true);
                        mCaseView.setCommentsCount(mCurrentWaitCommentIssueBeans);
                        break;

                    case WAIT_COMMENT_CASE_FAILED:
                        mCaseView.isShowWaitView(false);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    public void loadData(){
        isOutTime = false;
        isLoadMoreData = false;
//        getCaseCount();
        getCaseByStatus(CaseStatus.NO_HANDLE.getKey(), 1);
        getCaseByStatus(CaseStatus.WAIT_COMMENT.getKey(), 1);
        getCaseByStatus(CaseStatus.HANDLING.getKey(), 1);
        getCaseByStatus(CaseStatus.HANDLED.getKey(), 1);
    }

    /**
     * 加载更多数据
     * @param designateStatus
     */
    public void loadMoreData(int designateStatus){
        int page = -1;
        isLoadMoreData = true;
        if (designateStatus == CaseStatus.NO_HANDLE.getKey()) {
            if (mCurrentNoHandleCaseInfo != null) {
                if (mCurrentNoHandleCaseInfo.getCurrent() < mCurrentNoHandleCaseInfo.getPages()) {
                    page = mCurrentNoHandleCaseInfo.getCurrent() + 1;
                }
            }
        } else if(designateStatus == CaseStatus.HANDLING.getKey()){
                if(mCurrentHandlingCaseInfo.getCurrent() < mCurrentHandlingCaseInfo.getPages()){
                    page = mCurrentHandlingCaseInfo.getCurrent() + 1;
                }
        } else if(designateStatus == CaseStatus.WAIT_COMMENT.getKey()){
                if(mCurrentWaitCaseInfo.getCurrent() < mCurrentWaitCaseInfo.getPages()){
                    page = mCurrentWaitCaseInfo.getCurrent() + 1;
                }
        } else {
            if(mCurrentHandledCaseInfo.getCurrent() < mCurrentHandledCaseInfo.getPages()){
                page = mCurrentHandledCaseInfo.getCurrent() + 1;
            }
        }
        if (page != -1) {
            getCaseByStatus(designateStatus, page);
        } else {
            mCaseView.loadMoreCompleted();
        }
    }

    /**
     * 根据类型获取案件列表
     * @param designateStatus
     */
    public void getCaseByStatus(int designateStatus, int page) {
        Map<String, Object> map  = new HashMap<>();
        map.put("designateStatus", designateStatus);
        map.put("currentPage", page);
        map.put("pageSize", 10);
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
                }
            }

            @Override
            public void onFailure(Call<BaseBean<MyCaseInfo>> call, Throwable e) {
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

    private void dealCaseInfo(BaseBean<MyCaseInfo> listBaseBean, int caseStatus){
        Message message = new Message();
        if (listBaseBean.code == 0) {
            MyCaseInfo caseInfo = listBaseBean.data;
            if (caseStatus == CaseStatus.HANDLED.getKey()) {
                mCurrentHandledCaseInfo = caseInfo;
                addNewCaseToList(mCurrentHandledIssueBeans, caseInfo.getRecords(), caseStatus);
            } else if(caseStatus == CaseStatus.HANDLING.getKey()){
                mCurrentHandlingCaseInfo = caseInfo;
                addNewCaseToList(mCurrentHandlingIssueBeans, caseInfo.getRecords(), caseStatus);
            } else if(caseStatus == CaseStatus.WAIT_COMMENT.getKey()){
                mCurrentWaitCaseInfo = caseInfo;
                addNewCaseToList(mCurrentWaitIssueBeans, caseInfo.getRecords(), caseStatus);
            } else {
                mCurrentNoHandleCaseInfo = caseInfo;
                addNewCaseToList(mCurrentNoHandleIssueBeans, caseInfo.getRecords(), caseStatus);
            }
            message.arg1 = caseStatus;
            message.what = CASE_SUCCEED;
        } else {
            message.what = CASE_FAILED;
        }
        mHandler.sendMessage(message);
    }

    private synchronized void addNewCaseToList(List<IssueBean> currentBeans, List<IssueBean> newBeans, int caseStatus){
        if (newBeans == null) {
            return;
        }
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
    private  synchronized void sortCaseByCreateTime(List<IssueBean> currentBeans){
        ArrayList<IssueBean> arrayList = new ArrayList<IssueBean>(currentBeans);
        Collections.sort(arrayList, new Comparator<IssueBean>() {
            @Override
            public int compare(IssueBean issueBean, IssueBean issueBean1) {
                if (issueBean == null || issueBean1 == null) {
                    return 0;
                }
                String format = "yyyy-MM-dd HH:mm:ss";
                Long time = TimeUtils.getTimeStamp(issueBean.getCreateTime(), format) / (1000);
                Long time1 = TimeUtils.getTimeStamp(issueBean1.getCreateTime(), format) / (1000);
                return -time.compareTo(time1);
            }
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
        Map<String, Object> map  = new HashMap<>();
        map.put("caseId", caseId);
        map.put("designateId", designateId);
        String strEntity = mGson.toJson(map);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"),strEntity);
        CaseService caseService = RetrofitClient.getAPIService(CaseService.class);
        caseService.updateCaseStatus(body).enqueue(new Callback<BaseBean<Object>>() {
            @Override
            public void onResponse(Call<BaseBean<Object>> call, Response<BaseBean<Object>> response) {
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
                mHandler.sendEmptyMessage(UPDATE_CASE_STATUS_FAILED);
            }
        });

    }

    /**
     * 接警成功删除未处置中的案件
     */
    private void removeCaseFromNoHandle(String caseId){
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
    public void getWaitCommentCase(){
        CaseService caseService = RetrofitClient.getAPIService(CaseService.class);
        caseService.getWaitCommentCaseList().enqueue(new Callback<BaseBean<List<IssueBean>>>() {
            @Override
            public void onResponse(Call<BaseBean<List<IssueBean>>> call, Response<BaseBean<List<IssueBean>>> response) {
                final boolean isUnEmptyData =
                        response.body() != null && response.body().code == 0 && response.body().data != null;
                if (isUnEmptyData) {
                    BaseBean<List<IssueBean>> listBaseBean = response.body();
                    mCurrentWaitCommentIssueBeans = listBaseBean.data;
                    if (mCurrentWaitCommentIssueBeans != null) {
                        for (IssueBean currentWaitCommentIssueBean : mCurrentWaitCommentIssueBeans) {
                            currentWaitCommentIssueBean.setCaseStatus(CaseStatus.WAIT_COMMENT.getKey());
                        }
                    }
                    sortCaseByCreateTime(mCurrentWaitCommentIssueBeans);
                    mHandler.sendEmptyMessage(WAIT_COMMENT_CASE_SUCCEED);
                } else {
                    mHandler.sendEmptyMessage(WAIT_COMMENT_CASE_FAILED);
                }
            }

            @Override
            public void onFailure(Call<BaseBean<List<IssueBean>>> call, Throwable t) {
                mHandler.sendEmptyMessage(WAIT_COMMENT_CASE_FAILED);
            }
        });
    }
}
