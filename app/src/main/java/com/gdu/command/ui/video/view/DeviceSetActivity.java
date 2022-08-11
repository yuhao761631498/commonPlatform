package com.gdu.command.ui.video.view;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.command.R;
import com.gdu.model.config.StorageConfig;
//import com.gdu.command.ui.base.BaseActivity;
import com.gdu.model.device.DeviceInfo;

public class DeviceSetActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mBackImageView;
    private ImageView mDeviceTypeImageView;
    private TextView  mDeviceNameTextView;
    private ImageView mTargetTrackSwitch;
    private ImageView mFireAlarmSwitch;
    private TextView  mDeviceStatusTextView;
    private Button mDeleteButton;

    private DeviceInfo mCurrentDeviceInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_set;
    }

    @Override
    protected void initialize() {
        initData();
        initView();
        initListener();
        setData();
    }

    private void setData() {
        mDeviceNameTextView.setText(mCurrentDeviceInfo.getDeviceName());
    }

    private void initData() {
        Intent intent = getIntent();
        mCurrentDeviceInfo = (DeviceInfo) intent.getSerializableExtra(StorageConfig.DEVICE_INFO);
    }

    private void initView() {
        mBackImageView = findViewById(R.id.back_imageview);
        mDeviceTypeImageView = findViewById(R.id.device_type_imageview);
        mDeviceNameTextView = findViewById(R.id.device_name_textview);
        mTargetTrackSwitch = findViewById(R.id.target_track_imageview);
        mFireAlarmSwitch = findViewById(R.id.fire_alarm_imageview);
        mDeviceStatusTextView = findViewById(R.id.device_status_textview);
        mDeleteButton = findViewById(R.id.delete_device_button);
    }

    private void initListener() {
        mBackImageView.setOnClickListener(this);
        mTargetTrackSwitch.setOnClickListener(this);
        mFireAlarmSwitch.setOnClickListener(this);
    }

    private boolean isTargetTrackOpen;
    private boolean isFireAlarmOpen;

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_imageview:
                finish();
                break;
            case R.id.delete_device_button:

                break;
            case R.id.target_track_imageview:
                if (isTargetTrackOpen) {
                    mTargetTrackSwitch.setSelected(false);
                    isTargetTrackOpen = false;
                } else {
                    mTargetTrackSwitch.setSelected(true);
                    isTargetTrackOpen = true;
                }
                break;
            case R.id.fire_alarm_imageview:
                if (isFireAlarmOpen) {
                    mFireAlarmSwitch.setSelected(false);
                    isFireAlarmOpen = false;
                } else {
                    mFireAlarmSwitch.setSelected(true);
                    isFireAlarmOpen = true;
                }
                break;
        }
    }
}
