package com.gdu.command.ui.resource;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.Polygon;
import com.amap.api.maps2d.model.PolygonOptions;
import com.amap.api.maps2d.model.Polyline;
import com.amap.api.maps2d.model.PolylineOptions;
import com.bumptech.glide.Glide;
import com.gdu.baselibrary.BaseApplication;
import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.baselibrary.network.BaseBean;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.MyConstants;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.R;
import com.gdu.command.ui.base.dialog.ResourceDistributeDialog;
import com.gdu.command.ui.base.dialog.ResourceDistributeSelectDialog;
import com.gdu.command.ui.cases.navigate.BottomSelectDialogActivity;
import com.gdu.command.ui.video.view.VideoControlActivity;
import com.gdu.model.config.StorageConfig;
import com.gdu.model.resource.ResAirplaneInfoBean;
import com.gdu.model.resource.ResCaseInfoBean;
import com.gdu.model.resource.ResCircuitBean;
import com.gdu.model.resource.ResFocusPointBean;
import com.gdu.model.resource.ResHighPointDeviceBean;
import com.gdu.model.resource.ResOrganizationBean;
import com.gdu.model.resource.ResPersonInfoBean;
import com.gdu.model.resource.ResRegionBean;
import com.gdu.utils.LocationUtils;
import com.gdu.utils.MyUiUtils;
import com.gdu.utils.MyVariables;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.noober.background.view.BLRelativeLayout;
import com.rxjava.rxlife.RxLife;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.RequestBody;

/**
 * 资源分布
 */
public class ResDistributionActivity extends BaseActivity implements View.OnClickListener {

    /** marker弹出对话框 **/
    private ResourceDistributeDialog dialog;
    /** marker显示隐藏对话框 **/
    private ResourceDistributeSelectDialog selectMarkerDialog;

    private Gson mGson;
    private ResourceService resourceService;

    private ImageView backBtnIv;
    private MapView mMapView;
    private AMap mAMap;
    private EditText etSearch;
    private BLRelativeLayout rlShowMenu;
    private ImageView ivShowMenu;
    private ImageView ivMyselfPosition;

    /** 重点区域 **/
    private List<ResRegionBean> regionList = new ArrayList<>();
    /** 重点线路 **/
    private List<ResCircuitBean> circuitList = new ArrayList<>();
    /** 关注点位 **/
    private List<ResFocusPointBean> focusList = new ArrayList<>();
    /** 案件信息 **/
    private List<ResCaseInfoBean.RecordsDTO> caseList = new ArrayList<>();
    /** 组织人员 **/
    private List<ResPersonInfoBean> personList = new ArrayList<>();
    /** 高点监控 **/
    private List<ResHighPointDeviceBean> highPointList = new ArrayList<>();
    /** 无人机 **/
    private List<ResAirplaneInfoBean> airplaneInfoList = new ArrayList<>();
    /** 组织架构 **/
    private List<ResOrganizationBean> organizationList = new ArrayList<>();

    /** 绘制地图 标记 轨迹 区域 **/
    private List<Marker> markerList = new ArrayList<>();
    private List<Polygon> polygonList = new ArrayList<>();
    private List<Polyline> polylineList = new ArrayList<>();

