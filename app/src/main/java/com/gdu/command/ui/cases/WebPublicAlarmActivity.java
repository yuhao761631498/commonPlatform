package com.gdu.command.ui.cases;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.gdu.baselibrary.network.BaseBean;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.command.R;
import com.gdu.command.ui.duty.base.activity.BaseActivity;
import com.gdu.model.config.UrlConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Copyright (C), 2020-2030, 武汉中旗生物医疗电子有限公司
 * <p>
 * 功能描述:
 * <p>
 * 创建时间: 2022/6/10 13:18
 *
 * @author yuhao
 */
public class WebPublicAlarmActivity extends BaseActivity {
    private WebView web_public;
    private ValueCallback<Uri[]> filePathCallbacks;

    //http://218.200.69.104:8090/webRs/mp/report

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web_public_alarm;
    }

    @Override
    protected void initView() {
        web_public = ((WebView) findViewById(R.id.wb_public));
    }

    @Override
    protected void initData() {
        getLoadUrl();
    }

    private void loadWeb(String url) {
//        web_public.loadUrl(UrlConfig.BASE_IP + ":8002/webRs/mp/report");
        web_public.loadUrl(url + "/webRs/mp/report");

        WebSettings webSettings = web_public.getSettings();
        //设置开启javascript支持
        webSettings.setJavaScriptEnabled(true);
        //设置支持缩放
        webSettings.setSupportZoom(false);
        //开启缩放工具（会出现放大缩小的按钮）
        webSettings.setBuiltInZoomControls(false);
        //WebView两种缓存（网页、H5）方式，此处网页不缓存
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //允许JS打开新窗口（默认false）
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //打开本地缓存供JS调用
        webSettings.setDomStorageEnabled(true);
        //H5缓存内存大小（已过时，不必设置已可自动管理）
        //webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        //H5缓存路径
        String absolutePath = getApplicationContext().getCacheDir().getAbsolutePath();
        //H5缓存大小
        webSettings.setAppCachePath(absolutePath);
        //是否允许WebView访问内部文件（默认true）
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setGeolocationEnabled(true);

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //支持存储H5缓存
        webSettings.setAppCacheEnabled(true);
        //启动概述模式浏览界面，当页面宽度超过WebView显示宽度时，缩小页面适应WebView(默认false)
        webSettings.setLoadWithOverviewMode(true);

        web_public.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fileChooserParams) {
                filePathCallbacks = filePathCallback;
                Intent intent = fileChooserParams.createIntent();
                if (fileChooserParams != null) {
                    if (fileChooserParams.isCaptureEnabled()) {
                        //todo 打开相机
                        return true;
                    }
                    //打开图库或者其他文件选择
                    String[] mimeTypes = fileChooserParams.getAcceptTypes();
                    if (mimeTypes != null && mimeTypes.length > 0) {
                        intent.setType("*/*");
                    } else {
                        intent.setType(mimeTypes[0]);
                    }
                } else {
                    intent.setType("*/*");
                }

                startActivityForResult(intent, 100);
                return true;
            }
        });
    }

    private void getLoadUrl() {
        PublicLoadUrlService apiService = RetrofitClient.getAPIService(PublicLoadUrlService.class);
        apiService.getWebLoadUrl().enqueue(new Callback<BaseBean<String>>() {
            @Override
            public void onResponse(Call<BaseBean<String>> call, Response<BaseBean<String>> response) {
                BaseBean<String> baseBean = response.body();
                if (baseBean != null && baseBean.data != null && baseBean.code == 0) {
                    String url = baseBean.data;
                    runOnUiThread(() -> loadWeb(url));
                } else {
                    runOnUiThread(() -> Toast.makeText(WebPublicAlarmActivity.this, "获取url失败", Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onFailure(Call<BaseBean<String>> call, Throwable t) {
                runOnUiThread(() -> Toast.makeText(WebPublicAlarmActivity.this, "获取url失败", Toast.LENGTH_SHORT).show());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 100:
                    if (null != filePathCallbacks) {
                        Uri result = data == null || resultCode != Activity.RESULT_OK ? null
                                : data.getData();
                        filePathCallbacks.onReceiveValue(new Uri[]{result});
                        filePathCallbacks = null;
                    }
                    break;
                default:
                    break;
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            if (null != filePathCallbacks) {
                filePathCallbacks.onReceiveValue(null);
                filePathCallbacks = null;
            }
        }
    }
}
