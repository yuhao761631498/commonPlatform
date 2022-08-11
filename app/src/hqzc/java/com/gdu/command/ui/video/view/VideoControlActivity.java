package com.gdu.command.ui.video.view;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.baselibrary.base.BaseRVAdapter;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.R;
import com.gdu.command.ui.video.model.DevicesBean;
import com.gdu.command.ui.video.model.VideoService;
import com.gdu.command.views.DeviceSetView;
import com.gdu.model.LightType;
import com.gdu.model.config.StorageConfig;
import com.gdu.model.device.DeviceInfo;
import com.gyf.immersionbar.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoControlActivity extends BaseActivity implements View.OnClickListener {

    private final int DEVICE_LIST_SUCCEED = 0x01;
    private final int DEVICE_LIST_FAIL = 0x02;

    private ImageView mBackImageView;
    private ImageView mSearchImageView;
    private ImageView mSearchEditClearImageView;
    private RecyclerView devListRv;
    private DeviceSetView mDeviceSetView;
    private EditText mSearchEditText;
    private RelativeLayout mSearchEditLayout;
    private SmartRefreshLayout mSmartRefreshLayout;

//    private VideoListAdapter mVideoListAdapter;
    private BaseRVAdapter<DeviceInfo> mVideoListAdapter;
    private List<DeviceInfo> mDeviceInfoList = new ArrayList<>();
    private DeviceInfo mCurrentDeviceInfo;
    private LightType mCurrentLightType;
    private int mCurrentPosition;

    private Handler mHandler;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_control;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true).statusBarColor(
                R.color.color_224CD0).init();

        initView();
        initData();
        initListener();
        initHandler();
//        initTest();
    }

    private void initHandler() {
        mHandler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){

                    case DEVICE_LIST_SUCCEED:
                        closeProgress();
                        mVideoListAdapter.setList(mDeviceInfoList);
                        break;

                    case DEVICE_LIST_FAIL:
                        closeProgress();
                        mDeviceInfoList.clear();
                        mVideoListAdapter.setList(mDeviceInfoList);
                        break;

                    default:
                        break;
                }
            }
        };
    }

    private void closeProgress() {
        if (mSmartRefreshLayout != null && mSmartRefreshLayout.isRefreshing()) {
            mSmartRefreshLayout.finishRefresh();
        }
    }

    private void initData() {
        mCurrentLightType = LightType.VISIBLE_LIGHT;

        VideoService mVideoService = RetrofitClient.getAPIService(VideoService.class);
        mVideoService.getDeviceList().enqueue(new Callback<DevicesBean>() {
            @Override
            public void onResponse(Call<DevicesBean> call, Response<DevicesBean> response) {
                final DevicesBean bean = response.body();
                final boolean isEmptyData =
                        bean == null || bean.getCode() != 0 || bean.getData() == null;
                if (bean != null && bean.getCode() == 401) {
                    ToastUtil.s(bean.getMsg());
                    return;
                }
                if (isEmptyData) {
                    mHandler.sendEmptyMessage(DEVICE_LIST_FAIL);
                    return;
                }
                for (DevicesBean.DataBean deptAndDevice : bean.getData()) {
                    if (deptAndDevice.getDeviceTypeCode().equals("GDJK")) {
                        dealDevice(deptAndDevice);
                    }
                }
            }

            @Override
            public void onFailure(Call<DevicesBean> call, Throwable e) {
                mHandler.sendEmptyMessage(DEVICE_LIST_FAIL);
                MyLogUtils.e("获取社保列表出错", e);
            }
        });
    }

    private void dealDevice(DevicesBean.DataBean deptAndDevice){
        if (deptAndDevice == null) {
            mHandler.sendEmptyMessage(DEVICE_LIST_FAIL);
            return;
        }
        final List<DevicesBean.DataBean.DeviceListBean> deptListInfo =
                deptAndDevice.getDeviceList();
        mDeviceInfoList.clear();
        if (deptListInfo == null) {
            mHandler.sendEmptyMessage(DEVICE_LIST_FAIL);
            return;
        }
        for (DevicesBean.DataBean.DeviceListBean listInfo : deptListInfo) {
            List<DeviceInfo> realDevList = listInfo.getDeviceListX();
            if (realDevList == null) {
                continue;
            }
            final String deptNameStr = listInfo.getDeptName();
            List<DeviceInfo> deviceOnLine = new ArrayList<>();
            List<DeviceInfo> deviceOffline = new ArrayList<>();
            for (DeviceInfo deviceInfo : realDevList) {
                deviceInfo.setDeptName(deptNameStr);
                if (deviceInfo.getOnlineStatus().equals("online")) {
                    deviceOnLine.add(deviceInfo);
                } else {
                    deviceOffline.add(deviceInfo);
                }
            }
            mDeviceInfoList.addAll(deviceOnLine);
            mDeviceInfoList.addAll(deviceOffline);
            mHandler.sendEmptyMessage(DEVICE_LIST_SUCCEED);
        }
    }

