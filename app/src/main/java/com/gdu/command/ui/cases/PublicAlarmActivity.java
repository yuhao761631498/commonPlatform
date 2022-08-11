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
     * 上传图片路径集合
     */
    private String uploadPicStr = "";

    /**
     * 选择的地址坐标
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
        titleTv.setText("公众上报");

        initEventLocationView();  //事发地点

        initAlarmTimeView();  //告警时间

        initAlarmTypeView();  //违法类型

        initAlarmContendView();  //告警内容

        picContentRv = findViewById(R.id.rv_picContent);
        confirmBtnTv = findViewById(R.id.tv_confirmBtn);
    }


    private void initAlarmContendView() {
        inc_alarm_content = findViewById(R.id.inc_alarm_content);
        alarmContentLabelTv = inc_alarm_content.findViewById(R.id.tv_label);
        alarmContentTipTv = inc_alarm_content.findViewById(R.id.tv_importantTip);
        alarmContentEt = inc_alarm_content.findViewById(R.id.et_content);
        alarmContentRightIv = inc_alarm_content.findViewById(R.id.iv_icon);
        initItemView(alarmContentLabelTv, "告警内容：", true, alarmContentTipTv,
                alarmContentEt, "请输入告警内容", alarmContentRightIv,
                0);
    }


    private void initAlarmTypeView() {
        inc_alarm_type = findViewById(R.id.inc_alarm_type);
        alarmTypeLabelTv = inc_alarm_type.findViewById(R.id.tv_label);
        alarmTypeTipTv = inc_alarm_type.findViewById(R.id.tv_importantTip);
        alarmTypeContentEt = inc_alarm_type.findViewById(R.id.et_content);
        alarmTypeRightIv = inc_alarm_type.findViewById(R.id.iv_icon);
        alarmTypeContentEt.setEnabled(false);
        initItemView(alarmTypeLabelTv, "违法类型：", true, alarmTypeTipTv,
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
        initItemView(eventLocationLabelTv, "事发地点：", true, eventLocationTipTv,
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
        initItemView(alarmTimeLabelTv, "告警时间：", true, alarmTimeTipTv,
                alarmTimeContentEt, "", alarmTimeRightIv,
                0);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
        //获取当前时间
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
                        .useCamera(true) // 设置是否使用拍照
                        .setSingle(false)  //设置是否单选
                        .canPreview(true) //是否可以预览图片，默认为true
                        .setContainVideo(true)
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

    private void initListener() {
        backIv.setOnClickListener(v -> {
            finish();
        });

        alarmTypeRightIv.setOnClickListener(v -> {
            if (caseSourceData == null || caseSourceData.size() == 0) {
                ToastUtil.s("未获取到告警类型");
                return;
            }
            showTypeSelectDialog("选择告警类型", caseTypeData);
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
                    ToastUtil.s("上传文件太大");
                }
            } else {
                ToastUtil.s("请选择上传图片");
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
                    MyLogUtils.e("获取案件来源或案件类型出错", throwable);
                });
    }


    private void sendHandler() {
        final String reportAddressStr = eventLocationContentEt.getText().toString().trim();
        if (CommonUtils.isEmptyString(reportAddressStr)) {
            ToastUtil.s("事发地点不能为空");
            return;
        }

        final String caseTypeStr = alarmTypeContentEt.getText().toString().trim();
        if (CommonUtils.isEmptyString(caseTypeStr)) {
            ToastUtil.s("违法类型不能为空");
            return;
        }

        final String alarmContent = alarmContentEt.getText().toString().trim();
        if (CommonUtils.isEmptyString(alarmContent)) {
            ToastUtil.s("告警内容不能为空");
            return;
        }

        if (CommonUtils.isEmptyString(uploadPicStr)) {
            ToastUtil.s("图片上传失败,请删除后重新上传");
            return;
        }

        if (CommonUtils.isEmptyString(alarmTime)) {
            ToastUtil.s("告警时间不能为空");
            return;
        }

        //TODO 接口上传的时间格式
        addCase(alarmContent, "", alarmTime, caseTypeCodeStr, "",
                "", reportAddressStr, "", "",
                0, uploadPicStr, "",
                addressCoordinate != null ? addressCoordinate.latitude : 0,
                addressCoordinate != null ? addressCoordinate.longitude : 0,
                "",
                "", "");
    }


    /**
     * 添加案件
     *
     * @param caseDesc          案件简要描述 *
     * @param caseName          案件名称 *
     * @param caseStartTime     案发时间 *
     * @param caseType          案件类型 *
     * @param infoSource        案件来源 *
     * @param receivingAlarmMan 接警人、接案人员 *
     * @param reportAddr        举报地址 *
     * @param reportMan         举报人 *
     * @param reportTime        报警时间 *
     * @param alarmId           告警id
     * @param caseFile          案件文件(多个以逗号分隔)
     * @param designateMan      指派人(逗号分隔ID-人员ID)
     * @param latitude          举报地址纬度 *
     * @param longitude         举报地址经度 *
     * @param reportTel         举报电话 *
     * @param reporterGender    举报人性别
     * @param reporterIdentity  举报人身份证号
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
            eventLocationContentEt.setText(addressStr);
        }
    }

    /**
     * 显示时间选择
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
            ToastUtil.s("图片上传失败");
        }
    }

    @Override
    public void onStatusChange(int type, String urlImg, String urlVideo) {

    }
}