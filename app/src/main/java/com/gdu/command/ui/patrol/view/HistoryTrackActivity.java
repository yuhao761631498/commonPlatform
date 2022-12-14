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

        titleTv.setText("????????????");
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
            ToastUtil.s("??????????????????");
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
            endMarker.setTitle("??????: " + distance + "???");
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
    //            // ?????????3D??????
    //            mAMap.showBuildings(false);
            }

            mUiSettings.setZoomControlsEnabled(false);
//        LatLngBounds bounds = getLa
//        //?????????????????????
//        if (!CommonUtils.isHeiye(new Date())) {
//            mAMap.setMapType(AMap.MAP_TYPE_NIGHT);
//        } else {
//            mAMap.setMapType(AMap.MAP_TYPE_NORMAL);
//        }
            // ???????????????????????????
            final MyLocationStyle myLocationStyle = new MyLocationStyle();
            //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            myLocationStyle.interval(5000);
            // ???????????????????????????
            myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_up_arrow));
            // ??????????????????????????????????????????
            myLocationStyle.strokeColor(Color.TRANSPARENT);
            // ??????????????????????????????????????????
            myLocationStyle.strokeWidth(0);
            // ???????????????????????????
            myLocationStyle.radiusFillColor(Color.TRANSPARENT);
            // ???????????????????????????????????????????????????????????????????????????????????????.
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
            myLocationStyle.showMyLocation(false);
            // ??????????????? myLocationStyle ????????????????????????
            mAMap.setMyLocationStyle(myLocationStyle);
            mAMap.setMyLocationEnabled(true);

            final CameraUpdate cameraUpdate =  CameraUpdateFactory.newCameraPosition(new CameraPosition(
                    MyUiUtils.WUHAN, 18, 0, 0));
            mAMap.moveCamera(cameraUpdate);
            mAMap.setOnMyLocationChangeListener(mOnMyLocationChangeListener);
            mAMap.setOnMarkerClickListener(mOnMarkerClickListener);
            mAMap.setOnMapClickListener(mOnMapClickListener);
            mLocationClient = new AMapLocationClient(getActivity());
            // ??????????????????
            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
            // ??????????????????????????????
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            // ????????????????????????????????????????????????????????????
            mLocationOption.setNeedAddress(true);
            //???????????????????????????,?????????false
            mLocationOption.setOnceLocation(true);
            //????????????????????????WIFI????????????????????????
            mLocationOption.setWifiScan(true);
            //??????????????????????????????,?????????false????????????????????????
            mLocationOption.setMockEnable(false);
            //??????????????????,????????????,?????????2000ms
            mLocationOption.setInterval(2000);
            //????????????
            mLocationOption.setLocationCacheEnable(true);
            //??????????????????????????????????????????
            mLocationClient.setLocationOption(mLocationOption);
            //????????????
            mLocationClient.startLocation();
            // ???????????????????????? GPS_ACCURACY_GOOD == 1??? GPS_ACCURACY_BAD = 0???GPS_ACCURACY_UNKNOWN = -1???
            int gpsLevel = mLocationClient.getLastKnownLocation().getGpsAccuracyStatus();

            mAMap.setInfoWindowAdapter(new InfoWinAdapter());
            MyLogUtils.i("initAMap() gpsLevel = " + gpsLevel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ??????????????????????????????.
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
            // ????????????????????????GPS WIFI???????????????????????????????????????SDK??????
            int locationType = bundle.getInt(MyLocationStyle.LOCATION_TYPE);
//            MyLogUtils.i("onMyLocationChange() ??????????????? code: " + errorCode + " errorInfo: " + errorInfo + " locationType: " + locationType );
        } else {
            MyLogUtils.i("onMyLocationChange() ??????????????? bundle is null ");
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
     * ?????????????????????.
     */
    private void moveToCurLocationPoint() {
        MyLogUtils.i("moveToCurLocationPoint()");
        // ???????????????????????????????????????
        curMapScaleLevel = 18;
        mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(llStart, curMapScaleLevel));
    }

    /**
     * ???????????????????????????.
     */
    private void moveToCustomLatLng(LatLng latLng) {
        MyLogUtils.i("moveToCustomLatLng()");
        // ???????????????????????????????????????
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
