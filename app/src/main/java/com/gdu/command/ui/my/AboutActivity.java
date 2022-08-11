package com.gdu.command.ui.my;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.baselibrary.network.HostUrl;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.utils.AppUtil;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.BuildConfig;
import com.gdu.command.R;
import com.gdu.command.downloadfile.DownloadPresenter;
import com.gdu.command.downloadfile.MyDownloadListener;
import com.gdu.command.ui.main.MainActivity;
import com.gdu.command.ui.upgrade.AppUpdateDialog;
import com.gdu.command.ui.upgrade.AppVersionBean;
import com.gdu.command.ui.upgrade.IBtnClickListenerRecall;
import com.gdu.command.ui.upgrade.UpgradeService;
import com.gdu.model.config.UrlConfig;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.rxjava.rxlife.RxLife;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.sigpipe.jbsdiff.Patch;
import okhttp3.RequestBody;

public class AboutActivity extends BaseActivity {
    private ImageView backBtnTv;
    private TextView appVersionTv;
    private TextView checkUpdateTv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true).statusBarColor(
                R.color.color_224CD0).init();
        backBtnTv = findViewById(R.id.iv_back);
        appVersionTv = findViewById(R.id.tv_appVersion);
        checkUpdateTv = findViewById(R.id.tv_checkUpdateBtn);

        final String versionStr = AppUtil.getVersionName(this);
        if (!CommonUtils.isEmptyString(versionStr)) {
            appVersionTv.setText(versionStr);
        }

        backBtnTv.setOnClickListener(v -> finish());
        checkUpdateTv.setOnClickListener(v -> checkVersionNew());
    }

    /**
     *
     */
    public void checkVersion() {
        Map<String, String> map  = new HashMap<>();
        map.put("project", "5");
        map.put("type", "1");
        map.put("lang", "cn");
        final String strEntity = new Gson().toJson(map);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), strEntity);
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
            final String curAppVersionStr = AppUtil.getVersionName(AboutActivity.this);
            final int curAppVersion = Integer.valueOf(curAppVersionStr.replace(".", ""));
            final String serverAppVersionStr = callbackData.getData().getVersion();
            final int serverAppVersion = Integer.valueOf(serverAppVersionStr.replace(".", ""));
            final String downloadUrlStr = callbackData.getData().getDownload_file();
            if (serverAppVersion > curAppVersion && !CommonUtils.isEmptyString(downloadUrlStr)) {
                showUpgradeDialog(callbackData.getData(), false);
            }
        }, throwable -> {
            MyLogUtils.e("请求升级接口出错", throwable);
        }, () -> {

        });
    }

    /**
     * 检查更新
     */
    public void checkVersionNew() {
        MyLogUtils.d("checkVersionNew()");
        final long version = AppUtil.getVersionCode(getContext());
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
                }, () -> { });
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
        DownloadPresenter mDownloadPresenter = new DownloadPresenter(getContext(), this);
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
                                .to(RxLife.to(AboutActivity.this))
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
                                            } else {
                                                uri = Uri.fromFile(apkFile);
                                            }
                                            intent.setDataAndType(uri, "application/vnd.android.package-archive");
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

}