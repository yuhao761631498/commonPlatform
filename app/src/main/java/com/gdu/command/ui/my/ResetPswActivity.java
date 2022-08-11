package com.gdu.command.ui.my;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.utils.RegularUtil;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.command.R;
import com.gdu.command.ui.base.dialog.LoadingDialog;
import com.gdu.command.ui.setting.UpdatePassWordActivity;
import com.gdu.command.ui.splash.LoginService;
import com.gyf.immersionbar.ImmersionBar;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPswActivity extends BaseActivity<ResetPswPresenter> implements View.OnClickListener, IResetPswView {

    private ImageView iv_back;
    private EditText et_phone_number;
    private EditText et_verification_code;
    private TextView tv_get_verification_code;
    private TextView tv_next;
    private LoadingDialog loadingDialog;
    private final byte FAILE = 0;
    private final byte Success = 1;
    private EditText newPsw;
    private EditText confirmPsw;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rest_psw;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true).statusBarColor(
                R.color.color_224CD0).init();
        initView();
        initClickListener();

        getPresenter().setView(this);
    }

    private void initClickListener() {
        iv_back.setOnClickListener(this);
        tv_get_verification_code.setOnClickListener(this);
        tv_next.setOnClickListener(this);
    }

    private void initView() {
        iv_back = ((ImageView) findViewById(R.id.iv_back));
        et_phone_number = ((EditText) findViewById(R.id.et_phone_number));
        et_verification_code = ((EditText) findViewById(R.id.et_verification_code));
        tv_get_verification_code = ((TextView) findViewById(R.id.tv_get_verification_code));
        tv_next = ((TextView) findViewById(R.id.tv_next));
        newPsw = ((EditText) findViewById(R.id.new_password_edittext));
        confirmPsw = ((EditText) findViewById(R.id.confirm_password_edittext));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.tv_get_verification_code:  //调用接口获取验证码
                if (loadingDialog == null) {
                    loadingDialog = new LoadingDialog(ResetPswActivity.this);
                }
                loadingDialog.setCancelable(false);
                loadingDialog.setMessage(getString(R.string.base_dialog_loading));
                loadingDialog.show();
                String phoneNumber = et_phone_number.getText().toString().trim();
                if (!TextUtils.isEmpty(phoneNumber) && RegularUtil.isMobileSimple(phoneNumber)) {
                    getPresenter().getCheckCode(phoneNumber);
                } else {
                    if (loadingDialog != null) {
                        loadingDialog.dismiss();
                    }
                    Toast.makeText(ResetPswActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.tv_next:  //调用接口重置密码
                String phone = et_phone_number.getText().toString().trim();
                String code = et_verification_code.getText().toString().trim();
                String newPwd = newPsw.getText().toString().trim();
                String confirmPwd = confirmPsw.getText().toString().trim();

                if (TextUtils.isEmpty(phone) && RegularUtil.isMobileSimple(phone)) {
                    ToastUtil.s("请输入正确的手机号码");
                    return;
                }

                if (TextUtils.isEmpty(code)) {
                    ToastUtil.s("请输入正确的验证码");
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

                if (loadingDialog == null) {
                    loadingDialog = new LoadingDialog(ResetPswActivity.this);
                }
                loadingDialog.setCancelable(false);
                loadingDialog.setMessage(getString(R.string.base_dialog_loading));
                loadingDialog.show();

                getPresenter().getPwdByPhone(code, phone, newPwd, confirmPwd);
                break;
        }
    }


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull @NotNull Message message) {
            switch (message.what) {
                case FAILE:
                    if (loadingDialog != null) {
                        loadingDialog.dismiss();
                    }
                    Toast.makeText(ResetPswActivity.this, (String) message.obj, Toast.LENGTH_SHORT).show();
                    break;

                case Success:
                    if (loadingDialog != null) {
                        loadingDialog.dismiss();
                    }
                    String obj = (String) message.obj;
                    if (!TextUtils.isEmpty(obj)) {
                        Toast.makeText(ResetPswActivity.this, obj, Toast.LENGTH_SHORT).show();
                        if (obj.equals("密码重置成功")) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 500);
                        }
                    }
                    break;
            }
            return false;
        }
    });

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void success(boolean isNext) {
        Message mMessage = new Message();
        if (isNext) {
            mMessage.obj = "密码重置成功";
        } else {
            mMessage.obj = "验证码已发送";
        }
        mMessage.what = Success;
        handler.sendMessage(mMessage);
    }

    @Override
    public void failure(boolean isNext) {
        Message mMessage = new Message();
        mMessage.what = FAILE;
        if (!isNext) {
            mMessage.obj = "获取验证码失败,请检查网络或者是否设置授权码";
        } else {
            mMessage.obj = "重置密码失败";
        }
        handler.sendMessage(mMessage);
    }
}