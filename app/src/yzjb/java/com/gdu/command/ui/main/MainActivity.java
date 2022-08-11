package com.gdu.command.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.services.core.ServiceSettings;
import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.baselibrary.base.BasePresenter;
import com.gdu.baselibrary.network.HostUrl;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.utils.AppUtil;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.MMKVUtil;
import com.gdu.baselibrary.utils.MyConstants;
import com.gdu.baselibrary.utils.SPStringUtils;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.baselibrary.utils.eventbus.EventMessage;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.baselibrary.utils.watermark.WaterMarkManager;
import com.gdu.command.BuildConfig;
import com.gdu.command.R;
import com.gdu.command.downloadfile.DownloadPresenter;
import com.gdu.command.downloadfile.MyDownloadListener;
import com.gdu.command.event.LocationEvent;
import com.gdu.command.ui.message.NoticeAlarmBean;
import com.gdu.command.ui.message.NoticeMessageEvent;
import com.gdu.command.ui.message.NoticeMessageFragment;
import com.gdu.command.ui.my.LoginActivity;
import com.gdu.command.ui.splash.LoginService;
import com.gdu.command.ui.upgrade.AppUpdateDialog;
import com.gdu.command.ui.upgrade.AppVersionBean;
import com.gdu.command.ui.upgrade.IBtnClickListenerRecall;
import com.gdu.command.ui.upgrade.UpgradeService;
import com.gdu.model.chat.ReceiveChatEvent;
import com.gdu.model.config.GlobalVariable;
import com.gdu.model.config.StorageConfig;
import com.gdu.model.config.UrlConfig;
import com.gdu.model.user.PersonInfoBean;
import com.gdu.mqttchat.MqttService;
import com.gdu.mqttchat.chat.presenter.ChatService;
import com.gdu.utils.MyVariables;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.rxjava.rxlife.RxLife;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.sigpipe.jbsdiff.Patch;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.gdu.model.config.UrlConfig.MQTT_ALARM_NOTICE;
import static com.gdu.model.config.UrlConfig.MQTT_DUTY_NOTICE;

public class MainActivity extends BaseActivity<BasePresenter> implements AMapLocationListener {

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;

    private Intent mMqttServiceIntent;

    private BottomNavigationView navView;
    private Gson gson;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initialize() {
        this.getSupportActionBar().hide();
        ServiceSettings.updatePrivacyShow(this, true, true);
        ServiceSettings.updatePrivacyAgree(this, true);
        EventBus.getDefault().register(this);
        initData();
        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_video, R.id.navigation_organization,
                R.id.navigation_message, R.id.navigation_person)
                .build();
        NavController mNavController = Navigation.findNavController(this, R.id.nav_host_fragment1);
        NavigationUI.setupActionBarWithNavController(this, mNavController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, mNavController);
        navView.setItemIconTintList(null);
//        checkVersion();
//        checkVersionNew();
        final String useName = MMKVUtil.getString(SPStringUtils.USER_NAME);
        WaterMarkManager.setText(CommonUtils.convertNull2EmptyStr(useName));

        intiListener(mNavController);
    }

    private void intiListener(NavController mNavController) {
        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // 避免再次点击重复创建
                if (item.isChecked()) {
                    return true;
                }
                // 避免B返回到A重复创建
                boolean backStack = mNavController.popBackStack(item.getItemId(), false);
                if (backStack) {
                    // 已创建
                    return true;
                } else {
                    // 未创建
                    return NavigationUI.onNavDestinationSelected(item, mNavController);
                }
            }
        });


        mNavController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination,
                                             @Nullable Bundle arguments) {
                if (destination.getLabel().equals("消息")) {
                    navView.getMenu().getItem(3).setIcon(R.drawable.message_bottom_selector);
                }
            }
        });
    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
