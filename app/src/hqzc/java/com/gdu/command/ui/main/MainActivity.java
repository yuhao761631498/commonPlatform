package com.gdu.command.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.gdu.baselibrary.utils.MMKVUtil;
import com.gdu.baselibrary.utils.MyConstants;
import com.gdu.baselibrary.utils.SPStringUtils;
import com.gdu.command.R;
import com.gdu.command.event.LocationEvent;
import com.gdu.command.ui.upgrade.AppUpdateDialog;
import com.gdu.command.ui.upgrade.AppVersionBean;
import com.gdu.command.ui.upgrade.IBtnClickListenerRecall;
import com.gdu.model.config.GlobalVariable;
import com.gdu.model.config.StorageConfig;
import com.gdu.model.config.UrlConfig;
import com.gdu.mqttchat.MqttService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rxjava.rxlife.RxLife;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;

public class MainActivity extends AppCompatActivity implements AMapLocationListener {

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;

    private Intent mMqttServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_calendar);
        this.getSupportActionBar().hide();
        initData();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_organization, R.id.navigation_message, R.id.navigation_person)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
//        checkVersion();
    }

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
                }, throwable -> { }, () -> { });
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
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                aMapLocation.getLatitude();//获取纬度
                aMapLocation.getLongitude();//获取经度

                // 通过Mqtt发送坐标
                final Integer mUserID = (Integer) MMKVUtil.getInt(SPStringUtils.USER_ID, 0);
                final String msgStr = "{" +
                        "\"lat\":" + aMapLocation.getLatitude() + ""
                        + ", \"lon\":" + aMapLocation.getLongitude() + ""
                        + ", \"userId\":" + mUserID
                        + ", \"employeeId\":" + mUserID
                        + "}";
                MqttService.getMqttConfig().pubMsg(UrlConfig.MQTT_SEND_LOCATION_TOPIC, msgStr, 0);

                if (StorageConfig.sGPSLng == 0 && StorageConfig.sGPSLat == 0) {
                    StorageConfig.sGPSLat = aMapLocation.getLatitude();
                    StorageConfig.sGPSLng = aMapLocation.getLongitude();
                }
                aMapLocation.getAccuracy();//获取精度信息
                //城市
                String address = aMapLocation.getCity();//获取地址
                String district = aMapLocation.getDistrict();
                if (address != null) {
                    sendLocation(district);
                }
                MMKVUtil.putString(StorageConfig.GD_CITY, district);
                mLocationClient.unRegisterLocationListener(this);
                //---------------------------------------
                //----------------------------------------
            } else {
//                SPUtils.put(this, GduConfig.GD_COUNTRY, getString(R.string.Toast_myfragment_personal_settings_country_location_faile));
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());

//                sendLocation("");
            }
        }
    }

    private void sendLocation(String address) {
        EventBus.getDefault().post(new LocationEvent(address));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlobalVariable.mainActivityHadCreated = false;
        if (mMqttServiceIntent != null) {
            stopService(mMqttServiceIntent);
        }
    }

//    /**
//     * 根据类型获取案件列表
//     */
//    public void checkVersion() {
//        MyLogUtils.d("checkVersion()");
//        Map<String, String> map  = new HashMap<>();
//        map.put("project", "5");
//        map.put("type", "1");
//        map.put("lang", "cn");
//        final String strEntity = new Gson().toJson(map);
//        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
//        UpgradeService caseService = RetrofitClient.getAPIService(UpgradeService.class);
//        String urlStr;
//        if (BuildConfig.DEBUG) {
//            urlStr = "http://120.24.12.64:8088" + HostUrl.APP_UPGRADE;
//        } else {
//            urlStr = "http://api.prodrone-tech.com" + HostUrl.APP_UPGRADE;
//        }
//        caseService.checkVersion(urlStr, body).enqueue(new Callback<AppVersionBean>() {
//            @Override
//            public void onResponse(Call<AppVersionBean> call, Response<AppVersionBean> response) {
//                final AppVersionBean callbackData = response.body();
//                if (callbackData == null || callbackData.getError() != 0 || callbackData.getData() == null) {
//                    return;
//                }
//                final String curAppVersionStr = CommonUtils.getVersionCode(MainActivity.this);
//                final int curAppVersion = Integer.valueOf(curAppVersionStr.replace(".", ""));
//                final String serverAppVersionStr = callbackData.getData().getVersion();
//                final int serverAppVersion = Integer.valueOf(serverAppVersionStr.replace(".", ""));
//                final String downloadUrlStr = callbackData.getData().getDownload_file();
//                if (serverAppVersion > curAppVersion && !CommonUtils.isEmptyString(downloadUrlStr)) {
//                    final String ignoreVersionStr = MMKVUtil.getString(
//                            MyConstants.IGONRE_VERSION);
//                    if (CommonUtils.isEmptyString(ignoreVersionStr)
//                            || !serverAppVersionStr.equals(ignoreVersionStr)) {
//                        showUpgradeDialog(callbackData.getData());
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<AppVersionBean> call, Throwable e) {
//                MyLogUtils.e("请求升级接口失败", e);
//            }
//        });
//    }

    private void showUpgradeDialog(AppVersionBean.DataBean dataBean) {
        // 显示更新弹窗.
        AppUpdateDialog updateDialog = new AppUpdateDialog();
        updateDialog.setCancelable(false);
        updateDialog.show(dataBean.getVersion(), dataBean.getVersion_log(),
                getSupportFragmentManager(),
                new IBtnClickListenerRecall() {
                    @Override
                    public void cancelBtnClickListener() {
                        if (dataBean.getFlag() == 1) {
                            // 强制更新时的处理.
                            finish();
                        } else {
                            MMKVUtil.putString(MyConstants.IGONRE_VERSION,
                                    dataBean.getVersion());
                        }

                        updateDialog.dismiss();
                    }

                    @Override
                    public void okBtnClickListener() {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        Uri uri = Uri.parse(dataBean.getDownload_file());
                        intent.setData(uri);
                        startActivity(intent);
                        updateDialog.dismiss();
                    }
                });
    }
}