package com.gdu.command.ui.setting;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.baselibrary.network.RetrofitClient;
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

public class UpdatePersonGenderActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mLeftBack;
    private TextView mTitleName;
    private ImageView mRightConfirm;
    private TextView tv_man;
    private TextView tv_woman;
    private View view_line;
    private View view_line2;

    private String gender;
    private int resultCode;
    private int sexCode;

    private Gson mGson;
    private LoginService service;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_update_person_gender;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true).statusBarColor(
                R.color.color_224CD0).init();

        mGson = new Gson();
        service = RetrofitClient.getAPIService(LoginService.class);

        resultCode = getIntent().getIntExtra(MyVariables.UPDATE_INFO_RESULT_CODE, 0);
        gender = getIntent().getStringExtra(MyVariables.UPDATE_PERSON_INFO);

        initView();
        mTitleName.setText("修改性别");

        if(!isEmpty(gender)) {
            tv_man.setSelected(gender.equals("男"));
            tv_woman.setSelected(gender.equals("女"));
            view_line.setSelected(gender.equals("男"));
            view_line2.setSelected(gender.equals("女"));
            sexCode = gender.equals("男") ? 1 : 0;
        }
    }

    private void initView() {
        mLeftBack = findViewById(R.id.iv_left_back);
        mTitleName = findViewById(R.id.tv_left_title_name);
        mRightConfirm = findViewById(R.id.iv_right_confirm);
        mRightConfirm.setVisibility(View.VISIBLE);
        tv_man = findViewById(R.id.tv_update_man);
        tv_woman = findViewById(R.id.tv_update_woman);
        view_line = findViewById(R.id.view_line);
        view_line2 = findViewById(R.id.view_line2);

        mLeftBack.setOnClickListener(this);
        mRightConfirm.setOnClickListener(this);
        tv_man.setOnClickListener(this);
        tv_woman.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_back:
                finish();
                break;
            case R.id.iv_right_confirm:
                if (sexCode < 0) {
                    ToastUtil.s("请选择性别");
                } else {
                    updateUserSex();
                }
                break;
            case R.id.tv_update_man:
                tv_man.setSelected(true);
                tv_woman.setSelected(false);
                gender = "男";
                sexCode = 1;
                view_line.setSelected(true);
                view_line2.setSelected(false);
                break;
            case R.id.tv_update_woman:
                tv_man.setSelected(false);
                tv_woman.setSelected(true);
                view_line2.setSelected(true);
                view_line.setSelected(false);
                sexCode = 0;
                gender = "女";
                break;
        }
    }

    private void updateUserSex() {
        Map<String, Object> map  = new HashMap<>();
        map.put("userGender", sexCode);
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
                            ToastUtil.s("修改性别失败");
                        }
                    }
                }, throwable -> {
                    MyLogUtils.e("修改性别出错", throwable);
                });
    }

    /**
     * 修改成功后返回个人信息页
     */
    private void backToPersonActivity() {
        Intent intent = new Intent();
        intent.putExtra(MyVariables.UPDATE_PERSON_INFO, gender);
        setResult(resultCode, intent);
        ToastUtil.s("修改成功");
        finish();
    }

}
