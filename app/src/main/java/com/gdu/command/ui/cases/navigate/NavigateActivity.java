package com.gdu.command.ui.cases.navigate;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapException;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.gdu.baselibrary.BaseApplication;
import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.MyConstants;
import com.gdu.command.R;
import com.gdu.command.views.overlay.DrivingRouteOverlay;
import com.gdu.model.cases.IssueBean;
import com.gdu.model.config.GlobalVariable;
import com.gdu.model.config.StorageConfig;
import com.gdu.utils.MyUiUtils;
import com.gdu.utils.MyVariables;

import java.util.ArrayList;
import java.util.List;

public class NavigateActivity extends BaseActivity<NavigatePresenter> implements View.OnClickListener,RouteSearch.OnRouteSearchListener {

    private MapView mMapView;
    private AMap mAMap;
    private RouteSearch mRouteSearch;

    private ImageView mBackImageView;
//    private TextView mCaseNameTextView;
//    private TextView mCaseDescTextView;
//    private TextView mCaseLocationTextView;
//    private TextView mCaseTimeTextView;
//    private TextView mCaseReporterTextView;

    private LatLonPoint mLatLonPointStart;
    private LatLonPoint mLatLonPointEnd;
    private IssueBean mCurrentIssueBean;

//    private ImageView mIssueNavigateImageView;
//    private ImageView mIssueDialImageView;
//    private ImageView mIssueChatImageView;
//    private LinearLayout mDealIssueLayout;
    private TextView shotLocTv, detailLocTv, goHereBtnTv;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        super.onBaseCreate(savedInstanceState);
        isShowWaterMark = false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_case_navigate;
    }

    @Override
    protected void initialize() {
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        UiSettings uiSettings = mAMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
        mAMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                drawRoute();
            }
        });
        mRouteSearch.setRouteSearchListener(this);
        goHereBtnTv.setOnClickListener(this);
