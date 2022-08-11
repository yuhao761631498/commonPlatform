package com.gdu.command.ui.cases.deal;

import android.os.Handler;

import com.gdu.baselibrary.base.BasePresenter;
import com.gdu.baselibrary.network.BaseBean;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.MMKVUtil;
import com.gdu.baselibrary.utils.SPStringUtils;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.event.RefreshHomeEvent;
import com.gdu.command.ui.cases.CaseService;
import com.gdu.command.uploadpic.IUploadPicDiaryView;
import com.gdu.command.uploadpic.UploadPicPresenter;
import com.gdu.model.cases.CaseStatus;
import com.gdu.model.cases.CommentInfo;
import com.gdu.model.cases.IssueBean;
import com.gdu.model.config.GlobalVariable;
import com.gdu.util.TimeUtils;
import com.google.gson.Gson;
import com.rxjava.rxlife.RxLife;

import org.greenrobot.eventbus.EventBus;

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
 * 处理案件
 */
public class DealCasePresenter extends BasePresenter {

//    private final int CASE_FILE_SUCCEED = 1;
//    private final int CASE_FILE_FAILED = 11;
    private final int DESIGNATE_CASE_SUCCEED = 2;
    private final int DESIGNATE_CASE_FAILED = 21;
    private final int FINISH_CASE_SUCCEED = 3;
    private final int FINISH_CASE_FAILED = 31;
    private final int COMMENT_CASE_SUCCEED = 4;
    private final int COMMENT_CASE_FAILED = 41;
    private final int GOT_COMMENT_SUCCEED = 5;
    private final int GOT_COMMENT_FAILED = 6;

    private IDealCaseView mDealCaseView;
    private Handler mHandler;
    private Gson mGson;
    /** 上传图片后的Url */
    private String mCurrentFileUrls;
    /** 是否完成取证流程 */
    private boolean isCaseDesignateCompleted;
    /** 是否处在结案流程 */
    private boolean isStartFinishCase;
    /** 结案内容 */
    private String mCurrentComment;
    /** 批示列表数据 */
    private List<CommentInfo> mCommentInfos;

    private UploadPicPresenter mUploadPicPresenter;

    public DealCasePresenter(){
        mUploadPicPresenter = new UploadPicPresenter();
        mGson = new Gson();
        initHandler();
    }

    public void setUploadPicDiaryView(IUploadPicDiaryView view) {
        if (view != null) {
            mUploadPicPresenter.attachView(this);
            mUploadPicPresenter.setIView(view);
        }
    }

    /**
     * 加载批示类容
     * @param id
     */
    public void loadComment(String id){
        Map<String, Object> map  = new HashMap<>();
        map.put("id", id);
        String strEntity = mGson.toJson(map);
        RequestBody body = RequestBody.create(strEntity, okhttp3.MediaType.parse("application" +
                "/json;charset=UTF-8"));
        CaseService caseService = RetrofitClient.getAPIService(CaseService.class);
        caseService.selectCaseCommentS(body).enqueue(new Callback<BaseBean<List<CommentInfo>>>() {
            @Override
            public void onResponse(Call<BaseBean<List<CommentInfo>>> call, Response<BaseBean<List<CommentInfo>>> response) {
                final boolean isUnEmptyData =
                        response != null && response.body() != null && response.body().data != null;
                if (isUnEmptyData) {
                    mCommentInfos = response.body().data;
                    if (mCommentInfos != null ) {
                        mHandler.sendEmptyMessage(GOT_COMMENT_SUCCEED);
                        return;
                    }
                }
                mHandler.sendEmptyMessage(GOT_COMMENT_FAILED);
            }

            @Override
            public void onFailure(Call<BaseBean<List<CommentInfo>>> call, Throwable t) {
                mHandler.sendEmptyMessage(GOT_COMMENT_FAILED);
            }
        });
    }

    public void setView(IDealCaseView dealCaseView){
        mDealCaseView = dealCaseView;
    }