//    private void initTest(){
//        mDeviceInfoList = new ArrayList<>();
//        for (int i = 0; i < 30; i++) {
//            DeviceInfo deviceInfo = new DeviceInfo();
//            deviceInfo.setDeviceCode("6C01728PA4A9A78");
//            deviceInfo.setLightType(LightType.VISIBLE_LIGHT);
//            deviceInfo.setVisibleLightUrl("rtmp://58.200.131.2:1935/livetv/cctv1");
//            deviceInfo.setInfraredLightUrl("rtmp://58.200.131.2:1935/livetv/cctv2");
//            deviceInfo.setDeviceName("可见光" + i);
//            deviceInfo.setDeviceLongitude(114.427625);
//            deviceInfo.setDeviceLatitude(30.467067);
//            mDeviceInfoList.add(deviceInfo);
//        }
//    }

    private void initView() {
        mBackImageView = findViewById(R.id.back_imageview);
        mSearchImageView = findViewById(R.id.search_imageview);
        devListRv = findViewById(R.id.rv_devList);
        mDeviceSetView = findViewById(R.id.device_set_view);
        mSearchEditText = findViewById(R.id.search_edit_text);
        mSearchEditLayout = findViewById(R.id.search_edit_layout);
        mSearchEditClearImageView = findViewById(R.id.search_edit_clear_imageview);
        mSmartRefreshLayout = findViewById(R.id.srl_refreshLayout);
        mSmartRefreshLayout.requestLayout();
        initListView();
    }

    private void initListView() {
//        mVideoListAdapter = new VideoListAdapter(mDeviceInfoList);
        mVideoListAdapter = new BaseRVAdapter<DeviceInfo>(R.layout.item_device_info) {
            @Override
            public void onBindVH(BaseViewHolder holder, DeviceInfo data, int position) {
                MyLogUtils.d("onBindVH()");
                TextView mDeviceStatusTextView = holder.getView(R.id.line_status_textview);
                TextView mDeptNameTv = holder.getView(R.id.tv_deptName);
                ImageView mDeviceCoverImageView = holder.getView(R.id.device_cover_imageview);
                DeviceInfo deviceInfo = mDeviceInfoList.get(position);
                if (deviceInfo.getOnlineStatus().equals("online")) {
                    mDeviceStatusTextView.setText("在线");
                    mDeviceStatusTextView.setBackgroundColor(mContext.getResources().getColor(R.color.color_79AA57));
                    mDeviceCoverImageView.setImageResource(R.mipmap.icon_device_online);
                } else {
                    mDeviceStatusTextView.setText("离线");
                    mDeviceStatusTextView.setBackgroundColor(mContext.getResources().getColor(R.color.color_F24343));
                    mDeviceCoverImageView.setImageResource(R.mipmap.icon_device_offline);
                }
                mDeptNameTv.setText(CommonUtils.convertNull2EmptyStr(deviceInfo.getDeptName()));
                if (deviceInfo.getIconId() != 0) {
                    mDeviceCoverImageView.setImageResource(deviceInfo.getIconId());
                }
                holder.setText(R.id.device_position_textview, deviceInfo.getDeviceAddress());
                holder.setText(R.id.name_textview, deviceInfo.getDeviceName());
                holder.getView(R.id.set_imageview).setOnClickListener(v -> {
//                    mCurrentPosition = position;
//                mCurrentDeviceInfo = mDeviceInfoList.get(position);
//                mDeviceSetView.setVisibility(View.VISIBLE);
                });
            }
        };
        mVideoListAdapter.setEmptyView(R.layout.layout_empty_view);
        devListRv.setAdapter(mVideoListAdapter);
    }

    private void initListener() {
        mBackImageView.setOnClickListener(this);
        mSearchImageView.setOnClickListener(this);
        mSearchEditClearImageView.setOnClickListener(this);
        mSmartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            mSearchEditLayout.setVisibility(View.GONE);
            initData();
        });
        mSmartRefreshLayout.setOnLoadMoreListener(refreshLayout -> mSmartRefreshLayout.finishLoadMore());
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchDevice(editable.toString());
            }
        });

        mVideoListAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(mContext, VideoDetailActivity.class);
            intent.putExtra(StorageConfig.DEVICE_INFO, mDeviceInfoList.get(position));
            intent.putExtra(StorageConfig.DEVICE_LIGHT_TYPE, mCurrentLightType);
            startActivity(intent);
        });

        mDeviceSetView.setOnDeviceSetListener(new DeviceSetView.OnDeviceSetListener() {
            @Override
            public void onChangeClick() {
                if (mCurrentLightType == LightType.VISIBLE_LIGHT) {
                    mCurrentLightType = LightType.INFRARED_LIGHT;
                } else {
                    mCurrentLightType = LightType.VISIBLE_LIGHT;
                }
//                mVideoListAdapter.setChangePosition(mCurrentPosition, mCurrentLightType);
            }

            @Override
            public void onRefreshClick() {

            }

            @Override
            public void onSetClick() {
                Intent intent = new Intent(mContext,  DeviceSetActivity.class);
                intent.putExtra(StorageConfig.DEVICE_INFO, mCurrentDeviceInfo);
                startActivity(intent);
                mDeviceSetView.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 查找设备
     */
    private void searchDevice(String deviceName){
        MyLogUtils.d("searchDevice() deviceName = " + deviceName);
        if (CommonUtils.isEmptyString(deviceName)) {
            mVideoListAdapter.setList(mDeviceInfoList);
            return;
        }
        List<DeviceInfo> deviceInfos = new ArrayList<>();
        for (DeviceInfo deviceInfo : mDeviceInfoList) {
            if (deviceInfo.getDeviceName().contains(deviceName)) {
                deviceInfos.add(deviceInfo);
            }
        }
        mVideoListAdapter.setList(deviceInfos);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_imageview:
                finish();
                break;
            case R.id.search_imageview:
                if (mSearchEditLayout.getVisibility() == View.VISIBLE) {
                    mSearchEditLayout.setVisibility(View.GONE);
                } else {
                    mSearchEditLayout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.search_edit_clear_imageview:
                mSearchEditText.setText("");
                mVideoListAdapter.setList(mDeviceInfoList);
                devListRv.setVisibility(View.VISIBLE);
                mSearchEditLayout.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }
}
