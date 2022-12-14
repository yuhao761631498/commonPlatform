package com.gdu.command.ui.cases;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.rxjava.rxlife.RxLife;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class PublicAlarmActivity extends BaseActivity<UploadPicPresenter> implements IUploadPicDiaryView {

    private final String CASE_SOURCE = "caseSources";
    private final String CASE_TYPE = "caseTypes";

    private List<TypeCodeBean.DataBean> caseSourceData = new ArrayList<>();

    private List<TypeCodeBean.DataBean> caseTypeData = new ArrayList<>();
    private ImageView backIv;
    private TextView titleTv;
    private ConstraintLayout inc_event_location;
    private TextView eventLocationLabelTv;
    private TextView eventLocationTipTv;
    private EditText eventLocationContentEt;
    private ImageView eventLocationRightIv;
    private ConstraintLayout inc_alarm_time;
    private TextView alarmTimeLabelTv;
    private TextView alarmTimeTipTv;
    private EditText alarmTimeContentEt;
    private ImageView alarmTimeRightIv;
    private ConstraintLayout inc_alarm_type;
    private TextView alarmTypeLabelTv;
    private TextView alarmTypeTipTv;
    private EditText alarmTypeContentEt;
    private ImageView alarmTypeRightIv;
    private ConstraintLayout inc_alarm_content;
    private TextView alarmContentLabelTv;
    private TextView alarmContentTipTv;
    private EditText alarmContentEt;
    private ImageView alarmContentRightIv;

    private List<String> mPicturePathList = new ArrayList<>();
    private List<String> mRealImages;
    private BaseRVAdapter<String> mCasePictureAdapter;
    private RecyclerView picContentRv;
    private TextView confirmBtnTv;

    /**
     * ????????????????????????
     */
    private String uploadPicStr = "";

    /**
     * ?????????????????????
     */
    private LatLng addressCoordinate;
    private String alarmTime;
    private String caseTypeCodeStr;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_public_alarm;
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
        titleTv.setText("????????????");

        initEventLocationView();  //????????????

        initAlarmTimeView();  //????????????

        initAlarmTypeView();  //????????????

        initAlarmContendView();  //????????????

        picContentRv = findViewById(R.id.rv_picContent);
        confirmBtnTv = findViewById(R.id.tv_confirmBtn);
    }


    private void initAlarmContendView() {
        inc_alarm_content = findViewById(R.id.inc_alarm_content);
        alarmContentLabelTv = inc_alarm_content.findViewById(R.id.tv_label);
        alarmContentTipTv = inc_alarm_content.findViewById(R.id.tv_importantTip);
        alarmContentEt = inc_alarm_content.findViewById(R.id.et_content);
        alarmContentRightIv = inc_alarm_content.findViewById(R.id.iv_icon);
        initItemView(alarmContentLabelTv, "???????????????", true, alarmContentTipTv,
                alarmContentEt, "?????????????????????", alarmContentRightIv,
                0);
    }


    private void initAlarmTypeView() {
        inc_alarm_type = findViewById(R.id.inc_alarm_type);
        alarmTypeLabelTv = inc_alarm_type.findViewById(R.id.tv_label);
        alarmTypeTipTv = inc_alarm_type.findViewById(R.id.tv_importantTip);
        alarmTypeContentEt = inc_alarm_type.findViewById(R.id.et_content);
        alarmTypeRightIv = inc_alarm_type.findViewById(R.id.iv_icon);
        alarmTypeContentEt.setEnabled(false);
        initItemView(alarmTypeLabelTv, "???????????????", true, alarmTypeTipTv,
                alarmTypeContentEt, "", alarmTypeRightIv,
                R.mipmap.ic_case_select);
    }

    private void initEventLocationView() {
        inc_event_location = findViewById(R.id.inc_event_location);
        eventLocationLabelTv = inc_event_location.findViewById(R.id.tv_label);
        eventLocationTipTv = inc_event_location.findViewById(R.id.tv_importantTip);
        eventLocationContentEt = inc_event_location.findViewById(R.id.et_content);
        eventLocationRightIv = inc_event_location.findViewById(R.id.iv_icon);
        eventLocationContentEt.setEnabled(false);
        initItemView(eventLocationLabelTv, "???????????????", true, eventLocationTipTv,
                eventLocationContentEt, "", eventLocationRightIv,
                R.mipmap.ic_navigation1);
    }

    private void initAlarmTimeView() {
        inc_alarm_time = findViewById(R.id.inc_alarm_time);
        alarmTimeLabelTv = inc_alarm_time.findViewById(R.id.tv_label);
        alarmTimeTipTv = inc_alarm_time.findViewById(R.id.tv_importantTip);
        alarmTimeContentEt = inc_alarm_time.findViewById(R.id.et_content);
        alarmTimeRightIv = inc_alarm_time.findViewById(R.id.iv_icon);
        alarmTimeContentEt.setEnabled(false);
        initItemView(alarmTimeLabelTv, "???????????????", true, alarmTimeTipTv,
                alarmTimeContentEt, "", alarmTimeRightIv,
                0);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy???MM???dd??? HH:mm:ss");// HH:mm:ss
        //??????????????????
        Date date = new Date(System.currentTimeMillis());
        alarmTime = simpleDateFormat.format(date);
        alarmTimeContentEt.setText(alarmTime);
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
                    MyImageLoadUtils.loadImage(PublicAlarmActivity.this, path, 0,
                            contentIv);
                }
            }
        };
        mCasePictureAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mPicturePathList.get(position).equals("add")) {
                ImageSelector.builder()
                        .useCamera(true) // ????????????????????????
                        .setSingle(false)  //??????????????????
                        .canPreview(true) //????????????????????????????????????true
                        .setContainVideo(true)
                        .setContainPhoto(true)
                        .setMaxSelectCount(11 - mPicturePathList.size())
                        .start((Activity) mContext, MyConstants.REQUEST_PIC_CODE); // ????????????
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

    private void initListener() {
        backIv.setOnClickListener(v -> {
            finish();
        });

        alarmTypeRightIv.setOnClickListener(v -> {
            if (caseSourceData == null || caseSourceData.size() == 0) {
                ToastUtil.s("????????????????????????");
                return;
            }
            showTypeSelectDialog("??????????????????", caseTypeData);
        });

        eventLocationRightIv.setOnClickListener(v -> {
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
                    ToastUtil.s("??????????????????");
                }
            } else {
                ToastUtil.s("?????????????????????");
            }
        });
    }

    private void initItemView(TextView labelTv, String labelContent, boolean isImportant,
                              TextView tipTv, EditText contentEt, String hint,
                              ImageView rightIv, int rightPicId) {
        labelTv.setText(labelContent);
        tipTv.setVisibility(isImportant ? View.VISIBLE : View.INVISIBLE);
        contentEt.setHint(hint);
        if (rightIv != null) {
            rightIv.setVisibility(rightPicId != 0 ? View.VISIBLE : View.GONE);
            if (rightPicId != 0) {
                rightIv.setImageResource(rightPicId);
            }
        }
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
                    MyLogUtils.e("???????????????????????????????????????", throwable);
                });
    }


    private void sendHandler() {
        final String reportAddressStr = eventLocationContentEt.getText().toString().trim();
        if (CommonUtils.isEmptyString(reportAddressStr)) {
            ToastUtil.s("????????????????????????");
            return;
        }

        final String caseTypeStr = alarmTypeContentEt.getText().toString().trim();
        if (CommonUtils.isEmptyString(caseTypeStr)) {
            ToastUtil.s("????????????????????????");
            return;
        }

        final String alarmContent = alarmContentEt.getText().toString().trim();
        if (CommonUtils.isEmptyString(alarmContent)) {
            ToastUtil.s("????????????????????????");
            return;
        }

        if (CommonUtils.isEmptyString(uploadPicStr)) {
            ToastUtil.s("??????????????????,????????????????????????");
            return;
        }

        if (CommonUtils.isEmptyString(alarmTime)) {
            ToastUtil.s("????????????????????????");
            return;
        }

        //TODO ???????????????????????????
        addCase(alarmContent, "", alarmTime, caseTypeCodeStr, "",
                "", reportAddressStr, "", "",
                0, uploadPicStr, "",
                addressCoordinate != null ? addressCoordinate.latitude : 0,
                addressCoordinate != null ? addressCoordinate.longitude : 0,
                "",
                "", "");
    }


    /**
     * ????????????
     *
     * @param caseDesc          ?????????????????? *
     * @param caseName          ???????????? *
     * @param caseStartTime     ???????????? *
     * @param caseType          ???????????? *
     * @param infoSource        ???????????? *
     * @param receivingAlarmMan ???????????????????????? *
     * @param reportAddr        ???????????? *
     * @param reportMan         ????????? *
     * @param reportTime        ???????????? *
     * @param alarmId           ??????id
     * @param caseFile          ????????????(?????????????????????)
     * @param designateMan      ?????????(????????????ID-??????ID)
     * @param latitude          ?????????????????? *
     * @param longitude         ?????????????????? *
     * @param reportTel         ???????????? *
     * @param reporterGender    ???????????????
     * @param reporterIdentity  ?????????????????????
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
                    final boolean isFail = bean == null || bean.getCode() != 0 || bean.getData() == null;
                    if (isFail) {
                        ToastUtil.s("??????????????????");
                        return;
                    }
                    EventBus.getDefault().post(new RefreshMyCaseEvent(CaseStatus.HANDLING.getKey(),
                            bean.getData().getId()));
                    ToastUtil.s("??????????????????");
                    finish();
                }, throwable -> {
                    hideProgressDialog();
                    ToastUtil.s("??????????????????");
                    MyLogUtils.e("??????????????????", throwable);
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final boolean isHaveMapData = requestCode == MyConstants.SHOW_MANUAL_LOCATION_REQUEST_CODE
                && resultCode == MyConstants.RESULT_ADDRESS_INTENT
                && data != null && data.getExtras() != null;
        if (requestCode == MyConstants.REQUEST_PIC_CODE && data != null) {
            //??????????????????????????????
            mRealImages = data.getStringArrayListExtra(
                    ImageSelector.SELECT_RESULT);
            CommonUtils.listAddAllAvoidNPE(mPicturePathList, mRealImages);
            mCasePictureAdapter.setList(mPicturePathList);
        } else if (isHaveMapData) {
            final Bundle mBundle = data.getExtras();
            final String addressStr = mBundle.getString(MyConstants.RESULT_ADDRESS, "");
            addressCoordinate = mBundle.getParcelable(MyConstants.RESULT_ADDRESS_COORDINATE);
            eventLocationContentEt.setText(addressStr);
        }
    }

    /**
     * ??????????????????
     *
     * @param data
     */
    private void showTypeSelectDialog(String title, List<TypeCodeBean.DataBean> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        TypeSelectDialog dialog = new TypeSelectDialog(mContext, title, data);
        dialog.setListener((position, o) -> {
            MyLogUtils.d("onItemClick() position = " + position);
            alarmTypeContentEt.setText(data.get(position).getTypeName());
            caseTypeCodeStr = data.get(position).getTypeCode();
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
            ToastUtil.s("??????????????????");
        }
    }

    @Override
    public void onStatusChange(int type, String urlImg, String urlVideo) {

    }
}