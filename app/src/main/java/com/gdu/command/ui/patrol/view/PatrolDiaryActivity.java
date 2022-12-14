package com.gdu.command.ui.patrol.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;

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
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.PolylineOptions;
import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.FileUtil;
import com.gdu.baselibrary.utils.MMKVUtil;
import com.gdu.baselibrary.utils.MyConstants;
import com.gdu.baselibrary.utils.SPStringUtils;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.baselibrary.utils.eventbus.EventMessage;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.BuildConfig;
import com.gdu.command.R;
import com.gdu.command.ui.cases.CaseService;
import com.gdu.command.ui.cases.TypeCodeBean;
import com.gdu.command.ui.cases.TypeSelectDialog;
import com.gdu.command.ui.patrol.bean.AddPatrolBean;
import com.gdu.command.ui.patrol.presenter.IPatrolDiaryView;
import com.gdu.command.ui.patrol.presenter.PatrolPresenter;
import com.gdu.command.ui.patrol.presenter.PatrolService;
import com.gdu.command.uploadpic.IUploadPicDiaryView;
import com.gdu.utils.AMapUtil;
import com.gdu.utils.MyUiUtils;
import com.gdu.utils.MyVariables;
import com.gyf.immersionbar.ImmersionBar;
import com.rxjava.rxlife.RxLife;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * ??????????????????
 */
public class PatrolDiaryActivity extends BaseActivity<PatrolPresenter> implements IPatrolDiaryView {

    private AMap mAMap;
    private UiSettings mUiSettings;
    private AMapLocationClient mLocationClient;

    private MapView mMapView;

    private ImageView backBtnIv;
    private ImageView goHistoryBtnIv;
    private TextView startPatrolBtn;
    private TextView addPatrolBtn;
    private TextView endPatrolBtn;
    private Group patrolGroup;

    private ImageView enlargeBtnIv;
    private ImageView zoomOutBtnIv;
    private ImageView manualLocationBtn;

    private LatLng llStart;
    private boolean isFirstLocation;
    private int curMapScaleLevel = 18;
    private String selectPatrolTypeStr = "";

    private List<TypeCodeBean.DataBean> patrolTypeData = new ArrayList<>();

    private Disposable reportDisposable;

    private int patrolId;

