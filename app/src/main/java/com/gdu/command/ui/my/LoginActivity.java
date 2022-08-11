package com.gdu.command.ui.my;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.network.RetrofitClientUnToken;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.MMKVUtil;
import com.gdu.baselibrary.utils.SPStringUtils;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.R;
import com.gdu.command.ui.base.dialog.LoadingDialog;
import com.gdu.command.ui.main.MainActivity;
import com.gdu.command.ui.setting.SetAuthCodeActivity;
import com.gdu.command.ui.splash.LoginService;
import com.gdu.model.config.GlobalVariable;
import com.gdu.model.config.StorageConfig;
import com.gdu.model.user.LoginBean;
import com.gdu.model.user.PersonInfoBean;
import com.gdu.util.MD5Util;
import com.gyf.immersionbar.ImmersionBar;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_police_code;
    private EditText et_password;
    private TextView tv_login;
    private ImageView mSeePasswordImageView;
    private RelativeLayout titleLayoutRl;
    private RelativeLayout mEnvironmentLayout;
    private TextView mTv_CurEnvironment;
    private ImageView iv_settingBtn;

    private LoginBean loginBean;
    private PersonInfoBean personInfoBean;
    private final byte FAILE = 0;
    private final byte Success = 1;
    private final byte PERSON_INFO_SUCCEED = 2;
    private final byte PERSON_INFO_FAILED = 3;
    private boolean isRepeatLogin;
    private boolean isTest;

    private ActivityResultLauncher<Intent> setAuthCodeLauncher;
    private TextView tv_forget_psw;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, LoginActivity.class);
