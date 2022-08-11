package com.gdu.command.ui.alarm.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps2d.model.LatLng;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.MyConstants;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.baselibrary.utils.eventbus.EventMessage;
import com.gdu.baselibrary.utils.eventbus.GlobalEventBus;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.R;
import com.gdu.command.ui.alarm.dialog.AlarmAdditionalDialog;
import com.gdu.command.ui.alarm.dialog.AlarmDispatchDialog;
import com.gdu.command.ui.alarm.dialog.AlarmDispatchedBean;
import com.gdu.command.ui.alarm.dialog.AlarmDispatchedDialog;
import com.gdu.command.ui.alarm.dialog.AlarmFalseAlarmDialog;
import com.gdu.command.ui.alarm.dialog.AlarmHandleDialog;
import com.gdu.command.ui.cases.navigate.NavigateActivity;
import com.gdu.command.uploadpic.IUploadSeparateBackView;
import com.gdu.command.views.picpreview.PicturePreviewActivity;
import com.gdu.model.alarm.AlarmInfo;
import com.gdu.model.cases.IssueBean;
import com.gdu.model.config.GlobalVariable;
import com.gdu.model.organization.OrganizationInfo;
import com.gdu.mqttchat.chat.view.ChatActivity;
import com.gyf.immersionbar.ImmersionBar;
import com.noober.background.view.BLTextView;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 告警详情
 */