    private void initHandler() {
        mHandler = new Handler(msg -> {
            switch (msg.what){
//                case CASE_FILE_SUCCEED:
//                    dealCase();
//                    break;
//                case CASE_FILE_FAILED:
//                    mDealCaseView.showToast("文件上传失败");
//                    break;
                case DESIGNATE_CASE_SUCCEED:
//                       EventBus.getDefault().post(new RefreshHomeEvent(CaseStatus.HANDLING.getKey(), GlobalVariable.sCurrentIssueBean.getId()));
                    mDealCaseView.onForensicsFinished();
                    isCaseDesignateCompleted = true;
                    if (isStartFinishCase) {
                        finishCase(mCurrentComment);
                    }
                    break;
                case DESIGNATE_CASE_FAILED:
                    mDealCaseView.showToast("取证失败");
                    isCaseDesignateCompleted = true;
                    if (isStartFinishCase) {
                        finishCase(mCurrentComment);
                    }
                    break;
                case FINISH_CASE_SUCCEED:
                    mDealCaseView.onCaseFinished();
                    break;
                case FINISH_CASE_FAILED:
                    mDealCaseView.showToast("处置任务失败");
                    break;
                case COMMENT_CASE_SUCCEED:
                    EventBus.getDefault().post(new RefreshHomeEvent(CaseStatus.WAIT_COMMENT.getKey(), GlobalVariable.sCurrentIssueBean.getId()));
                    mDealCaseView.onCaseCommented();
                    break;
                case COMMENT_CASE_FAILED:
                    mDealCaseView.showToast("批示任务失败");
                    break;
                case GOT_COMMENT_FAILED:

                    break;
                case GOT_COMMENT_SUCCEED:
                    mDealCaseView.showComments(mCommentInfos);
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    /**
     * 结案
     */
    public void finishCase(String record){
        isStartFinishCase = true;
        mCurrentComment = record;
        if (!isCaseDesignateCompleted) {
           return;
        }
        IssueBean bean = GlobalVariable.sCurrentIssueBean;
        Map<String, Object> map  = new HashMap<>();
        map.put("id", bean.getId());
        String time = TimeUtils.getCurrentTime();
        final int mUserID = (int) MMKVUtil.getInt(SPStringUtils.USER_ID, -1);
        map.put("dispositionTime", time);
        map.put("dispositionRecord", record);
        map.put("dispositionMan", mUserID);
        map.put("disFinishAttachment", mCurrentFileUrls);

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
            }
        });
    }

    private List<String> getImageUrls(){
        List<String> allUrls = new ArrayList<>();
        if (mCurrentFileUrls == null) {
            return null;
        }
        String[] urls = mCurrentFileUrls.split(",");
        String imageUrl = "";
        String videoUrl = "";
        if (urls != null && urls.length > 0) {
            for (String url : urls) {
                if (url.contains("mp4")) {
                    if (CommonUtils.isEmptyString(videoUrl)) {
                        videoUrl += url;
                    } else {
                        videoUrl += ",";
                        videoUrl += url;
                    }
                } else {
                    if (CommonUtils.isEmptyString(imageUrl)) {
                        imageUrl += url;
                    } else {
                        imageUrl += ",";
                        imageUrl += url;
                    }
                }
            }
        }
        allUrls.add(imageUrl);
        allUrls.add(videoUrl);
        return allUrls;
    }

    /**
     * 处置案件
     */
    public void dealCase(){
        isCaseDesignateCompleted = false;
        IssueBean bean = GlobalVariable.sCurrentIssueBean;
        Map<String, Object> map  = new HashMap<>();
        map.put("caseId", bean.getId());
        map.put("designateId", bean.getDesignateId());
        String url = mCurrentFileUrls;
        List<String> urls = getImageUrls();
        if (!CommonUtils.isEmptyList(urls)) {
            final String imageUrls = urls.get(0);
            if (!CommonUtils.isEmptyString(imageUrls)) {
                map.put("disImgUrl", url);
            }
            final String videoUrls = urls.get(1);
            if (!CommonUtils.isEmptyString(videoUrls)) {
                map.put("disVideoUrl", url);
            }
        }

        final String strEntity = mGson.toJson(map);
        RequestBody body = RequestBody.create(strEntity, okhttp3.MediaType.parse("application" +
                "/json;charset=UTF-8"));
        CaseService caseService = RetrofitClient.getAPIService(CaseService.class);
        caseService.designateCase(body).enqueue(new Callback<BaseBean<Object>>() {
            @Override
            public void onResponse(Call<BaseBean<Object>> call, Response<BaseBean<Object>> response) {
                BaseBean myCaseInfoBaseBean = response.body();
                final boolean isUnEmptyData =
                        myCaseInfoBaseBean != null && myCaseInfoBaseBean.code == 0 && myCaseInfoBaseBean.data != null;
                if (isUnEmptyData) {
                    mHandler.sendEmptyMessage(DESIGNATE_CASE_SUCCEED);
                } else {
                    mHandler.sendEmptyMessage(DESIGNATE_CASE_FAILED);
                }
            }

            @Override
            public void onFailure(Call<BaseBean<Object>> call, Throwable t) {
                mHandler.sendEmptyMessage(DESIGNATE_CASE_FAILED);
            }
        });
    }

    /**
     * 上传文件并结案
     * @param images
     */
    public void uploadFilesAndCaseClosed(List<String> images, List<String> videos,
                                         boolean isCaseClose, String content){
        isStartFinishCase = isCaseClose;
        mCurrentComment = content;
        uploadFiles(images, videos);
    }

    /**
     * 上传文件
     * @param images
     */
    public void uploadFiles(List<String> images, List<String> videos){
        mUploadPicPresenter.uploadFiles(images, videos);
    }

    /**
     * 批示案件
     * @param comments
     */
    public void commentCase(String comments){
        IssueBean bean = GlobalVariable.sCurrentIssueBean;
        Map<String, Object> map  = new HashMap<>();
        map.put("caseId", bean.getId());
        map.put("checkComment", comments);
        String strEntity = mGson.toJson(map);
        RequestBody body = RequestBody.create(strEntity, okhttp3.MediaType.parse("application" +
                "/json;charset=UTF-8"));
        CaseService caseService = RetrofitClient.getAPIService(CaseService.class);
        caseService.commentCase(body).enqueue(new Callback<BaseBean<Object>>() {
            @Override
            public void onResponse(Call<BaseBean<Object>> call, Response<BaseBean<Object>> response) {
                final boolean isUnEmptyData =
                        response.body() != null && response.body().code == 0 && response.body().data != null;
                if (isUnEmptyData) {
                    mHandler.sendEmptyMessage(COMMENT_CASE_SUCCEED);
                } else {
                    mHandler.sendEmptyMessage(COMMENT_CASE_FAILED);
                }
            }

            @Override
            public void onFailure(Call<BaseBean<Object>> call, Throwable t) {
                mHandler.sendEmptyMessage(COMMENT_CASE_FAILED);
            }
        });
    }

    /**
     * 二次定位更新案件地址
     */
    public void updateLocation(String caseId, double latitude, double longitude, String reportAddr){
        CaseService caseService = RetrofitClient.getAPIService(CaseService.class);
        Map<String, Object> map  = new HashMap<>();
        map.put("caseId", caseId);
        map.put("latitude", latitude);
        map.put("longitude", longitude);
        map.put("reportAddr", reportAddr);
        String strEntity = mGson.toJson(map);
        RequestBody body = RequestBody.create(strEntity, MediaType.parse("application/json;charset=UTF-8"));
        caseService.updateLocation(body)
                .subscribeOn(Schedulers.io())
                .to(RxLife.toMain(mView.getBaseActivity()))
                .subscribe(bean -> {
                    final boolean isError = bean == null || bean.code != 0;
                    if (isError) {
                        ToastUtil.s("二次定位更新失败");
                        return;
                    }
                    ToastUtil.s("二次定位更新成功");
                }, throwable -> {
                    ToastUtil.s("二次定位更新失败");
                    MyLogUtils.e("二次定位更新出错", throwable);
                });
    }

    public void setCurrentFileUrls(String currentFileUrls) {
        mCurrentFileUrls = currentFileUrls;
    }

    public void setStartFinishCase(boolean mStartFinishCase) {
        isStartFinishCase = mStartFinishCase;
    }
}
