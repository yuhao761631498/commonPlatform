package com.gdu.command.ui.cases.dealt;

import android.os.Handler;

import com.gdu.baselibrary.base.BasePresenter;
import com.gdu.baselibrary.network.BaseBean;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.command.ui.cases.CaseService;
import com.gdu.model.cases.CommentInfo;
import com.gdu.model.cases.IssueBean;
import com.gdu.model.config.GlobalVariable;
import com.gdu.mqttchat.chat.presenter.ChatService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 处理案件
 */
public class DealtCasePresenter extends BasePresenter {

    private final int GOT_CASE_DETAIL_SUCCEED = 1;
    private final int GOT_CASE_DETAIL_FAILED = 2;
    private final int GOT_RECORDS_SUCCEED = 3;
    private final int GOT_RECORDS_FAILED = 4;
    private final int GOT_COMMENT_SUCCEED = 5;
    private final int GOT_COMMENT_FAILED = 6;

    private IDealtCaseView mDealCaseView;
    private Handler mHandler;
    private Gson mGson;
    private List<CommentInfo> mCommentInfos;
    private IssueBean mCurrentIssueBean;
    private List<Map<String, Object>> mCaseRecords;

    public DealtCasePresenter(){
        mGson = new Gson();
        initHandler();
    }


    /**
     * 加载案件详情，批示，记录
     * @param id
     */
    public void loadDatas(String id){
        loadCaseDetail(id);
    }

    /**
     * 加载批示类容
     * @param id
     */
    private void loadComment(String id){
        Map<String, Object> map  = new HashMap<>();
        map.put("id", id);
        String strEntity = mGson.toJson(map);
        RequestBody body = RequestBody.create(strEntity, MediaType.parse("application/json;charset=UTF-8"));
        CaseService caseService = RetrofitClient.getAPIService(CaseService.class);
        caseService.selectCaseCommentS(body).enqueue(new Callback<BaseBean<List<CommentInfo>>>() {
            @Override
            public void onResponse(Call<BaseBean<List<CommentInfo>>> call, Response<BaseBean<List<CommentInfo>>> response) {
                if (response.body() != null && response.body().data != null) {
                    mCommentInfos = response.body().data;
                    mHandler.sendEmptyMessage(GOT_COMMENT_SUCCEED);
                } else {
                    mHandler.sendEmptyMessage(GOT_COMMENT_FAILED);
                }
                loadCaseRecords(id);
            }

            @Override
            public void onFailure(Call<BaseBean<List<CommentInfo>>> call, Throwable t) {
                mHandler.sendEmptyMessage(GOT_COMMENT_FAILED);
                loadCaseRecords(id);
            }
        });
    }

    /**
     *
     案件记录-处置记录
     * @param id
     */
    private void loadCaseRecords(String id){
        Map<String, Object> map  = new HashMap<>();
        map.put("id", id);
        String strEntity = mGson.toJson(map);
        RequestBody body = RequestBody.create(strEntity, MediaType.parse("application/json;charset=UTF-8"));
        CaseService caseService = RetrofitClient.getAPIService(CaseService.class);
        caseService.selectCaseRecord(body).enqueue(new Callback<BaseBean<Map<String, List<Map<String, Object>>>>>() {
            @Override
            public void onResponse(Call<BaseBean<Map<String, List<Map<String, Object>>>>> call, Response<BaseBean<Map<String, List<Map<String, Object>>>>> response) {
                if (response != null && response.body() != null && response.body().data != null) {
                    mCaseRecords = response.body().data.get("records");
                    if (mCaseRecords != null ) {
                        GlobalVariable.sCaseRecords = mCaseRecords;
                        mHandler.sendEmptyMessage(GOT_RECORDS_SUCCEED);
                        return;
                    }
                }
                mHandler.sendEmptyMessage(GOT_RECORDS_FAILED);
            }

            @Override
            public void onFailure(Call<BaseBean<Map<String, List<Map<String, Object>>>>> call, Throwable t) {
                mHandler.sendEmptyMessage(GOT_COMMENT_FAILED);
            }
        });
    }

    /**
     * 获取案件详情
     */
    private void loadCaseDetail(String id){
        ChatService caseService = RetrofitClient.getAPIService(ChatService.class);
        Map<String, Object> map  = new HashMap<>();
        map.put("id", id);
        String strEntity = mGson.toJson(map);
        RequestBody body = RequestBody.create(strEntity, MediaType.parse("application/json;charset=UTF-8"));
        caseService.getCaseDetail(body).enqueue(new Callback<BaseBean<IssueBean>>() {
            @Override
            public void onResponse(Call<BaseBean<IssueBean>> call, Response<BaseBean<IssueBean>> response) {
                BaseBean issueBean = response.body();
                if (issueBean != null && issueBean.code == 0 && issueBean.data != null) {
                    mCurrentIssueBean = (IssueBean) issueBean.data;
                    mHandler.sendEmptyMessage(GOT_CASE_DETAIL_SUCCEED);
                } else {
                    mHandler.sendEmptyMessage(GOT_CASE_DETAIL_FAILED);
                }
                loadComment(id);
            }

            @Override
            public void onFailure(Call<BaseBean<IssueBean>> call, Throwable t) {
                mHandler.sendEmptyMessage(GOT_CASE_DETAIL_FAILED);
                loadComment(id);
            }
        });
    }

    public void setView(IDealtCaseView dealCaseView){
        mDealCaseView = dealCaseView;
    }

    /**
     * 处理案件详情
     */
    private void dealCaseDetail(){
        String attachment = mCurrentIssueBean.getDisFinishAttachment();
        if (CommonUtils.isEmptyString(attachment)) {
            return;
        }
        String[] paths = attachment.split(",");
        List<String> imageList = new ArrayList<>();
        List<String> videoList = new ArrayList<>();
        if (paths != null && paths.length > 0) {
            for (String path : paths) {
                final String urlStr = CommonUtils.getSinglePicRealPath(path);
                if (path.contains("mp4")) {
                    videoList.add(urlStr);
                } else {
                    imageList.add(urlStr);
                }
            }
            if (imageList.size() > 0) {
                mDealCaseView.showPictures(imageList);
            }
            if (videoList.size() > 0) {
                mDealCaseView.showVideos(videoList);
            }
        }
    }

    private void initHandler() {
        mHandler = new Handler(msg -> {
            switch (msg.what){
                case GOT_CASE_DETAIL_SUCCEED:
                    dealCaseDetail();
                    break;
                case GOT_CASE_DETAIL_FAILED:
                    break;
                case GOT_RECORDS_SUCCEED:
                    mDealCaseView.showRecords();
                    break;
                case GOT_RECORDS_FAILED:

                    break;
                case GOT_COMMENT_FAILED:

                    break;
                case GOT_COMMENT_SUCCEED:
                    mDealCaseView.showComments(mCommentInfos);
                    break;
                default:
                    break;
            }
            return false;
        });
    }
}