    /** 菜单列表里的总数 **/
    private int org, person, airplane, high, region, circuit, focus, case_no;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_res_distribution;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).statusBarDarkFont(true).statusBarColor(R.color.white).init();
        mGson = new Gson();
        resourceService = RetrofitClient.getAPIService(ResourceService.class);

        initDialog();
        getPerson();
        initView();
        initMap();
    }

    /**
     * 初始化view
     */
    private void initView() {
        backBtnIv = findViewById(R.id.back_imageview);
        mMapView = findViewById(R.id.resource_map_view);
        etSearch = findViewById(R.id.ed_search);
        rlShowMenu = findViewById(R.id.rl_show_menu);
        ivShowMenu = findViewById(R.id.iv_show_menu);
        ivMyselfPosition = findViewById(R.id.iv_position_myself);

        //点击事件监听
        backBtnIv.setOnClickListener(this);
        ivShowMenu.setOnClickListener(this);
        ivMyselfPosition.setOnClickListener(this);
        rlShowMenu.setOnClickListener(this);
    }

    /**
     * Map初始化和相关加载
     */
    private void initMap() {
        mMapView.onCreate(mSavedInstanceState);
        mAMap = mMapView.getMap();
        // 固定一个中心区域坐标
        LatLng mLatLng = new LatLng(30.542, 114.27881630542);
        // 设置地图中心点以及缩放级别
        mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 12));
        // 隐藏缩放按钮
        mAMap.getUiSettings().setZoomControlsEnabled(false);
        // 是否显示定位按钮
        mAMap.getUiSettings().setMyLocationButtonEnabled(false);
        // marker点击事件
        initMarkerListener();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_imageview:
                finish();
                break;
            case R.id.rl_show_menu:
            case R.id.iv_show_menu:
                //显示marker菜单弹窗
                selectMarkerDialog.showSelectMarkerDialog();
                selectMarkerDialog.show();
                break;
            case R.id.iv_position_myself:
                showCurrentLocation();
                break;
        }
    }

    /**
     * 定位到当前位置，设置定位蓝点样式
     */
    private void showCurrentLocation() {
        // 定位当前位置
        mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(StorageConfig.sGPSLat, StorageConfig.sGPSLng), 18));
        MyLocationStyle myLocationStyle;
        // 初始化定位蓝点样式类
        myLocationStyle = new MyLocationStyle();
        // 连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
        // 设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.interval(2000);
        // 设置自定义蓝点样式。
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_up_arrow));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(Color.TRANSPARENT);
        // 自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(0);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(Color.TRANSPARENT);
        // 设置定位蓝点的Style
        mAMap.setMyLocationStyle(myLocationStyle);
        // 设置默认定位按钮是否显示，非必需设置。
