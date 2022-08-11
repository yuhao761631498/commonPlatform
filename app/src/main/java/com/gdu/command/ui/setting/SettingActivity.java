package com.gdu.command.ui.setting;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.baselibrary.utils.ActivityManager;
import com.gdu.baselibrary.utils.FileUtil;
import com.gdu.baselibrary.utils.MMKVUtil;
import com.gdu.baselibrary.utils.SPStringUtils;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.command.R;
import com.gdu.command.ui.my.LoginActivity;
import com.gdu.model.config.GlobalVariable;
import com.gdu.model.config.UrlConfig;
import com.gdu.mqttchat.MqttService;
import com.gyf.immersionbar.ImmersionBar;
import com.rxjava.rxlife.RxLife;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;

/**
 * 系统设置界面
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout mUpdatePasswordLayout;
    private LinearLayout mClearCacheLayout;
    private TextView     mLogoutTextView;
    private ImageView mBackImageView;
    private TextView cache_textview;
    private ConstraintLayout cl_authCodeLayout;
    private ActivityResultLauncher<Intent> changePwdLauncher;
    private ActivityResultLauncher<Intent> setAuthCodeLauncher;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true).statusBarColor(R.color.color_224CD0).init();
        initView();

        //当前缓存大小
        cache_textview.setText(FileUtil.getTotalCacheSize(this));

        //退出登录发出通知
        Observable.just("").delay(2, TimeUnit.SECONDS)
                .to(RxLife.to(this))
                .subscribe(s -> {
                    MqttService.subTopic(UrlConfig.MQTT_SEND_APP_OFF_LINE);
                }, throwable -> { }, () -> { });
    }

    private void initView() {
        mUpdatePasswordLayout = findViewById(R.id.update_password_layout);
        mClearCacheLayout = findViewById(R.id.clear_cache_layout);
        mLogoutTextView = findViewById(R.id.tv_login_out);
        mBackImageView = findViewById(R.id.iv_back);
        cache_textview = findViewById(R.id.cache_textview);
        cl_authCodeLayout = findViewById(R.id.cl_authCodeLayout);

        mBackImageView.setOnClickListener(this);
        mLogoutTextView.setOnClickListener(this);
        mClearCacheLayout.setOnClickListener(this);
        mUpdatePasswordLayout.setOnClickListener(this);
        cl_authCodeLayout.setOnClickListener(this);

        changePwdLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() != RESULT_OK) {
                return;
            }
            logoutHandle();
        });
        setAuthCodeLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() != RESULT_OK) {
                return;
            }
            logoutHandle();
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.update_password_layout:
                intent = new Intent(this, UpdatePassWordActivity.class);
                changePwdLauncher.launch(intent);
//                startActivity(intent);
                break;

            case R.id.clear_cache_layout: //清理缓存
                if (!cache_textview.getText().toString().startsWith("0.0")) {
                    showProgressDialog("清理缓存中...");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hideProgressDialog();
                            FileUtil.clearAllCache(SettingActivity.this);
                            cache_textview.setText(FileUtil.getTotalCacheSize(SettingActivity.this));
                        }
                    }, 1500);
                } else {
                    ToastUtil.s("没有缓存可清理");
                }
                break;

            case R.id.cl_authCodeLayout:
                intent = new Intent(this, SetAuthCodeActivity.class);
                setAuthCodeLauncher.launch(intent);
                break;

            // 通过Mqtt发送退出app通知
            case R.id.tv_login_out:
                logoutHandle();
                break;

            case R.id.iv_back:
                finish();
                break;

            default:
                break;
        }
    }

    private void logoutHandle() {
        final Integer mUserID = (Integer) MMKVUtil.getInt(SPStringUtils.USER_ID, 0);
        //判断用户是否登录
        if (mUserID != 0) {
            final Integer employId = (Integer) GlobalVariable.sPersonInfoBean.getData().getEmployeeId();
            final String msgStr = "{" +
                    "\"employeeId\":" + employId + ""
                    + "}";
            MqttService.getMqttConfig().pubMsg(UrlConfig.MQTT_SEND_APP_OFF_LINE, msgStr, 0);
        }

        MMKVUtil.putString(SPStringUtils.TOKEN, "NULL");
        Intent intent1 = new Intent(this, LoginActivity.class);
        startActivity(intent1);
        ActivityManager.getInstance().finishAll();
    }
}
