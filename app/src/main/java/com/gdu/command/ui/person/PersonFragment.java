package com.gdu.command.ui.person;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.baselibrary.utils.imageloade.MyImageLoadUtils;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.R;
import com.gdu.command.ui.cases.ArtificialAlarmActivity;
import com.gdu.command.ui.cases.WebPublicAlarmActivity;
import com.gdu.command.ui.duty.custom.CustomActivity;
import com.gdu.command.ui.my.AboutActivity;
import com.gdu.command.ui.my.LoginActivity;
import com.gdu.command.ui.my.PersonCenterActivity;
import com.gdu.command.ui.organization.OrganizationActivity;
import com.gdu.command.ui.patrol.MassesPatrolRecordActivity;
import com.gdu.command.ui.patrol.PersonalPatrolRecordActivity;
import com.gdu.command.ui.setting.SettingActivity;
import com.gdu.command.ui.splash.LoginService;
import com.gdu.model.config.GlobalVariable;
import com.gdu.model.config.UrlConfig;
import com.gdu.model.user.PersonInfoBean;
import com.gdu.permission.PermissionUtil;
import com.kyleduo.switchbutton.SwitchButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LOCATION_SERVICE;

public class PersonFragment extends Fragment implements View.OnClickListener {

    //    private PersonViewModel mPersonViewModel;
    private ImageView iv_head_img;
    private TextView tv_name;
    private TextView tv_work_unit;
    private TextView mQuestionLayout;
    private TextView mAboutLayout;
    private TextView mSettingLayout;
    private TextView mJobTv;
    private TextView mPersonPatrol;
    private TextView mMassesPatrol;

