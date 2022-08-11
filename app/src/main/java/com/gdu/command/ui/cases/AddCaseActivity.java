package com.gdu.command.ui.cases;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps2d.model.LatLng;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.donkingliang.imageselector.utils.ImageSelector;
import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.baselibrary.base.BaseRVAdapter;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.MyConstants;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.baselibrary.utils.imageloade.MyImageLoadUtils;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.R;
import com.gdu.command.event.RefreshMyCaseEvent;
import com.gdu.command.ui.secondmap.ManualLocationActivity;
import com.gdu.command.uploadpic.IUploadPicDiaryView;
import com.gdu.command.uploadpic.UploadPicPresenter;
import com.gdu.model.cases.CaseStatus;
import com.gdu.model.config.GlobalVariable;
import com.gdu.picktime.DatePickDialog;
import com.gdu.picktime.bean.DateType;
import com.gdu.util.TimeUtils;
import com.gdu.utils.MyUiUtils;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.rxjava.rxlife.RxLife;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 添加案件
 */
public class AddCaseActivity extends BaseActivity<UploadPicPresenter>
        implements IUploadPicDiaryView {

    private ImageView backIv;
    private TextView titleTv;

    private ConstraintLayout inc_caseDesc;
    private TextView caseDescLabelTv;
    private TextView caseDescTipTv;
    private EditText caseDescContentEt;
    private ImageView caseDescRightIv;

    private ConstraintLayout inc_caseSource;
    private TextView caseSourceLabelTv;
    private TextView caseSourceTipTv;
    private EditText caseSourceContentEt;
    private ImageView caseSourceRightIv;

    private ConstraintLayout inc_caseTime;
    private TextView caseTimeLabelTv;
    private TextView caseTimeTipTv;
    private EditText caseTimeContentEt;
    private ImageView caseTimeRightIv;

    private ConstraintLayout inc_caseType;
    private TextView caseTypeLabelTv;
    private TextView caseTypeTipTv;
    private EditText caseTypeContentEt;
    private ImageView caseTypeRightIv;

    private ConstraintLayout inc_alarmTime;
    private TextView alarmTimeLabelTv;
    private TextView alarmTimeTipTv;
    private EditText alarmTimeContentEt;
    private ImageView alarmTimeRightIv;

    private ConstraintLayout inc_reportPeople;
    private TextView reportPeopleLabelTv;
    private TextView reportPeopleTipTv;
    private EditText reportPeopleContentEt;
    private ImageView reportPeopleRightIv;

    private ConstraintLayout inc_contactPhone;
    private TextView contactPhoneLabelTv;
    private TextView contactPhoneTipTv;
    private EditText contactPhoneContentEt;
    private ImageView contactPhoneRightIv;

    private ConstraintLayout inc_idCard;
    private TextView idCardLabelTv;
    private TextView idCardTipTv;
    private EditText idCardContentEt;
    private ImageView idCardRightIv;

    private ConstraintLayout inc_reportAddress;
    private TextView reportAddressLabelTv;
    private TextView reportAddressTipTv;
    private EditText reportAddressContentEt;
    private ImageView reportAddressRightIv;

    private ConstraintLayout inc_situationReport;
    private TextView situationReportLabelTv;
    private TextView situationReportTipTv;
    private EditText situationReportContentEt;
    private ImageView situationReportRightIv;

    private RecyclerView picContentRv;
    private TextView confirmBtnTv;

    private long mStartTimeL;
    private long mEndTimeL;

    private List<String> mPicturePathList = new ArrayList<>();
    private List<String>  mRealImages;
    private BaseRVAdapter<String> mCasePictureAdapter;

    private final String CASE_SOURCE = "caseSources";
    private final String CASE_TYPE = "caseTypes";

    private List<TypeCodeBean.DataBean> caseSourceData = new ArrayList<>();
    private List<TypeCodeBean.DataBean> caseTypeData = new ArrayList<>();

    /** 选择的地址坐标 */
    private LatLng addressCoordinate;
    /** 上传图片路径集合 */
    private String uploadPicStr = "";
    /** 案件来源类型内容 */
    private String caseSourceCodeStr = "";
    /** 案件类型内容 */
    private String caseTypeCodeStr = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_case;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).autoDarkModeEnable(true).statusBarColor(
                R.color.color_224CD0).init();
        getPresenter().setIView(this);
        initView();
        initAdapter();
        initListener();

        getTypeOrSource(CASE_SOURCE);
    }

    private void initView() {
        backIv = findViewById(R.id.iv_left_back);
        titleTv = findViewById(R.id.tv_left_title_name);
        titleTv.setText("新增案件");

        initCaseDescView();

        initCaseSourceView();

        initCaseTimeView();

        initCaseTypeView();

        initAlarmTimeView();

        initReportContentView();

        initContactPhoneView();

        initIdCardView();

        initReportAddressView();

        initSituationReportView();

        picContentRv = findViewById(R.id.rv_picContent);
        confirmBtnTv = findViewById(R.id.tv_confirmBtn);
    }

    private void initAdapter() {
        mPicturePathList.clear();
        mPicturePathList.add("add");

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
                    MyImageLoadUtils.loadImage(AddCaseActivity.this, path, 0,
                            contentIv);
                }
            }
        };
        mCasePictureAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mPicturePathList.get(position).equals("add")) {
                ImageSelector.builder()
                        .useCamera(true) // 设置是否使用拍照
                        .setSingle(false)  //设置是否单选
                        .canPreview(true) //是否可以预览图片，默认为true
                        .setContainVideo(false)
                        .setContainPhoto(true)
                        .setMaxSelectCount(11 - mPicturePathList.size())
                        .start((Activity) mContext, MyConstants.REQUEST_PIC_CODE); // 打开相册
            }
        });
        mCasePictureAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.iv_delBtn) {
                for (int i = 0; i < mPicturePathList.size(); i++) {
                    if (mPicturePathList.get(position).equals(mPicturePathList.get(i))) {
                        mPicturePathList.remove(position);
                        break;
                    }
                }
                mCasePictureAdapter.setList(mPicturePathList);
            }
        });
        mCasePictureAdapter.addChildClickViewIds(R.id.iv_delBtn);
        mCasePictureAdapter.setList(mPicturePathList);
        picContentRv.setAdapter(mCasePictureAdapter);
    }

    private void initSituationReportView() {
        inc_situationReport = findViewById(R.id.inc_situationReport);
        situationReportLabelTv = inc_situationReport.findViewById(R.id.tv_label);
        situationReportTipTv = inc_situationReport.findViewById(R.id.tv_importantTip);
        situationReportContentEt = inc_situationReport.findViewById(R.id.et_content);
        situationReportRightIv = inc_situationReport.findViewById(R.id.iv_icon);
        View line = inc_situationReport.findViewById(R.id.view_line1);
        line.setVisibility(View.GONE);
        situationReportContentEt.setMinHeight(MyUiUtils.dip2px(80));
        situationReportContentEt.setGravity(Gravity.START);
        situationReportContentEt.setPadding(MyUiUtils.dip2px(3), MyUiUtils.dip2px(3),
                MyUiUtils.dip2px(3), MyUiUtils.dip2px(3));
        final GradientDrawable mDrawable = new GradientDrawable();
        mDrawable.setCornerRadius(MyUiUtils.dip2px(8));
        mDrawable.setStroke(MyUiUtils.dip2px(1), MyUiUtils.getColor(R.color.color_E9EEFF));
        situationReportContentEt.setBackground(mDrawable);
        final ConstraintLayout.LayoutParams mLayoutParams =
                new ConstraintLayout.LayoutParams(0,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT);
        mLayoutParams.startToEnd = R.id.tv_importantTip;
        mLayoutParams.topToTop = R.id.inc_situationReport;
        mLayoutParams.endToEnd = R.id.inc_situationReport;
        mLayoutParams.setMargins(0, MyUiUtils.dip2px(15), 0, 0);
        situationReportContentEt.setLayoutParams(mLayoutParams);
        initItemView(situationReportLabelTv, "情况说明", true, situationReportTipTv,
                situationReportContentEt, "请输入情况说明", situationReportRightIv, 0);
    }

    private void initReportAddressView() {
        inc_reportAddress = findViewById(R.id.inc_reportAddress);
        reportAddressLabelTv = inc_reportAddress.findViewById(R.id.tv_label);
        reportAddressTipTv = inc_reportAddress.findViewById(R.id.tv_importantTip);
        reportAddressContentEt = inc_reportAddress.findViewById(R.id.et_content);
        reportAddressRightIv = inc_reportAddress.findViewById(R.id.iv_icon);
        reportAddressContentEt.setEnabled(false);
        initItemView(reportAddressLabelTv, "举报地址", true, reportAddressTipTv,
                reportAddressContentEt, "请输入举报地址", reportAddressRightIv,
                R.mipmap.ic_navigation1);
    }

    private void initIdCardView() {
        inc_idCard = findViewById(R.id.inc_idCard);
        idCardLabelTv = inc_idCard.findViewById(R.id.tv_label);
        idCardTipTv = inc_idCard.findViewById(R.id.tv_importantTip);
        idCardContentEt = inc_idCard.findViewById(R.id.et_content);
        idCardRightIv = inc_idCard.findViewById(R.id.iv_icon);
        initItemView(idCardLabelTv, "身份证号", false, idCardTipTv,
                idCardContentEt, "请输入身份证号", idCardRightIv,
                0);
    }

    private void initContactPhoneView() {
        inc_contactPhone = findViewById(R.id.inc_contactPhone);
        contactPhoneLabelTv = inc_contactPhone.findViewById(R.id.tv_label);
        contactPhoneTipTv = inc_contactPhone.findViewById(R.id.tv_importantTip);
        contactPhoneContentEt = inc_contactPhone.findViewById(R.id.et_content);
        contactPhoneRightIv = inc_contactPhone.findViewById(R.id.iv_icon);
        contactPhoneContentEt.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        initItemView(contactPhoneLabelTv, "联系电话", false, contactPhoneTipTv,
                contactPhoneContentEt, "请输入联系电话", contactPhoneRightIv,
                0);
    }

    private void initReportContentView() {
        inc_reportPeople = findViewById(R.id.inc_reportPeople);
        reportPeopleLabelTv = inc_reportPeople.findViewById(R.id.tv_label);
        reportPeopleTipTv = inc_reportPeople.findViewById(R.id.tv_importantTip);
        reportPeopleContentEt = inc_reportPeople.findViewById(R.id.et_content);
        reportPeopleRightIv = inc_reportPeople.findViewById(R.id.iv_icon);
        initItemView(reportPeopleLabelTv, "举报人", false, reportPeopleTipTv,
                reportPeopleContentEt, "请输入举报人", reportPeopleRightIv,
                0);
    }

    private void initAlarmTimeView() {
        inc_alarmTime = findViewById(R.id.inc_alarmTime);
        alarmTimeLabelTv = inc_alarmTime.findViewById(R.id.tv_label);
        alarmTimeTipTv = inc_alarmTime.findViewById(R.id.tv_importantTip);
        alarmTimeContentEt = inc_alarmTime.findViewById(R.id.et_content);
        alarmTimeRightIv = inc_alarmTime.findViewById(R.id.iv_icon);
        alarmTimeContentEt.setEnabled(false);
        initItemView(alarmTimeLabelTv, "报警时间", true, alarmTimeTipTv,
                alarmTimeContentEt, "请选择报警时间", alarmTimeRightIv,
                R.mipmap.ic_case_canlendar);
    }

    private void initCaseTypeView() {
        inc_caseType = findViewById(R.id.inc_caseType);
        caseTypeLabelTv = inc_caseType.findViewById(R.id.tv_label);
        caseTypeTipTv = inc_caseType.findViewById(R.id.tv_importantTip);
        caseTypeContentEt = inc_caseType.findViewById(R.id.et_content);
        caseTypeRightIv = inc_caseType.findViewById(R.id.iv_icon);
        caseTypeContentEt.setEnabled(false);
        initItemView(caseTypeLabelTv, "案件类型", true, caseTypeTipTv,
                caseTypeContentEt, "请选择案件类型", caseTypeRightIv,
                R.mipmap.ic_case_select);
    }

    private void initCaseTimeView() {
        inc_caseTime = findViewById(R.id.inc_caseTime);
        caseTimeLabelTv = inc_caseTime.findViewById(R.id.tv_label);
        caseTimeTipTv = inc_caseTime.findViewById(R.id.tv_importantTip);
        caseTimeContentEt = inc_caseTime.findViewById(R.id.et_content);
        caseTimeRightIv = inc_caseTime.findViewById(R.id.iv_icon);
        caseTimeContentEt.setEnabled(false);
        initItemView(caseTimeLabelTv, "案发时间", true, caseTimeTipTv,
                caseTimeContentEt, "请选择案发时间", caseTimeRightIv,
                R.mipmap.ic_case_canlendar);
    }

    private void initCaseSourceView() {
        inc_caseSource = findViewById(R.id.inc_caseSource);
        caseSourceLabelTv = inc_caseSource.findViewById(R.id.tv_label);
        caseSourceTipTv = inc_caseSource.findViewById(R.id.tv_importantTip);
        caseSourceContentEt = inc_caseSource.findViewById(R.id.et_content);
        caseSourceRightIv = inc_caseSource.findViewById(R.id.iv_icon);
        caseSourceContentEt.setEnabled(false);
        initItemView(caseSourceLabelTv, "案件来源", true, caseSourceTipTv,
                caseSourceContentEt, "请选择案件来源", caseSourceRightIv,
                R.mipmap.ic_case_select);
    }

    private void initCaseDescView() {
        inc_caseDesc = findViewById(R.id.inc_caseDesc);
        caseDescLabelTv = inc_caseDesc.findViewById(R.id.tv_label);
        caseDescTipTv = inc_caseDesc.findViewById(R.id.tv_importantTip);
        caseDescContentEt = inc_caseDesc.findViewById(R.id.et_content);
        caseDescRightIv = inc_caseDesc.findViewById(R.id.iv_icon);
        initItemView(caseDescLabelTv, "案件简述", true, caseDescTipTv,
                caseDescContentEt, "请输入案件简述", caseDescRightIv,
                0);
    }

    private void initListener() {
        backIv.setOnClickListener(v -> {
            finish();
        });
        caseSourceRightIv.setOnClickListener(v -> {
            if (caseSourceData == null || caseSourceData.size() == 0) {
                ToastUtil.s("未获取到案件来源数据");
                return;
            }
            showTypeSelectDialog("选择案件来源", caseSourceData);
        });
        caseTimeRightIv.setOnClickListener(v -> {
            showDatePickDialog(DateType.TYPE_YMDHMS, false);
        });
        caseTypeRightIv.setOnClickListener(v -> {
            if (caseSourceData == null || caseSourceData.size() == 0) {
                ToastUtil.s("未获取到案件类型数据");
                return;
            }
            showTypeSelectDialog("选择案件类型", caseTypeData);
        });
        alarmTimeRightIv.setOnClickListener(v -> {
            showDatePickDialog(DateType.TYPE_YMDHMS, true);
        });
        reportAddressRightIv.setOnClickListener(v -> {
            openActivityForResult(ManualLocationActivity.class, MyConstants.SHOW_MANUAL_LOCATION_REQUEST_CODE);
        });
        confirmBtnTv.setOnClickListener(v -> {
            List<String> files = new ArrayList<>();
            if (mRealImages != null && mRealImages.size() > 0) {
                files.addAll(mRealImages);
            }
            if (files.size() != 0) {
                if (!CommonUtils.isOverSize(files)) {
                    getPresenter().uploadFiles(mRealImages, null);
                } else {
                    ToastUtil.s("上传文件太大");
                }
            } else {
                ToastUtil.s("请选择上传图片");
            }
        });
    }

    private void sendHandler() {
        final String caseDescStr = caseDescContentEt.getText().toString().trim();
        if (CommonUtils.isEmptyString(caseDescStr)) {
            ToastUtil.s("案件简述不能为空");
            return;
        }
        final String caseSourceStr = caseSourceContentEt.getText().toString().trim();
        if (CommonUtils.isEmptyString(caseSourceStr)) {
            ToastUtil.s("案件来源不能为空");
            return;
        }
        final String caseTimeStr = caseTimeContentEt.getText().toString().trim();
        if (CommonUtils.isEmptyString(caseTimeStr)) {
            ToastUtil.s("案发时间不能为空");
            return;
        }
        final String caseTypeStr = caseTypeContentEt.getText().toString().trim();
        if (CommonUtils.isEmptyString(caseTypeStr)) {
            ToastUtil.s("案发类型不能为空");
            return;
        }
        final String alarmTimeStr = alarmTimeContentEt.getText().toString().trim();
        if (CommonUtils.isEmptyString(alarmTimeStr)) {
            ToastUtil.s("报警时间不能为空");
            return;
        }
        final String reportPeopleStr = reportPeopleContentEt.getText().toString().trim();
//        if (CommonUtils.isEmptyString(reportPeopleStr)) {
//            ToastUtil.s("举报人不能为空");
//            return;
//        }
        final String contactPhoneStr = contactPhoneContentEt.getText().toString().trim();
//        if (CommonUtils.isEmptyString(contactPhoneStr)) {
//            ToastUtil.s("联系电话不能为空");
//            return;
//        }
        final String idCardStr = idCardContentEt.getText().toString().trim();

        final String reportAddressStr = reportAddressContentEt.getText().toString().trim();
        if (CommonUtils.isEmptyString(reportAddressStr)) {
            ToastUtil.s("举报地址不能为空");
            return;
        }
        final String situationReportStr = situationReportContentEt.getText().toString().trim();
        if (CommonUtils.isEmptyString(situationReportStr)) {
            ToastUtil.s("情况说明不能为空");
            return;
        }

        if (CommonUtils.isEmptyString(uploadPicStr)) {
            ToastUtil.s("图片上传失败,请删除后重新上传");
            return;
        }

        addCase(situationReportStr, caseDescStr, caseTimeStr, caseTypeCodeStr, caseSourceCodeStr,
                "", reportAddressStr, reportPeopleStr, alarmTimeStr,
                0, uploadPicStr, "",
                addressCoordinate != null ? addressCoordinate.latitude : 0,
                addressCoordinate != null ? addressCoordinate.longitude : 0,
                contactPhoneStr,
                "", idCardStr);
    }

    private void initItemView(TextView labelTv, String labelContent, boolean isImportant,
                              TextView tipTv, EditText contentEt, String hint,
                              ImageView rightIv, int rightPicId) {
        labelTv.setText(labelContent);
        tipTv.setVisibility(isImportant ? View.VISIBLE : View.INVISIBLE);
        contentEt.setHint(hint);
        rightIv.setVisibility(rightPicId != 0 ? View.VISIBLE : View.GONE);
        if (rightPicId != 0) {
            rightIv.setImageResource(rightPicId);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode,  resultCode, data);
        final boolean isHaveMapData = requestCode == MyConstants.SHOW_MANUAL_LOCATION_REQUEST_CODE
                && resultCode == MyConstants.RESULT_ADDRESS_INTENT
                && data != null && data.getExtras() != null;
        if (requestCode == MyConstants.REQUEST_PIC_CODE && data != null) {
            //获取选择器返回的数据
            mRealImages = data.getStringArrayListExtra(
                    ImageSelector.SELECT_RESULT);
            CommonUtils.listAddAllAvoidNPE(mPicturePathList, mRealImages);
            mCasePictureAdapter.setList(mPicturePathList);
        } else if (isHaveMapData) {
            final Bundle mBundle = data.getExtras();
            final String addressStr = mBundle.getString(MyConstants.RESULT_ADDRESS, "");
            addressCoordinate = mBundle.getParcelable(MyConstants.RESULT_ADDRESS_COORDINATE);
            reportAddressContentEt.setText(addressStr);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void getTypeOrSource(String typeCode) {
        CaseService caseService = RetrofitClient.getAPIService(CaseService.class);
        caseService.getTypeOrSource(typeCode)
                .subscribeOn(Schedulers.io())
                .to(RxLife.toMain(this))
                .subscribe(bean -> {
                    final boolean isEmpty = bean == null || bean.getCode() != 0
                            || bean.getData() == null || bean.getData().size() == 0;
                    if (isEmpty) {
                        return;
                    }
                    if (CASE_SOURCE.equals(typeCode)) {
                        caseSourceData.clear();
                        CommonUtils.listAddAllAvoidNPE(caseSourceData, bean.getData());
                        getTypeOrSource(CASE_TYPE);
                    } else {
                        caseTypeData.clear();
                        CommonUtils.listAddAllAvoidNPE(caseTypeData, bean.getData());
                    }
                }, throwable -> {
                    if (CASE_SOURCE.equals(typeCode)) {
                        getTypeOrSource(CASE_TYPE);
                    }
                    MyLogUtils.e("获取案件来源或案件类型出错", throwable);
                });
    }

    /**
     * 添加案件
     * @param caseDesc 案件简要描述 *
     * @param caseName 案件名称 *
     * @param caseStartTime 案发时间 *
     * @param caseType 案件类型 *
     * @param infoSource 案件来源 *
     * @param receivingAlarmMan 接警人、接案人员 *
     * @param reportAddr 举报地址 *
     * @param reportMan 举报人 *
     * @param reportTime 报警时间 *
     * @param alarmId 告警id
     * @param caseFile 案件文件(多个以逗号分隔)
     * @param designateMan 指派人(逗号分隔ID-人员ID)
     * @param latitude 举报地址纬度 *
     * @param longitude 举报地址经度 *
     * @param reportTel 举报电话 *
     * @param reporterGender 举报人性别
     * @param reporterIdentity 举报人身份证号
     */
    public void addCase(String caseDesc, String caseName, String caseStartTime, String caseType,
                        String infoSource, String receivingAlarmMan, String reportAddr,
                        String reportMan, String reportTime, int alarmId, String caseFile,
                        String designateMan, double latitude, double longitude,
                        String reportTel, String reporterGender, String reporterIdentity) {
        final Map<String, Object> map = new HashMap<>();
        map.put("caseDesc", caseDesc);
        map.put("caseName", caseName);
        map.put("caseStartTime", caseStartTime);
        map.put("caseType", caseType);
        map.put("infoSource", infoSource);
        if (!CommonUtils.isEmptyString(receivingAlarmMan)) {
            map.put("receivingAlarmMan", receivingAlarmMan);
        }
        map.put("reportAddr", reportAddr);
        map.put("reportMan", reportMan);
        map.put("reportTime", reportTime);
        if (alarmId != 0) {
            map.put("alarmId", alarmId);
        }
        if (!CommonUtils.isEmptyString(caseFile)) {
            map.put("caseFile", caseFile);
        }
        if (!CommonUtils.isEmptyString(designateMan)) {
            map.put("designateMan", designateMan);
        }
        if (latitude != 0) {
            map.put("latitude", latitude);
        }
        if (longitude != 0) {
            map.put("longitude", longitude);
        }
        if (!CommonUtils.isEmptyString(reportTel)) {
            map.put("reportTel", reportTel);
        }
        if (!CommonUtils.isEmptyString(reporterGender)) {
            map.put("reporterGender", reporterGender);
        }
        if (!CommonUtils.isEmptyString(reporterIdentity)) {
            map.put("reporterIdentity", reporterIdentity);
        }

        final String strEntity = new Gson().toJson(map);
        final RequestBody body = RequestBody.create(strEntity, MediaType.parse("application/json;" +
                "charset=UTF-8"));
        CaseService caseService = RetrofitClient.getAPIService(CaseService.class);
        caseService.addCase(body)
                .subscribeOn(Schedulers.io())
                .to(RxLife.toMain(this))
                .subscribe(bean -> {
                    hideProgressDialog();
                    final boolean isFail = bean == null || bean.getCode() != 0 || bean.getData() == null ;
                    if (isFail) {
                        ToastUtil.s("新增案件失败");
                        return;
                    }
                    EventBus.getDefault().post(new RefreshMyCaseEvent(CaseStatus.HANDLING.getKey(),
                            bean.getData().getId()));
                    ToastUtil.s("案件新增成功");
                    finish();
                }, throwable -> {
                    hideProgressDialog();
                    ToastUtil.s("新增案件失败");
                    MyLogUtils.e("新增案件出错", throwable);
                });
    }

    /**
     * 显示时间选择
     * @param type 时间格式
     * @param isAlarmTime 是否是报警时间
     */
    private void showDatePickDialog(DateType type, final boolean isAlarmTime) {
        DatePickDialog dialog = new DatePickDialog(mContext);
        //设置上下年分限制
        dialog.setYearLimt(5);
        //设置标题
        dialog.setTitle("选择时间");
        //设置类型
        dialog.setType(type);
        //设置消息体的显示格式，日期格式
        dialog.setMessageFormat("yyyy-MM-dd HH:mm:ss");
        //设置选择回调
        dialog.setOnChangeLisener(null);
        //设置点击确定按钮回调
        dialog.setOnSureLisener(date -> {
            final long selectTime = date.getTime();
            final long curTime = System.currentTimeMillis();
            if (selectTime > curTime) {
                ToastUtil.s("所选时间不能超过当前时间");
                return;
            }
            String timeStr;
            if (isAlarmTime) {
                final long diff = selectTime - mStartTimeL;
                MyLogUtils.d("showDatePickDialog() selectTime = " + selectTime
                        + "; mStartTimeL = " + mStartTimeL + "; diff = " + diff);
                final boolean haveOver = mStartTimeL != 0 && diff < 0;
                if(haveOver){
                    ToastUtil.s("报警时间不能小于案发时间");
                    return;
                }
                mEndTimeL = selectTime;
                timeStr = TimeUtils.getTime(selectTime, DateType.TYPE_YMDHMS.getFormat());
                alarmTimeContentEt.setText(timeStr);
            } else {
                final long diff = mEndTimeL - selectTime;
                MyLogUtils.d("showDatePickDialog() mEndTimeL = " + mEndTimeL
                        + "; selectTime = " + selectTime + "; diff = " + diff);
                final boolean haveOver = mEndTimeL != 0 && diff < 0;
                if(haveOver){
                    ToastUtil.s("案发时间不能大于报警时间");
                    return;
                }
                mStartTimeL = selectTime;
                timeStr = TimeUtils.getTime(selectTime, type.getFormat());
                caseTimeContentEt.setText(timeStr);
            }
        });
        dialog.show();
    }

    /**
     * 显示时间选择
     * @param data
     */
    private void showTypeSelectDialog(String title, List<TypeCodeBean.DataBean> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        TypeSelectDialog dialog = new TypeSelectDialog(mContext, title, data);
        dialog.setListener((position, o) -> {
            MyLogUtils.d("onItemClick() position = " + position);
            if ("选择案件来源".equals(title)) {
                caseSourceContentEt.setText(data.get(position).getTypeName());
                caseSourceCodeStr = data.get(position).getTypeCode();
            } else {
                caseTypeContentEt.setText(data.get(position).getTypeName());
                caseTypeCodeStr = data.get(position).getTypeCode();
            }
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void showOrHidePb(boolean isShow, String tip) {
        if (isShow) {
            showProgressDialog();
        } else {
            hideProgressDialog();
        }
    }

    @Override
    public void onStatusChange(int type, String content) {
        if (type == IUploadPicDiaryView.UPLOAD_SUC) {
            uploadPicStr = content;
            sendHandler();
        } else {
            ToastUtil.s("图片上传失败");
        }
    }

    @Override
    public void onStatusChange(int type, String urlImg, String urlVideo) {

    }
}
