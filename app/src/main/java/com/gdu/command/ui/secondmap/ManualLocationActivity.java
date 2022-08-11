package com.gdu.command.ui.secondmap;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
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
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.baselibrary.base.BasePresenter;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.MyConstants;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.R;
import com.gdu.utils.MyUiUtils;
import com.gdu.utils.MyVariables;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 地图选点/搜索定位界面
 * @author wixche
 */
public class ManualLocationActivity extends BaseActivity<BasePresenter> {
    private MapView mMapView;
    private ImageView backBtnIv;
    private EditText inputAddEt;
    private ImageView locationBtnIv;
    private ImageView zoomOutBtnIv;
    private ImageView enlargeBtnIv;
    private RecyclerView searchContentRv;
    private ConstraintLayout curAddressLayout;
    private TextView curAddressTv;
    private TextView confirmTv;

    private AMap mAMap;
    /** 地理编码检索对象. */
    private GeocodeSearch mGeoCoderSearch;
    /** 最后定位的中心点坐标. */
    private LatLng lastCenterLatLng;
    /** 地理反编码获取到的对象. */
    private RegeocodeAddress mRegeocodeAddress;
    private Marker centerMarker = null;
    private AMapLocationClient mLocationClient;
    private LatLng llStart;
    private boolean isFirstLocation;
    private boolean isFirstLoc;
    /** 当前地图缩放等级. */
    private int curMapScaleLevel;

