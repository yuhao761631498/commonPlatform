package com.gdu.command.ui.cases.deal;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.baselibrary.base.BaseRVAdapter;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.FileUtil;
import com.gdu.baselibrary.utils.MyConstants;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.baselibrary.utils.imageloade.MyImageLoadUtils;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.R;
import com.gdu.command.event.RefreshMyCaseEvent;
import com.gdu.command.ui.cases.navigate.NavigateActivity;
import com.gdu.command.uploadpic.IUploadPicDiaryView;
import com.gdu.command.views.DealCaseView;
import com.gdu.command.views.picpreview.PicturePreviewActivity;
import com.gdu.model.cases.CaseStatus;
import com.gdu.model.cases.CommentInfo;
import com.gdu.model.cases.IssueBean;
import com.gdu.model.config.GlobalVariable;
import com.gdu.model.config.UrlConfig;
import com.gdu.utils.MapContainer;
import com.gyf.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 结案处理
 */
public class CaseClosedActivity extends BaseActivity<DealCasePresenter> implements View.OnClickListener,IDealCaseView {

    private static final int REQUEST_PIC_CODE = 100;  //图片请求
    private static final int REQUEST_VIDEO_CODE = 101;  //视频请求

    private ImageView mBackImageView;
    private TextView mConfirmCaseImageView;
    private TextView mCaseIdTextView;
    private TextView mCaseDescTextView;
    private TextView mCaseSrcTextView;
    private TextView mCaseReporterTextView;
    private TextView mCaseReportTimeTextView;
    private TextView mCaseReportPhoneTextView;
    private ImageView mCallBtnIv;
    private TextView mCaseLocationTextView;
    private ImageView mNavigationIv;
    private DealCaseView mDealCaseView;
    private LinearLayout mCaseCommentsLayout;
    private ConstraintLayout mCaseHandleLayout;
    private EditText mCaseCommentsEditText;
    private TextView handleResultTv;
    private ConstraintLayout suggestionLayout;

    private RecyclerView mCasePictureGridView;
//    private CasePictureAdapter mCasePictureAdapter;
    private BaseRVAdapter<String> mCasePictureAdapter;
    private ImageView mAddVideoImageView;
    private ImageView mVideoCoverImageView;
    private Group videoGroup;
    private Group videoGroup3;

    private RecyclerView mCommentsListView;
//    private CaseCommentsAdapter mCaseCommentsAdapter;
    private BaseRVAdapter<CommentInfo> mCaseCommentsAdapter;
    private TextView mLookMoreCommentsTextView;

    private List<String> mPicturePathList = new ArrayList<>();
    private List<String>  mRealImages;
    private List<String> mVideoPath;
    private int mCurrentIssueStatus;
    private List<CommentInfo> mCommentInfoList;

    private NestedScrollView mScrollView;
    private MapContainer mMapContainer;
    private MapView mMapView;
    private AMap mAMap;

    private ImageView photoIv;
    private TextView secondLocationBtnTv;