    /** ????????????????????? */
    private List<LatLng> linePoint = new ArrayList<>();
    /** ?????????????????? */
    private boolean isPatroling = false;
    /** ????????????????????????????????? */
    private String uploadPicStr = "";

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        super.onBaseCreate(savedInstanceState);
        isShowWaterMark = false;
    }

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapView.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_patrol_diary;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true)
                .statusBarColor(R.color.white).init();
        getPresenter().setIView(this);
        initView();
        initListener();
        initAMap();

        getPatrolType();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    private void initView() {
        mMapView = findViewById(R.id.view_mapContent);
        backBtnIv = findViewById(R.id.iv_backBtn);
        goHistoryBtnIv = findViewById(R.id.iv_goHistoryBtn);
        startPatrolBtn = findViewById(R.id.tv_startPatrolBtn);
        addPatrolBtn = findViewById(R.id.tv_addPatrolBtn);
        endPatrolBtn = findViewById(R.id.tv_endPatrolBtn);
        patrolGroup = findViewById(R.id.view_onPatrolGroup);
        enlargeBtnIv = findViewById(R.id.iv_enlargeBtn);
        zoomOutBtnIv = findViewById(R.id.iv_zoomOutBtn);
        manualLocationBtn = findViewById(R.id.iv_locationBtn);
    }

    private void initListener() {
        backBtnIv.setOnClickListener(v -> {
            if (isPatroling) {
                ToastUtil.s("??????????????????,??????????????????????????????");
                return;
            }
            finish();
        });
        goHistoryBtnIv.setOnClickListener(v -> {
            finish();
            EventBus.getDefault().post(new EventMessage(MyConstants.JUMP_TO_PERSON_FRAGMENT));
        });
        startPatrolBtn.setOnClickListener(v -> {
            showTypeSelectDialog("??????????????????", patrolTypeData);
        });

        getPresenter().setUploadPicDiaryView(new IUploadPicDiaryView() {
            @Override
            public void showOrHidePb(boolean isShow, String tip) {
                if (isShow) {
                    showProgressDialog(tip);
                } else {
                    hideProgressDialog();
                }
            }

            @Override
            public void onStatusChange(int type, String content) {
                hideProgressDialog();
                if (type == IUploadPicDiaryView.UPLOAD_SUC) {
                    uploadPicStr = content;
                    sendEndCmdHandle();
                } else {
                    ToastUtil.s("??????????????????");
                }
            }

            @Override
            public void onStatusChange(int type, String urlImg, String urlVideo) {

            }
        });

        addPatrolBtn.setOnClickListener(v -> {
            final Bundle mBundle = new Bundle();
            mBundle.putInt(MyConstants.DEFAULT_PARAM_KEY_3, patrolId);
            openActivityForResult(AddPatrolDiaryActivity.class, mBundle,
                    MyConstants.OPEN_ACTIVITY_DEFAULT_REQUEST_CODE_3);
        });
        endPatrolBtn.setOnClickListener(v -> {
            endBtnClickHandle();
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

    private void endBtnClickHandle() {
        isPatroling = false;
        final MarkerOptions mOptions = new MarkerOptions().anchor(0.5f, 1.0f)
                .position(linePoint.get(linePoint.size() - 1))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_patrol_distination));
        mAMap.addMarker(mOptions);

        if (reportDisposable != null && !reportDisposable.isDisposed()) {
            reportDisposable.dispose();
        }

        LatLngBounds.Builder newBounds = new LatLngBounds.Builder();
        for (int i = 0; i < linePoint.size(); i++) {
            newBounds.include(linePoint.get(i));
        }
        // ????????????????????????????????????
        mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(newBounds.build(), 15));

        Observable.just("")
                .delay(1500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .map(ss -> {
                    mAMap.getMapScreenShot(bitmap -> {
                        saveAndUploadPicFile(bitmap);
                    });
                    return "";
                })
                .to(RxLife.toMain(this))
                .subscribe(sss -> {
                }, throwable -> {
                    MyLogUtils.e("??????/?????????????????????", throwable);
                });
    }

    private void saveAndUploadPicFile(Bitmap bitmap) {
        MyLogUtils.d("OnMapScreenShotListener() bitmap = " + bitmap);
        if (bitmap == null) {
            MyLogUtils.d("????????????????????????");
            return;
        }
        // ????????????????????????
        String saveFileFolder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveFileFolder = this.getFilesDir() + File.separator +
                    "PatrolShot" + File.separator;
        } else {
            saveFileFolder = Environment.getExternalStorageDirectory()
                    + File.separator + "YuZhenJinBu" + File.separator
                    + "PatrolShot" + File.separator;
        }
        if (!FileUtil.isFileExist(saveFileFolder)) {
            FileUtil.createFolder(saveFileFolder);
        }
        if (!FileUtil.isFileExist(saveFileFolder)) {
            MyLogUtils.d("???????????????????????????");
            return;
        }
        final String fileName = "Patrol_" + System.currentTimeMillis() + ".png";
        final File saveFile = new File(saveFileFolder, fileName);
        MyLogUtils.d("?????????????????????: " + saveFile);

        FileOutputStream fos = null;
        try {
            saveFile.createNewFile();
            fos = new FileOutputStream(saveFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    saveFile.deleteOnExit();
                    e.printStackTrace();
                }
            }
        }
        if (!saveFile.exists()) {
            MyLogUtils.d("??????????????????");
            return;
        }
        List<String> paths = new ArrayList<>();
        paths.add(saveFile.getPath());
        getPresenter().uploadFiles(paths);
    }

    private void sendEndCmdHandle() {
        MyLogUtils.d("sendEndCmdHandle()");
        final float distance = AMapUtil.getPatrolDistance(linePoint);
        final double latValue = MyVariables.curCoordinate.latitude;
        final double longValue = MyVariables.curCoordinate.longitude;
        linePoint.add(new LatLng(latValue, longValue));
        getPresenter().updateRecord(patrolId, latValue, longValue, (int) distance, uploadPicStr);
    }

    private void switchViewModel(boolean isOnPatrol) {
        startPatrolBtn.setVisibility(isOnPatrol ? View.GONE: View.VISIBLE);
        patrolGroup.setVisibility(isOnPatrol ? View.VISIBLE: View.GONE);
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
            //????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
            // ??????????????? myLocationStyle ????????????????????????
            mAMap.setMyLocationStyle(myLocationStyle);
            mAMap.setMyLocationEnabled(true);

            final CameraUpdate cameraUpdate =  CameraUpdateFactory.newCameraPosition(new CameraPosition(
                    MyUiUtils.WUHAN, 18, 0, 0));
            mAMap.moveCamera(cameraUpdate);
            mAMap.setOnMyLocationChangeListener(mOnMyLocationChangeListener);

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
            MyLogUtils.i("initAMap() gpsLevel = " + gpsLevel);
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMapView != null) {
            mMapView.onDestroy();
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

    public void scaleLargeMap(float scaleValue) {
        final CameraPosition cameraPosition = mAMap.getCameraPosition();
        mAMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cameraPosition.target, scaleValue));
    }

    @Override
    public void showOrHidePb(boolean isShow, String tip) {

    }

    private double tempLat;
    private double tempLon;

    @Override
    public void onRequestCallback(String requestName, boolean isSuc, Object result) {
        switch (requestName) {

            case PatrolService.REQ_NAME_ADD_PATROL:
                if (isSuc) {
                    isPatroling = true;
                    patrolId = ((AddPatrolBean) result).getData().getId();

                    final MarkerOptions mOptions = new MarkerOptions().anchor(0.5f, 1.0f)
                            .position(linePoint.get(0))
                            .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_resource_circiut));
                    mAMap.addMarker(mOptions);

                    Observable.interval(10, 10, TimeUnit.SECONDS)
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Observer<Long>() {
                                @Override
                                public void onSubscribe(@NonNull Disposable d) {
                                    MyLogUtils.d("onSubscribe()");
                                    reportDisposable = d;
                                }

                                @Override
                                public void onNext(@NonNull Long aLong) {
                                    MyLogUtils.d("onNext() aLong = " + aLong);
                                    if (!BuildConfig.DEBUG) {
                                        final double latValue = MyVariables.curCoordinate.latitude;
                                        final double longValue = MyVariables.curCoordinate.longitude;
                                        linePoint.add(new LatLng(latValue, longValue));
                                        getPresenter().reportLocate(patrolId, latValue, longValue);
                                        return;
                                    }

                                    // ???????????????
                                    double latValue;
                                    double longValue;
                                    if (MyVariables.curCoordinate.latitude == 0) {
                                        latValue = MyVariables.curCoordinate.latitude;
                                        longValue = MyVariables.curCoordinate.longitude;
                                        tempLat = latValue;
                                        tempLon = longValue;
                                    } else {
                                        latValue = tempLat += +0.001;
                                        longValue = tempLon += 0.001;
                                    }
                                    linePoint.add(new LatLng(latValue, longValue));
                                    getPresenter().reportLocate(patrolId, latValue, longValue);
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    MyLogUtils.d("onError()");
                                    MyLogUtils.e("??????????????????", e);
                                    reportDisposable.dispose();
                                }

                                @Override
                                public void onComplete() {
                                    MyLogUtils.d("onComplete()");
                                    reportDisposable.dispose();
                                }
                            });
                    switchViewModel(true);
                } else {
                    linePoint.clear();
                    ToastUtil.s("??????????????????");
                }
                break;

            case PatrolService.REQ_NAME_REPORT_LOCATE:
                if (isSuc) {
                    mAMap.addPolyline(new PolylineOptions()
                            .addAll(linePoint)
                            .width(5)
                            .color(MyUiUtils.getColor(R.color.yellow_F7B500)));
                } else {
                    linePoint.remove(linePoint.size() - 1);
                }
                break;

            case PatrolService.REQ_NAME_UPDATE_RECORD:
                if (isSuc) {
                    switchViewModel(false);
                    final Bundle mBundle = new Bundle();
                    mBundle.putInt(MyConstants.DEFAULT_PARAM_KEY_1, patrolId);
                    openActivity(PatrolResultActivity.class, mBundle);
                } else {
                    linePoint.remove(linePoint.size() - 1);
                    ToastUtil.s("??????????????????");
                }
                break;

            default:
                break;
        }
    }

    private void getPatrolType() {
        CaseService caseService = RetrofitClient.getAPIService(CaseService.class);
        caseService.getTypeOrSource("patrolType")
                .subscribeOn(Schedulers.io())
                .to(RxLife.toMain(this))
                .subscribe(bean -> {
                    final boolean isEmpty = bean == null || bean.getCode() != 0
                            || bean.getData() == null || bean.getData().size() == 0;
                    if (isEmpty) {
                        return;
                    }
                    patrolTypeData.clear();
                    CommonUtils.listAddAllAvoidNPE(patrolTypeData, bean.getData());
                }, throwable -> {
                    MyLogUtils.e("??????????????????????????????", throwable);
                });
    }

    /**
     * ??????????????????
     * @param data
     */
    private void showTypeSelectDialog(String title, List<TypeCodeBean.DataBean> data) {
        if (data == null || data.size() == 0) {
            ToastUtil.s("????????????????????????");
            return;
        }
        linePoint.clear();
        mAMap.clear();

        TypeSelectDialog dialog = new TypeSelectDialog(mContext, title, data);
        dialog.setListener((position, o) -> {
            MyLogUtils.d("onItemClick() position = " + position);
            selectPatrolTypeStr = data.get(position).getTypeName();
            final Integer mUserID = MMKVUtil.getInt(SPStringUtils.USER_ID, 0);
            final double latValue = MyVariables.curCoordinate.latitude;
            final double longValue = MyVariables.curCoordinate.longitude;
            linePoint.add(new LatLng(latValue, longValue));
            getPresenter().addPatrol(latValue, longValue, selectPatrolTypeStr, mUserID);
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean haveCoordinateResult = requestCode == MyConstants.OPEN_ACTIVITY_DEFAULT_REQUEST_CODE_3
                && resultCode == RESULT_OK && data != null;
        if (haveCoordinateResult) {
            final LatLng reportCoordination =
                    data.getParcelableExtra(MyConstants.DEFAULT_PARAM_KEY_1);
            if (reportCoordination == null) {
                return;
            }
            final MarkerOptions mOptions = new MarkerOptions().anchor(0.5f, 1.0f)
                    .position(reportCoordination)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_patrol_record_normal_new));
            mAMap.addMarker(mOptions);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
