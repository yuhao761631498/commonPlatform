package com.gdu.command.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gdu.command.R;
import com.gdu.model.device.PTZCmdType;
import com.gdu.model.device.PTZType;
import com.gdu.utils.FastClickUtils;

/**
 * 云台基本指令
 */
public class GimbalPTZView extends BaseView {

    private OnGimbalPTZListener mOnGimbalPTZListener;
    private Button mMinusTextView;
    private Button mPlusTextView;
    private TextView mValueTextView;
    private PTZType mCurrentPTZType;

    private int mCurrentSpeed = 3;

    public GimbalPTZView(Context context) {
        super(context);
    }

    public GimbalPTZView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GimbalPTZView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnGimbalPTZListener(OnGimbalPTZListener onGimbalPTZListener){
        mOnGimbalPTZListener = onGimbalPTZListener;
    }

    public void setName(String name){
        mValueTextView.setText(name);
    }

    /**
     * 设置云台操作类型
     * @param type
     */
    public void setType(PTZType type){
        mCurrentPTZType = type;
        String typeName = "";
        mValueTextView.setVisibility(INVISIBLE);
        switch (type){
            case ZOOM:
                typeName = "变倍";
                break;

            case FOCUS:
                typeName = "变焦";
                break;

            case IRIS:
                typeName = "光圈";
                break;

            case SPEED:
                typeName = "速度";
                mValueTextView.setVisibility(VISIBLE);
                mValueTextView.setText(mCurrentSpeed + "");
                break;

            default:
                break;
        }
        mPlusTextView.setText(typeName + "+");
        mMinusTextView.setText(typeName + "-");
    }

    @Override
    public void initListener() {
        mMinusTextView.setOnClickListener(this);
        mPlusTextView.setOnClickListener(this);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.view_gimbal_ptz;
    }

    @Override
    public void initView() {
        super.initView();
        mValueTextView =  findViewById(R.id.tv_value);
        mPlusTextView =  findViewById(R.id.plus_layout);
        mMinusTextView = findViewById(R.id.minus_layout);
        mValueTextView.setVisibility(VISIBLE);
        mCurrentSpeed = 3;
        mValueTextView.setText(mCurrentSpeed + "");
        mCurrentPTZType = PTZType.SPEED;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.minus_layout:
                // 防止快速点击
                if (FastClickUtils.isFastClick(R.id.minus_layout)) {
                    return;
                }
                if (mOnGimbalPTZListener != null) {
                    PTZCmdType ptzCmdType = PTZCmdType.Stop;
                    if (mCurrentPTZType == null) {
                        return;
                    }
                    switch (mCurrentPTZType) {
                        case SPEED:
                            ptzCmdType = PTZCmdType.SpeedMinus;
                            if (mCurrentSpeed > 1) {
                                mCurrentSpeed--;
                            }
                            mOnGimbalPTZListener.onSpeedChange(mCurrentSpeed);
                            mValueTextView.setText(mCurrentSpeed + "");
                            break;
                        case IRIS:
                            ptzCmdType = PTZCmdType.FIIrisOut;
                            break;
                        case ZOOM:
                            ptzCmdType = PTZCmdType.ZoomOut;
                            break;
                        case FOCUS:
                            ptzCmdType = PTZCmdType.FIFocusNear;
                            break;
                        default:
                            break;
                    }
                    mOnGimbalPTZListener.onMinusClick(ptzCmdType);
                }

                break;
            case R.id.plus_layout:
                // 防止快速点击
                if (FastClickUtils.isFastClick(R.id.plus_layout)) {
                    return;
                }
                if (mOnGimbalPTZListener != null) {
                    PTZCmdType ptzCmdType = PTZCmdType.Stop;
                    if (mCurrentPTZType == null) {
                        return;
                    }
                    switch (mCurrentPTZType) {
                        case SPEED:
                            if (mCurrentSpeed < 10) {
                                mCurrentSpeed++;
                            }
                            mOnGimbalPTZListener.onSpeedChange(mCurrentSpeed);
                            mValueTextView.setText(mCurrentSpeed + "");
                            ptzCmdType = PTZCmdType.SpeedPlus;
                            break;
                        case IRIS:
                            ptzCmdType = PTZCmdType.FIIrisIn;
                            break;
                        case ZOOM:
                            ptzCmdType = PTZCmdType.ZoomIn;
                            break;
                        case FOCUS:
                            ptzCmdType = PTZCmdType.FIFocusFar;
                            break;
                        default:
                            break;
                    }
                    mOnGimbalPTZListener.onPlusClick(ptzCmdType);
                }
                break;

            default:
                break;
        }
    }

    public interface OnGimbalPTZListener{
        void onMinusClick(PTZCmdType cmdType);
        void onPlusClick(PTZCmdType cmdType);
        void onSpeedChange(int speed);
    }
}
