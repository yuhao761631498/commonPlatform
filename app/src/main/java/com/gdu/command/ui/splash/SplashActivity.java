package com.gdu.command.ui.splash;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.utils.AppUtil;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.MMKVUtil;
import com.gdu.baselibrary.utils.MyConstants;
import com.gdu.baselibrary.utils.SPStringUtils;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.R;
import com.gdu.command.permissions.PermissionHelp;
import com.gdu.command.ui.base.Base2Activity;
import com.gdu.command.ui.main.MainActivity;
import com.gdu.command.ui.my.LoginActivity;
import com.gdu.command.ui.setting.RemarkBean;
import com.gdu.command.ui.setting.ServerAuthBean;
import com.gdu.model.config.GlobalVariable;
import com.gdu.model.config.StorageConfig;
import com.gdu.model.config.UrlConfig;
import com.gdu.model.user.PersonInfoBean;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashActivity extends Base2Activity implements View.OnClickListener {
    private Context mContext;
    private PermissionHelp mPermissionHelp;
    private final byte PERSON_INFO_SUCCEED = 2;
    private final byte PERSON_INFO_FAILED = 3;

    private boolean isConnectPlan;//是否连接过飞机
    private boolean isPressedBack;
    private boolean customBoolean;
//    private ImageView iv_splash_bg;
    private RelativeLayout rl_guide_new;
    private TextView tv_current_page;
    private TextView tv_guide_title;
    private TextView tv_guide_content;
    private ImageView iv_guide_content;
    private TextView tv_guide_next;
    private TextView tv_guide_skip;
    private int num = 1;
    private TextView mVersionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true).statusBarColor(
                R.color.white).init();
        StorageConfig.isNormalBoot = true;
        super.onCreate(savedInstanceState);
        mContext = this;
        isPressedBack = false;
        final String saveAuthCodeStr = MMKVUtil.getString(MyConstants.SAVE_AUTH_CODE_CONTENT_KEY);
        if (!CommonUtils.isEmptyString(saveAuthCodeStr)) {
            final Gson mGson = new Gson();
            final ServerAuthBean mServerAuthBean = mGson.fromJson(saveAuthCodeStr, ServerAuthBean.class);
            UrlConfig.BASE_IP = mServerAuthBean.getHttpIp();
            UrlConfig.BASE_PORT = mServerAuthBean.getOrganizationPort() + "";
            UrlConfig.MQTT_IP = mServerAuthBean.getMqttHost();
            UrlConfig.MQTT_PORT = mServerAuthBean.getMqttPort() + "";
            final String remarkStr = mServerAuthBean.getRemark();
            if (!CommonUtils.isEmptyString(remarkStr)) {
                final RemarkBean mRemarkBean = mGson.fromJson(remarkStr, RemarkBean.class);
                UrlConfig.MQTT_USE_NAME = mRemarkBean.getMq_u();
                UrlConfig.MQTT_USE_PWD = mRemarkBean.getMq_p();
                UrlConfig.IMG_PORT = mRemarkBean.getImg_port() + "";
            }
        }
        mPermissionHelp = new PermissionHelp(mContext);
        mPermissionHelp.setAppName(getString(R.string.app_name));
        applyPermissions();
//        boolean isNotFirstBoot = SPUtils.getBoolean(this, SPUtils.IS_NOT_FIRST_BOOT);
    }

    private void init() {
        int waitTime = 2000;
        if (GlobalVariable.mainActivityHadCreated) {
            waitTime = 500;
        }
        handler.sendEmptyMessageDelayed(0, waitTime);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_splash;
    }

    private void applyPermissions() {
        mPermissionHelp.setCallback(new PermissionHelp.PermissionCallback() {
            @Override
            public void onSuccessful() {
                init();
            }

            @Override
            public void onFailure() {
                finish();
                System.exit(0);
            }
        });
//        RonLog.LogE("开始申请权限");
        mPermissionHelp.applyPermission();
//        RonLog.LogE("开始申请权限111111");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionHelp.REQUEST_CODE_SETTING) {
            applyPermissions();
        } else {
            System.exit(0);
        }
    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void findViews() {
        mVersionTextView = findViewById(R.id.version_textview);
//        iv_splash_bg = ((ImageView) findViewById(R.id.iv_splash_bg));
//        rl_guide_new = ((RelativeLayout) findViewById(R.id.rl_guide_new));
//        tv_current_page = ((TextView) findViewById(R.id.tv_current_page));
//        tv_guide_title = ((TextView) findViewById(R.id.tv_guide_title));
//        tv_guide_content = ((TextView) findViewById(R.id.tv_guide_content));
//        iv_guide_content = ((ImageView) findViewById(R.id.iv_guide_content));
//        tv_guide_next = ((TextView) findViewById(R.id.tv_guide_next));
//        tv_guide_skip = ((TextView) findViewById(R.id.tv_guide_skip));
    }

    @Override
    public void initViews() {
        mVersionTextView.setText(AppUtil.getVersionName(this));
    }

    @Override
    public void initLisenter() {
//        tv_guide_next.setOnClickListener(this);
//        tv_guide_skip.setOnClickListener(this);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    goToDeviceFragment();
                    break;
                case 1:
                    SplashActivity.this.finish();
                    break;
                case PERSON_INFO_SUCCEED:
                    startActivity(new Intent(mContext, MainActivity.class));
                    finish();
                    break;
                case PERSON_INFO_FAILED:
                    gotoLoginActivity();
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    private void gotoLoginActivity() {
        if (!isPressedBack) {
            Intent intentMain = new Intent(SplashActivity.this, LoginActivity.class);
            intentMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intentMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentMain);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPermissionHelp.onDestroy();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isPressedBack = true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.tv_guide_skip:  //跳过
//                goToDeviceFragment();
//                break;
        }
    }

    private void goToDeviceFragment() {
        MyLogUtils.d("goToDeviceFragment()");
        if (!"NULL".equals(MMKVUtil.getString("token","NULL"))) {
            getPersonInfo();
        } else {
            gotoLoginActivity();
            finish();
        }
    }

    private void getPersonInfo() {
        MyLogUtils.d("getPersonInfo()");

        LoginService mLoginService = RetrofitClient.getAPIService(LoginService.class);
        mLoginService.getPersonInfo().enqueue(new Callback<PersonInfoBean>() {
            @Override
            public void onResponse(Call<PersonInfoBean> call, Response<PersonInfoBean> response) {
                MyLogUtils.d("onResponse()");
                final PersonInfoBean personInfoBean = response.body();
                final boolean isEmptyData = personInfoBean == null || personInfoBean.getCode() != 0 || personInfoBean.getData() == null;
                if (isEmptyData) {
                    handler.sendEmptyMessage(PERSON_INFO_FAILED);
                    return;
                }
                GlobalVariable.sPersonInfoBean = personInfoBean;

                MMKVUtil.putString(SPStringUtils.DEPT_NAME, personInfoBean.getData().getDeptName());
                MMKVUtil.putString(SPStringUtils.USER_NAME, personInfoBean.getData().getUseraccount());
                MMKVUtil.putInt(SPStringUtils.USER_ID, personInfoBean.getData().getId());

                handler.sendEmptyMessage(PERSON_INFO_SUCCEED);
            }

            @Override
            public void onFailure(Call<PersonInfoBean> call, Throwable e) {
                MyLogUtils.e("获取用户信息接口失败", e);
                handler.sendEmptyMessage(PERSON_INFO_FAILED);
            }
        });
    }
}




