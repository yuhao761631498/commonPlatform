package com.gdu.command.ui.organization;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.command.R;
import com.gdu.model.organization.OrganizationInfo;
import com.gdu.utils.MyVariables;
import com.gyf.immersionbar.ImmersionBar;
import com.noober.background.view.BLTextView;

/**
 * 组织机构人员详情页
 * create by zyf
 */
public class OrganizationPersonDetailActivity extends BaseActivity implements View.OnClickListener {

    private ImageView backBtnIv;
    private TextView title;
    private ImageView iv_header;
    private TextView tv_name;
    private TextView tv_position;
    private TextView tv_phone_number;
    private TextView tv_office_phone;
    private TextView tv_department;
    private BLTextView rl_send_message;
    private BLTextView rl_speech_sound;
    private BLTextView rl_call_number;

    private OrganizationInfo.UseBean data;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_organization_person_detail;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true).statusBarColor(
                R.color.color_224CD0).init();

        data = (OrganizationInfo.UseBean) getIntent().getSerializableExtra(MyVariables.ORG_EMPLOYEE_BEAN);

        initView();
        initListener();
        setTextValue(data);
    }

    private void initView() {
        backBtnIv = findViewById(R.id.iv_left_back);
        title = findViewById(R.id.tv_left_title_name);
        iv_header = findViewById(R.id.iv_header);
        tv_name = findViewById(R.id.tv_name);
        tv_position = findViewById(R.id.tv_position);
        tv_phone_number = findViewById(R.id.tv_phone_number);
        tv_office_phone = findViewById(R.id.tv_office_phone);
        tv_department = findViewById(R.id.tv_department);
        rl_send_message = findViewById(R.id.rl_send_message);
        rl_speech_sound = findViewById(R.id.rl_speech_sound);
        rl_call_number = findViewById(R.id.rl_call_number);
    }

    private void initListener() {
        backBtnIv.setOnClickListener(this);
        rl_send_message.setOnClickListener(this);
        rl_speech_sound.setOnClickListener(this);
        rl_call_number.setOnClickListener(this);
    }

    private void setTextValue(OrganizationInfo.UseBean bean) {
        title.setText("个人信息");

        String name = bean.getUsername();
//        String dept = bean.getEmployeeIdentity();
        String phone = bean.getMobile();
        String department = bean.getOrgName();

        //姓名
        tv_name.setText(!TextUtils.isEmpty(name) ? name : "");
        //职位
//        tv_position.setText(!TextUtils.isEmpty(dept) ? dept : "");
        //部门
        tv_department.setText(!TextUtils.isEmpty(department) ? department : "");
        //联系电话
        tv_phone_number.setText(!TextUtils.isEmpty(phone) ? phone : "");
        //办公电话
        if (TextUtils.isEmpty(tv_phone_number.getText().toString())) {
            rl_call_number.setVisibility(View.GONE);
        } else {
            rl_call_number.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left_back:
                finish();
                break;
            case R.id.rl_call_number:
                if (!TextUtils.isEmpty(tv_phone_number.getText().toString())) {
                    callPhone(tv_phone_number.getText().toString());
                }
                break;

            default:
                break;
        }
    }

    /**
     * 拨打电话
     */
    private void callPhone(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ToastUtil.s("请授权拨打电话权限");
            return;
        }
        startActivity(intent);
    }
}
