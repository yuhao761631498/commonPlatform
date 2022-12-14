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
 * ????????????/??????????????????
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
    /** ????????????????????????. */
    private GeocodeSearch mGeoCoderSearch;
    /** ??????????????????????????????. */
    private LatLng lastCenterLatLng;
    /** ?????????????????????????????????. */
    private RegeocodeAddress mRegeocodeAddress;
    private Marker centerMarker = null;
    private AMapLocationClient mLocationClient;
    private LatLng llStart;
    private boolean isFirstLocation;
    private boolean isFirstLoc;
    /** ????????????????????????. */
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
                ToastUtil.s("?????????????????????");
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
            if (rCode == 1000) {// ????????????
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
            // ?????????????????????
            curMapScaleLevel = 18;
            final CameraUpdate cameraUpdate =  CameraUpdateFactory.newCameraPosition(new CameraPosition(
                    MyUiUtils.WUHAN, curMapScaleLevel, 0, 0));
            mAMap.moveCamera(cameraUpdate);
//        //?????????????????????
//        if (!CommonUtils.isHeiye(new Date())) {
//            mAMap.setMapType(AMap.MAP_TYPE_NIGHT);
//        } else {
//            mAMap.setMapType(AMap.MAP_TYPE_NORMAL);
//        }

            UiSettings uiSettings = mAMap.getUiSettings();
            uiSettings.setZoomControlsEnabled(false);

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
            //????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
            // ??????????????? myLocationStyle ????????????????????????
            mAMap.setMyLocationStyle(myLocationStyle);
            mAMap.setMyLocationEnabled(true);

            mAMap.setOnCameraChangeListener(mOnCameraChangeListener);
            mAMap.setOnMapLoadedListener(() -> addMarkerInScreenCenter(mAMap.getCameraPosition().target));
            mAMap.setOnMyLocationChangeListener(mOnMyLocationChangeListener);
//            mAMap.setOnMapClickListener(this);

            mLocationClient = new AMapLocationClient(this);
            // ??????????????????
            mLocationClient.setLocationListener(mAMapLocationListener);
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
            mLocationOption.setInterval(5000);
            //????????????
            mLocationOption.setLocationCacheEnable(true);
            //??????????????????????????????????????????
            mLocationClient.setLocationOption(mLocationOption);
            //????????????
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
        // ???activity??????onDestroy?????????mMapView.onDestroy()?????????????????????????????????
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
            // ??????????????????
            if(aMapLocation != null) {
                MyLogUtils.i("onMyLocationChange ??????????????? lat: " + aMapLocation.getLatitude() + " lon:" +
                        " " + aMapLocation.getLongitude());
                Bundle bundle = aMapLocation.getExtras();
                if(bundle != null) {
                    int errorCode = bundle.getInt(MyLocationStyle.ERROR_CODE);
                    String errorInfo = bundle.getString(MyLocationStyle.ERROR_INFO);
                    // ????????????????????????GPS WIFI???????????????????????????????????????SDK??????
                    int locationType = bundle.getInt(MyLocationStyle.LOCATION_TYPE);

                    MyLogUtils.d("??????????????? code: " + errorCode + " errorInfo: " + errorInfo + " locationType: " + locationType );
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
                    MyLogUtils.i("??????????????? bundle is null ");
                }
            } else {
                MyLogUtils.i("????????????");
            }
        }
    };

    /**
     * ?????????????????????.
     */
    private void moveToPoint() {
        // ???????????????????????????????????????
        curMapScaleLevel = 18;
        mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastCenterLatLng, curMapScaleLevel));
    }

    /**
     * ???????????????????????????.
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
                    //??????????????????????????????
                    String address = mRegeocodeAddress.getFormatAddress();
                    MyLogUtils.i("???????????????????????????: " + address);
                    address = address.replace(provinceStr, "");
                    address = address.replace(cityStr, "");
                    address = address.replace(districtStr, "");
                    MyLogUtils.i("????????????????????????????????????: " + address);
                    curAddressLayout.setVisibility(View.VISIBLE);
                    curAddressTv.setText(address);
                } else {
                    //????????????????????????
                    MyLogUtils.i("????????????????????????");
                }
            } else {
                //????????????????????????
                MyLogUtils.i("????????????????????????");
            }
        }

        @Override
        public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

        }
    };

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
                    moveToCurLocationPoint();
                }
            }
            String errorInfo = bundle.getString(MyLocationStyle.ERROR_INFO);
            // ????????????????????????GPS WIFI???????????????????????????????????????SDK??????
            int locationType = bundle.getInt(MyLocationStyle.LOCATION_TYPE);
//            LogUtil.i("onMyLocationChange() ??????????????? code: " + errorCode + " errorInfo: " + errorInfo + " locationType: " + locationType );
        } else {
            MyLogUtils.i("onMyLocationChange() ??????????????? bundle is null ");
        }
    };

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
     * ???????????????????????????Marker
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
        //??????Marker????????????,?????????????????????
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

                    centerMarker.setTitle("??????");
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