    private LatLng addressCoordinate;
    /** 目标地点Marker */
    private Marker endPointMarker;
    /** 当前顶部控件显示图片的Url */
    private String curPicShowUrl = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_deal_case;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true).statusBarColor(R.color.color_224CD0).init();
        getPresenter().setView(this);
        initViews();
        initData();
        initListener();
        initAMap();
    }

    private void initListener() {
        getPresenter().setUploadPicDiaryView(new IUploadPicDiaryView() {
            @Override
            public void showOrHidePb(boolean isShow, String tip) {
                if (isShow) {
                    showProgressDialog(tip);
                } else {
                    hideProgressDialog();
                }
            }

            @Override
            public void onStatusChange(int type, String content) {
                if (IUploadPicDiaryView.UPLOAD_SUC == type) {
                    getPresenter().setCurrentFileUrls(content);
                    getPresenter().dealCase();
                } else {
                    ToastUtil.s("文件上传失败");
                }
            }

            @Override
            public void onStatusChange(int type, String urlImg, String urlVideo) {

            }
        });
        photoIv.setOnClickListener(this);
        mBackImageView.setOnClickListener(this);
        mConfirmCaseImageView.setOnClickListener(this);
        mLookMoreCommentsTextView.setOnClickListener(this);
        mAddVideoImageView.setOnClickListener(this);
        mCallBtnIv.setOnClickListener(this);
        mNavigationIv.setOnClickListener(this);
        secondLocationBtnTv.setOnClickListener(this);
    }

    private void initViews() {
        mBackImageView = findViewById(R.id.back_imageview);
        mConfirmCaseImageView = findViewById(R.id.tv_confirmBtn);
        mCaseIdTextView = findViewById(R.id.case_id_textview);
        mCaseDescTextView = findViewById(R.id.case_desc_textview);
        mCaseSrcTextView = findViewById(R.id.case_src_textview);
        mCaseReporterTextView = findViewById(R.id.case_reporter_textview);
        mCaseReportTimeTextView = findViewById(R.id.case_report_time_textview);
        mCaseReportPhoneTextView = findViewById(R.id.case_report_phone_textview);
        mCallBtnIv = findViewById(R.id.iv_call);
        mCaseLocationTextView = findViewById(R.id.case_location_textview);
        mNavigationIv = findViewById(R.id.iv_navigation);
        mDealCaseView = findViewById(R.id.deal_case_view);
        mAddVideoImageView = findViewById(R.id.add_video_imageview);
        mVideoCoverImageView = findViewById(R.id.video_cover_imageview);
        videoGroup = findViewById(R.id.view_group2);
        mCasePictureGridView = findViewById(R.id.case_picture_gridview);
        mCaseCommentsLayout = findViewById(R.id.case_comments_layout);
        mCaseCommentsEditText = findViewById(R.id.case_comments_edittext);
        mCaseHandleLayout = findViewById(R.id.handle_case_layout);
        handleResultTv = findViewById(R.id.tv_instructionLabel);
        suggestionLayout = findViewById(R.id.cl_subLayout1);

        mCommentsListView = findViewById(R.id.case_comments_listview);
        mLookMoreCommentsTextView = findViewById(R.id.look_more_comments_textview);

        mScrollView = findViewById(R.id.nsv_rootLayout);
        mMapContainer = findViewById(R.id.mc_container);
        mMapView = findViewById(R.id.mv_subMap);
        videoGroup3 = findViewById(R.id.view_group3);

        photoIv = findViewById(R.id.iv_photoContainer);
        secondLocationBtnTv = findViewById(R.id.tv_secondLocation);

        secondLocationBtnTv.setVisibility(View.VISIBLE);
        mMapContainer.setScrollView(mScrollView);

        handleResultTv.setText("处理结果");
        mCaseCommentsEditText.setHint("请输入处理内容");
        mConfirmCaseImageView.setText("确定");
        initListView();
    }

    private void initListView() {
//        mCasePictureAdapter = new CasePictureAdapter(this);
        mCasePictureAdapter = new BaseRVAdapter<String>(R.layout.item_case_picture) {
            @Override
            public void onBindVH(BaseViewHolder holder, String data, int position) {
                if (CommonUtils.isEmptyString(data)) {
                    return;
                }
                final ImageView contentIv = holder.getView(R.id.case_imageview);
                if (data.equals("add")) {
                    holder.getView(R.id.iv_delBtn).setVisibility(View.GONE);
                    contentIv.setImageResource(R.mipmap.icon_add_picture);
                } else {
                    holder.getView(R.id.iv_delBtn).setVisibility(View.VISIBLE);
                    String path = data;
                    if (!data.contains("http:")) {
                        path = "file://" + data;
                    }
                    MyImageLoadUtils.loadImage(CaseClosedActivity.this, path, 0,
                            contentIv);
                }
                holder.getView(R.id.iv_delBtn).setOnClickListener(v -> {
                    for (int i = 0; i < mPicturePathList.size(); i++) {
                        if (mPicturePathList.get(position).equals(mPicturePathList.get(i))) {
                            mPicturePathList.remove(position);
                            break;
                        }
                    }
                });
            }
        };
        mCasePictureGridView.setAdapter(mCasePictureAdapter);
        mCasePictureAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mPicturePathList.get(position).equals("add")) {
                ImageSelector.builder()
                        .useCamera(true) // 设置是否使用拍照
                        .setSingle(false)  //设置是否单选
                        .canPreview(true) //是否可以预览图片，默认为true
                        .setContainVideo(false)
                        .setContainPhoto(true)
                        .setMaxSelectCount(11 - mPicturePathList.size())
                        .start((Activity) mContext, REQUEST_PIC_CODE); // 打开相册
            }
            mCasePictureAdapter.setList(mPicturePathList);
        });
