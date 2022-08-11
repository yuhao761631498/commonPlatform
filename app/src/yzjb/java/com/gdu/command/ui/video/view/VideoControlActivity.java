package com.gdu.command.ui.video.view;

import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.baselibrary.base.BaseRVAdapter;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.imageloade.MyImageLoadUtils;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.R;
import com.gdu.command.ui.video.model.ChildrenBean;
import com.gdu.command.ui.video.model.VideoService;
import com.gdu.command.views.DeviceSetView;
import com.gdu.model.LightType;
import com.gdu.model.config.StorageConfig;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.rxjava.rxlife.RxLife;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.schedulers.Schedulers;

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
    private BaseRVAdapter<ChildrenBean> mVideoListAdapter;
    private List<ChildrenBean> mDeviceInfoList = new ArrayList<>();
//    private DeviceInfo mCurrentDeviceInfo;
    private LightType mCurrentLightType;
//    private int mCurrentPosition;

    private Handler mHandler;

    private Gson mGson;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_control;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true).statusBarColor(
                R.color.color_224CD0).init();
        mGson = new Gson();
        initView();
        initData();
        initListener();
        initHandler();
//        initTest();
    }

    private void initHandler() {
        mHandler = new Handler(msg -> {
            switch (msg.what){

                case DEVICE_LIST_SUCCEED:
                    closeProgress();
                    final List<ChildrenBean> onlineList = new ArrayList<>();
//                    final List<ChildrenBean> offlineList = new ArrayList<>();
                    final List<ChildrenBean> tempTotalList = new ArrayList<>();
                    if (!CommonUtils.isEmptyList(mDeviceInfoList)) {
                        for (final ChildrenBean mDeviceInfo : mDeviceInfoList) {
                            if (!CommonUtils.isEmptyString(mDeviceInfo.getOnlineStatus())
                                    && mDeviceInfo.getOnlineStatus().equals("1")) {
                                CommonUtils.listAddAvoidNull(onlineList, mDeviceInfo);
                            }
//                        else {
//                            CommonUtils.listAddAvoidNull(offlineList, mDeviceInfo);
//                        }
                        }
                    }
                    CommonUtils.listAddAllAvoidNPE(tempTotalList, onlineList);
//                    CommonUtils.listAddAllAvoidNPE(tempTotalList, offlineList);
                    mDeviceInfoList.clear();
                    CommonUtils.listAddAllAvoidNPE(mDeviceInfoList, tempTotalList);
                    mVideoListAdapter.setList(mDeviceInfoList);
                    tempTotalList.clear();
                    break;

                case DEVICE_LIST_FAIL:
                    closeProgress();
                    mDeviceInfoList.clear();
                    mVideoListAdapter.setList(mDeviceInfoList);
                    break;

                default:
                    break;
            }

            return true;
        });
    }

    private void closeProgress() {
        if (mSmartRefreshLayout != null && mSmartRefreshLayout.isRefreshing()) {
            mSmartRefreshLayout.finishRefresh();
        }
    }

    private void initData() {
        mCurrentLightType = LightType.VISIBLE_LIGHT;

        final HashMap<String, String> params = new HashMap<>();
        params.put("showCount", "true");
        params.put("showChannel", "true");
        params.put("onlyQueryFolderAndDevice", "true");
        VideoService mVideoService = RetrofitClient.getAPIService(VideoService.class);
        mVideoService.getDeviceList(params)
                .subscribeOn(Schedulers.io())
                .to(RxLife.to(this))
                .subscribe(data -> {
                    try {
                        final String resultStr = data.string();
                        final JSONObject rootObj = new JSONObject(resultStr);
                        if (!rootObj.has("code")) {
                            return;
                        }
                        final int code = rootObj.getInt("code");
                        if (code != 0 && code != 200) {
                            return;
                        }
                        if (!rootObj.has("data")) {
                            return;
                        }
                        final JSONArray dataJsonArr = rootObj.getJSONArray("data");
                        for (int i = 0; i < dataJsonArr.length(); i++) {
                            parserDataBean((JSONObject) dataJsonArr.get(i));
                        }
                        if (CommonUtils.isEmptyList(mDeviceInfoList)) {
                            mHandler.sendEmptyMessage(DEVICE_LIST_FAIL);
                        } else {
                            mHandler.sendEmptyMessage(DEVICE_LIST_SUCCEED);
                        }
                    } catch (Exception e) {
                        mHandler.sendEmptyMessage(DEVICE_LIST_FAIL);
                        MyLogUtils.e("解析设备列表数据出错", e);
                    }
                }, throwable -> {
                   MyLogUtils.e("获取视频设备列表出错", throwable);
                });
    }

    private void parserDataBean(JSONObject obj) {
//        MyLogUtils.i("parserDataBean() objStr = " + obj.toString());
        try {
            if (obj.has("count")) {
                if (!obj.has("isFolder")) {
                    return;
                }
                if (!obj.has("children")) {
                    return;
                }
                final JSONArray childArr = obj.getJSONArray("children");
                for (int i = 0; i < childArr.length(); i++) {
                    parserDataBean((JSONObject) childArr.get(i));
                }
            } else {
                if (!obj.has("isFolder")) {
                    return;
                }
                if (!obj.has("children")) {
                    return;
                }
//                MyLogUtils.i("parserDataBean() childObjStr = " + obj);
                final ChildrenBean mChildrenBean = mGson.fromJson(obj.toString(), ChildrenBean.class);
                CommonUtils.listAddAvoidNull(mDeviceInfoList, mChildrenBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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
        mVideoListAdapter = new BaseRVAdapter<ChildrenBean>(R.layout.item_device_info) {
            @Override
            public void onBindVH(BaseViewHolder holder, ChildrenBean data, int position) {
                final TextView mDeviceStatusTextView = holder.getView(R.id.line_status_textview);
                final TextView mDeptNameTv = holder.getView(R.id.tv_deptName);
                final ImageView mDeviceCoverImageView = holder.getView(R.id.device_cover_imageview);
                final TextView channel1NameTv = holder.getView(R.id.tv_channel1Name);
                final TextView channel2NameTv = holder.getView(R.id.tv_channel2Name);
                final String labelStr = data.getLabel();
                if (data.getOnlineStatus().equals("1")) {
                    mDeviceStatusTextView.setText("在线");
                    mDeviceStatusTextView.setBackgroundColor(mContext.getResources().getColor(R.color.color_79AA57));
                    if ("公安陈家台".equals(labelStr)) {
                        MyImageLoadUtils.loadImage(VideoControlActivity.this, R.drawable.ic_video_chenjiatai,
                                R.mipmap.icon_device_online, mDeviceCoverImageView);
                    } else if ("新垸村".equals(labelStr)) {
                        MyImageLoadUtils.loadImage(VideoControlActivity.this, R.drawable.ic_video_xinhuangcun,
                                R.mipmap.icon_device_online, mDeviceCoverImageView);
                    } else if ("弥市镇陈家湾村委会".equals(labelStr)) {
                        MyImageLoadUtils.loadImage(VideoControlActivity.this, R.drawable.ic_video_chenjiawan,
                                R.mipmap.icon_device_online, mDeviceCoverImageView);
                    } else if ("公安沿江".equals(labelStr)) {
                        MyImageLoadUtils.loadImage(VideoControlActivity.this, R.drawable.ic_video_gonganyanjiang,
                                R.mipmap.icon_device_online, mDeviceCoverImageView);
                    } else if ("江陵文村".equals(labelStr)) {
                        MyImageLoadUtils.loadImage(VideoControlActivity.this, R.drawable.ic_video_jianglingwencun,
                                R.mipmap.icon_device_online, mDeviceCoverImageView);
                    } else if ("公安埠河荆南六组".equals(labelStr)) {
                        MyImageLoadUtils.loadImage(VideoControlActivity.this, R.drawable.ic_video_jingnanliuzu,
                                R.mipmap.icon_device_online, mDeviceCoverImageView);
                    } else if ("龙州".equals(labelStr)) {
                        MyImageLoadUtils.loadImage(VideoControlActivity.this, R.drawable.ic_video_longzhou,
                                R.mipmap.icon_device_online, mDeviceCoverImageView);
                    } else if ("荆州太平口".equals(labelStr)) {
                        MyImageLoadUtils.loadImage(VideoControlActivity.this, R.drawable.ic_video_jingzhoutaipingkou,
                                R.mipmap.icon_device_online, mDeviceCoverImageView);
                    } else if ("荆州金城超市".equals(labelStr)) {
                        MyImageLoadUtils.loadImage(VideoControlActivity.this, R.drawable.ic_video_jinglingchaoshi,
                                R.mipmap.icon_device_online, mDeviceCoverImageView);
                    } else if ("公安北闸风景区".equals(labelStr)) {
                        MyImageLoadUtils.loadImage(VideoControlActivity.this, R.drawable.ic_video_gonganbeizha,
                                R.mipmap.icon_device_online, mDeviceCoverImageView);
                    } else {
                        mDeviceCoverImageView.setImageResource(R.mipmap.icon_device_online);
                    }
                } else {
                    mDeviceStatusTextView.setText("离线");
                    mDeviceStatusTextView.setBackgroundColor(mContext.getResources().getColor(R.color.color_F24343));
                    mDeviceCoverImageView.setImageResource(R.mipmap.icon_device_offline);
                }
                mDeptNameTv.setText(CommonUtils.convertNull2EmptyStr(data.getDeptName()));
                if (data.getIconId() != 0) {
                    mDeviceCoverImageView.setImageResource(data.getIconId());
                }
//                holder.setText(R.id.device_position_textview, data.getDeviceAddress());
                holder.setText(R.id.name_textview, labelStr);
                final List<ChildrenBean> streamList =
                        data.getChildren();
                if (CommonUtils.isEmptyList(data.getChildren())) {
                    channel1NameTv.setVisibility(View.GONE);
                    channel2NameTv.setVisibility(View.GONE);
                } else if (streamList.size() == 1) {
                    channel1NameTv.setVisibility(View.VISIBLE);
                    channel2NameTv.setVisibility(View.INVISIBLE);
                    channel1NameTv.setText(streamList.get(0).getChannelType() == 1 ? "可见光" : "热成像");
                } else if (streamList.size() > 1) {
                    channel1NameTv.setVisibility(View.VISIBLE);
                    channel2NameTv.setVisibility(View.VISIBLE);
                    channel1NameTv.setText(streamList.get(0).getChannelType() == 1 ? "可见光" : "热成像");
                    channel2NameTv.setText(streamList.get(1).getChannelType() == 1 ? "可见光" : "热成像");
                }
            }
        };
        mVideoListAdapter.setEmptyView(R.layout.layout_empty_view);
        mVideoListAdapter.addChildClickViewIds(R.id.tv_channel1Name, R.id.tv_channel2Name);
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

        mVideoListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            Intent intent = new Intent(mContext, VideoDetailActivity.class);
            intent.putExtra(StorageConfig.DEVICE_INFO, mDeviceInfoList.get(position));
            switch (view.getId()) {
                case R.id.tv_channel1Name:
                    mCurrentLightType = mDeviceInfoList.get(position).getChildren().get(0).getChannelType() == 1 ?
                            LightType.VISIBLE_LIGHT :
                            LightType.INFRARED_LIGHT;
                    intent.putExtra(StorageConfig.DEVICE_LIGHT_TYPE, mCurrentLightType);
                    intent.putExtra(StorageConfig.DEVICE_CHANNEL_TYPE,
                            mDeviceInfoList.get(position).getChildren().get(0).getChannelType() + "");
                    break;

                case R.id.tv_channel2Name:
                    mCurrentLightType = mDeviceInfoList.get(position).getChildren().get(1).getChannelType() == 1 ?
                            LightType.VISIBLE_LIGHT :
                            LightType.INFRARED_LIGHT;
                    intent.putExtra(StorageConfig.DEVICE_LIGHT_TYPE, mCurrentLightType);
                    intent.putExtra(StorageConfig.DEVICE_CHANNEL_TYPE,
                            mDeviceInfoList.get(position).getChildren().get(1).getChannelType() + "");
                    break;

                default:
                    break;
            }
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
//                Intent intent = new Intent(mContext,  DeviceSetActivity.class);
//                intent.putExtra(StorageConfig.DEVICE_INFO, mCurrentDeviceInfo);
//                startActivity(intent);
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
        List<ChildrenBean> deviceInfos = new ArrayList<>();
        for (ChildrenBean deviceInfo : mDeviceInfoList) {
            if (deviceInfo.getLabel().contains(deviceName)) {
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