public class AlarmDetailActivity extends BaseActivity<AlarmDetailPresenter>
        implements View.OnClickListener, IAlarmDetailView {

    private ImageView mBackImageView;
    private ImageView iv_chart;
    private Banner mPicBanner;
    private TextView mAlarmStatusTextView;
    private BLTextView mLocationTv;
    private BLTextView mVideoButton;
    private TextView tv_alarmInfoContent;
    private TextView tv_alarmTimeContent;
    private TextView tv_remarkContent;
    private TextView tv_answerThePoliceContent;
    private TextView tv_dispatchLabel;
    private TextView tv_operateNameAndTime;
    private TextView tv_operatorRemarkContent;
    private TextView tv_handleLabel;
    private RecyclerView rv_handleContent;
    private RecyclerView rv_supplementaryHandleContent;
    private BLTextView tv_botBtn1;
    private BLTextView tv_botBtn2;
    private BLTextView tv_botBtn3;
    /** 顶部案件备注备注/接警信息控件组 */
    private Group view_group1;
    /** 顶部已分派(操作员/备注信息)控件组 */
    private Group view_group2;
    /** 已处理相关控件组 */
    private Group view_group3;
    /** 补充处理信息相关控件组 */
    private Group view_group4;

    private AlarmInfo mAlarmInfo;

    private List<String> bannerData = new ArrayList<>();
    private ImageTitleNumAdapter mImageTitleNumAdapter;

    private AlarmAdditionalDialog mAlarmAdditionalDialog;
    private AlarmDispatchDialog mAlarmDispatchDialog;
    private AlarmDispatchedDialog mAlarmDispatchedDialog;
    private AlarmFalseAlarmDialog mAlarmFalseAlarmDialog;
    private AlarmHandleDialog mAlarmHandleDialog;

    private int curShowDialogType = -1;

    private AlarmHandleSendBean mHandleSendBean;
    private List<AlarmHandleFileBean> mUploadFiles = new ArrayList<>();

    private AlarmDispatchSendBean mDispatchSendBean;

    private List<AlarmDetailBean.DataBean.AssignmentAndHandleInfoBean.DispositionNodeDataBean> mHandledData =
            new ArrayList<>();
    private AlarmHandledAdapter mHandledAdapter;

    private List<AlarmDetailBean.DataBean.AssignmentAndHandleInfoBean.DispositionNodeDataBean> mSupplementHandleData =
            new ArrayList<>();
    private AlarmSupplementHandleAdapter mSupplementHandleAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_alarm_detail;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true).statusBarColor(R.color.color_224CD0).init();
        getPresenter().setView(this);
        getPresenter().setUploadCallbackView(uploadCallback);
        initData();
        initView();
        setData();
        initListener();
        getPresenter().getCaseDetailInfo(mAlarmInfo.getId(), mAlarmInfo.getHandleType());
    }

    private void initData() {
        mAlarmInfo = GlobalVariable.sAlarmInfo;
    }

    private void initListener() {
        mBackImageView.setOnClickListener(this);
        iv_chart.setOnClickListener(this);
        mPicBanner.setOnClickListener(this);
        mVideoButton.setOnClickListener(this);
        mLocationTv.setOnClickListener(this);
        tv_dispatchLabel.setOnClickListener(this);
//        tv_handleLabel.setOnClickListener(this);
        tv_botBtn1.setOnClickListener(this);
        tv_botBtn2.setOnClickListener(this);
        tv_botBtn3.setOnClickListener(this);
    }

    private void initView() {
        mBackImageView = findViewById(R.id.back_imageview);
        iv_chart = findViewById(R.id.iv_chart);
        mPicBanner = findViewById(R.id.alarm_photo_imageview);
        mAlarmStatusTextView = findViewById(R.id.alarm_status_textview);
        mLocationTv = findViewById(R.id.tv_locationBtn);
        mVideoButton = findViewById(R.id.tv_videoBtn);

        tv_alarmInfoContent = findViewById(R.id.tv_alarmInfoContent);
        tv_alarmTimeContent = findViewById(R.id.tv_alarmTimeContent);
        tv_remarkContent = findViewById(R.id.tv_remarkContent);
        tv_answerThePoliceContent = findViewById(R.id.tv_answerThePoliceContent);
        tv_dispatchLabel = findViewById(R.id.tv_dispatchLabel);
        tv_operateNameAndTime = findViewById(R.id.tv_operateNameAndTime);
        tv_operatorRemarkContent = findViewById(R.id.tv_operatorRemarkContent);
        tv_handleLabel = findViewById(R.id.tv_handleLabel);
        rv_handleContent = findViewById(R.id.rv_handleContent);
        rv_supplementaryHandleContent = findViewById(R.id.rv_supplementaryHandleContent);
        tv_botBtn1 = findViewById(R.id.tv_botBtn1);
        tv_botBtn2 = findViewById(R.id.tv_botBtn2);
        tv_botBtn3 = findViewById(R.id.tv_botBtn3);
        view_group1 = findViewById(R.id.view_group1);
        view_group2 = findViewById(R.id.view_group2);
        view_group3 = findViewById(R.id.view_group3);
        view_group4 = findViewById(R.id.view_group4);

        mHandledAdapter = new AlarmHandledAdapter(mHandledData);
        rv_handleContent.setAdapter(mHandledAdapter);
        mHandledAdapter.setPicRvItemClickListener(((adapter, view, position) -> {
            final AlarmDetailBean.DataBean.AssignmentAndHandleInfoBean.DispositionNodeDataBean.FileKeyBean bean =
                    (AlarmDetailBean.DataBean.AssignmentAndHandleInfoBean.DispositionNodeDataBean.FileKeyBean) adapter.getData().get(position);
            openPicOrVideo(bean);
        }));

        mSupplementHandleAdapter = new AlarmSupplementHandleAdapter(mSupplementHandleData);
        rv_supplementaryHandleContent.setAdapter(mSupplementHandleAdapter);
        mSupplementHandleAdapter.setPicRvItemClickListener(((adapter, view, position) -> {
            final AlarmDetailBean.DataBean.AssignmentAndHandleInfoBean.DispositionNodeDataBean.FileKeyBean bean =
                    (AlarmDetailBean.DataBean.AssignmentAndHandleInfoBean.DispositionNodeDataBean.FileKeyBean) adapter.getData().get(position);
            openPicOrVideo(bean);
        }));

        view_group3.setVisibility(View.GONE);
        view_group4.setVisibility(View.GONE);

        if (mAlarmInfo != null) {
            switch (mAlarmInfo.getHandleType()) {
                case 1:
                    view_group1.setVisibility(View.GONE);
                    view_group2.setVisibility(View.GONE);

                    tv_botBtn1.setText("分派");
                    tv_botBtn3.setText("处理");
                    tv_botBtn2.setText("误报");
                    tv_botBtn1.setVisibility(View.VISIBLE);
                    tv_botBtn2.setVisibility(View.GONE);
                    tv_botBtn3.setVisibility(View.VISIBLE);
                    break;

                case 3:
                    view_group1.setVisibility(GlobalVariable.isAppMenu ? View.GONE : View.VISIBLE);
                    view_group2.setVisibility(GlobalVariable.isAppMenu ? View.VISIBLE : View.GONE);

                    tv_botBtn1.setText("分派");
                    tv_botBtn2.setText("误报");
                    tv_botBtn3.setText("处理");
                    if (GlobalVariable.isAppMenu) {
                        tv_botBtn1.setVisibility(View.VISIBLE);
                        tv_botBtn2.setVisibility(View.GONE);
                        tv_botBtn3.setVisibility(View.VISIBLE);
                    } else {
                        tv_botBtn2.setText("接警");
                        tv_botBtn1.setVisibility(View.GONE);
                        tv_botBtn2.setVisibility(View.VISIBLE);
                        tv_botBtn3.setVisibility(View.GONE);
                    }
                    break;

                case 4:
                    view_group1.setVisibility(GlobalVariable.isAppMenu ? View.GONE : View.VISIBLE);
                    view_group2.setVisibility(GlobalVariable.isAppMenu ? View.VISIBLE : View.GONE);

                    tv_botBtn1.setText("分派");
                    tv_botBtn2.setText("误报");
                    tv_botBtn3.setText("处理");
                    if (GlobalVariable.isAppMenu) {
                        tv_botBtn1.setVisibility(View.VISIBLE);
                    } else {
                        tv_botBtn1.setVisibility(View.GONE);
                    }
                    tv_botBtn2.setVisibility(View.GONE);
                    tv_botBtn3.setVisibility(View.VISIBLE);
                    break;

                case 5:
                    view_group1.setVisibility(GlobalVariable.isAppMenu ? View.GONE : View.VISIBLE);
                    view_group2.setVisibility(GlobalVariable.isAppMenu ? View.VISIBLE : View.GONE);

                    tv_botBtn1.setText("分派");
                    tv_botBtn2.setText("误报");
                    tv_botBtn3.setText("补充处理信息");
                    tv_botBtn1.setVisibility(View.GONE);
                    tv_botBtn2.setVisibility(View.GONE);
                    tv_botBtn3.setVisibility(View.VISIBLE);
                    break;

                default:
                    break;
            }
        }
        initBanner();
    }

    private void openPicOrVideo(AlarmDetailBean.DataBean.AssignmentAndHandleInfoBean.DispositionNodeDataBean.FileKeyBean bean) {
        Bundle mBundle;
        Intent mIntent;
        if ("1".equals(bean.getFileType())) {
            mBundle = new Bundle();
            mBundle.putString(MyConstants.SEND_PIC_PATH_KEY, bean.getFileKey());
            mIntent = new Intent(this, PicturePreviewActivity.class);
            mIntent.putExtras(mBundle);
        } else {
            mBundle = new Bundle();
            mBundle.putString(MyConstants.DEFAULT_PARAM_KEY_1, bean.getFileKey());
            mIntent = new Intent(this, AlarmDetailVideoActivity.class);
            mIntent.putExtras(mBundle);
        }
        startActivity(mIntent);
    }

    private void initBanner() {
        mPicBanner.setLoopTime(3000);
        mImageTitleNumAdapter = new ImageTitleNumAdapter(bannerData);
        mImageTitleNumAdapter.setOnBannerListener((clickData, position) -> {
            if (mAlarmInfo == null) {
                return;
            }
            final Bundle bundle1 = new Bundle();
            bundle1.putString(MyConstants.SEND_PIC_PATH_KEY, (String) clickData);
            final Intent mIntent = new Intent(this, PicturePreviewActivity.class);
            mIntent.putExtras(bundle1);
            startActivity(mIntent);
        });
        mPicBanner.setAdapter(mImageTitleNumAdapter).addBannerLifecycleObserver(this);
    }

    /**
     * 设置数据
     */
    private void setData() {
        setHandleType(mAlarmInfo.getHandleType());
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){

            case R.id.back_imageview:
                finish();
                break;

            case R.id.iv_chart:
                intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(GlobalVariable.EXTRA_CASE_ID, mAlarmInfo.getId() + "");
                startActivity(intent);
                break;

            case R.id.tv_videoBtn:
                if (CommonUtils.isEmptyString(mAlarmInfo.getVideoUrl())) {
                    ToastUtil.s("未获取到可播放视频");
                    return;
                }
                final Bundle mBundle = new Bundle();
                mBundle.putString(MyConstants.DEFAULT_PARAM_KEY_1, mAlarmInfo.getVideoUrl());
                final Intent mIntent = new Intent(this, AlarmDetailVideoActivity.class);
                mIntent.putExtras(mBundle);
                startActivity(mIntent);
                hideSoftInput();
                break;

            case R.id.tv_locationBtn:
                intent = new Intent(AlarmDetailActivity.this, NavigateActivity.class);
                final IssueBean bean = new IssueBean();
                if (!CommonUtils.isEmptyString(mAlarmInfo.getDeviceAddress())) {
                    bean.setReportAddr(mAlarmInfo.getDeviceAddress());
                }
                if (mAlarmInfo.getPresetLat() == 0 || mAlarmInfo.getPresetLon() == 0) {
                    ToastUtil.s("未获取到有效座标位置");
                    return;
                }
                bean.setCaseDesc(mAlarmInfo.getAlarmTypeName());
                bean.setLatitude(mAlarmInfo.getPresetLat());
                bean.setLongitude(mAlarmInfo.getPresetLon());
                GlobalVariable.sCurrentIssueBean = bean;
                startActivity(intent);
                break;

            case R.id.tv_dispatchLabel:
                curShowDialogType = 3;
                if (mAlarmDispatchedDialog == null) {
                    mAlarmDispatchedDialog = new AlarmDispatchedDialog(this);
                }
                if (!CommonUtils.isEmptyList(getPresenter().getAssignmentData())) {
                    mAlarmDispatchedDialog.show();
                    return;
                }
                showProgressDialog();
                int useId = 0;
                if (GlobalVariable.sPersonInfoBean != null && GlobalVariable.sPersonInfoBean.getData() != null) {
                    useId = GlobalVariable.sPersonInfoBean.getData().getId();
                }
                getPresenter().queryAssignmentList(mAlarmInfo.getId(), 0, useId);
                break;

            case R.id.tv_botBtn1:
                curShowDialogType = 2;
                if (mAlarmDispatchDialog == null) {
                    mAlarmDispatchDialog = new AlarmDispatchDialog(this);
                    mAlarmDispatchDialog.setDispatchCallback((dispatchUsers, remark) -> {
                        final List<DispatchManBean> dispatchList = new ArrayList<>();
                        mDispatchSendBean = new AlarmDispatchSendBean();
                        mDispatchSendBean.setAlarmId(mAlarmInfo.getId());
                        mDispatchSendBean.setRemark(remark);

                        for (int i = 0; i < dispatchUsers.size(); i++) {
                            final OrganizationInfo.UseBean originBean = dispatchUsers.get(i);
                            final DispatchManBean sendUseBean = new DispatchManBean();
                            sendUseBean.setEmployeeId(originBean.getId());
                            sendUseBean.setEmployeeName(originBean.getUsername());
                            sendUseBean.setOrgName(originBean.getOrgName());
                            CommonUtils.listAddAvoidNull(dispatchList, sendUseBean);
                        }
                        mDispatchSendBean.setDesignateMan(dispatchList);
                        hideSoftInput();
                        getPresenter().assignment(mDispatchSendBean);
                    });
                }
                if (!CommonUtils.isEmptyList(getPresenter().getOrganizationList())) {
                    mAlarmDispatchDialog.show();
                    return;
                }
                showProgressDialog();
                getPresenter().getDeptTree();
                break;

            case R.id.tv_botBtn2:
                if ("误报".equals(tv_botBtn2.getText().toString())) {
                    curShowDialogType = 4;
                    if (mAlarmFalseAlarmDialog == null) {
                        mAlarmFalseAlarmDialog = new AlarmFalseAlarmDialog(this);
                        mAlarmFalseAlarmDialog.setFalseAlarmCallback(content -> {
                            hideSoftInput();
                            getPresenter().updateAlarm(mAlarmInfo.getId(), 3);
                        });
                    }
                    mAlarmFalseAlarmDialog.show();
                } else if ("接警".equals(tv_botBtn2.getText().toString())) {
                    // 接警处理
                    getPresenter().answerPolice(mAlarmInfo.getId());
                }
                break;

            case R.id.tv_botBtn3:
                if ("处理".equals(tv_botBtn3.getText().toString())) {
                    curShowDialogType = 5;
                    if (mAlarmHandleDialog == null) {
                        mAlarmHandleDialog = new AlarmHandleDialog(this);
                        mAlarmHandleDialog.updateLocation(mAlarmInfo.getDeviceAddress(),
                                new LatLng(mAlarmInfo.getLat(), mAlarmInfo.getLon()), false);
                        mAlarmHandleDialog.setHandleCallback(
                                (selectResultType, secondAddress, lat, lon, uploadPics
                                , uploadVideo, remark) -> {
                                    final List<String> needUploadFiles = new ArrayList<>();
                                    if (uploadPics != null && uploadPics.size() > 0) {
                                        for (int i = 0; i < uploadPics.size(); i++) {
                                            CommonUtils.listAddAvoidNull(needUploadFiles, uploadPics.get(i));
                                        }
                                    }

                                    final List<String> mRealVideos = new ArrayList<>();
                                    if (!CommonUtils.isEmptyString(uploadVideo)) {
                                        CommonUtils.listAddAvoidNull(needUploadFiles, uploadVideo);
                                        CommonUtils.listAddAvoidNull(mRealVideos, uploadVideo);
                                    }
                                    mHandleSendBean = new AlarmHandleSendBean();
                                    mHandleSendBean.setAlarmId(mAlarmInfo.getId());
                                    mHandleSendBean.setAlarmAddress(secondAddress);
                                    mHandleSendBean.setHandleContent(remark);
                                    mHandleSendBean.setHandleResultType(selectResultType);
                                    mHandleSendBean.setId(0);
                                    mHandleSendBean.setLat(lat);
                                    mHandleSendBean.setLon(lon);
                                    hideSoftInput();
                                    showProgressDialog();
                                    if (needUploadFiles.size() > 0) {
                                        if (!CommonUtils.isOverSize(needUploadFiles)) {
                                            getPresenter().uploadFiles(uploadPics, mRealVideos);
                                        } else {
                                            ToastUtil.s("上传文件太大");
                                        }
                                        return;
                                    }
                                    getPresenter().alarmHandle(mHandleSendBean);
                                });
                    }
                    mAlarmHandleDialog.show();
                } else {
                    curShowDialogType = 1;
                    if (mAlarmAdditionalDialog == null) {
                        mAlarmAdditionalDialog = new AlarmAdditionalDialog(this);
                        mAlarmAdditionalDialog.setAdditionalCallback((uploadPics, uploadVideo, remark) -> {
                            final List<String> needUploadFiles = new ArrayList<>();
                            if (uploadPics != null && uploadPics.size() > 0) {
                                for (int i = 0; i < uploadPics.size(); i++) {
                                    CommonUtils.listAddAvoidNull(needUploadFiles, uploadPics.get(i));
                                }
                            }

                            final List<String> mRealVideos = new ArrayList<>();
                            if (!CommonUtils.isEmptyString(uploadVideo)) {
                                CommonUtils.listAddAvoidNull(needUploadFiles, uploadVideo);
                                CommonUtils.listAddAvoidNull(mRealVideos, uploadVideo);
                            }

                            mHandleSendBean = new AlarmHandleSendBean();
                            mHandleSendBean.setAlarmId(mAlarmInfo.getId());
                            mHandleSendBean.setSupplementHandleContent(remark);
                            hideSoftInput();
                            showProgressDialog();
                            if (needUploadFiles.size() > 0) {
                                if (!CommonUtils.isOverSize(needUploadFiles)) {
                                    getPresenter().uploadFiles(uploadPics, mRealVideos);
                                } else {
                                    ToastUtil.s("上传文件太大");
                                }
                                return;
                            }
                            getPresenter().supplementHandle(mHandleSendBean);
                        });
                    }
                    mAlarmAdditionalDialog.show();
                }
                break;

            default:
                break;
        }
    }


    @Override
    public void showAlarmRemarkList(List<Map<String, String>> alarmRemarks) {
    }

    @Override
    public void showAlarmStatus(int status) {
        finish();
    }

    private void setHandleType(int type) {
        String status = "";
        int bkRes = 0;
        int textColorId = 0;

        switch (type) {

            case 1:
                status = "待核实";
                bkRes = R.drawable.shape_alarm_status_blue;
                textColorId = R.color.color_DC9954;
                break;

            case 3:
                status = "待接警";
                bkRes = R.drawable.shape_alarm_status_yellow;
                textColorId = R.color.color_DCD726;
                break;

            case 4:
                status = "待处理";
                bkRes = R.drawable.shape_alarm_status_red;
                textColorId = R.color.color_FF5B5B;
                break;

            case 5:
                status = "已处理";
                bkRes = R.drawable.shape_alarm_status_green;
                textColorId = R.color.color_79AA57;
                break;
            default:
                break;
        }
        if (CommonUtils.isEmptyString(status)) {
            return;
        }
        mAlarmStatusTextView.setText(status);
        mAlarmStatusTextView.setTextColor(ContextCompat.getColor(getContext(), textColorId));
        mAlarmStatusTextView.setBackgroundResource(bkRes);
    }

    @Override
    public void remarkCallback(int status) {
        if (status == 0) {
            finish();
        }
    }

    @Override
    public void showDetailInfo(AlarmDetailBean.DataBean bean) {
        bannerData.clear();
        CommonUtils.listAddAllAvoidNPE(bannerData, bean.getImageUrls());
        mImageTitleNumAdapter.notifyDataSetChanged();

        mAlarmInfo.setVideoUrl(bean.getVideoUrl());
        mAlarmInfo.setLat(bean.getPresetLat());
        mAlarmInfo.setLon(bean.getPresetLon());

        tv_alarmInfoContent.setText(CommonUtils.convertNull2EmptyStr(bean.getAlarmInfo()));
        tv_alarmTimeContent.setText(CommonUtils.convertNull2EmptyStr(bean.getCreateTime()));
        tv_remarkContent.setText(CommonUtils.convertNull2EmptyStr(bean.getRemark()));
        tv_answerThePoliceContent.setText(CommonUtils.convertNull2EmptyStr(bean.getAnswerPoliceTime()));

        for (int i = 0; i < bean.getAssignmentAndHandleInfo().size(); i++) {
            final AlarmDetailBean.DataBean.AssignmentAndHandleInfoBean subBean = bean.getAssignmentAndHandleInfo().get(i);
            if ("0".equals(subBean.getDispositionNode())) {
                final AlarmDetailBean.DataBean.AssignmentAndHandleInfoBean.DispositionNodeDataBean dispatchBean
                        = subBean.getDispositionNodeData().get(0);
                final String dispatchInfoStr = "已分派" + dispatchBean.getAssignmentCount()
                        + "人 (已接警" + dispatchBean.getAnswerPolice() + "人）";
                tv_dispatchLabel.setText(dispatchInfoStr);
                tv_operateNameAndTime.setText(CommonUtils.convertNull2EmptyStr(dispatchBean.getUsername()) + " "
                        + CommonUtils.convertNull2EmptyStr(dispatchBean.getCreateTime()));
                tv_operatorRemarkContent.setText(CommonUtils.convertNull2EmptyStr(dispatchBean.getRemark()));
            } else if ("1".equals(subBean.getDispositionNode())) {
                if (CommonUtils.isEmptyList(subBean.getDispositionNodeData())) {
                    view_group3.setVisibility(View.GONE);
                    return;
                }
                view_group3.setVisibility(View.VISIBLE);
                CommonUtils.listAddAllAvoidNPE(mHandledData, subBean.getDispositionNodeData());
                mHandledAdapter.notifyDataSetChanged();
            } else if ("2".equals(subBean.getDispositionNode())) {
                if (CommonUtils.isEmptyList(subBean.getDispositionNodeData())) {
                    view_group4.setVisibility(View.GONE);
                    return;
                }
                view_group4.setVisibility(View.VISIBLE);
                CommonUtils.listAddAllAvoidNPE(mSupplementHandleData, subBean.getDispositionNodeData());
                if (mSupplementHandleData.size() > 0) {
                    tv_botBtn3.setVisibility(View.GONE);
                }
                mSupplementHandleAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void callBackOrganizationList(boolean isSuc, List<OrganizationInfo> organizationList) {
        if (mAlarmDispatchDialog == null) {
            return;
        }
        if (!isSuc) {
            ToastUtil.s("获取组织列表失败");
            return;
        }
        mAlarmDispatchDialog.updateOrganizationList(organizationList);
        hideProgressDialog();
        mAlarmDispatchDialog.show();
    }

    @Override
    public void caseHandleResult(boolean isSuc) {
        hideProgressDialog();
        mUploadFiles.clear();
        mHandleSendBean = null;
        if (isSuc) {
            ToastUtil.s("预警处理成功");
            GlobalEventBus.getBus().post(new EventMessage(MyConstants.ALARM_HANDLE_SUC));
            finish();
        } else {
            ToastUtil.s("预警处理失败");
        }
    }

    @Override
    public void supplementHandleResult(boolean isSuc) {
        hideProgressDialog();
        mUploadFiles.clear();
        mHandleSendBean = null;
        if (isSuc) {
            ToastUtil.s("预警补充处理成功");
            GlobalEventBus.getBus().post(new EventMessage(MyConstants.ALARM_HANDLE_SUC));
            finish();
        } else {
            ToastUtil.s("预警补充处理失败");
        }
    }

    @Override
    public void getDispatchedListResult(boolean isSuc, List<AlarmDispatchedBean.DataBean> data) {
        hideProgressDialog();
        if (isSuc) {
            if (CommonUtils.isEmptyList(data)) {
                ToastUtil.s("无分派数据");
                return;
            }
            mAlarmDispatchedDialog.updateListData(data);
            mAlarmDispatchedDialog.show();
        } else {
            ToastUtil.s("查询被分派列表失败");
        }
    }

    @Override
    public void dispatchedResult(boolean isSuc) {
        if (isSuc) {
            ToastUtil.s("分派成功");
            GlobalEventBus.getBus().post(new EventMessage(MyConstants.ALARM_HANDLE_SUC));
            finish();
        } else {
            ToastUtil.s("分派失败");
        }
    }

    @Override
    public void answerPoliceResult(boolean isSuc) {
        if (isSuc) {
            ToastUtil.s("接警成功");
            GlobalEventBus.getBus().post(new EventMessage(MyConstants.ALARM_HANDLE_SUC));
            finish();
        } else {
            ToastUtil.s("接警失败");
        }
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftInput(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode,  resultCode, data);
        MyLogUtils.i("onActivityResult() requestCode = " + requestCode);
        final boolean isHaveMapData = requestCode == MyConstants.SHOW_MANUAL_LOCATION_REQUEST_CODE
                && resultCode == MyConstants.RESULT_ADDRESS_INTENT
                && data != null && data.getExtras() != null;
        if (requestCode == MyConstants.REQUEST_PIC_CODE && data != null) {
            //获取选择器返回的数据
            final List<String> mPicList = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
            MyLogUtils.i("onActivityResult() mPicListSize = " + mPicList.size());
            if (curShowDialogType == 1) {
                mAlarmAdditionalDialog.updatePicData(mPicList);
            } else {
                mAlarmHandleDialog.updatePicData(mPicList);
            }
        } else if (requestCode == MyConstants.REQUEST_VIDEO_CODE && data != null) {
            //获取选择器返回的数据
            final List<String> mVideoList = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
            MyLogUtils.i("onActivityResult() mVideoListSize = " + mVideoList.size());
            if (curShowDialogType == 1) {
                mAlarmAdditionalDialog.updateVideoData(mVideoList.get(0));
            } else {
                mAlarmHandleDialog.updateVideoData(mVideoList.get(0));
            }
        } else if (isHaveMapData) {
            final Bundle mBundle = data.getExtras();
            final String addressStr = mBundle.getString(MyConstants.RESULT_ADDRESS, "");
            final LatLng addressCoordinate = mBundle.getParcelable(MyConstants.RESULT_ADDRESS_COORDINATE);
            if (mAlarmHandleDialog == null) {
                return;
            }
            mAlarmHandleDialog.updateLocation(addressStr, addressCoordinate, true);
        }
    }

    /**
     * 上传图片(视频)状态回调
     */
    private IUploadSeparateBackView uploadCallback = new IUploadSeparateBackView() {
        @Override
        public void showOrHidePb(boolean isShow, String tip) {
            if (isShow) {
                showProgressDialog(tip);
            } else {
                hideProgressDialog();
            }
        }

        @Override
        public void onUpResultCallback(int type, List<String> upResult) {
            final String sizeStr = upResult == null ? "0" : upResult.size() + "";
            MyLogUtils.i("onUpResultCallback() type = " + type + "; dataSize = " + sizeStr);
            if (type == IUploadSeparateBackView.UPLOAD_FAIL || CommonUtils.isEmptyList(upResult)) {
                return;
            }
            for (int i = 0; i < upResult.size(); i++) {
                final AlarmHandleFileBean upBean = new AlarmHandleFileBean();
                upBean.setFileKey(upResult.get(i));
                upBean.setFileType(type == IUploadSeparateBackView.UPLOAD_PIC_SUC ? 1 + "" : 2 + "");
                CommonUtils.listAddAvoidNull(mUploadFiles, upBean);
            }
        }

        @Override
        public void onUpFinish() {
            MyLogUtils.i("onUpFinish()");
            if (mHandleSendBean == null) {
                return;
            }
            mHandleSendBean.setFileList(mUploadFiles);
            if (curShowDialogType == 1) {
                getPresenter().supplementHandle(mHandleSendBean);
            } else {
                getPresenter().alarmHandle(mHandleSendBean);
            }
        }

    };
}