//        mAMap.getUiSettings().setMyLocationButtonEnabled(true);
        // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        mAMap.setMyLocationEnabled(true);
    }

    /**
     * 设置菜单列表总数
     */
    private void setDialogTextNumber() {
        selectMarkerDialog.setTextValue(org,person,airplane,region, circuit,focus,case_no,high);
    }

    /**
     * 关注点位添加marker
     */
    private void addFocusMarker(List<ResFocusPointBean> list) {
        for (ResFocusPointBean bean : list) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.draggable(false);
//            String icon = UrlConfig.BASE_IP + bean.getResourcesIcon();
//            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(setGeniusIcon(icon)));
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_resource_focus));
            Marker marker = mAMap.addMarker(markerOptions);
            LocateInfo locateInfo = LocationUtils.wgs84ToGcj02(bean.getResourcesLatitude(), bean.getResourcesLongitude());
            marker.setPosition(new LatLng(locateInfo.getLatitude(), locateInfo.getLongitude()));
            marker.setObject(bean);
            markerList.add(marker);
        }
    }

    /**
     * 高点监控添加marker
     */
    private void addMonitorMarker(List<ResHighPointDeviceBean> list) {
        for (ResHighPointDeviceBean bean : list) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.draggable(false);
//            String icon = UrlConfig.BASE_IP + bean.getIconUrl();
//            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(setGeniusIcon(icon)));
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_resource_high_monitor));
            Marker marker = mAMap.addMarker(markerOptions);
            LocateInfo locateInfo = LocationUtils.wgs84ToGcj02(bean.getDeviceLatitude(), bean.getDeviceLongitude());
            marker.setPosition(new LatLng(locateInfo.getLatitude(), locateInfo.getLongitude()));
            marker.setObject(bean);
            markerList.add(marker);
        }
    }

    /**
     * 无人机添加marker
     */
    private void addAirplaneMarker(List<ResAirplaneInfoBean> list) {
        for (ResAirplaneInfoBean bean : list) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.draggable(false);
//            String icon = UrlConfig.BASE_IP + bean.getIconUrl();
//            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(setGeniusIcon(icon)));
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_resource_airplane));
            Marker marker = mAMap.addMarker(markerOptions);
            LocateInfo locateInfo = LocationUtils.wgs84ToGcj02(bean.getDeviceLatitude(), bean.getDeviceLongitude());
            marker.setPosition(new LatLng(locateInfo.getLatitude(), locateInfo.getLongitude()));
            marker.setObject(bean);
            markerList.add(marker);
        }
    }

    /**
     * 组织机构marker
     */
    private void addOrganizationMarker(List<ResOrganizationBean> list) {
        for (ResOrganizationBean bean : list) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.draggable(false);
//            String icon = UrlConfig.BASE_IP + bean.getDeptIcon();
//            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(setGeniusIcon(icon)));
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_resource_organization));
            Marker marker = mAMap.addMarker(markerOptions);
            LocateInfo locateInfo = LocationUtils.wgs84ToGcj02(bean.getDeptLatitude(), bean.getDeptLongitude());
            marker.setPosition(new LatLng(locateInfo.getLatitude(), locateInfo.getLongitude()));
            marker.setObject(bean);
            markerList.add(marker);
        }
    }

    /**
     * 人员信息marker
     */
    private void addPersonMarker(List<ResPersonInfoBean> list) {
        for (ResPersonInfoBean bean : list) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.draggable(false);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_resource_person));
            Marker marker = mAMap.addMarker(markerOptions);
            LocateInfo locateInfo = LocationUtils.wgs84ToGcj02(bean.getUserLatitude(), bean.getUserLongitude());
            marker.setPosition(new LatLng(locateInfo.getLatitude(), locateInfo.getLongitude()));
            marker.setObject(bean);
            markerList.add(marker);
        }
    }

    /**
     * 重点区域和marker
     * 单独绘制一块区域
     * @param bean
     */
    @TargetApi(Build.VERSION_CODES.O)
    private void addOneRegionPolygon(ResRegionBean bean) {
        List<LatLng> latLngList = new ArrayList<>();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.draggable(false);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_resource_region));
        Marker marker = mAMap.addMarker(markerOptions);
        marker.setObject(bean);
        markerList.add(marker);

        // 区域填充颜色
        float alphaFill = Float.parseFloat(bean.getFillRgbaDTO().getAlpha());
        float redFill = Float.parseFloat(bean.getFillRgbaDTO().getRed());
        float blueFill = Float.parseFloat(bean.getFillRgbaDTO().getBlue());
        float greenFill = Float.parseFloat(bean.getFillRgbaDTO().getGreen());
        // 线条颜色
        float alphaLine = Float.parseFloat(bean.getLineRgbaDTO().getAlpha());
        float redLine = Float.parseFloat(bean.getLineRgbaDTO().getAlpha());
        float blueLine = Float.parseFloat(bean.getLineRgbaDTO().getAlpha());
        float greenLine = Float.parseFloat(bean.getLineRgbaDTO().getAlpha());

        List<ResRegionBean.LonAndLatListDTO> latListDTOS = bean.getLonAndLatList();
        for (ResRegionBean.LonAndLatListDTO listDTO : latListDTOS) {
            LocateInfo locateInfo = LocationUtils.wgs84ToGcj02(listDTO.getLat(), listDTO.getLon());
            marker.setPosition(new LatLng(locateInfo.getLatitude(), locateInfo.getLongitude()));
            latLngList.add(new LatLng(locateInfo.getLatitude(), locateInfo.getLongitude()));
        }
        Polygon polygon = mAMap.addPolygon(new PolygonOptions()
                .addAll(latLngList)
                .strokeWidth(bean.getLineWidth())
                .strokeColor(Color.argb(alphaLine, redLine, blueLine, greenLine))
                .fillColor(Color.argb(alphaFill, redFill, greenFill, blueFill)));
        polygonList.add(polygon);
    }

    /**
     * 重点线路marker
     * 单独绘制每段线路
     */
    @SuppressLint("NewApi")
    private void addCircuitMarker(ResCircuitBean bean) {
        List<LatLng> latLngList = new ArrayList<>();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.draggable(false);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_resource_circiut));
        Marker marker = mAMap.addMarker(markerOptions);

        //轨迹线条颜色 设置顺序分别是透明度红绿蓝
        float alpha = Float.parseFloat(bean.getLineRgbaDTO().getAlpha());
        float red = Float.parseFloat(bean.getLineRgbaDTO().getRed());
        float blue = Float.parseFloat(bean.getLineRgbaDTO().getBlue());
        float green = Float.parseFloat(bean.getLineRgbaDTO().getGreen());

        for (int i = 0; i < bean.getLonAndLatList().size(); i++) {
            double lat1 = bean.getLonAndLatList().get(i).getLat();
            double lng1 = bean.getLonAndLatList().get(i).getLon();
            LocateInfo locateInfo = LocationUtils.wgs84ToGcj02(lat1, lng1);
            marker.setPosition(new LatLng(locateInfo.getLatitude(), locateInfo.getLongitude()));
            latLngList.add(new LatLng(locateInfo.getLatitude(), locateInfo.getLongitude()));
        }
        Polyline polyline = mAMap.addPolyline(new PolylineOptions()
                .addAll(latLngList)
                .setDottedLine(false)
                .width(MyUiUtils.dip2px(bean.getLineWidth()))
                .color(Color.argb(alpha, red, green, blue)));
        polylineList.add(polyline);
        marker.setObject(bean);
        markerList.add(marker);
    }

    /**
     * 案件信息marker
     */
    private void addCaseInfoMarker(ResCaseInfoBean entity) {
       List<ResCaseInfoBean.RecordsDTO> list = entity.getRecords();
        for (ResCaseInfoBean.RecordsDTO bean : list) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.draggable(false);
//            String icon = UrlConfig.BASE_IP + bean.getCaseFile();
//            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(setGeniusIcon(icon)));
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_center_location));
            Marker marker = mAMap.addMarker(markerOptions);
            LocateInfo locateInfo = LocationUtils.wgs84ToGcj02(bean.getLatitude(), bean.getLongitude());
            marker.setPosition(new LatLng(locateInfo.getLatitude(), locateInfo.getLongitude()));
            marker.setObject(bean);
            markerList.add(marker);
        }
    }

    /**
     * 设置图片地址到imageView并转为bitmap
     * @param url
     * @return
     */
    public Bitmap setGeniusIcon(String url) {
        Bitmap bitmap = null;
        View view = View.inflate(this, R.layout.view_marker, null);
        ImageView imageView = view.findViewById(R.id.iv_marker);
        Glide.with(this).load(url).into(imageView);
        bitmap = convertViewToBitmap(view);
        return bitmap;
    }

    /**
     * view转bitmap
     * @param view
     * @return
     */
    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    /**
     * marker点击事件
     */
    private void initMarkerListener() {
        mAMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Object object = marker.getObject();
                if (object instanceof ResHighPointDeviceBean) {
                    // 高点监控
                    ResHighPointDeviceBean bean = (ResHighPointDeviceBean) object;
                    dialog.showHighMonitorInfoDialog(bean);
                }
                if (object instanceof ResPersonInfoBean) {
                    // 人员信息
                    ResPersonInfoBean bean = (ResPersonInfoBean) object;
                    dialog.showPersonInfoDialog(bean);
                }
                if (object instanceof ResAirplaneInfoBean) {
                    // 无人机
                    ResAirplaneInfoBean bean = (ResAirplaneInfoBean) object;
                    dialog.showAirplaneInfoDialog(bean);
                }
                if (object instanceof ResOrganizationBean) {
                    // 组织架构
                    ResOrganizationBean bean = (ResOrganizationBean) object;
                    dialog.showOrganizationDialog(bean);
                }
                if (object instanceof ResFocusPointBean) {
                    // 关注点位 (重点点位)
                    ResFocusPointBean bean = (ResFocusPointBean) object;
                    dialog.showEmphasisPointDialog(bean);
                }
                if (object instanceof ResCaseInfoBean.RecordsDTO) {
                    // 案件信息
                    ResCaseInfoBean.RecordsDTO bean = (ResCaseInfoBean.RecordsDTO) object;
                    dialog.showCaseInfoDialog(bean);
                }
                if (object instanceof ResCircuitBean) {
                    // 线路信息
                    ResCircuitBean bean = (ResCircuitBean) object;
                    dialog.showCircuitDialog(bean);
                }
                if (object instanceof ResRegionBean) {
                    // 重点区域 (管控范围)
                    ResRegionBean bean = (ResRegionBean) object;
                    dialog.showControlScopeDialog(bean);
                }
                return true;
            }
        });
    }

    /**
     * 弹框初始化
     */
    private void initDialog() {
        if (dialog == null) {
            dialog = new ResourceDistributeDialog(this);
        }

        if (selectMarkerDialog == null) {
            selectMarkerDialog = new ResourceDistributeSelectDialog(this);
        }

        // 人员信息联系电话
        dialog.setOnPersonPhoneListener(new OnDialogCallListener() {
            @Override
            public void onClick(String phoneNumber) {
                callPhone(phoneNumber);
            }
        });

        // 人员信息组织电话
        dialog.setOnPersonOrgListener(new OnDialogCallListener() {
            @Override
            public void onClick(String phoneNumber) {
                callPhone(phoneNumber);
            }
        });

        initDialogListener();
        showHideMarker();
    }

    /**
     * 显示隐藏marker
     */
    private void showHideMarker() {
        selectMarkerDialog.setOnCheckBoxChangeListener(new OnCheckBoxChangeListener() {
            @Override
            public void onOrganizationChanged(boolean isChecked) {
                // 组织机构
                ResourceConfig.isOrganizationChecked = isChecked;
                for (int i = 0; i < markerList.size(); i++) {
                    if (markerList.get(i).getObject() instanceof ResOrganizationBean) {
                        markerList.get(i).setVisible(isChecked);
                    }
                }
            }

            @Override
            public void onPersonChanged(boolean isChecked) {
                // 人员信息
                ResourceConfig.isPersonChecked = isChecked;
                for (int i = 0; i < markerList.size(); i++) {
                    if (markerList.get(i).getObject() instanceof ResPersonInfoBean) {
                        markerList.get(i).setVisible(isChecked);
                    }
                }
            }

            @Override
            public void onAirplaneChanged(boolean isChecked) {
                // 无人机
                ResourceConfig.isAirplaneChecked = isChecked;
                for (int i = 0; i < markerList.size(); i++) {
                    if (markerList.get(i).getObject() instanceof ResAirplaneInfoBean) {
                        markerList.get(i).setVisible(isChecked);
                    }
                }
            }

            @Override
            public void onHighPointChanged(boolean isChecked) {
                // 高点监控
                ResourceConfig.isMonitorChecked = isChecked;
                for (int i = 0; i < markerList.size(); i++) {
                    if (markerList.get(i).getObject() instanceof ResHighPointDeviceBean) {
                        markerList.get(i).setVisible(isChecked);
                    }
                }
            }

            @Override
            public void onRegionChanged(boolean isChecked) {
                // 重点区域
                ResourceConfig.isRegionChecked = isChecked;
                for (int i = 0; i < polygonList.size(); i++) {
                    polygonList.get(i).setVisible(isChecked);
                }
                for (int i = 0; i < markerList.size(); i++) {
                    if (markerList.get(i).getObject() instanceof ResRegionBean) {
                        markerList.get(i).setVisible(isChecked);
                    }
                }
            }

            @Override
            public void onCircuitChanged(boolean isChecked) {
                // 重点线路
                ResourceConfig.isCircuitChecked = isChecked;
                for (int i = 0; i < polylineList.size(); i++) {
                    polylineList.get(i).setVisible(isChecked);
                }

                for (int i = 0; i < markerList.size(); i++) {
                    if (markerList.get(i).getObject() instanceof ResCircuitBean) {
                        markerList.get(i).setVisible(isChecked);
                    }
                }
            }

            @Override
            public void onFocusChanged(boolean isChecked) {
                // 关注点位
                ResourceConfig.isFocusChecked = isChecked;
                for (int i = 0; i < markerList.size(); i++) {
                    if (markerList.get(i).getObject() instanceof ResFocusPointBean) {
                        markerList.get(i).setVisible(isChecked);
                    }
                }
            }

            @Override
            public void onCaseInfoChanged(boolean isChecked) {
                // 案件信息
                ResourceConfig.isCaseChecked = isChecked;
                for (int i = 0; i < markerList.size(); i++) {
                    if (markerList.get(i).getObject() instanceof ResCaseInfoBean.RecordsDTO) {
                        markerList.get(i).setVisible(isChecked);
                    }
                }
            }
        });
    }

    /**
     * 弹框点击事件
     */
    private void initDialogListener() {
        dialog.setOnDialogClickListener(new OnDialogClickListener() {
            @Override
            public void onEmphasisPointGuideListener(double lat, double lng, String address) {
                // 重点点位到这里去
                showNavigationDialog(new LatLng(lat, lng), address);
            }

            @Override
            public void onMonitorVideoListener() {
                // 高点监控信息视频点
                openActivity(VideoControlActivity.class);
            }

            @Override
            public void onMonitorDispatchListener() {
                // 高点监控信息云台操作点击

            }

            @Override
            public void onOrganizationGuideListener(double lat, double lng, String address) {
                // 组织机构到这里去
                showNavigationDialog(new LatLng(lat, lng), address);
            }

            @Override
            public void onCaseInfoGuideListener(double lat, double lng, String address) {
                // 案件信息到这里去
                showNavigationDialog(new LatLng(lat, lng), address);
            }
        });
    }

    /**
     * 获取组织人员信息
     */
    private void getPerson() {
        Map<String, Object> map  = new HashMap<>();
        map.put("content", "");
        String strEntity = mGson.toJson(map);
        RequestBody body = RequestBody.create(strEntity, okhttp3.MediaType.parse("application" + "/json;charset=UTF-8"));
        resourceService.getPerson(body).subscribeOn(Schedulers.io())
                .to(RxLife.toMain(this))
                .subscribe(new Consumer<BaseBean<List<ResPersonInfoBean>>>() {
                    @Override
                    public void accept(BaseBean<List<ResPersonInfoBean>> listBaseBean) throws Throwable {
                        boolean isNotEmpty = null != listBaseBean && listBaseBean.data != null && listBaseBean.data.size() != 0;
                        if (isNotEmpty) {
                            personList.addAll(listBaseBean.data);
                            addPersonMarker(personList);
                            person = personList.size();
                        }
                        getHighPoint();
                    }
                }, throwable -> {
                    getHighPoint();
                    MyLogUtils.e("获取组织人员信息出错", throwable);
                });
    }

    /**
     * 获取高点监控列表信息
     */
    private void getHighPoint() {
        resourceService.getHighMonitor().subscribeOn(Schedulers.io())
                .to(RxLife.toMain(this))
                .subscribe(new Consumer<BaseBean<List<ResHighPointDeviceBean>>>() {
                    @Override
                    public void accept(BaseBean<List<ResHighPointDeviceBean>> listBaseBean) throws Throwable {
                        boolean isNotEmpty = null != listBaseBean && listBaseBean.data != null && listBaseBean.data.size() != 0;
                        if (isNotEmpty) {
                            highPointList.addAll(listBaseBean.data);
                            addMonitorMarker(highPointList);
                            high = highPointList.size();
                        }
                        getAirplane();
                    }
                }, throwable -> {
                    getAirplane();
                    MyLogUtils.e("获取高点设备列表信息出错", throwable);
                });
    }

    /**
     * 获取无人机信息列表
     */
    private void getAirplane() {
        resourceService.getAirplane().subscribeOn(Schedulers.io())
                .to(RxLife.toMain(this))
                .subscribe(new Consumer<BaseBean<List<ResAirplaneInfoBean>>>() {
                    @Override
                    public void accept(BaseBean<List<ResAirplaneInfoBean>> listBaseBean) throws Throwable {
                        boolean isNotEmpty = null != listBaseBean && listBaseBean.data != null && listBaseBean.data.size() != 0;
                        if (isNotEmpty) {
                            airplaneInfoList.addAll(listBaseBean.data);
                            addAirplaneMarker(airplaneInfoList);
                            airplane = airplaneInfoList.size();
                        }
                        getOrganization();
                    }
                }, throwable -> {
                    getOrganization();
                    MyLogUtils.e("获取无人机列表信息出错", throwable);
                });
    }

    /**
     * 获取组织机构列表
     */
    private void getOrganization() {
        resourceService.getOrganization().subscribeOn(Schedulers.io())
                .to(RxLife.toMain(this))
                .subscribe(new Consumer<BaseBean<List<ResOrganizationBean>>>() {
                    @Override
                    public void accept(BaseBean<List<ResOrganizationBean>> listBaseBean) throws Throwable {
                        boolean isNotEmpty = null != listBaseBean && listBaseBean.data != null && listBaseBean.data.size() != 0;
                        if (isNotEmpty) {
                            organizationList.addAll(listBaseBean.data);
                            addOrganizationMarker(organizationList);
                            org = organizationList.size();
                        }
                        getFocus();
                    }
                }, throwable -> {
                    getFocus();
                    MyLogUtils.e("获取获取组织机构列表出错", throwable);
                });
    }

    /**
     * 获取关注点位资源
     */
    private void getFocus() {
        resourceService.getFocus().subscribeOn(Schedulers.io())
                .to(RxLife.toMain(this))
                .subscribe(new Consumer<BaseBean<List<ResFocusPointBean>>>() {
                    @Override
                    public void accept(BaseBean<List<ResFocusPointBean>> listBaseBean) throws Throwable {
                        boolean isNotEmpty = null != listBaseBean && listBaseBean.data != null && listBaseBean.data.size() != 0;
                        if (isNotEmpty) {
                            focusList.addAll(listBaseBean.data);
                            addFocusMarker(focusList);
                            focus = focusList.size();
                        }
                        getCircuit();
                    }
                }, throwable -> {
                    getCircuit();
                    MyLogUtils.e("获取点资源出错", throwable);
                });
    }

    /**
     * 获取重点线路资源
     */
    private void getCircuit() {
        resourceService.getCircuit().subscribeOn(Schedulers.io())
                .to(RxLife.toMain(this))
                .subscribe(new Consumer<BaseBean<List<ResCircuitBean>>>() {
                    @Override
                    public void accept(BaseBean<List<ResCircuitBean>> listBaseBean) throws Throwable {
                        boolean isNotEmpty = null != listBaseBean && listBaseBean.data != null && listBaseBean.data.size() != 0;
                        if (isNotEmpty) {
                            circuitList.addAll(listBaseBean.data);
                            for (int i = 0; i < circuitList.size(); i++) {
                                addCircuitMarker(circuitList.get(i));
                            }
                            circuit = circuitList.size();
                        }
                        getRegion();
                    }
                }, throwable -> {
                    getRegion();
                    MyLogUtils.e("获取线资源出错", throwable);
                });
    }

    /**
     * 获取重点区域资源
     */
    private void getRegion() {
        resourceService.getRegion()
                .subscribeOn(Schedulers.io())
                .to(RxLife.toMain(this))
                .subscribe(listBaseBean -> {
                    boolean isNotEmpty = null != listBaseBean && listBaseBean.data != null
                            && listBaseBean.data.size() != 0;
                    if (isNotEmpty) {
                        regionList.addAll(listBaseBean.data);
//                            addRegionMarker(regionList);
                        for (int i = 0; i < regionList.size(); i++) {
                            addOneRegionPolygon(regionList.get(i));
                        }
                        region = regionList.size();
                    }
                    getCase();
                }, throwable -> {
                    getCase();
                    MyLogUtils.e("获取线资源出错", throwable);
                });
    }

    /**
     * 获取案件信息
     */
    private void getCase() {
        Map<String, Object> map  = new HashMap<>();
        map.put("currentPage", 1);
        map.put("pageSize", 1000);
        String strEntity = mGson.toJson(map);
        RequestBody body = RequestBody.create(strEntity, okhttp3.MediaType.parse("application" + "/json;charset=UTF-8"));
        resourceService.getCase(body).subscribeOn(Schedulers.io())
                .to(RxLife.toMain(this))
                .subscribe(new Consumer<BaseBean<ResCaseInfoBean>>() {
                    @Override
                    public void accept(BaseBean<ResCaseInfoBean> listBaseBean) throws Throwable {
                        boolean isNotEmpty = null != listBaseBean && listBaseBean.data != null && listBaseBean.data.getRecords().size() != 0;
                        if (isNotEmpty) {
                            caseList.addAll(listBaseBean.data.getRecords());
                            addCaseInfoMarker(listBaseBean.data);
                            case_no = caseList.size();
                        }
                        setDialogTextNumber();
                    }
                }, throwable -> {
                    setDialogTextNumber();
                    MyLogUtils.e("获取案件信息出错", throwable);
                });
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

    /**
     * 弹框显示已安装地图并跳转
     * @param mapCoordinate 经纬度
     * @param addressContent 地址名称
     */
    public void showNavigationDialog(LatLng mapCoordinate, String addressContent) {
        final List<String> navigationMaps = getCanNavigationItem();
        if (navigationMaps.size() > 0) {
            MyVariables.SELECTITEMS.clear();
            MyVariables.SELECTITEMS.addAll(navigationMaps);
            final Intent intent = new Intent(this, BottomSelectDialogActivity.class);
            final Bundle bundle = new Bundle();
            bundle.putParcelable(MyConstants.MAP_COORDINATE, mapCoordinate);
            bundle.putString(MyConstants.MAP_ADDRESS_CONTENT, addressContent);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
           ToastUtil.s("未检测到地图应用,请先安装地图");
        }
    }

    /**
     * 拨打电话
     * @param phoneNumber
     */
    private void callPhone(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            ToastUtil.s("电话号码为空");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ToastUtil.s("请授权拨打电话权限");
            return;
        }
        startActivity(intent);
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
        // 页面退出后重置选中状态
        ResourceConfig.resetCheckState();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mMapView.onSaveInstanceState(outState);
    }
}
