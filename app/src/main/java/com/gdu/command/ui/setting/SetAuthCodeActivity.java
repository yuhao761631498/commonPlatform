package com.gdu.command.ui.setting;

import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.baselibrary.network.HostUrl;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.network.RetrofitClientUnToken;
import com.gdu.baselibrary.network.RetrofitDownloadClient;
import com.gdu.baselibrary.network.RetrofitUploadClient;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.MMKVUtil;
import com.gdu.baselibrary.utils.MyConstants;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.R;
import com.gdu.model.config.UrlConfig;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 设置授权码界面
 * @author wixche
 */
public class SetAuthCodeActivity extends BaseActivity implements View.OnClickListener {
    private ImageView backIv, iv_confirmBtn;
    private EditText authCodeEt;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_authcode;
    }

    private Handler mHandler = new Handler(msg -> {
        switch (msg.what) {
            case MyConstants.SET_AUTH_CODE_FAIL:
                ToastUtil.s("设置授权码失败");
                break;

            case MyConstants.SET_AUTH_CODE_SUC:
                RetrofitClient.switchRetrofit();
                RetrofitClientUnToken.switchRetrofit();
                RetrofitDownloadClient.switchRetrofit();
                RetrofitUploadClient.switchRetrofit();
                ToastUtil.s("设置成功");
                setResult(RESULT_OK);
                finish();
                break;

            default:
                break;
        }
       return true;
    });

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true).statusBarColor(
                R.color.color_224CD0).init();

        initView();
    }

    private void initView() {
        backIv = findViewById(R.id.iv_back);
        iv_confirmBtn = findViewById(R.id.iv_confirmBtn);
        authCodeEt = findViewById(R.id.et_authCode);

        final String oldCode = MMKVUtil.getString(MyConstants.SAVE_AUTH_CODE_KEY);
        if (!CommonUtils.isEmptyString(oldCode)) {
            authCodeEt.setText(oldCode);
        }

        backIv.setOnClickListener(this);
        iv_confirmBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_confirmBtn:
                final String codeStr = authCodeEt.getText().toString();
                if (CommonUtils.isEmptyString(codeStr)) {
                    ToastUtil.s("请输入授权码");
                    return;
                }
                setAuthCode(codeStr);
                break;

            case R.id.iv_back:
                finish();
                break;

            default:
                break;
        }
    }

    private void setAuthCode(String code){
        final Map<String, String> params = new HashMap<>();
        params.put("token", code);
        final String url = "http://appconfig.gdu-tech.com/resauth/config/get_config/00003";
        final SettingsService settingService = RetrofitClient.getAPIService(SettingsService.class);
        settingService.setServerAuthCode(url, params).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String responseStr = response.body().string();
                    if (responseStr.startsWith("[") && responseStr.endsWith("]")) {
                        responseStr = responseStr.substring(1, responseStr.length() - 1);
                        final Gson mGson = new Gson();
                        final ServerAuthBean bean = mGson.fromJson(responseStr, ServerAuthBean.class);
                        if (bean == null) {
                            mHandler.sendEmptyMessage(MyConstants.SET_AUTH_CODE_FAIL);
                            return;
                        }

                        MMKVUtil.putString(MyConstants.SAVE_AUTH_CODE_KEY, code);
                        MMKVUtil.putString(MyConstants.SAVE_AUTH_CODE_CONTENT_KEY, responseStr);

                        UrlConfig.BASE_IP = bean.getHttpIp();
                        UrlConfig.BASE_PORT = bean.getOrganizationPort() + "";
                        UrlConfig.MQTT_IP = bean.getMqttHost();
                        UrlConfig.MQTT_PORT = bean.getMqttPort() + "";
                        final String remarkStr = bean.getRemark();
                        if (!CommonUtils.isEmptyString(remarkStr)) {
                            final RemarkBean mRemarkBean = mGson.fromJson(remarkStr, RemarkBean.class);
                            UrlConfig.MQTT_USE_NAME = mRemarkBean.getMq_u();
                            UrlConfig.MQTT_USE_PWD = mRemarkBean.getMq_p();
                            UrlConfig.IMG_PORT = mRemarkBean.getImg_port() + "";
                        }
                        UrlConfig.resetIpAndPort();

                        mHandler.sendEmptyMessage(MyConstants.SET_AUTH_CODE_SUC);
                    }
                } catch (Exception e) {
                    MyLogUtils.e("设置授权码出错", e);
                    mHandler.sendEmptyMessage(MyConstants.SET_AUTH_CODE_FAIL);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                MyLogUtils.e("设置授权码出错", t);
                mHandler.sendEmptyMessage(MyConstants.SET_AUTH_CODE_FAIL);
            }
        });
    }
}
