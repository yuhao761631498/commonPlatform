package com.gdu.command.ui.cases;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.gdu.command.event.DealSiteEvent;
import com.gdu.command.ui.secondmap.ManualLocationActivity;
import com.gdu.command.uploadpic.IUploadPicDiaryView;
import com.gdu.command.uploadpic.UploadPicPresenter;
import com.gdu.model.config.GlobalVariable;
import com.gdu.picktime.DatePickDialog;
import com.gdu.picktime.bean.DateType;
import com.gdu.util.TimeUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.rxjava.rxlife.RxLife;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ArtificialAlarmActivity extends BaseActivity<UploadPicPresenter> implements IUploadPicDiaryView {

    private ImageView backIv;
    private TextView titleTv;

    private ConstraintLayout inc_alarm_type;
    private TextView alarmTypeLabelTv;
    private EditText alarmTypeContentEt;
    private ImageView alarmTypeRightIv;
    private TextView alarmTypeImportantTip;

    private ConstraintLayout inc_alarm_location;
    private TextView alarmLocationLabelTv;
    private EditText alarmLocationContentEt;
    private ImageView alarmLocationRightIv;
    private TextView alarmLocationImportantTip;

    private ConstraintLayout inc_alarm_time;
    private TextView alarmTimeLabelTv;
    private EditText alarmTimeContentEt;
    private ImageView alarmTimeRightIv;
    private TextView alarmTimeImportantTip;

    private ConstraintLayout inc_alarm_area;
    private TextView alarmAreaLabelTv;
    private EditText alarmAreaContentEt;
    private ImageView alarmAreaRightIv;
    private TextView alarmAreaImportantTip;

    private RecyclerView picContentRv;
    private RecyclerView videoContentRv;
    private TextView confirmBtnTv;

    private List<String> mPicturePathList = new ArrayList<>();
    private List<String> mRealImages;
    private BaseRVAdapter<String> mCasePictureAdapter;

    private List<String> mVideoPathList = new ArrayList<>();
    private List<String> mRealVideos;
    private BaseRVAdapter<String> mCaseVideoAdapter;

    private final String alarmType = "alarmType";

    private List<TypeCodeBean.DataBean> alarmTypeData = new ArrayList<>();

    private List<AlarmAreaBean.AreaBean> alarmAreaData = new ArrayList<>();

    /**
     * 上传图片路径集合
     */
    private String uploadPicStr = "";

    private String uploadVideoStr = "";

    private int alarmTypeValue;

    private LatLng addressCoordinate;
    private TextView tv_site_deal;
    private String alarmArea;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_artificial_alarm;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).autoDarkModeEnable(true).statusBarColor(
                R.color.color_224CD0).init();
        getPresenter().setIView(this);
        initView();
        initAdapter();
        initListener();

        getTypeOrSource(alarmType);
    }

    private void initView() {
        backIv = findViewById(R.id.iv_left_back);
        titleTv = findViewById(R.id.tv_left_title_name);
        titleTv.setText("人工告警");

        initAlarmAreaView();  //预警区域

        initAlarmTimeView();  //告警时间

        initAlarmTypeView();  //告警类型

        initReportAddressView();  //高杆点位

        picContentRv = findViewById(R.id.rv_picContent);

        videoContentRv = findViewById(R.id.rv_VideoContent);

        confirmBtnTv = findViewById(R.id.tv_confirmBtn);

        tv_site_deal = findViewById(R.id.tv_site_deal);
    }

    private void initAdapter() {
        mPicturePathList.clear();
        mPicturePathList.add("add");

        mVideoPathList.clear();
        mVideoPathList.add("add");

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
                    MyImageLoadUtils.loadImage(ArtificialAlarmActivity.this, path, 0,
                            contentIv);
                }
            }
        };

        mCaseVideoAdapter = new BaseRVAdapter<String>(R.layout.item_case_picture) {
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
                    MyImageLoadUtils.loadImage(ArtificialAlarmActivity.this, path, 0,
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
                        .setMaxSelectCount(8 - mPicturePathList.size())
                        .start((Activity) mContext, MyConstants.REQUEST_PIC_CODE); // 打开相册
            }
        });

        mCaseVideoAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mVideoPathList.get(position).equals("add")) {
                ImageSelector.builder()
                        .useCamera(true) // 设置是否使用拍照
                        .setSingle(false)  //设置是否单选
                        .canPreview(true) //是否可以预览图片，默认为true
                        .setContainVideo(true)
                        .setContainPhoto(false)
                        .setMaxSelectCount(2 - mVideoPathList.size())
                        .start((Activity) mContext, MyConstants.REQUEST_VIDEO_CODE); // 打开相册
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

        mCaseVideoAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.iv_delBtn) {
                for (int i = 0; i < mVideoPathList.size(); i++) {
                    if (mVideoPathList.get(position).equals(mVideoPathList.get(i))) {
                        mVideoPathList.remove(position);
                        break;
                    }
                }
                mCaseVideoAdapter.setList(mVideoPathList);
            }
        });

        mCasePictureAdapter.addChildClickViewIds(R.id.iv_delBtn);
        mCasePictureAdapter.setList(mPicturePathList);
        picContentRv.setAdapter(mCasePictureAdapter);

        mCaseVideoAdapter.addChildClickViewIds(R.id.iv_delBtn);
        mCaseVideoAdapter.setList(mVideoPathList);
        videoContentRv.setAdapter(mCaseVideoAdapter);
    }

    private void initAlarmAreaView() {
        inc_alarm_area = findViewById(R.id.inc_alarm_area);
        alarmAreaLabelTv = inc_alarm_area.findViewById(R.id.tv_label);
        alarmAreaContentEt = inc_alarm_area.findViewById(R.id.et_content);
        alarmAreaRightIv = inc_alarm_area.findViewById(R.id.iv_icon);
        alarmAreaImportantTip = inc_alarm_area.findViewById(R.id.tv_importantTip);
        alarmAreaContentEt.setEnabled(false);
        initItemView(alarmAreaLabelTv, "预警区域：",
                alarmAreaContentEt, "请选择", alarmAreaRightIv,
                R.mipmap.icon_type_select, alarmAreaImportantTip, true);
    }

    private void initAlarmTimeView() {
        inc_alarm_time = findViewById(R.id.inc_alarm_time);
        alarmTimeLabelTv = inc_alarm_time.findViewById(R.id.tv_label);
        alarmTimeContentEt = inc_alarm_time.findViewById(R.id.et_content);
        alarmTimeRightIv = inc_alarm_time.findViewById(R.id.iv_icon);
        alarmTimeImportantTip = inc_alarm_time.findViewById(R.id.tv_importantTip);
        alarmTimeContentEt.setEnabled(false);
        initItemView(alarmTimeLabelTv, "预警时间：",
                alarmTimeContentEt, "请选择", alarmTimeRightIv,
                R.mipmap.icon_time_select, alarmTimeImportantTip, true);
    }

    private void initReportAddressView() {
        inc_alarm_location = findViewById(R.id.inc_alarm_location);
        alarmLocationLabelTv = inc_alarm_location.findViewById(R.id.tv_label);
        alarmLocationContentEt = inc_alarm_location.findViewById(R.id.et_content);
        alarmLocationRightIv = inc_alarm_location.findViewById(R.id.iv_icon);
        alarmLocationImportantTip = inc_alarm_time.findViewById(R.id.tv_importantTip);
        initItemView(alarmLocationLabelTv, "预警地址：",
                alarmLocationContentEt, "请选择", alarmLocationRightIv,
                R.mipmap.icon_location_select, alarmLocationImportantTip, true);
    }

    private void initAlarmTypeView() {
        inc_alarm_type = findViewById(R.id.inc_alarm_type);
        alarmTypeLabelTv = inc_alarm_type.findViewById(R.id.tv_label);
        alarmTypeContentEt = inc_alarm_type.findViewById(R.id.et_content);
        alarmTypeRightIv = inc_alarm_type.findViewById(R.id.iv_icon);
        alarmTypeImportantTip = inc_alarm_time.findViewById(R.id.tv_importantTip);
        alarmTypeContentEt.setEnabled(false);
        initItemView(alarmTypeLabelTv, "预警类型：",
                alarmTypeContentEt, "请选择", alarmTypeRightIv,
                R.mipmap.icon_type_select, alarmTypeImportantTip, true);
    }

    private void initListener() {

        EventBus.getDefault().register(this);

        backIv.setOnClickListener(v -> {
            finish();
        });

        alarmTimeRightIv.setOnClickListener(v -> {
            showDatePickDialog(DateType.TYPE_YMDHMS);
        });

        alarmTypeRightIv.setOnClickListener(v -> {
            if (alarmTypeData == null || alarmTypeData.size() == 0) {
                ToastUtil.s("未获取到预警类型");
                return;
            }
            showTypeSelectDialog("选择预警类型", alarmTypeData);
        });

        alarmAreaRightIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (alarmAreaData == null || alarmAreaData.size() == 0) {
                    ToastUtil.s("未获取到预警区域");
                    return;
                }
                showAlarmAreaSelectDialog("选择预警区域", alarmAreaData);
            }
        });

        alarmLocationRightIv.setOnClickListener(v -> {
            openActivityForResult(ManualLocationActivity.class, MyConstants.SHOW_MANUAL_LOCATION_REQUEST_CODE);
        });

        confirmBtnTv.setOnClickListener(v -> {
            String caseAreaStr = alarmAreaContentEt.getText().toString().trim();
            if (CommonUtils.isEmptyString(caseAreaStr)) {
                ToastUtil.s("预警区域不能为空");
                return;
            }

            String caseDescStr = alarmTimeContentEt.getText().toString().trim();
            if (CommonUtils.isEmptyString(caseDescStr)) {
                ToastUtil.s("预警时间不能为空");
                return;
            }

            final String caseTypeStr = alarmTypeContentEt.getText().toString().trim();
            if (CommonUtils.isEmptyString(caseTypeStr)) {
                ToastUtil.s("预警类型不能为空");
                return;
            }

            final String reportAddressStr = alarmLocationContentEt.getText().toString().trim();
            if (CommonUtils.isEmptyString(reportAddressStr)) {
                ToastUtil.s("预警地址不能为空");
                return;
            }

            if (mRealImages == null || mRealImages.size() <= 0) {
                ToastUtil.s("请选择上传图片");
                return;
            }

            if (mRealVideos == null || mRealVideos.size() <= 0) {
                ToastUtil.s("请选择上传视频");
                return;
            }

            if (!CommonUtils.isOverSize(mRealImages)) {
                getPresenter().uploadFiles(mRealImages, mRealVideos);
            } else {
                ToastUtil.s("上传文件太大");
            }
        });

        tv_site_deal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ArtificialAlarmActivity.this, SiteDealActivity.class));
            }
        });
    }

    private void sendHandler() {
        if (CommonUtils.isEmptyString(uploadPicStr) && CommonUtils.isEmptyString(uploadVideoStr)) {
            ToastUtil.s("图片上传失败,请删除后重新上传");
            return;
        }

        if (addressCoordinate != null) {
            long currentTime = System.currentTimeMillis();
            @SuppressLint("SimpleDateFormat") String timeNow =
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTime);
            addCase(alarmTypeValue, "", "", addressCoordinate.latitude, addressCoordinate.longitude, 0, 0,
                    uploadVideoStr, uploadPicStr, timeNow, alarmArea);
        }
    }

    private void initItemView(TextView labelTv, String labelContent, EditText contentEt, String hint,
                              ImageView rightIv, int rightPicId, TextView importantTip, boolean isShow) {
        labelTv.setText(labelContent);
        contentEt.setHint(hint);
        importantTip.setVisibility(isShow ? View.VISIBLE : View.GONE);
        if (rightIv != null) {
            rightIv.setVisibility(rightPicId != 0 ? View.VISIBLE : View.GONE);
            if (rightPicId != 0) {
                rightIv.setImageResource(rightPicId);
            }
        }
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
        } else if (requestCode == MyConstants.REQUEST_VIDEO_CODE && data != null) {
            //获取选择器返回的数据
            mRealVideos = data.getStringArrayListExtra(
                    ImageSelector.SELECT_RESULT);
            CommonUtils.listAddAllAvoidNPE(mVideoPathList, mRealVideos);
            mCaseVideoAdapter.setList(mVideoPathList);
        } else if (isHaveMapData) {
            final Bundle mBundle = data.getExtras();
            final String addressStr = mBundle.getString(MyConstants.RESULT_ADDRESS, "");
            addressCoordinate = mBundle.getParcelable(MyConstants.RESULT_ADDRESS_COORDINATE);

            Log.e("yuhao", "addressCoordinate: " + addressCoordinate);

            alarmLocationContentEt.setText(addressStr);
        }
    }

    @Override
    protected void onDestroy() {
        hideProgressDialog();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEvent(DealSiteEvent dealSiteEvent) {
        finish();
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

                    alarmTypeData.clear();
                    CommonUtils.listAddAllAvoidNPE(alarmTypeData, bean.getData());

                }, throwable -> {
                    MyLogUtils.e("获取案件来源或案件类型出错", throwable);
                });

        caseService.getAlarmArea().subscribeOn(Schedulers.io())
                .to(RxLife.toMain(this))
                .subscribe(new Consumer<AlarmAreaBean>() {
                    @Override
                    public void accept(AlarmAreaBean alarmAreaBean) throws Throwable {
                        final boolean isEmpty = alarmAreaBean == null || alarmAreaBean.getCode() != 0
                                || alarmAreaBean.getData() == null || alarmAreaBean.getData().size() == 0;
                        if (isEmpty) {
                            return;
                        }
                        alarmAreaData.clear();
                        CommonUtils.listAddAllAvoidNPE(alarmAreaData, alarmAreaBean.getData());
                    }
                });
    }

    public void addCase(int alarmType, String deviceId, String deviceName, double lat,
                        double lon, double presetLat, double presetLon,
                        String video, String rawFrameUrls, String createTime, String area) {

        final Map<String, String> map = new HashMap<>();

        map.put("alarmType", String.valueOf(alarmType));

        if (!CommonUtils.isEmptyString(deviceId)) {
            map.put("deviceId", deviceId);
        }

        if (!CommonUtils.isEmptyString(deviceName)) {
            map.put("deviceName", deviceName);
        }

        if (lat != 0) {
            map.put("lat", String.valueOf(lat));
        }

        if (lon != 0) {
            map.put("lon", String.valueOf(lon));
        }

        if (presetLat != 0) {
            map.put("presetLat", String.valueOf(presetLat));
        }

        if (presetLon != 0) {
            map.put("presetLon", String.valueOf(presetLon));
        }

        map.put("video", video);

        map.put("rawFrameUrls", rawFrameUrls);

        map.put("createTime", createTime);

        map.put("customerId", String.valueOf(GlobalVariable.sPersonInfoBean.getData().getCustomerId()));

        if (!TextUtils.isEmpty(area)) {
            Log.e("yuhao", "area: "+area);
            map.put("area", area);
        }

        CaseService caseService = RetrofitClient.getAPIService(CaseService.class);
        caseService.uploadAlarm(map)
                .subscribeOn(Schedulers.io())
                .to(RxLife.toMain(this))
                .subscribe(bean -> {
                    hideProgressDialog();
                    final boolean isFail = bean == null || bean.code != 0 || bean.data == null;
                    if (isFail) {
                        ToastUtil.s("上传警告失败");
                        Log.e("yuhao", "上传警告失败: " + bean.msg);
                        return;
                    }
                    ToastUtil.s("上传警告成功");
                    finish();
                }, throwable -> {
                    hideProgressDialog();
                    ToastUtil.s("上传警告失败");
                    MyLogUtils.e("新增案件出错", throwable);
                });
    }


    /**
     * 显示时间选择
     *
     * @param type 时间格式
     */
    private void showDatePickDialog(DateType type) {
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

            timeStr = TimeUtils.getTime(selectTime, type.getFormat());
            alarmTimeContentEt.setText(timeStr);
        });
        dialog.show();
    }

    /**
     * 显示预警类型选择
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
            if ("选择预警类型".equals(title)) {
                alarmTypeContentEt.setText(data.get(position).getTypeName());
//              caseTypeCodeStr = data.get(position).getTypeCode();
                alarmTypeValue = data.get(position).getTypeValue();
            }
            dialog.dismiss();
        });
        dialog.show();
    }


    /**
     * 显示预警区域选择
     *
     * @param data
     */
    private void showAlarmAreaSelectDialog(String title, List<AlarmAreaBean.AreaBean> data) {
        if (data == null || data.size() == 0) {
            return;
        }

        List<TypeCodeBean.DataBean> dataBeans = new ArrayList<>();
        for (AlarmAreaBean.AreaBean areaBean : data) {
            TypeCodeBean.DataBean dataBean = new TypeCodeBean.DataBean();
            dataBean.setTypeName(areaBean.getDept_name());
            dataBeans.add(dataBean);
        }
        TypeSelectDialog dialog = new TypeSelectDialog(mContext, title, dataBeans);
        dialog.setListener((position, o) -> {
            if ("选择预警区域".equals(title)) {
                alarmArea = data.get(position).getDept_name();
                alarmAreaContentEt.setText(alarmArea);
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

    }

    @Override
    public void onStatusChange(int type, String urlImg, String urlVideo) {
        if (type == IUploadPicDiaryView.UPLOAD_SUC) {
            uploadPicStr = urlImg;
            uploadVideoStr = urlVideo;
            Log.e("yuhao", "uploadPicStr: " + uploadPicStr);
            Log.e("yuhao", "uploadVideoStr: " + uploadVideoStr);
            sendHandler();
        } else {
            ToastUtil.s("图片上传失败");
        }
    }


}