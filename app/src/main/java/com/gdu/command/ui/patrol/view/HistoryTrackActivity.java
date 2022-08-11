package com.gdu.command.ui.patrol.view;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.PolylineOptions;
import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.baselibrary.base.BasePresenter;
import com.gdu.baselibrary.utils.MyConstants;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.R;
import com.gdu.command.ui.patrol.adapter.InfoWinAdapter;
import com.gdu.command.ui.patrol.bean.QueryDataBean;
import com.gdu.utils.AMapUtil;
import com.gdu.utils.MyUiUtils;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wixche
 */
public class HistoryTrackActivity extends BaseActivity<BasePresenter> {
    private AMap mAMap;
    private UiSettings mUiSettings;
    private AMapLocationClient mLocationClient;

    private MapView mMapView;

    private ImageView backBtnIv;
    private TextView titleTv;

    private ImageView enlargeBtnIv;
    private ImageView zoomOutBtnIv;
    private ImageView manualLocationBtn;

    private LatLng llStart;
    private boolean isFirstLocation;
    private int curMapScaleLevel = 18;

    private QueryDataBean.DataBean historyData;
    private int clickRecordIndex = -1;

    private Marker oldMarker;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        isShowWaterMark = false;
        final Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            historyData = (QueryDataBean.DataBean) mBundle.getSerializable(MyConstants.DEFAULT_PARAM_KEY_1);
            clickRecordIndex = mBundle.getInt(MyConstants.DEFAULT_PARAM_KEY_2, -1);
        }
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
    protected int getLayoutId() {
        return R.layout.activity_history_track;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true).statusBarColor(
                R.color.color_224CD0).init();
        initView();
        initListener();
        initData();
    }

    private void initView() {
        mMapView = findViewById(R.id.view_mapContent);
        backBtnIv = findViewById(R.id.iv_left_back);
        titleTv = findViewById(R.id.tv_left_title_name);

        enlargeBtnIv = findViewById(R.id.iv_enlargeBtn);
        zoomOutBtnIv = findViewById(R.id.iv_zoomOutBtn);
        manualLocationBtn = findViewById(R.id.iv_locationBtn);

        initAMap();

        titleTv.setText("巡逻轨迹");
    }

    private void initListener() {
        backBtnIv.setOnClickListener(v -> {
            finish();
        });

        enlargeBtnIv.setOnClickListener(v -> {
            scaleLargeMap(++curMapScaleLevel);
        });
        zoomOutBtnIv.setOnClickListener(v -> {
            scaleLargeMap(--curMapScaleLevel);
        });
        manualLocationBtn.setOnClickListener(v -> {
            moveToCurLocationPoint();
        });
    }

    private void initData() {
        if (historyData == null) {
            ToastUtil.s("未获取到数据");
            return;
        }
        final List<LatLng> trackPoints = new ArrayList<>();
        Marker endMarker = null;
        for (int i = 0; i < historyData.getPatrolLocateList().size(); i++) {
            final QueryDataBean.DataBean.PatrolLocateListBean bean =
                    historyData.getPatrolLocateList().get(i);
            final MarkerOptions mOptions = new MarkerOptions();
            final LatLng mLatLng = new LatLng(bean.getLat(), bean.getLon());
            mOptions.position(mLatLng);
            if (i == 0) {
                mOptions.anchor(0.5f, 1f);
                mOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_resource_circiut));
                moveToCustomLatLng(mLatLng);
                mAMap.addMarker(mOptions);
            } else if (i == historyData.getPatrolLocateList().size() - 1) {
                mOptions.anchor(0.5f, 0.5f);
                mOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_patrol_distination));
                endMarker = mAMap.addMarker(mOptions);
            }
            trackPoints.add(mLatLng);
        }
        if (endMarker != null) {
            final float distance = AMapUtil.getPatrolDistance(trackPoints);
            endMarker.setTitle("全长: " + distance + "米");
            endMarker.setSnippet("");
            endMarker.showInfoWindow();
        }

        mAMap.addPolyline(new PolylineOptions().addAll(trackPoints).width(5)
                .color(MyUiUtils.getColor(R.color.yellow_F7B500)));

        for (int j = 0; j < historyData.getPatrolRecordList().size(); j++) {
            final QueryDataBean.DataBean.PatrolRecordListBean bean =
                    historyData.getPatrolRecordList().get(j);
            final MarkerOptions mOptions = new MarkerOptions();
            final LatLng mLatLng = new LatLng(bean.getLat(), bean.getLon());
            mOptions.position(mLatLng);
            mOptions.title(bean.getTypeName());
            mOptions.snippet(bean.getContent());
            if (clickRecordIndex == j) {
                mOptions.anchor(0.5f, 0.5f);
                mOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_patrol_record_selected_new));
            } else {
                mOptions.anchor(0.5f, 0.5f);
                mOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_patrol_record_normal_new));
            }
            if (clickRecordIndex == j) {
                mAMap.addMarker(mOptions).showInfoWindow();
            } else {
                mAMap.addMarker(mOptions);
            }
        }
    }

    private void initAMap() {
        MyLogUtils.i("initAMap()");
        try {
            if (mAMap == null) {
                mAMap = mMapView.getMap();
                mUiSettings = mAMap.getUiSettings();
    //            // 不显示3D楼栋
    //            mAMap.showBuildings(false);
            }

            mUiSettings.setZoomControlsEnabled(false);
//        LatLngBounds bounds = getLa
//        //判断是否为黑夜
//        if (!CommonUtils.isHeiye(new Date())) {
//            mAMap.setMapType(AMap.MAP_TYPE_NIGHT);
//        } else {
//            mAMap.setMapType(AMap.MAP_TYPE_NORMAL);
//        }
            // 自定义系统定位蓝点
            final MyLocationStyle myLocationStyle = new MyLocationStyle();
            //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
            myLocationStyle.interval(5000);
            // 自定义定位蓝点图标
            myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_up_arrow));
            // 自定义精度范围的圆形边框颜色
            myLocationStyle.strokeColor(Color.TRANSPARENT);
            // 自定义精度范围的圆形边框宽度
            myLocationStyle.strokeWidth(0);
            // 设置圆形的填充颜色
            myLocationStyle.radiusFillColor(Color.TRANSPARENT);
            // 连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动.
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
            myLocationStyle.showMyLocation(false);
            // 将自定义的 myLocationStyle 对象添加到地图上
            mAMap.setMyLocationStyle(myLocationStyle);
            mAMap.setMyLocationEnabled(true);

            final CameraUpdate cameraUpdate =  CameraUpdateFactory.newCameraPosition(new CameraPosition(
                    MyUiUtils.WUHAN, 18, 0, 0));
            mAMap.moveCamera(cameraUpdate);
            mAMap.setOnMyLocationChangeListener(mOnMyLocationChangeListener);
            mAMap.setOnMarkerClickListener(mOnMarkerClickListener);
            mAMap.setOnMapClickListener(mOnMapClickListener);
            mLocationClient = new AMapLocationClient(getActivity());
            // 设置定位监听
            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
            // 设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            // 设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            //设置是否只定位一次,默认为false
            mLocationOption.setOnceLocation(true);
            //设置是否强制刷新WIFI，默认为强制刷新
            mLocationOption.setWifiScan(true);
            //设置是否允许模拟位置,默认为false，不允许模拟位置
            mLocationOption.setMockEnable(false);
            //设置定位间隔,单位毫秒,默认为2000ms
            mLocationOption.setInterval(2000);
            //缓存机制
            mLocationOption.setLocationCacheEnable(true);
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            //启动定位
            mLocationClient.startLocation();
            // 获取卫星型号强度 GPS_ACCURACY_GOOD == 1； GPS_ACCURACY_BAD = 0；GPS_ACCURACY_UNKNOWN = -1；
            int gpsLevel = mLocationClient.getLastKnownLocation().getGpsAccuracyStatus();

            mAMap.setInfoWindowAdapter(new InfoWinAdapter());
            MyLogUtils.i("initAMap() gpsLevel = " + gpsLevel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 地图定位结果返回监听.
     */
    private AMap.OnMyLocationChangeListener mOnMyLocationChangeListener = location -> {
        Bundle bundle = location.getExtras();
        if(bundle != null) {
            int errorCode = bundle.getInt(MyLocationStyle.ERROR_CODE);
            if (errorCode == AMapLocation.LOCATION_SUCCESS) {
                llStart = new LatLng(location.getLatitude(), location.getLongitude());
                if (isFirstLocation) {
                    isFirstLocation = false;
//                    moveToCurLocationPoint();
                }
            }
            String errorInfo = bundle.getString(MyLocationStyle.ERROR_INFO);
            // 定位类型，可能为GPS WIFI等，具体可以参考官网的定位SDK介绍
            int locationType = bundle.getInt(MyLocationStyle.LOCATION_TYPE);
//            MyLogUtils.i("onMyLocationChange() 定位信息， code: " + errorCode + " errorInfo: " + errorInfo + " locationType: " + locationType );
        } else {
            MyLogUtils.i("onMyLocationChange() 定位信息， bundle is null ");
        }
    };

    private AMap.OnMarkerClickListener mOnMarkerClickListener = marker -> {
        hideOldMarker();
        oldMarker = marker;
        marker.showInfoWindow();
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_patrol_record_selected_new));
        return false;
    };

    private void hideOldMarker() {
        if (oldMarker != null) {
            oldMarker.hideInfoWindow();
            oldMarker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_patrol_record_normal_new));
        }
    }

    private AMap.OnMapClickListener mOnMapClickListener = latLng -> {
        hideOldMarker();
    };

    public void scaleLargeMap(float scaleValue) {
        final CameraPosition cameraPosition = mAMap.getCameraPosition();
        mAMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cameraPosition.target, scaleValue));
    }

    /**
     * 移动到定位到点.
     */
    private void moveToCurLocationPoint() {
        MyLogUtils.i("moveToCurLocationPoint()");
        // 设置地图中心点以及缩放级别
        curMapScaleLevel = 18;
        mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(llStart, curMapScaleLevel));
    }

    /**
     * 移动到定位指定位置.
     */
    private void moveToCustomLatLng(LatLng latLng) {
        MyLogUtils.i("moveToCustomLatLng()");
        // 设置地图中心点以及缩放级别
        curMapScaleLevel = 18;
        mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, curMapScaleLevel));
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
        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }
}
