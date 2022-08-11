package com.gdu.command.ui.resource;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.R;
import com.gdu.command.ui.my.LoginActivity;
import com.gdu.command.ui.video.model.ChildrenBean;
import com.gdu.command.ui.video.model.VideoService;
import com.gdu.command.ui.video.view.VideoDetailActivity;
import com.gdu.model.LightType;
import com.gdu.model.config.StorageConfig;
import com.gdu.utils.LocationUtils;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.rxjava.rxlife.RxLife;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ManMachineProtectionActivity extends BaseActivity<ManMachinePresenter> implements View.OnClickListener,
        IManMachineView {

    private MapView mMapView;
    private ImageView backBtnIv;
    private AMap mAMap;
    private ImageView locationBtnIv;
    private ImageView zoomOutBtnIv;
    private ImageView enlargeBtnIv;
    private ConstraintLayout cl_video_next;
    private TextView tv_curMonitor_name;
    private LatLng lastCenterLatLng;

    private Marker preMarker;


    private List<ChildrenBean> mDeviceInfoList = new ArrayList<>();
    private IManMachineView mView;
    private Gson mGson;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_man_machine_protection;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).statusBarColor(R.color.white).init();

        mGson = new Gson();

        initView();
        initMap();

        initListener();

        getDevicesInfo();
    }

    private void initListener() {
        backBtnIv.setOnClickListener(this);
        locationBtnIv.setOnClickListener(this);
        zoomOutBtnIv.setOnClickListener(this);
        enlargeBtnIv.setOnClickListener(this);
        cl_video_next.setOnClickListener(this);
    }

    private void initView() {
        mMapView = findViewById(R.id.man_machine_view_mapContent);
        backBtnIv = findViewById(R.id.iv_backBtn);

        locationBtnIv = findViewById(R.id.iv_locationBtn);
        zoomOutBtnIv = findViewById(R.id.iv_zoomOutBtn);
        enlargeBtnIv = findViewById(R.id.iv_enlargeBtn);

        cl_video_next = findViewById(R.id.cl_video_next);
        tv_curMonitor_name = findViewById(R.id.tv_curMonitor_name);
    }

    /**
     * Map初始化和相关加载
     */
    private void initMap() {
        mMapView.onCreate(mSavedInstanceState);
        mAMap = mMapView.getMap();
        // 固定一个中心区域坐标
//        LatLng mLatLng = new LatLng(30.542, 114.27881630542);
        lastCenterLatLng = new LatLng(30.312289, 112.182566);
        // 设置地图中心点以及缩放级别
        mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastCenterLatLng, 12));
        // 隐藏缩放按钮
        mAMap.getUiSettings().setZoomControlsEnabled(false);
        // 是否显示定位按钮
        mAMap.getUiSettings().setMyLocationButtonEnabled(false);
        // marker点击事件
        initMarkerListener();
    }

    /**
     * marker点击事件
     */
    private void initMarkerListener() {
        mAMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                ChildrenBean childrenBean = (ChildrenBean) marker.getObject();
                cl_video_next.setVisibility(View.VISIBLE);
                tv_curMonitor_name.setText(childrenBean.getLabel());
                if (preMarker == null) {
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_select_marker));
                } else if (preMarker.getPosition().latitude != marker.getPosition().latitude && preMarker.getPosition().longitude != marker.getPosition().longitude) {
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_select_marker));
                    preMarker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_normal_marker));
                }
                preMarker = marker;
                return true;
            }
        });
    }


    /**
     * 添加高点信息marker
     */
    private void addCaseInfoMarker(List<ChildrenBean> mDeviceInfoList) {
        for (ChildrenBean childrenBean : mDeviceInfoList) {
            if (!TextUtils.isEmpty(childrenBean.getLat()) || TextUtils.isEmpty(childrenBean.getLon())) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.draggable(false);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_normal_marker));
                Marker marker = mAMap.addMarker(markerOptions);

                double lat = Double.parseDouble(childrenBean.getLat());
                double lon = Double.parseDouble(childrenBean.getLon());
                LocateInfo locateInfo = LocationUtils.wgs84ToGcj02(lat, lon);
                marker.setPosition(new LatLng(locateInfo.getLatitude(), locateInfo.getLongitude()));
                marker.setObject(childrenBean);
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_backBtn:
                finish();
                break;

            case R.id.iv_locationBtn:
                moveToCurLocationPoint();
                break;

            case R.id.iv_zoomOutBtn:
                mAMap.animateCamera(CameraUpdateFactory.zoomOut());
                break;

            case R.id.iv_enlargeBtn:
                mAMap.animateCamera(CameraUpdateFactory.zoomIn());
                break;

            case R.id.cl_video_next:
                if (preMarker!=null){
                    ChildrenBean childrenBean = (ChildrenBean) preMarker.getObject();
                    Intent intent = new Intent(getActivity(), VideoDetailActivity.class);
                    intent.putExtra(StorageConfig.DEVICE_INFO,childrenBean);
                    if (childrenBean.isVisible()) {
                        intent.putExtra(StorageConfig.DEVICE_LIGHT_TYPE, LightType.VISIBLE_LIGHT);
                        intent.putExtra(StorageConfig.DEVICE_CHANNEL_TYPE, "1");
                        String videoType = childrenBean.getChildren().get(0).getChannelType() + "";
                    } else {
                        String videoType = childrenBean.getChildren().get(1).getChannelType() + "";
                        intent.putExtra(StorageConfig.DEVICE_LIGHT_TYPE, LightType.INFRARED_LIGHT);
                        intent.putExtra(StorageConfig.DEVICE_CHANNEL_TYPE, "2");
                    }
                    startActivity(intent);
                }
                break;
        }
    }


    /**
     * 移动到定位到点.
     */
    private void moveToCurLocationPoint() {
        // 设置地图中心点以及缩放级别
        mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastCenterLatLng, 12));
        lastCenterLatLng = new LatLng(30.312289, 112.182566);
    }

    @Override
    public void getDeviceList(List<ChildrenBean> mDeviceInfoList) {
        addCaseInfoMarker(mDeviceInfoList);
    }


    private void getDevicesInfo() {
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
                            if (code == 401) {
                                getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                            }
                            return;
                        }
                        if (!rootObj.has("data")) {
                            return;
                        }
                        final JSONArray dataJsonArr = rootObj.getJSONArray("data");
                        for (int i = 0; i < dataJsonArr.length(); i++) {
                            parserDataBean((JSONObject) dataJsonArr.get(i));
                        }
                        Log.e("yuhao", "getDevicesInfo: " + mDeviceInfoList.size());
                        if (!CommonUtils.isEmptyList(mDeviceInfoList)) {
                            runOnUiThread(() -> getDeviceList(mDeviceInfoList));
                        }
                    } catch (Exception e) {
                        MyLogUtils.e("解析设备列表数据出错", e);
                    }
                }, throwable -> {
                    MyLogUtils.e("获取视频设备列表出错", throwable);
                });
    }


    private void parserDataBean(JSONObject obj) {
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
}