    private List<Tip> mCurrentTipList = new ArrayList<>();
    private InputTipsAdapter mInputTipsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapView.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_manual_location;
    }

    @Override
    protected void initialize() {
        mMapView = findViewById(R.id.view_mapContent);
        backBtnIv = findViewById(R.id.iv_backBtn);
        inputAddEt = findViewById(R.id.et_inputContent);
        locationBtnIv = findViewById(R.id.iv_locationBtn);
        zoomOutBtnIv = findViewById(R.id.iv_zoomOutBtn);
        enlargeBtnIv = findViewById(R.id.iv_enlargeBtn);
        searchContentRv = findViewById(R.id.rv_searchContent);
        curAddressLayout = findViewById(R.id.cl_subLayout2);
        curAddressTv = findViewById(R.id.tv_curMonitor_name);
        confirmTv = findViewById(R.id.tv_confirmBtn);

        initAMap();
        initAdapter();

        initListener();
    }

    private void initListener() {
        backBtnIv.setOnClickListener(v -> {
            finish();
        });

        inputAddEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String searchAddressStr = s.toString();
                if (!CommonUtils.isEmptyString(searchAddressStr)) {
                    InputtipsQuery inputQuery = new InputtipsQuery(searchAddressStr,
                            MyVariables.CUR_LOCATION_CITY);
                    Inputtips inputTips = new Inputtips(ManualLocationActivity.this,
                            inputQuery);
                    inputTips.setInputtipsListener(mInputtipsListener);
                    inputTips.requestInputtipsAsyn();
                } else {
                    if (mInputTipsAdapter != null && mCurrentTipList != null) {
                        mCurrentTipList.clear();
                        mInputTipsAdapter.notifyDataSetChanged();
                        searchContentRv.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        enlargeBtnIv.setOnClickListener(v -> {
            scaleLargeMap(++curMapScaleLevel);
        });
        zoomOutBtnIv.setOnClickListener(v -> {
            scaleLargeMap(--curMapScaleLevel);
        });
        locationBtnIv.setOnClickListener(v -> {
            moveToCurLocationPoint();
        });
        confirmTv.setOnClickListener(v -> {
            final String resultStr = curAddressTv.getText().toString().trim();
            if (CommonUtils.isEmptyString(resultStr)) {
                ToastUtil.s("请选择一处位置");
                return;
            }
            final Intent mIntent = new Intent();
            final Bundle mBundle = new Bundle();
            mBundle.putString(MyConstants.RESULT_ADDRESS, resultStr);
            mBundle.putParcelable(MyConstants.RESULT_ADDRESS_COORDINATE, lastCenterLatLng);
            mIntent.putExtras(mBundle);
            setResult(MyConstants.RESULT_ADDRESS_INTENT, mIntent);
            this.finish();
        });
    }

    private void initAdapter() {
        mInputTipsAdapter = new InputTipsAdapter(mCurrentTipList);
        searchContentRv.setLayoutManager(new LinearLayoutManager(this));
        searchContentRv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        searchContentRv.setAdapter(mInputTipsAdapter);
        mInputTipsAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mCurrentTipList != null) {
                Tip tip = mCurrentTipList.get(position);
                CommonUtils.hideInputSoftKey(this, inputAddEt);
                if (tip.getPoiID() != null && !"".equals(tip.getPoiID())) {
                    final LatLonPoint point = tip.getPoint();
                    final LatLng markerPosition = new LatLng(point.getLatitude(), point.getLongitude());
                    mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, curMapScaleLevel));
//                    MyUiUtils.startJumpAnimation(mAMap, centerMarker);
                    lastCenterLatLng = markerPosition;
                    getCurCenterAddress();
                }
                curAddressLayout.setVisibility(View.VISIBLE);
                curAddressTv.setText(tip.getName());
                inputAddEt.setText("");
//                Intent intent = new Intent();
//                intent.putExtra(MyConstants.EXTRA_TIP, tip);
//                setResult(MyConstants.RESULT_CODE_INPUTTIPS, intent);
//                this.finish();
            }
        });
    }

    private Inputtips.InputtipsListener mInputtipsListener = new Inputtips.InputtipsListener() {
        @Override
        public void onGetInputtips(List<Tip> tipList, int rCode) {
            if (rCode == 1000) {// 正确返回
                mCurrentTipList.clear();
                mCurrentTipList.addAll(tipList);
//                List<String> listString = new ArrayList<String>();
//                for (int i = 0; i < tipList.size(); i++) {
//                    listString.add(tipList.get(i).getName());
//                }
                mInputTipsAdapter.notifyDataSetChanged();
                searchContentRv.setVisibility(View.VISIBLE);
            } else {
                ToastUtil.s(rCode + "");
            }
        }
    };

    private void initAMap() {
        try {
            mGeoCoderSearch = new GeocodeSearch(this);
            mGeoCoderSearch.setOnGeocodeSearchListener(geoCodeListener);

            if (mAMap == null) {
                mAMap = mMapView.getMap();
            }
            // 修改默认中心点
            curMapScaleLevel = 18;
            final CameraUpdate cameraUpdate =  CameraUpdateFactory.newCameraPosition(new CameraPosition(
                    MyUiUtils.WUHAN, curMapScaleLevel, 0, 0));
            mAMap.moveCamera(cameraUpdate);
//        //判断是否为黑夜
//        if (!CommonUtils.isHeiye(new Date())) {
//            mAMap.setMapType(AMap.MAP_TYPE_NIGHT);
//        } else {
//            mAMap.setMapType(AMap.MAP_TYPE_NORMAL);
//        }

            UiSettings uiSettings = mAMap.getUiSettings();
            uiSettings.setZoomControlsEnabled(false);

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
            //连续定位、蓝点不自动移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动（最常用的）
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
            // 将自定义的 myLocationStyle 对象添加到地图上
            mAMap.setMyLocationStyle(myLocationStyle);
            mAMap.setMyLocationEnabled(true);

            mAMap.setOnCameraChangeListener(mOnCameraChangeListener);
            mAMap.setOnMapLoadedListener(() -> addMarkerInScreenCenter(mAMap.getCameraPosition().target));
            mAMap.setOnMyLocationChangeListener(mOnMyLocationChangeListener);
//            mAMap.setOnMapClickListener(this);

            mLocationClient = new AMapLocationClient(this);
            // 设置定位监听
            mLocationClient.setLocationListener(mAMapLocationListener);
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
            mLocationOption.setInterval(5000);
            //缓存机制
            mLocationOption.setLocationCacheEnable(true);
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            //启动定位
            mLocationClient.startLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        hideKeyBoard();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        if (mAMap != null) {
            mAMap.setMyLocationEnabled(false);
        }
        if (mMapView != null) {
            mMapView.onDestroy();
        }
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
            mLocationClient = null;
        }
    }

    private AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            // 定位回调监听
            if(aMapLocation != null) {
                MyLogUtils.i("onMyLocationChange 定位成功， lat: " + aMapLocation.getLatitude() + " lon:" +
                        " " + aMapLocation.getLongitude());
                Bundle bundle = aMapLocation.getExtras();
                if(bundle != null) {
                    int errorCode = bundle.getInt(MyLocationStyle.ERROR_CODE);
                    String errorInfo = bundle.getString(MyLocationStyle.ERROR_INFO);
                    // 定位类型，可能为GPS WIFI等，具体可以参考官网的定位SDK介绍
                    int locationType = bundle.getInt(MyLocationStyle.LOCATION_TYPE);

                    MyLogUtils.d("定位信息， code: " + errorCode + " errorInfo: " + errorInfo + " locationType: " + locationType );
                    if (errorCode != AMapLocation.LOCATION_SUCCESS) {
                        return;
                    }

                    if (isFirstLoc) {
                        lastCenterLatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
//                        if (centerMarker != null) {
//                            MyUiUtils.startJumpAnimation(mAMap, centerMarker);
//                        }
                        moveToPoint();
                        getCurCenterAddress();
                        isFirstLoc = false;
                        mLocationClient.stopLocation();
                    }
                } else {
                    MyLogUtils.i("定位信息， bundle is null ");
                }
            } else {
                MyLogUtils.i("定位失败");
            }
        }
    };

    /**
     * 移动到定位到点.
     */
    private void moveToPoint() {
        // 设置地图中心点以及缩放级别
        curMapScaleLevel = 18;
        mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastCenterLatLng, curMapScaleLevel));
    }

    /**
     * 地理编码检索监听者.
     */
    private GeocodeSearch.OnGeocodeSearchListener geoCodeListener = new GeocodeSearch.OnGeocodeSearchListener() {
        @Override
        public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
            if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                if (result != null && result.getRegeocodeAddress() != null
                        && result.getRegeocodeAddress().getFormatAddress() != null) {
                    mRegeocodeAddress = result.getRegeocodeAddress();
                    final String provinceStr = mRegeocodeAddress.getProvince();
//                    provinceContentTV.setText(provinceStr);
                    final String cityStr = mRegeocodeAddress.getCity();
//                    cityContentTV.setText(cityStr);
                    final String districtStr = mRegeocodeAddress.getDistrict();
//                    districtContentTV.setText(districtStr);
                    //获取反向地理编码结果
                    String address = mRegeocodeAddress.getFormatAddress();
                    MyLogUtils.i("反编码得到的地址为: " + address);
                    address = address.replace(provinceStr, "");
                    address = address.replace(cityStr, "");
                    address = address.replace(districtStr, "");
                    MyLogUtils.i("反编码得到的地址简化后为: " + address);
                    curAddressLayout.setVisibility(View.VISIBLE);
                    curAddressTv.setText(address);
                } else {
                    //没有找到检索结果
                    MyLogUtils.i("没有找到检索结果");
                }
            } else {
                //没有找到检索结果
                MyLogUtils.i("没有找到检索结果");
            }
        }

        @Override
        public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

        }
    };

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
                    moveToCurLocationPoint();
                }
            }
            String errorInfo = bundle.getString(MyLocationStyle.ERROR_INFO);
            // 定位类型，可能为GPS WIFI等，具体可以参考官网的定位SDK介绍
            int locationType = bundle.getInt(MyLocationStyle.LOCATION_TYPE);
