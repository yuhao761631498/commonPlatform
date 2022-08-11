package com.gdu.command.ui.setting;

import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.baselibrary.network.BaseBean;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.MMKVUtil;
import com.gdu.baselibrary.utils.RegularUtil;
import com.gdu.baselibrary.utils.SPStringUtils;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.command.R;
import com.gdu.command.ui.my.PersonService;
import com.gdu.util.MD5Util;
import com.gyf.immersionbar.ImmersionBar;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 系统设置界面
 */
public class UpdatePassWordActivity extends BaseActivity implements View.OnClickListener {

    private final int UPDATE_PASSWORD_SUCCEED = 1;
    private final int UPDATE_PASSWORD_FAILED = 2;

    private ImageView mBackImageView;
    private ImageView mConfirmImageView;

    private EditText mOldPasswordEditText;
    private EditText mNewPasswordEditText;
    private EditText mConfirmPasswordEditText;
    private ImageView iv_hideOrShowPwd1, iv_hideOrShowPwd2;

    private Handler mHandler;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_password;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true).statusBarColor(
                R.color.color_224CD0).init();

        initView();
        initHandler();
    }

    private void initHandler() {
        mHandler = new Handler(msg -> {
            switch (msg.what) {
                case UPDATE_PASSWORD_SUCCEED:
                    mOldPasswordEditText.setText("");
                    mNewPasswordEditText.setText("");
                    mConfirmPasswordEditText.setText("");
                    ToastUtil.s("修改密码成功");
                    setResult(RESULT_OK);
                    finish();
                    break;

                case UPDATE_PASSWORD_FAILED:
                    ToastUtil.s("修改密码失败");
                    break;

                default:
                    break;
            }
            return true;
        });
    }

    private void initView() {
        mOldPasswordEditText = findViewById(R.id.old_password_edittext);
        mNewPasswordEditText = findViewById(R.id.new_password_edittext);
        mConfirmPasswordEditText = findViewById(R.id.confirm_password_edittext);
        iv_hideOrShowPwd1 = findViewById(R.id.iv_hideOrShowPwd1);
        iv_hideOrShowPwd2 = findViewById(R.id.iv_hideOrShowPwd2);

        mBackImageView = findViewById(R.id.iv_back);
        mConfirmImageView = findViewById(R.id.confirm_imageview);

        mBackImageView.setOnClickListener(this);
        mConfirmImageView.setOnClickListener(this);
        iv_hideOrShowPwd1.setOnClickListener(this);
        iv_hideOrShowPwd2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear_cache_layout:

                break;

            case R.id.tv_login_out:
                MMKVUtil.putString(SPStringUtils.TOKEN, "NULL");
                finish();
                break;

            case R.id.confirm_imageview:
                updatePwd();
                break;

            case R.id.iv_hideOrShowPwd1:
                if (mNewPasswordEditText.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                    mNewPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    mNewPasswordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;

            case R.id.iv_hideOrShowPwd2:
                if (mConfirmPasswordEditText.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                    mConfirmPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    mConfirmPasswordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;

            case R.id.iv_back:
                finish();
                break;

            default:
                break;
        }
    }

    private void updatePwd() {
//        final Gson gson = new Gson();
        final String oldPwd = mOldPasswordEditText.getText().toString().trim();
        final String newPwd = mNewPasswordEditText.getText().toString().trim();
        final String confirmPwd = mConfirmPasswordEditText.getText().toString().trim();
        if (CommonUtils.isEmptyString(oldPwd)) {
            ToastUtil.s("请输入旧密码");
            return;
        }
        if (CommonUtils.isEmptyString(newPwd)) {
            ToastUtil.s("请输入新密码");
            return;
        }
        if (newPwd.length() < 10 || newPwd.length() > 20) {
            ToastUtil.s("新密码输入长度有误");
            return;
        }
        if (!RegularUtil.isPassword1(newPwd)) {
            ToastUtil.s("密码长度需在10~20之间且必须包含字母、数字和特殊字符");
            return;
        }
        if (!newPwd.equals(confirmPwd)) {
            ToastUtil.s("两个新密码不相同");
            return;
        }

        if (oldPwd.equals(newPwd)) {
            ToastUtil.s("旧密码和新密码相同");
            return;
        }
        final Map<String, Object> map = new HashMap<>();
        map.put("oldPassword", MD5Util.ToMD5NOKey(oldPwd));
        map.put("newPassword", MD5Util.ToMD5NOKey(newPwd));
        map.put("confirmPassword", MD5Util.ToMD5NOKey(confirmPwd));
//        final String strEntity = gson.toJson(map);
//        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"),strEntity);
        final PersonService personService = RetrofitClient.getAPIService(PersonService.class);
        personService.updatePwd(MD5Util.ToMD5NOKey(confirmPwd), MD5Util.ToMD5NOKey(newPwd),
                MD5Util.ToMD5NOKey(oldPwd)).enqueue(new Callback<BaseBean<Object>>() {
            @Override
            public void onResponse(Call<BaseBean<Object>> call, Response<BaseBean<Object>> response) {
                Log.e("yuhao", "onResponse: " + response);
                final boolean isUnEmptyData = response.body() != null;
                if (!isUnEmptyData) {
                    mHandler.sendEmptyMessage(UPDATE_PASSWORD_FAILED);
                    return;
                }
                BaseBean baseBean = response.body();
                if (baseBean.code == 0) {
                    mHandler.sendEmptyMessage(UPDATE_PASSWORD_SUCCEED);
                    return;
                } else {
                    mHandler.sendEmptyMessage(UPDATE_PASSWORD_FAILED);
                }
            }

            @Override
            public void onFailure(Call<BaseBean<Object>> call, Throwable t) {
                mHandler.sendEmptyMessage(UPDATE_PASSWORD_FAILED);
            }
        });
    }
}