//        setContentView(R.layout.activity_main);
//    }

    private void initData() {
        GlobalVariable.mainActivityHadCreated = true;
        mMqttServiceIntent = new Intent(getApplicationContext(), MqttService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(mMqttServiceIntent);
        } else {
            startService(mMqttServiceIntent);
        }

        getNewLocation();

        Observable.just("").delay(2, TimeUnit.SECONDS)
                .to(RxLife.to(this))
                .subscribe(s -> {
//                    MqttService.addMqttListener(mMqttListener);
                    MqttService.subTopic(UrlConfig.MQTT_SEND_LOCATION_TOPIC);
                }, throwable -> {
                }, () -> {
                });

        Observable.just("").delay(1, TimeUnit.SECONDS)
                .to(RxLife.to(this))
                .subscribe(s -> {
                    MqttService.subTopic(UrlConfig.MQTT_SEND_APP_OFF_LINE);
                }, throwable -> {
                }, () -> {
                });

        Observable.just("").delay(3, TimeUnit.SECONDS)
                .to(RxLife.to(this))
                .subscribe(s -> {
                    int userId = MMKVUtil.getInt(SPStringUtils.USER_ID);
                    MqttService.subTopic(MQTT_DUTY_NOTICE + userId);
                }, throwable -> {
                }, () -> {
                });


        Observable.just("").delay(4, TimeUnit.SECONDS)
                .to(RxLife.to(this))
                .subscribe(s -> {
                    int userId = MMKVUtil.getInt(SPStringUtils.USER_ID);
                    MqttService.subTopic(MQTT_DUTY_NOTICE + userId);
                }, throwable -> {
                }, () -> {
                });

        getPersonInfo();
    }


    /**
     * 获取用户信息
     */
    private void getPersonInfo() {
        LoginService service = RetrofitClient.getAPIService(LoginService.class);
        service.getPersonInfo().enqueue(new Callback<PersonInfoBean>() {
            @Override
            public void onResponse(Call<PersonInfoBean> call, Response<PersonInfoBean> response) {
                PersonInfoBean personInfoBean = response.body();
                final boolean isEmptyData =
                        personInfoBean == null || personInfoBean.getCode() != 0 || personInfoBean.getData() == null;
                if (isEmptyData) {
                    if (personInfoBean != null && personInfoBean.getCode() == 401) {
                        startActivity(new Intent(getContext(), LoginActivity.class));
                    }
                    return;
                }
                int customerId = personInfoBean.getData().getCustomerId();
                MqttService.subTopic(MQTT_ALARM_NOTICE + customerId);
            }

            @Override
            public void onFailure(Call<PersonInfoBean> call, Throwable e) {

            }
        });
    }

    private void getNewLocation() {
        try {
            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
            //初始化定位
            mLocationClient = new AMapLocationClient(this.getApplicationContext());
            //设置定位回调监听
            mLocationClient.setLocationListener(this);
            //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位间隔,单位毫秒,默认为2000ms
            mLocationOption.setInterval(2000);
            //设置定位参数
            mLocationClient.setLocationOption(mLocationOption);

            mLocationClient.startLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
//        MyLogUtils.d("onLocationChanged()");
        if (aMapLocation == null) {
            return;
        }
        if (aMapLocation.getErrorCode() == 0) {
//            MyLogUtils.d("onLocationChanged() 位置获取成功");
            //定位成功回调信息，设置相关消息
            aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
            aMapLocation.getLatitude();//获取纬度
            aMapLocation.getLongitude();//获取经度

            MyVariables.curCoordinate = new LatLng(aMapLocation.getLatitude(),
                    aMapLocation.getLongitude());

            // 通过Mqtt发送坐标
            final Integer mUserID = (Integer) MMKVUtil.getInt(SPStringUtils.USER_ID, 0);
            // 判断用户是否登录
            if (mUserID != 0) {
                final Integer employId = (Integer) GlobalVariable.sPersonInfoBean.getData().getEmployeeId();
                final String msgStr = "{" +
                        "\"lat\":" + aMapLocation.getLatitude() + ""
                        + ", \"lon\":" + aMapLocation.getLongitude() + ""
                        + ", \"userId\":" + mUserID
                        + ", \"employeeId\":" + employId + ""
                        + "}";
                MqttService.getMqttConfig().pubMsg(UrlConfig.MQTT_SEND_LOCATION_TOPIC, msgStr, 0);
            }

            if (StorageConfig.sGPSLng == 0 && StorageConfig.sGPSLat == 0) {
                StorageConfig.sGPSLat = aMapLocation.getLatitude();
                StorageConfig.sGPSLng = aMapLocation.getLongitude();
            }
            aMapLocation.getAccuracy();//获取精度信息
            //城市
            String cityAddress = aMapLocation.getCity();
            MyVariables.CUR_LOCATION_CITY = cityAddress;
            String district = aMapLocation.getDistrict();
            if (cityAddress != null) {
                sendLocation(district);
            }
            MMKVUtil.putString(StorageConfig.GD_CITY, district);
//            mLocationClient.unRegisterLocationListener(this);
        } else {
//                SPUtils.put(this, GduConfig.GD_COUNTRY, getString(R.string
//                .Toast_myfragment_personal_settings_country_location_faile));
            //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
            MyLogUtils.d("解析位置信息出错: ErrCode:"
                    + aMapLocation.getErrorCode() + ", errInfo:"
                    + aMapLocation.getErrorInfo());

//                sendLocation("");
        }
    }

    private void sendLocation(String address) {
        EventBus.getDefault().post(new LocationEvent(address));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        GlobalVariable.mainActivityHadCreated = false;
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(this);
        }
        if (mMqttServiceIntent != null) {
            stopService(mMqttServiceIntent);
        }
//        MqttService.removeMqttListener(mMqttListener);

        //通过Mqtt发送退出app通知
        final Integer mUserID = (Integer) MMKVUtil.getInt(SPStringUtils.USER_ID, 0);
        //判断用户是否登录
        if (mUserID != 0) {
            final Integer employId = (Integer) GlobalVariable.sPersonInfoBean.getData().getEmployeeId();
            final String msgStr = "{" +
                    "\"employeeId\":" + employId + ""
                    + "}";
            MqttService.getMqttConfig().pubMsg(UrlConfig.MQTT_SEND_APP_OFF_LINE, msgStr, 0);
        }
    }

    /**
     * 根据类型获取案件列表
     */
    public void checkVersion() {
        MyLogUtils.d("checkVersion()");
        Map<String, String> map = new HashMap<>();
        map.put("project", "5");
        map.put("type", "1");
        map.put("lang", "cn");
        final String strEntity = new Gson().toJson(map);
        RequestBody body = RequestBody.create(strEntity, MediaType.parse("application/json;charset=UTF-8"));
        UpgradeService caseService = RetrofitClient.getAPIService(UpgradeService.class);
        String urlStr;
        if (BuildConfig.DEBUG) {
            urlStr = "http://120.24.12.64:8088" + HostUrl.APP_UPGRADE;
        } else {
            urlStr = "http://api.prodrone-tech.com" + HostUrl.APP_UPGRADE;
        }
        caseService.checkVersion(urlStr, body).subscribeOn(Schedulers.io())
                .to(RxLife.to(this))
                .subscribe(response -> {
                    final AppVersionBean callbackData = response;
                    if (callbackData == null || callbackData.getError() != 0 || callbackData.getData() == null) {
                        return;
                    }
                    final long curAppVersionCode = AppUtil.getVersionCode(MainActivity.this);
                    final int serverAppVersionCode = callbackData.getData().getVersion_code();
                    final String downloadUrlStr = callbackData.getData().getDownload_file();
                    if (serverAppVersionCode <= curAppVersionCode || CommonUtils.isEmptyString(downloadUrlStr)) {
                        return;
                    }
                    final String ignoreVersionStr = MMKVUtil.getString(MyConstants.IGONRE_VERSION);
                    if (!CommonUtils.isEmptyString(ignoreVersionStr)) {
                        return;
                    }
                    showUpgradeDialog(callbackData.getData(), false);
                }, throwable -> {
                    MyLogUtils.e("请求升级接口出错", throwable);
                });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshMessage(ReceiveChatEvent receiveChatEvent) {
        String topic = receiveChatEvent.getTopic();
        if (!TextUtils.isEmpty(topic)) {
            if (topic.contains(UrlConfig.MQTT_DUTY_NOTICE) || topic.contains(UrlConfig.MQTT_ALARM_NOTICE)) {
                //收到通知消息
                Log.e("yuhao", "onRefreshMessage: " + receiveChatEvent.getMessage());
                EventBus.getDefault().post(new NoticeMessageEvent(receiveChatEvent.getTopic(),
                        receiveChatEvent.getMessage()));
                if (receiveChatEvent.getTopic().contains(UrlConfig.MQTT_DUTY_NOTICE)) {
                    //收到通知消息
                    Log.e("yuhao", "onRefreshMessage: " + receiveChatEvent.getMessage());
                    NoticeMessageFragment.messageList.add(receiveChatEvent.getMessage());
                } else if (receiveChatEvent.getTopic().contains(UrlConfig.MQTT_ALARM_NOTICE)) {
                    Log.e("yuhao", "onRefreshMessage: " + receiveChatEvent.getMessage());
                    if (gson == null) {
                        gson = new Gson();
                    }
                    NoticeAlarmBean noticeAlarmBean = gson.fromJson(receiveChatEvent.getMessage(),
                            NoticeAlarmBean.class);
                    NoticeMessageFragment.messageList.add(noticeAlarmBean.getMsgVo().getMsgContent());
                }
                navView.getMenu().getItem(3).setIcon(R.drawable.message_new_bottom_selector);
            }
        }
    }

    /**
     * 检查更新
     */
    public void checkVersionNew() {
        MyLogUtils.d("checkVersionNew()");
        final long version = AppUtil.getVersionCode(MainActivity.this);
        UpgradeService caseService = RetrofitClient.getAPIService(UpgradeService.class);
        final String urlStr = UrlConfig.BASE_IP + ":8990/upgrade/patch/getDownloadUrl/2/" + version;
        caseService.getDownloadUrl(urlStr).subscribeOn(Schedulers.io())
                .to(RxLife.to(this))
                .subscribe(result -> {
                    final AppVersionBean callbackData = result;
                    if (callbackData == null || callbackData.getCode() != 0 || callbackData.getData() == null) {
                        return;
                    }
                    showUpgradeDialog(callbackData.getData(), true);
                }, throwable -> {
                    MyLogUtils.e("请求升级接口出错", throwable);
                }, () -> {
                });
    }

    private void showUpgradeDialog(AppVersionBean.DataBean dataBean, boolean isNewApi) {
        // 显示更新弹窗.
        AppUpdateDialog updateDialog = new AppUpdateDialog();
        updateDialog.setCancelable(false);
        updateDialog.show(isNewApi ? dataBean.getNewVersionName() : dataBean.getVersion(), isNewApi ?
                        dataBean.getRemark()
                        : dataBean.getVersion_log(),
                getSupportFragmentManager(),
                new IBtnClickListenerRecall() {
                    @Override
                    public void cancelBtnClickListener() {
                        final boolean isForceUpdate = isNewApi ? dataBean.isForceUpgrade() : dataBean.getFlag() == 1;
                        if (isForceUpdate) {
                            // 强制更新时的处理.
                            finish();
                        } else {
                            MMKVUtil.putString(MyConstants.IGONRE_VERSION,
                                    isNewApi ? dataBean.getNewVersionName() : dataBean.getVersion());
                        }
                        updateDialog.dismiss();
                    }

                    @Override
                    public void okBtnClickListener() {
                        if (isNewApi) {
                            newApiUpdateHandle(dataBean, updateDialog);
                        } else {
                            jumpWebDownloadApk(dataBean.getDownload_file());
                            updateDialog.dismiss();
                        }
                    }
                });
    }

    private void jumpWebDownloadApk(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(url);
        intent.setData(uri);
        startActivity(intent);
    }

    /**
     * 新接口确认升级处理
     *
     * @param dataBean
     * @param updateDialog
     */
    private void newApiUpdateHandle(AppVersionBean.DataBean dataBean, AppUpdateDialog updateDialog) {
        MyLogUtils.i("newApiUpdateHandle()");
        // 先下载差分文件
        final String externalCacheDir = getContext().getExternalCacheDir().getPath();
        final String fileSaveFolderPath = externalCacheDir + File.separator + "patch";
        DownloadPresenter mDownloadPresenter = new DownloadPresenter(MainActivity.this,
                MainActivity.this);
        mDownloadPresenter.downloadFile(dataBean.getPatchDownloadUrl(), fileSaveFolderPath,
                new MyDownloadListener() {

                    @Override
                    public void onStart() {
                        MyLogUtils.i("onStart()");
                    }

                    @Override
                    public void onProgress(int progress) {
//                        MyLogUtils.i("onProgress() progress = " + progress);
                    }

                    @Override
                    public void onFinish(String path) {
                        MyLogUtils.i("onFinish() path = " + path);
                        Observable.just(path)
                                .subscribeOn(Schedulers.computation())
                                .map(s -> {
                                    final String externalCacheDir = getContext().getExternalCacheDir().getPath();
                                    final String saveNewApkFolder = externalCacheDir + File.separator + "upgrade";
                                    final File saveNewApkFolderFile = new File(saveNewApkFolder);
                                    if (!saveNewApkFolderFile.exists()) {
                                        saveNewApkFolderFile.mkdirs();
                                    }
                                    final byte[] old = CommonUtils.readFileToByteArray(getApplicationInfo().sourceDir);
                                    final byte[] patch = CommonUtils.readFileToByteArray(s);
                                    final String saveNewApkPath = saveNewApkFolder + File.separator + "upgrade.apk";
                                    final FileOutputStream outOs = new FileOutputStream(saveNewApkPath);
                                    // 合并差分文件
                                    Patch.patch(old, patch, outOs);
                                    return saveNewApkPath;
                                })
                                .to(RxLife.to(MainActivity.this))
                                .subscribe(result -> {
                                    if (!CommonUtils.isEmptyString(result)) {
                                        updateDialog.dismiss();
                                        // 拆分文件合并成功开始安装Apk
                                        final File apkFile = new File(result);
                                        try {//这里有文件流的读写，需要处理一下异常
                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                            Uri uri;
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                //如果SDK版本 =24，即：Build.VERSION.SDK_INT  = 24
                                                final String packageName =
                                                        getContext().getApplicationContext().getPackageName();
                                                final String authority =
                                                        new StringBuilder(packageName).append(".provider").toString();
                                                uri = FileProvider.getUriForFile(getContext(), authority, apkFile);
                                                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                                            } else {
                                                uri = Uri.fromFile(apkFile);
                                                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                                            }
                                            getContext().startActivity(intent);
                                        } catch (Exception e) {
                                            updateDialog.dismiss();
                                            MyLogUtils.e("", e);
                                        }
                                    } else {
                                        // 拆分文件合并失败跳转网页下载完整Apk安装
                                        jumpWebDownloadApk(dataBean.getVersionDownloadUrl());
                                        updateDialog.dismiss();
                                    }
                                }, throwable -> {
                                    MyLogUtils.e("差分文件合并出错", throwable);
                                    // 拆分文件合并出错跳转网页下载完整Apk安装
                                    jumpWebDownloadApk(dataBean.getVersionDownloadUrl());
                                    updateDialog.dismiss();
                                }, () -> {

                                });
                    }

                    @Override
                    public void onFail(String errorInfo) {
                        MyLogUtils.i("onFail() errorInfo = " + errorInfo);
                        updateDialog.dismiss();
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventMsgHandler(EventMessage msg) {
        MyLogUtils.d("eventMsgHandler() msgType = " + msg.getMsgType());
        if (msg.getMsgType() == MyConstants.JUMP_TO_PERSON_FRAGMENT) {
//            NavHostFragment navHostFragment =
//                    (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_person);
//            NavHostFragment.findNavController(navHostFragment);

//            Navigation.createNavigateOnClickListener(R.id.navigation_person);

            NavController mNavController = Navigation.findNavController(this, R.id
                    .nav_host_fragment1);
            NavigationUI.setupWithNavController(navView, mNavController);
            mNavController.navigate(R.id.navigation_person);
        }
    }

    private long lastPressTime;

    @Override
    public void onBackPressed() {
        long currentPressTime = System.currentTimeMillis();
        if (lastPressTime != 0 && currentPressTime - lastPressTime < 2000) {
            lastPressTime = 0;
            System.exit(0);
            finish();
        } else {
            ToastUtil.s("再按一次退出App");
        }
        lastPressTime = currentPressTime;
    }
}