//	    starter.putExtra( );
        context.startActivity(starter);
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true).statusBarColor(
                R.color.color_224CD0).init();
        initView();
        initClickListener();
    }

    private void initClickListener() {
        tv_login.setOnClickListener(this);
        mSeePasswordImageView.setOnClickListener(this);
        titleLayoutRl.setOnClickListener(this);
        mEnvironmentLayout.setOnClickListener(this);
        mTv_CurEnvironment.setOnClickListener(this);
        iv_settingBtn.setOnClickListener(this);

        setAuthCodeLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {

                });

        tv_forget_psw.setOnClickListener(this);
    }

    private void initView() {
        et_police_code = ((EditText) findViewById(R.id.et_police_code));
        et_password = findViewById(R.id.et_password);
        tv_login = ((TextView) findViewById(R.id.tv_login));
        mSeePasswordImageView = findViewById(R.id.see_password_imageview);
        titleLayoutRl = findViewById(R.id.title_layout);
        mEnvironmentLayout = findViewById(R.id.rl_environment_layout);
        mTv_CurEnvironment = findViewById(R.id.tv_cur_environment);
        iv_settingBtn = findViewById(R.id.iv_settingBtn);
        isTest = MMKVUtil.getBoolean(StorageConfig.TestServerMode, false);
        mTv_CurEnvironment.setText(isTest ? "测试服务器环境" : "正式服务器环境");
        String userName = (String) MMKVUtil.getString(SPStringUtils.USER_NAME, "");
        String userPassword = MMKVUtil.getString(SPStringUtils.USER_PASSWORD, "");
        if (!(TextUtils.isEmpty(userName) && TextUtils.isEmpty(userPassword))) {
            et_police_code.setText(userName);
            et_password.setText(userPassword);
        }

        tv_forget_psw = ((TextView) findViewById(R.id.tv_forget_psw));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                isRepeatLogin = false;
                login();
                break;

            case R.id.see_password_imageview:
                if (et_password.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;

            case R.id.title_layout:
//                testClickNum++;
//                if (testClickNum % 5 == 0) {
//                    mEnvironmentLayout.setVisibility(View.VISIBLE);
//                }
                break;

            case R.id.tv_cur_environment:
                isTest = !isTest;
                mTv_CurEnvironment.setText(isTest ? "测试服务器环境" : "正式服务器环境");
                MMKVUtil.putBoolean(StorageConfig.TestServerMode, isTest);
//                ToolManager.SwitchAppEnvironment(isTest);
                break;

            case R.id.iv_settingBtn:
                Intent mIntent = new Intent(this, SetAuthCodeActivity.class);
                setAuthCodeLauncher.launch(mIntent);
                break;

            case R.id.tv_forget_psw:  //忘记密码
                Intent resetIntent = new Intent(this, ResetPswActivity.class);
                startActivity(resetIntent);
                break;

            default:
                break;
        }
    }

    private void login() {
        String policeCode = et_police_code.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(policeCode)) {
            Toast.makeText(this, R.string.please_input_code_toast, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, R.string.please_input_password_toast, Toast.LENGTH_SHORT).show();
            return;
        }
        sendPost(policeCode, password);
    }

    private void sendPost(final String username, String rawpassword) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(LoginActivity.this);
        }
        loadingDialog.setCancelable(false);
        loadingDialog.setMessage(getString(R.string.logining));
        loadingDialog.show();

        final String password = MD5Util.ToMD5NOKey(rawpassword);
        final Map<String, String> paramMap = new HashMap<>();
        paramMap.put("password", password);
        paramMap.put("username", username);
        LoginService mLoginService = RetrofitClientUnToken.getAPIService(LoginService.class);
        mLoginService.login(paramMap).enqueue(new Callback<LoginBean>() {
            @Override
            public void onResponse(Call<LoginBean> call, Response<LoginBean> response) {
                MyLogUtils.d("onResponse()");
                final LoginBean loginBean = response.body();
                final boolean isEmptyData =
                        loginBean == null || loginBean.getCode() != 0 || loginBean.getData() == null;
                final Message mMessage = new Message();
                if (isEmptyData) {
                    mMessage.what = FAILE;
                    if (loginBean != null) {
                        mMessage.obj = loginBean.getMsg();
                    }
                    handler.sendMessage(mMessage);
                    return;
                }
                MMKVUtil.putString(SPStringUtils.TOKEN, loginBean.getData().getAccess_token());
                MMKVUtil.putString(SPStringUtils.USER_NAME, username);
                MMKVUtil.putString(SPStringUtils.USER_PASSWORD, rawpassword);
                mMessage.what = Success;
                handler.sendMessageDelayed(mMessage, 200);
            }

            @Override
            public void onFailure(Call<LoginBean> call, Throwable e) {
                MyLogUtils.e("获取用户信息接口失败", e);
                final Message mMessage = new Message();
                mMessage.what = FAILE;
                mMessage.obj = "登录失败";
                handler.sendMessage(mMessage);
            }
        });
    }

    private LoadingDialog loadingDialog;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case Success:
                    getPersonInfo();
                    break;
                case FAILE:
                    if (!isRepeatLogin) {
                        isRepeatLogin = true;
                        login();
                    } else {
                        String tipMsg = (String) message.obj;
                        if (CommonUtils.isEmptyString(tipMsg)) {
                            tipMsg = "登录失败";
                        }
                        showFailToast(tipMsg);
                    }
                    break;
                case PERSON_INFO_SUCCEED:
                    if (!isFinishing()) {
                        loadingDialog.cancel();
                        MMKVUtil.putString(SPStringUtils.DEPT_NAME, personInfoBean.getData().getDeptName());
                        MMKVUtil.putString(SPStringUtils.USER_NAME, personInfoBean.getData().getUseraccount());
                        MMKVUtil.putInt(SPStringUtils.USER_ID, personInfoBean.getData().getId());
                        startActivity(new Intent(mContext, MainActivity.class));
                        finish();
                    }
                    break;
                case PERSON_INFO_FAILED:
                    showFailToast((String) message.obj);
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    private void showFailToast(String msg){
//        String toast = "登录失败";
//        switch (type){
//            case 1037:
//                toast = "账号已过期";
//                break;
//            default:
//                break;
//        }
        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
        loadingDialog.cancel();
    }

    private void getPersonInfo() {
        MyLogUtils.d("getPersonInfo()");
        LoginService mLoginService = RetrofitClient.getAPIService(LoginService.class);
        mLoginService.getPersonInfo().enqueue(new Callback<PersonInfoBean>() {
            @Override
            public void onResponse(Call<PersonInfoBean> call, Response<PersonInfoBean> response) {
                MyLogUtils.d("onResponse()");
                personInfoBean = response.body();
                final boolean isEmptyData = personInfoBean == null || personInfoBean.getCode() != 0 || personInfoBean.getData() == null;
                if (isEmptyData) {
                    final Message msg = new Message();
                    msg.what = PERSON_INFO_FAILED;
                    msg.obj = personInfoBean.getMsg();
                    handler.sendMessage(msg);
                    return;
                }
                GlobalVariable.sPersonInfoBean = personInfoBean;
                handler.sendEmptyMessage(PERSON_INFO_SUCCEED);
            }

            @Override
            public void onFailure(Call<PersonInfoBean> call, Throwable e) {
                MyLogUtils.e("获取用户信息接口失败", e);
                final Message msg = new Message();
                msg.what = PERSON_INFO_FAILED;
                msg.obj = "获取用户信息出错";
                handler.sendMessage(msg);
            }
        });
    }
}