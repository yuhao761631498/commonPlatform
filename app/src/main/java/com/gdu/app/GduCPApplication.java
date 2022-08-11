package com.gdu.app;

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.gdu.baselibrary.BaseApplication;
import com.gdu.baselibrary.BuildConfig;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.MyConstants;
import com.gdu.baselibrary.utils.imageloade.GlideLoader;
import com.gdu.baselibrary.utils.imageloade.ImageLoaderStrategy;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.baselibrary.utils.watermark.WaterMarkInfo;
import com.gdu.baselibrary.utils.watermark.WaterMarkManager;
import com.gdu.utils.MyUiUtils;
import com.gdu.utils.eventbus.EventBusHelper;
import com.tencent.bugly.crashreport.CrashReport;

import io.reactivex.rxjava3.plugins.RxJavaPlugins;

public class GduCPApplication extends BaseApplication {
    private final String TAG = "GduCPApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        /** 检测当前进程名称是否为应用包名，否则return （像百度地图等sdk需要在单独的进程中执行，会多次执行Application的onCreate()
         * 方法，所以为了只初始化一次应用配置，作此判断）*/
        if (!CommonUtils.getProcessName(this).equals(getPackageName())) {
            return;
        }
        instance = this;
//        GduCPInit.init(this);

        // 腾讯Bugly初始化
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
        strategy.setUploadProcess(true);
        CrashReport.initCrashReport(getApplicationContext(), MyConstants.BUGLY_APPID, BuildConfig.DEBUG);
        // Logger初始化
        MyLogUtils.init(com.gdu.command.BuildConfig.DEBUG);
        // EventBus工具类初始化.
        EventBusHelper.init();
        // 图片加载框架初始化.
        ImageLoaderStrategy.getInstance().setImageLoader(new GlideLoader());
        setRxJavaErrorHandler();

        WaterMarkManager.setInfo(
                WaterMarkInfo.create()
                        .setDegrees(-45)
                        .setTextSize(MyUiUtils.dip2px(16))
                        .setTextColor(Color.parseColor("#4CD1D1D1"))
                        .setTextBold(true)
                        .setDx(MyUiUtils.dip2px(60))
                        .setDy(MyUiUtils.dip2px(100))
                        .setAlign(Paint.Align.CENTER)
                        .generate());
    }

    private void setRxJavaErrorHandler() {
        RxJavaPlugins.setErrorHandler(it -> Log.e(TAG, "捕获RxJava取消后的异常", it));
    }

}
