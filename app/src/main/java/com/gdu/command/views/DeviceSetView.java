package com.gdu.command.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.gdu.command.R;

public class DeviceSetView extends BaseView {

    private LinearLayout mChangeLayout;
    private LinearLayout mRefreshLayout;
    private LinearLayout mJumpSetLayout;
    private LinearLayout mCancelLayout;

    private OnDeviceSetListener mOnDeviceSetListener;

    public DeviceSetView(Context context) {
        super(context);
    }

    public DeviceSetView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DeviceSetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnDeviceSetListener(OnDeviceSetListener onDeviceSetListener){
        mOnDeviceSetListener = onDeviceSetListener;
    }

    @Override
    public void initListener() {
        mChangeLayout.setOnClickListener(this);
        mRefreshLayout.setOnClickListener(this);
        mJumpSetLayout.setOnClickListener(this);
        mCancelLayout.setOnClickListener(this);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.view_device_set;
    }

    @Override
    public void initView() {
        super.initView();
//        mDeviceSetLayout = findViewById(R.id.device_set_layout);
        mChangeLayout =    findViewById(R.id.change_layout);
        mRefreshLayout =   findViewById(R.id.refresh_cover_layout);
        mJumpSetLayout =   findViewById(R.id.set_layout);
        mCancelLayout =   findViewById(R.id.cancel_layout);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.change_layout:
                mOnDeviceSetListener.onChangeClick();
                break;
            case R.id.refresh_cover_layout:
                mOnDeviceSetListener.onRefreshClick();
                break;
            case R.id.set_layout:
                mOnDeviceSetListener.onSetClick();
                break;
            case R.id.cancel_layout:
                setVisibility(GONE);
                break;
        }
    }

    public interface OnDeviceSetListener{
        void onChangeClick();
        void onRefreshClick();
        void onSetClick();
    }
}