//        mCaseCommentsAdapter = new CaseCommentsAdapter(mContext);
        mCaseCommentsAdapter = new BaseRVAdapter<CommentInfo>(R.layout.item_case_comment) {
            @Override
            public void onBindVH(BaseViewHolder holder, CommentInfo data, int position) {
                String name = data.getDeptName();
                if (CommonUtils.isEmptyString(name)) {
                    name = "执法员" + position;
                }
                holder.setText(R.id.name_textview, name);
                holder.setText(R.id.time_textview, data.getCheckTime());
                holder.setText(R.id.comments_textview, data.getCheckComment());
            }
        };
        mCommentsListView.setAdapter(mCaseCommentsAdapter);

        mDealCaseView.setOnDealCaseListener(comments -> {
            showProgressDialog("案件处置中...");
            getPresenter().finishCase(comments);
        });
    }

    private void initAMap() {
        if (mAMap == null) {
            mAMap = mMapView.getMap();
        }
        UiSettings uiSettings = mAMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);

        mAMap.setOnMapLoadedListener(() -> {
            final LatLng mLatLng = new LatLng(GlobalVariable.sCurrentIssueBean.getLatitude(),
                    GlobalVariable.sCurrentIssueBean.getLongitude());
            addMarkerInScreenCenter(mLatLng);
            moveToCurLocationPoint(mLatLng);
        });
    }

    /**
     * 给目的地添加一个Marker
     */
    private void addMarkerInScreenCenter(LatLng position) {
        MyLogUtils.i("addMarkerInScreenCenter()");
        if (mAMap == null) {
            return;
        }
        if (endPointMarker != null) {
            endPointMarker.destroy();
        }
        ImageView markerIconIv = new ImageView(getActivity());
        ViewGroup.LayoutParams layoutParams =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        markerIconIv.setLayoutParams(layoutParams);
        markerIconIv.setImageResource(R.mipmap.ic_center_location);
        endPointMarker = mAMap.addMarker(new MarkerOptions()
                .position(position)
                .icon(BitmapDescriptorFactory.fromView(markerIconIv)));
    }

    private void initData() {
        MyLogUtils.d("initData()");
        IssueBean issueBean = GlobalVariable.sCurrentIssueBean;
        Intent intent = getIntent();
        getPresenter().loadComment(issueBean.getId());
        mCurrentIssueStatus = intent.getIntExtra(GlobalVariable.EXTRA_CASE_STATUS, -1);
        if (mCurrentIssueStatus == -1) {
            final int caseStatus = intent.getIntExtra("caseStatus", -1);
            mCurrentIssueStatus = caseStatus;
        }
//        Uri data = intent.getData();
//        if (data != null && mCurrentIssueStatus == -1) {
//            String caseStatus = data.getQueryParameter("caseStatus");
//            mCurrentIssueStatus =  Integer.parseInt(caseStatus);
//        }
        MyLogUtils.d("initData() mCurrentIssueStatus = " + mCurrentIssueStatus);

        String snStr = "";
        if (!CommonUtils.isEmptyString(issueBean.getCaseNo())) {
            snStr = issueBean.getCaseNo();
        }
        if (CommonUtils.isEmptyString(snStr) && !CommonUtils.isEmptyString(issueBean.getId())) {
            snStr = issueBean.getId();
        }
        if (CommonUtils.isEmptyString(snStr)) {
            snStr = "";
        }
        mCaseIdTextView.setText(snStr);

        mCaseDescTextView.setText(issueBean.getCaseDesc());
        mCaseSrcTextView.setText(issueBean.getDeptName());
        String name = issueBean.getReportMan();
        if (CommonUtils.isEmptyString(name)) {
            name = issueBean.getReport_man();
        }
        mCaseReporterTextView.setText(name);
        mCaseReportTimeTextView.setText(issueBean.getReportTime());
        mCaseReportPhoneTextView.setText(issueBean.getReportTel());
        mCaseLocationTextView.setText(issueBean.getReportAddr());

        curPicShowUrl = CommonUtils.getSinglePicRealPath(issueBean.getCaseFile());
        MyImageLoadUtils.loadImage(CaseClosedActivity.this,
                curPicShowUrl,
                R.mipmap.icon_case_default1, photoIv);
        mPicturePathList.clear();
        mPicturePathList.add("add");
        mCasePictureAdapter.setList(mPicturePathList);

        videoGroup3.setVisibility(View.GONE);
        videoGroup.setVisibility(View.GONE);
        suggestionLayout.setVisibility(View.GONE);
        mCaseCommentsLayout.setVisibility(View.VISIBLE);
        mCaseHandleLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.back_imageview:
                finish();
                break;

            case R.id.tv_confirmBtn:
                CommonUtils.hideInputSoftKey(this, mCaseCommentsEditText);
                final String comments = mCaseCommentsEditText.getText().toString();
                if (CommonUtils.isEmptyString(comments)) {
                    ToastUtil.s("处理结果不能为空");
                    return;
                }
                List<String> files = new ArrayList<>();
                if (mRealImages != null && mRealImages.size() > 0) {
                    files.addAll(mRealImages);
                }

                if (files.size() != 0) {
                    if (!isOverSize(files)) {
                        getPresenter().uploadFilesAndCaseClosed(mRealImages, mVideoPath, true,
                                comments);
                    } else {
                        ToastUtil.s("上传文件太大");
                    }
                } else {
                    getPresenter().setStartFinishCase(true);
                    getPresenter().dealCase();
                }
                break;

            case R.id.look_more_comments_textview:
                GlobalVariable.sCommentInfoList = mCommentInfoList;
                Intent intent = new Intent(CaseClosedActivity.this, CommentsActivity.class);
                startActivity(intent);
                break;

            case R.id.add_video_imageview:
                ImageSelector.builder()
                        .useCamera(true) // 设置是否使用拍照
                        .setSingle(false)  //设置是否单选
                        .canPreview(true) //是否可以预览图片，默认为true
                        .setContainVideo(true)
                        .setContainPhoto(false)
                        .setMaxSelectCount(1)
                        .start((Activity) mContext, REQUEST_VIDEO_CODE); // 打开相册
                break;

            case R.id.iv_call:
                dialPhone(GlobalVariable.sCurrentIssueBean.getReportTel());
                break;

            case R.id.iv_navigation:
                Intent navIntent = new Intent(CaseClosedActivity.this, NavigateActivity.class);
                startActivity(navIntent);
                break;

            case R.id.iv_photoContainer:
                if (GlobalVariable.sCurrentIssueBean == null
                        || CommonUtils.isEmptyString(curPicShowUrl)) {
                    return;
                }
                final Bundle bundle1 = new Bundle();
                bundle1.putString(MyConstants.SEND_PIC_PATH_KEY, curPicShowUrl);
                final Intent mIntent = new Intent(this, PicturePreviewActivity.class);
                mIntent.putExtras(bundle1);
                startActivity(mIntent);
                break;

            default:
                break;
        }
    }

    /**
     * 拨打电话
     * @param phone
     */
    private void dialPhone(String phone){
        if (!CommonUtils.isEmptyString(phone)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phone));
            mContext.startActivity(intent);
        } else {
            ToastUtil.l("电话号码错误");
        }
    }

    /**
     * 文件是否超大
     * @param paths
     * @return
     */
    private boolean isOverSize(List<String> paths){
        long totalSize = 0;
        for (String path : paths) {
          long size = FileUtil.getFileLength(new File(path));
          totalSize += size;
        }
        double realSize = totalSize / (1024 * 1024.0);
        if (realSize > 10) {
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final boolean isHaveMapData = requestCode == MyConstants.SHOW_MANUAL_LOCATION_REQUEST_CODE
                && resultCode == MyConstants.RESULT_ADDRESS_INTENT && data != null
                && data.getExtras() != null;
        if (requestCode == REQUEST_PIC_CODE && data != null) {
            //获取选择器返回的数据
            mRealImages = data.getStringArrayListExtra(
                    ImageSelector.SELECT_RESULT);
            CommonUtils.listAddAllAvoidNPE(mPicturePathList, mRealImages);
            mCasePictureAdapter.setList(mPicturePathList);
        } else if(requestCode == REQUEST_VIDEO_CODE && data != null){
            List<String> videoUrlList = data.getStringArrayListExtra(
                    ImageSelector.SELECT_RESULT);
            String videoUrl = videoUrlList.get(0);
            if (FileUtil.getFileLength(new File(videoUrl)) / (1024 * 1024.0) > 10) {
                ToastUtil.s("文件过大");
                videoGroup.setVisibility(View.GONE);
                mAddVideoImageView.setVisibility(View.VISIBLE);
                return;
            }
            mVideoPath = videoUrlList;
            videoGroup.setVisibility(View.VISIBLE);
            mAddVideoImageView.setVisibility(View.GONE);
            String path = "file://" + videoUrl;
            MyImageLoadUtils.loadImage(CaseClosedActivity.this, path, 0,
                    mVideoCoverImageView);
        } else if (isHaveMapData) {
            final Bundle mBundle = data.getExtras();
            final String addressStr = mBundle.getString(MyConstants.RESULT_ADDRESS, "");
            addressCoordinate = mBundle.getParcelable(MyConstants.RESULT_ADDRESS_COORDINATE);
            GlobalVariable.sCurrentIssueBean.setLatitude(addressCoordinate.latitude);
            GlobalVariable.sCurrentIssueBean.setLongitude(addressCoordinate.longitude);
            addMarkerInScreenCenter(addressCoordinate);
            moveToCurLocationPoint(addressCoordinate);
            mCaseLocationTextView.setText(addressStr);

            getPresenter().updateLocation(GlobalVariable.sCurrentIssueBean.getId(),
                    addressCoordinate.latitude, addressCoordinate.longitude, addressStr);
        }
    }

    @Override
    public void onForensicsFinished() {

    }
    @Override
    public void onCaseFinished() {
        hideProgressDialog();
        ToastUtil.l("结案成功");
        EventBus.getDefault().post(new RefreshMyCaseEvent(CaseStatus.HANDLING.getKey(), GlobalVariable.sCurrentIssueBean.getId()));
        finish();
    }

    @Override
    public void showComments(List<CommentInfo> commentInfoList) {
        mCommentInfoList = commentInfoList;
        List<CommentInfo> tmp;
        if (commentInfoList.size() > 2) {
            mLookMoreCommentsTextView.setVisibility(View.VISIBLE);
            tmp = commentInfoList.subList(0, 1);
        } else {
            mLookMoreCommentsTextView.setVisibility(View.GONE);
            tmp = commentInfoList;
        }
        mCaseCommentsAdapter.setList(tmp);
    }

    @Override
    public void onCaseCommented() {

    }

    @Override
    public void showToast(String toast) {
        hideProgressDialog();
        ToastUtil.s(toast);
    }

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapView.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    /**
     * 移动到定位到点.
     */
    private void moveToCurLocationPoint(LatLng position) {
        MyLogUtils.i("moveToCurLocationPoint()");
        // 设置地图中心点以及缩放级别
        mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 18));
    }
}
