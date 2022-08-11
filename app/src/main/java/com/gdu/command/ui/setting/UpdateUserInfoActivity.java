package com.gdu.command.ui.setting;

import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.utils.IDCardUtil;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.R;
import com.gdu.command.ui.splash.LoginService;
import com.gdu.utils.MyVariables;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.rxjava.rxlife.RxLife;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.RequestBody;

/**
 * 个人信息修改公用界面
 */
public class UpdateUserInfoActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mLeftBack;
    private TextView mTitleName;
    private ImageView mRightConfirm;
    private EditText mEditText;
    private ImageView mIvClear;

    private String titleName;
    private String userInfo;
    private int resultCode;
    private String key;

    private Gson mGson;
    private LoginService service;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_user_info;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true).statusBarColor(
                R.color.color_224CD0).init();

        mGson = new Gson();
        service = RetrofitClient.getAPIService(LoginService.class);

        titleName = getIntent().getStringExtra(MyVariables.UPDATE_TITLE_NAME);
        userInfo = getIntent().getStringExtra(MyVariables.UPDATE_PERSON_INFO);
        resultCode = getIntent().getIntExtra(MyVariables.UPDATE_INFO_RESULT_CODE, 0);

        initView();
        initEdit();
    }

    private void initView() {
        mLeftBack = findViewById(R.id.iv_left_back);
        mTitleName = findViewById(R.id.tv_left_title_name);
        mRightConfirm = findViewById(R.id.iv_right_confirm);
        mRightConfirm.setVisibility(View.VISIBLE);
        mEditText = findViewById(R.id.edit_update_content);
        mIvClear = findViewById(R.id.iv_edit_clear);

        mLeftBack.setOnClickListener(this);
        mRightConfirm.setOnClickListener(this);
        mIvClear.setOnClickListener(this);
    }

    /**
     * 提交修改个人信息
     */
    private void submitUpdateInfo(String key, String value) {
        Map<String, Object> map  = new HashMap<>();
        map.put(key, value);
        String strEntity = mGson.toJson(map);
        RequestBody body = RequestBody.create(strEntity, okhttp3.MediaType.parse("application" + "/json;charset=UTF-8"));
        service.updateUserInfo(body).subscribeOn(Schedulers.io())
                .to(RxLife.toMain(this))
                .subscribe(new Consumer<UpdateUserInfoBean>() {
                    @Override
                    public void accept(UpdateUserInfoBean bean) throws Throwable {
                        if (bean.getCode() == 0) {
                            backToPersonActivity();
                        } else {
                            ToastUtil.s("修改信息失败");
                        }
                    }
                }, throwable -> {
                    MyLogUtils.e("修改个人信息出错", throwable);
                });

    }

    private void initEdit() {
        mTitleName.setText(!isEmpty(titleName) ? titleName : "");
        mEditText.setText(!isEmpty(userInfo) ? userInfo : "");
        mEditText.setSelection(userInfo.length());
        //200毫秒自动弹出输入法
        showSoftInput(mEditText, 200);
        mIvClear.setVisibility(!isEmpty(userInfo) ? View.VISIBLE : View.GONE);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mIvClear.setVisibility(!isEmpty(s.toString()) ? View.VISIBLE : View.GONE);
            }
        });

        //设置输入长度和输入类型
        if (titleName.equals("姓名")) {
            mEditText.setHint("请输入姓名");
            mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        } else if (titleName.equals("身份证号")) {
            mEditText.setHint("请输入身份证号");
            mEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});
        } else if (titleName.equals("办公电话")) {
            mEditText.setHint("请输入办公电话");
            mEditText.setInputType(InputType.TYPE_CLASS_PHONE);
            mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        } else if (titleName.equals("联系方式")) {
            mEditText.setHint("请输入联系方式");
            mEditText.setInputType(InputType.TYPE_CLASS_PHONE);
            mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_back:
                finish();
                break;
            case R.id.iv_right_confirm:
                if (isEmpty(mEditText)) {
                   if (titleName.equals("姓名")) {
                       ToastUtil.s("请输入姓名");
                   } else if (titleName.equals("身份证号")) {
                       ToastUtil.s("请输入身份证号码");
                   } else if (titleName.equals("办公电话")) {
                       ToastUtil.s("请输入办公电话");
                   } else if (titleName.equals("联系方式")) {
                       ToastUtil.s("请输入联系方式");
                   }
                } else {
                    if (titleName.equals("身份证号") && !IDCardUtil.isIdCard(valueOf(mEditText))) {
                        return;
                    }

                    if (titleName.equals("姓名")) {
                        key = "username";
                    } else if (titleName.equals("身份证号")) {
                        key = "idCard";
                    } else if (titleName.equals("办公电话")) {
                        key = "officeMobile";
                    } else if (titleName.equals("联系方式")) {
                        key = "mobile";
                    }
                    submitUpdateInfo(key, valueOf(mEditText));
                }
                break;
            case R.id.iv_edit_clear:
                mEditText.setText("");
                break;
        }
    }

    /**
     * 修改成功后返回个人信息页
     */
    private void backToPersonActivity() {
        ToastUtil.s("修改成功");
        Intent intent = new Intent();
        intent.putExtra(MyVariables.UPDATE_PERSON_INFO, valueOf(mEditText));
        setResult(resultCode, intent);
        finish();
    }
}