//        mIssueNavigateImageView.setOnClickListener(this);
//        mIssueDialImageView.setOnClickListener(this);
//        mIssueChatImageView.setOnClickListener(this);
//        mDealIssueLayout.setOnClickListener(this);
    }

    private void initView() {
        mMapView = findViewById(R.id.map);
        mMapView = findViewById(R.id.map);
        mBackImageView = findViewById(R.id.back_imageview);
        mBackImageView.setOnClickListener(this);

        shotLocTv = findViewById(R.id.tv_locShotName);
        detailLocTv = findViewById(R.id.tv_locDetailName);
        goHereBtnTv = findViewById(R.id.tv_goHereBtn);

//        mCaseNameTextView = findViewById(R.id.case_name_textview);
//        mCaseDescTextView = findViewById(R.id.tv_issue_description);
//        mCaseLocationTextView = findViewById(R.id.tv_issue_location);
//        mCaseTimeTextView = findViewById(R.id.tv_issue_time);
//        mCaseReporterTextView = findViewById(R.id.tv_issue_department);
//        mIssueNavigateImageView = findViewById(R.id.issue_locate_imageview);
//        mIssueDialImageView = findViewById(R.id.issue_dial_imageview);
//        mIssueChatImageView = findViewById(R.id.issue_chat_imageview);
//        mDealIssueLayout = findViewById(R.id.deal_issue_layout);
    }

    private void initData() {
        try {
            mMapView.onCreate(mSavedInstanceState);
            mAMap = mMapView.getMap();
            mRouteSearch = new RouteSearch(this);
            mCurrentIssueBean = GlobalVariable.sCurrentIssueBean;

            shotLocTv.setVisibility(CommonUtils.isEmptyString(mCurrentIssueBean.getReportAddr()) ?
                    View.GONE : View.VISIBLE);
            shotLocTv.setText(CommonUtils.convertNull2EmptyStr(mCurrentIssueBean.getReportAddr()));
            detailLocTv.setText(mCurrentIssueBean.getCaseDesc());
            detailLocTv.setVisibility(CommonUtils.isEmptyString(mCurrentIssueBean.getCaseDesc()) ?
                    View.GONE : View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        mCaseNameTextView.setText(mCurrentIssueBean.getCaseName());
//        mCaseDescTextView.setText(mCurrentIssueBean.getCaseDesc());
//        mCaseLocationTextView.setText(mCurrentIssueBean.getReportAddr());
//        mCaseTimeTextView.setText(mCurrentIssueBean.getReportTime());
//        String reportMan;
//        final String nameStr = CommonUtils.convertNull2EmptyStr(mCurrentIssueBean.getReport_man());
//        final String mobileStr = CommonUtils.convertNull2EmptyStr(mCurrentIssueBean.getReportTel());
//        reportMan  = CommonUtils.isEmptyString(nameStr) ? "匿名" : nameStr;
//        if (!CommonUtils.isEmptyString(mobileStr)) {
//            reportMan += "(" + mobileStr + ")";
//        }
//        mCaseReporterTextView.setText(reportMan);
    }

    /**
     * 画导航路线
     */
    private void drawRoute(){
        mLatLonPointStart = new LatLonPoint(StorageConfig.sGPSLat, StorageConfig.sGPSLng);
        mLatLonPointEnd = new LatLonPoint(mCurrentIssueBean.getLatitude(), mCurrentIssueBean.getLongitude());
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(
                new RouteSearch.FromAndTo(mLatLonPointStart,
                        mLatLonPointEnd), RouteSearch.DrivingDefault, null, null, "");
        mRouteSearch.calculateDriveRouteAsyn(query);
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
        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_imageview:
                finish();
                break;

            case R.id.tv_goHereBtn:
//            case R.id.issue_locate_imageview:
                navigate();
                break;
//            case R.id.issue_dial_imageview:
//                dialPhone(mCurrentIssueBean.getReportTel());
//                break;
//            case R.id.issue_chat_imageview:
//                Intent chatIntent = new Intent(NavigateActivity.this, ChatActivity.class);
//                chatIntent.putExtra(GlobalVariable.EXTRA_CASE_ID, mCurrentIssueBean.getId());
//                startActivity(chatIntent);
//                break;
//            case R.id.deal_issue_layout:
//                Intent dealIntent = new Intent(NavigateActivity.this, DealCaseActivity.class);
//                dealIntent.putExtra(GlobalVariable.EXTRA_CASE_STATUS, CaseStatus.HANDLED.getKey());
//                startActivityForResult(dealIntent, GlobalVariable.REQUEST_DEAL_CASE_CODE);
//                break;
            default:
                break;
        }
    }

//    /**
//     * 拨打电话
//     * @param phone
//     */
//    private void dialPhone(String phone){
//        if (!TextUtils.isEmpty(phone)) {
//            Intent intent = new Intent();
//            intent.setAction(Intent.ACTION_CALL);
//            intent.setData(Uri.parse("tel:" + phone));
//            mContext.startActivity(intent);
//        } else {
//            ToastUtil.l("电话号码错误");
//        }
//    }

    private void navigate() {
        showNavigationDialog(new LatLng(mCurrentIssueBean.getLatitude(),
                mCurrentIssueBean.getLongitude()), GlobalVariable.sCurrentIssueBean.getReportAddr());
//        boolean isInstall = AppUtil.isInstallApp(this, "com.autonavi.minimap");
//        if (isInstall) {
//            // 高德地图
//            double gdLatitude = mCurrentIssueBean.getLatitude();
//            double gdLongitude = mCurrentIssueBean.getLongitude();
//            String uri = "androidamap://route?sourceApplication=" + R.string.app_name
//                    + "&sname=我的位置&dlat=" + gdLatitude
//                    + "&dlon=" + gdLongitude
//                    + "&dname=" + mCurrentIssueBean.getReportAddr()
//                    + "&dev=0&m=0&t=1";
//            Intent intent = new Intent();
//            intent.setAction(Intent.ACTION_VIEW);
//            intent.addCategory("android.intent.category.DEFAULT");
//            intent.setData(Uri.parse(uri));
//            intent.setPackage("com.autonavi.minimap");
//            mContext.startActivity(intent);
//        } else {
//            ToastUtil.l("地图未安装");
//        }
    }

    /**
     * 获取安装了的地图App数量.
     */
    public List<String> getCanNavigationItem() {
        final List<String> mapItems = new ArrayList<>();
        if (CommonUtils.baiduMapIsInstalled(BaseApplication.getInstance())) {
            mapItems.add("百度地图");
        }
        if (CommonUtils.gaodeMapIsInstalled(BaseApplication.getInstance())) {
            mapItems.add("高德地图");
        }
        if (CommonUtils.tencentMapIsInstalled(BaseApplication.getInstance())) {
            mapItems.add("腾讯地图");
        }
        return mapItems;
    }

    public void showNavigationDialog(LatLng mapCoordinate, String addresContent) {
        final List<String> navigationMaps = getCanNavigationItem();
        if (navigationMaps.size() > 0) {
            MyVariables.SELECTITEMS.clear();
            MyVariables.SELECTITEMS.addAll(navigationMaps);
            final Intent intent = new Intent(this, BottomSelectDialogActivity.class);
            final Bundle bundle = new Bundle();
            bundle.putParcelable(MyConstants.MAP_COORDINATE, mapCoordinate);
            bundle.putString(MyConstants.MAP_ADDRESS_CONTENT, addresContent);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            MyUiUtils.showToast("未检测到地图应用,请先安装地图.", Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
        DrivePath drivePath = driveRouteResult.getPaths().get(0);
        DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay (this, mAMap, drivePath, driveRouteResult.getStartPos(),
                driveRouteResult.getTargetPos(), null);
        drivingRouteOverlay.setNodeIconVisibility(false);//隐藏转弯的节点
        drivingRouteOverlay.addToMap();
        drivingRouteOverlay.zoomToSpan();
        try {
            mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(
                    new LatLng(mLatLonPointStart.getLatitude(), mLatLonPointStart.getLongitude()),
                    new LatLng(mLatLonPointEnd.getLatitude(), mLatLonPointEnd.getLongitude())),50));
        } catch (AMapException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
}