//            LogUtil.i("onMyLocationChange() 定位信息， code: " + errorCode + " errorInfo: " + errorInfo + " locationType: " + locationType );
        } else {
            MyLogUtils.i("onMyLocationChange() 定位信息， bundle is null ");
        }
    };

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
     * 在屏幕中心添加一个Marker
     */
    private void addMarkerInScreenCenter(LatLng targetLatLng) {
        Point screenPosition = mAMap.getProjection().toScreenLocation(targetLatLng);
        ImageView markerIconIv = new ImageView(this);
        ViewGroup.LayoutParams layoutParams =
                new ViewGroup.LayoutParams(MyUiUtils.dip2px(35), ViewGroup.LayoutParams.WRAP_CONTENT);
        markerIconIv.setLayoutParams(layoutParams);
        markerIconIv.setImageResource(R.mipmap.ic_mini_map_center_location);
        centerMarker = mAMap.addMarker(new MarkerOptions()
                .anchor(0.5f,1.0f)
                .icon(BitmapDescriptorFactory.fromView(markerIconIv)));
        //设置Marker在屏幕上,不跟随地图移动
        centerMarker.setPositionByPixels(screenPosition.x,screenPosition.y);
    }

    private AMap.OnCameraChangeListener mOnCameraChangeListener =
            new AMap.OnCameraChangeListener() {

                @Override
                public void onCameraChange(CameraPosition cameraPosition) {

                }

                @Override
                public void onCameraChangeFinish(CameraPosition cameraPosition) {
                    if (centerMarker.getPosition().latitude == cameraPosition.target.latitude
                            && centerMarker.getPosition().longitude == cameraPosition.target.longitude) {
                        return;
                    }
//                    if (centerMarker != null) {
//                        MyUiUtils.startJumpAnimation(mAMap, centerMarker);
//                    }
                    lastCenterLatLng = cameraPosition.target;

                    centerMarker.setTitle("坐标");
                    centerMarker.setSnippet(lastCenterLatLng.latitude+","+lastCenterLatLng.longitude);
                    centerMarker.showInfoWindow();

                    getCurCenterAddress();
                }
            };

    private void getCurCenterAddress() {
        LatLonPoint mLatLonPoint = new LatLonPoint(lastCenterLatLng.latitude,
                lastCenterLatLng.longitude);
        RegeocodeQuery mRegeocodeQuery = new RegeocodeQuery(mLatLonPoint, 100, "");
        mGeoCoderSearch.getFromLocationAsyn(mRegeocodeQuery);
    }

    public void scaleLargeMap(float scaleValue) {
        mAMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastCenterLatLng, scaleValue));
    }


}