    private PersonInfoBean personInfoBean;
    private SwitchButton sb_location;
    private TextView tv_duty_arrangements;
    private TextView tv_artificial_alarm;
    private TextView tv_public_upload;
    private TextView tv_organization;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        mPersonViewModel =
//                new ViewModelProvider(this).get(PersonViewModel.class);
        View root = inflater.inflate(R.layout.fragment_person, container, false);
        initView(root);
        initListener();
        getPersonInfo();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPersonInfo();
        setLocationSwitch();
    }

    private void setLocationSwitch() {
        boolean locationEnabled = isLocationEnabled();
        sb_location.setChecked(locationEnabled);
    }

    private void initListener() {
        tv_name.setOnClickListener(this);
        mQuestionLayout.setOnClickListener(this);
        mAboutLayout.setOnClickListener(this);
        mSettingLayout.setOnClickListener(this);
        iv_head_img.setOnClickListener(this);
        mPersonPatrol.setOnClickListener(this);
        mMassesPatrol.setOnClickListener(this);
        tv_duty_arrangements.setOnClickListener(this);
        tv_artificial_alarm.setOnClickListener(this);
        tv_organization.setOnClickListener(this);
        sb_location.setOnClickListener(this);

        tv_public_upload.setOnClickListener(this);

        mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
    }

    private void initView(View root) {
        iv_head_img = root.findViewById(R.id.iv_head_img);
        tv_name = root.findViewById(R.id.tv_name);
        tv_work_unit = root.findViewById(R.id.tv_work_unit);
        mQuestionLayout = root.findViewById(R.id.tv_question);
        mAboutLayout = root.findViewById(R.id.tv_about);
        mSettingLayout = root.findViewById(R.id.tv_setting);
        mJobTv = root.findViewById(R.id.tv_job_title);
        mPersonPatrol = root.findViewById(R.id.tv_person_patrol);
        mMassesPatrol = root.findViewById(R.id.tv_masses_patrol);

        sb_location = root.findViewById(R.id.sb_location);  //定位开关

        tv_organization = ((TextView) root.findViewById(R.id.tv_organization));

        tv_duty_arrangements = root.findViewById(R.id.tv_duty_arrangements);
        tv_artificial_alarm = root.findViewById(R.id.tv_artificial_alarm);
        tv_public_upload = root.findViewById(R.id.tv_public_upload);
    }

    /**
     * 获取用户信息
     */
    private void getPersonInfo() {
        LoginService service = RetrofitClient.getAPIService(LoginService.class);
        service.getPersonInfo().enqueue(new Callback<PersonInfoBean>() {
            @Override
            public void onResponse(Call<PersonInfoBean> call, Response<PersonInfoBean> response) {
                personInfoBean = response.body();
                final boolean isEmptyData =
                        personInfoBean == null || personInfoBean.getCode() != 0 || personInfoBean.getData() == null;
                if (isEmptyData) {
                    if (personInfoBean != null && personInfoBean.getCode() == 401) {
                        startActivity(new Intent(getContext(), LoginActivity.class));
                    }
                    return;
                }
                GlobalVariable.sPersonInfoBean = personInfoBean;
                showInfo();
            }

            @Override
            public void onFailure(Call<PersonInfoBean> call, Throwable e) {
                MyLogUtils.e("获取用户信息接口失败", e);
            }
        });
    }

    private void showInfo() {
        if (personInfoBean != null) {
            PersonInfoBean.DataBean data = personInfoBean.getData();
            if (data != null) {
                tv_name.setText(data.getUsername());
                tv_work_unit.setText(data.getDeptName());
                if (data.getRoles() != null && data.getRoles().size() > 0) {
                    mJobTv.setText(data.getRoles().get(0).getRoleName());
                }
                MyImageLoadUtils.loadCircleImageWithUrl(requireContext(),
                        data.getHeadImg(), R.mipmap.default_head, iv_head_img);
            }
        }
    }


    /**
     * 判断定位服务是否开启
     *
     * @return true 表示开启
     */
    public boolean isLocationEnabled() {
        boolean locationPermission = PermissionUtil.checkLocatePermissionsForLOLLIPOP(getContext());
        if (!locationPermission) {
            return false;
        }
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(getActivity().getContentResolver(),
                        Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(getActivity().getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }


    /* 强制帮用户打开GPS
     *
     * @param context
     */
    public static void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }


    private LocationManager mLocationManager;
    private final ContentObserver mGpsMonitor = new ContentObserver(null) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);

            boolean enabled = mLocationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    sb_location.setChecked(enabled);
                }
            });
            System.out.println("gps enabled? " + enabled);
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        getActivity().getContentResolver()
                .registerContentObserver(
                        Settings.Secure
                                .getUriFor(Settings.System.LOCATION_PROVIDERS_ALLOWED),
                        false, mGpsMonitor);
    }


    @Override
    public void onStop() {
        super.onStop();
        getActivity().getContentResolver().unregisterContentObserver(mGpsMonitor);
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.tv_name:
            case R.id.iv_head_img:
                intent.setClass(requireContext(), PersonCenterActivity.class);
                startActivity(intent);
                break;

            case R.id.tv_person_patrol:
                intent.setClass(requireContext(), PersonalPatrolRecordActivity.class);
                startActivity(intent);
                break;

            case R.id.tv_masses_patrol:
                intent.setClass(requireContext(), MassesPatrolRecordActivity.class);
                startActivity(intent);
                break;

            case R.id.tv_question:

                break;

            case R.id.tv_about:
                intent.setClass(requireContext(), AboutActivity.class);
                startActivity(intent);
                break;

            case R.id.tv_setting:
                intent.setClass(requireContext(), SettingActivity.class);
                startActivity(intent);
                break;

            case R.id.tv_duty_arrangements:
                Intent warnRankIntent = new Intent(getActivity(), CustomActivity.class);
                startActivity(warnRankIntent);
                break;

            case R.id.tv_artificial_alarm:
                Intent artificialAlarmIntent = new Intent(getActivity(), ArtificialAlarmActivity.class);
                startActivity(artificialAlarmIntent);
                break;

            case R.id.tv_organization:
                startActivity(new Intent(getActivity(), OrganizationActivity.class));
                break;

            case R.id.tv_public_upload:
                getPermissionForCamera();
                break;

            case R.id.sb_location:
                getPermissionForLocation();
                break;

            default:
                break;
        }
    }


    private void getPermissionForCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE}, 103);
            } else {
                startActivity(new Intent(getActivity(), WebPublicAlarmActivity.class));
            }
        } else {
            startActivity(new Intent(getActivity(), WebPublicAlarmActivity.class));
        }
    }

    private void getPermissionForLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 102);
            } else {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 12);
            }
        } else {
            startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 12);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 103:
                if (grantResults.length > 0) {
                    List<String> deniedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            deniedPermissions.add(permission);
                        }
                    }
                    //被拒绝权限
                    if (!deniedPermissions.isEmpty()) {
                        startActivity(new Intent(getActivity(), WebPublicAlarmActivity.class));
                    } else {
                        Toast.makeText(getActivity(), "请前往权限管理开启相机和相册相关权限", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case 102:
                if (grantResults.length > 0) {
                    List<String> deniedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            deniedPermissions.add(permission);
                        }
                    }
                    //被拒绝权限
                    if (!deniedPermissions.isEmpty()) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 12);
                    } else {
                        Toast.makeText(getActivity(), "请前往权限管理开启位置权限", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 12 && data != null) {
            setLocationSwitch();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}