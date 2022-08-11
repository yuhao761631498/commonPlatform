package com.gdu.command.ui.video.view;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.command.R;
import com.gdu.model.config.StorageConfig;
import com.gdu.model.device.DeviceInfo;

import androidx.annotation.NonNull;

public class DevicePositionActivity extends BaseActivity implements View.OnClickListener {

    private MapView mMapView;
    private DeviceInfo mDeviceInfo;
    private ImageView mBackImageView;
    private TextView mDeviceNameTextView;
    private TextView mDeviceSNTextView;
    private TextView mDeviceModelTextView;
    private TextView mDevicePositionTextView;

    private AMap mAMap;

    @Override
    protected void onBaseCreate(Bundle savedInstanceState) {
        super.onBaseCreate(savedInstanceState);
        isShowWaterMark = false;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_position;
    }

    @Override
    protected void initialize() {
        initViews();
        initData();
        addPosition();
    }

    private void addPosition() {
        CameraPosition cameraPosition = new CameraPosition(new LatLng(mDeviceInfo.getDeviceLatitude(),
                mDeviceInfo.getDeviceLongitude()), 15, 0, 30);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mAMap.moveCamera(cameraUpdate);
        final Marker marker = mAMap.addMarker(getMarkerOptions());
    }

    private MarkerOptions getMarkerOptions() {
        LatLng latLng = new LatLng(mDeviceInfo.getDeviceLatitude(), mDeviceInfo.getDeviceLongitude());
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng);
        markerOption.draggable(false);//设置Marker可拖动
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.mipmap.icon_device_position)));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
//        markerOption.setFlat(true);//设置marker平贴地图效果
        return markerOption;
    }

    private void initData() {
        Intent intent = getIntent();
        mDeviceInfo = (DeviceInfo) intent.getSerializableExtra(StorageConfig.DEVICE_INFO);
        mMapView.onCreate(mSavedInstanceState);
        mAMap = mMapView.getMap();

        if (mDeviceInfo.getDeviceTypeCode().equals("GDJK") || mDeviceInfo.getDeviceTypeCode().equals("3")) {
            mDeviceNameTextView.setText("高点监控");
        } else {
            mDeviceNameTextView.setText("无人机");
        }
        mDeviceSNTextView.setText(mDeviceInfo.getDeviceCode());
        mDeviceModelTextView.setText(mDeviceInfo.getDeviceBrand());
        mDevicePositionTextView.setText(mDeviceInfo.getDeviceAddress());

    }

    private void initViews() {
        mMapView = findViewById(R.id.map);
        mBackImageView = findViewById(R.id.back_imageview);
        mBackImageView.setOnClickListener(this);
        mDeviceNameTextView = findViewById(R.id.device_name_textview);
        mDeviceSNTextView = findViewById(R.id.device_sn_textview);
        mDeviceModelTextView = findViewById(R.id.device_model_textview);
        mDevicePositionTextView = findViewById(R.id.device_position_textview);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_imageview:
                finish();
                break;
        }
    }
}
