package com.gdu.command.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gdu.command.R;
import com.gdu.picktime.DatePickDialog;
import com.gdu.picktime.bean.DateType;
import com.gdu.util.TimeUtils;

/**
 * 预警时间选择
 */
public class AllAlarmTimeChooseView extends BaseView {

    private TextView selectDayTimeTv;
    private Button mResetButton;
    private Button mConfirmButton;

    private OnAlarmChooseListener mOnAlarmChooseListener;

    private long mStartTimeL = 0;
    private long mEndTimeL = 0;

    private String mStartTime = "";
    private String mEndTime = "";

    public AllAlarmTimeChooseView(Context context) {
        super(context);
    }

    public AllAlarmTimeChooseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AllAlarmTimeChooseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnAlarmChooseListener(OnAlarmChooseListener onAlarmChooseListener){
        mOnAlarmChooseListener = onAlarmChooseListener;
    }

    @Override
    public void initListener() {
        selectDayTimeTv.setOnClickListener(this);
        mResetButton.setOnClickListener(this);
        mConfirmButton.setOnClickListener(this);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.view_all_alarm_time;
    }

    @Override
    public void initView() {
        super.initView();
        selectDayTimeTv = findViewById(R.id.tv_alarmDayTime);
        mResetButton = findViewById(R.id.tv_resetBtn);
        mConfirmButton = findViewById(R.id.tv_confirmBtn);

        initTime();
    }

    private void initTime() {
        final String curTimeStr = TimeUtils.getTime(System.currentTimeMillis(), "yyyy/MM/dd");
        selectDayTimeTv.setText(curTimeStr);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_alarmDayTime:
                showDatePickDialog(DateType.TYPE_YMD);
                break;
            case R.id.tv_confirmBtn:
                mOnAlarmChooseListener.onConfirm(mStartTime, mEndTime);
                break;
            case R.id.tv_resetBtn:
                reset();
                mOnAlarmChooseListener.onReset();
                break;
            default:
                break;
        }
    }

    public void reset(){
        mStartTime = "";
        mEndTime = "";
        mStartTimeL = 0;
        mEndTimeL = 0;
        selectDayTimeTv.setText("");
        mOnAlarmChooseListener.onConfirm(mStartTime, mEndTime);
    }

    /**
     * 显示时间选择
     * @param type
     */
    private void showDatePickDialog(DateType type) {
        DatePickDialog dialog = new DatePickDialog(mContext);
        //设置上下年分限制
        dialog.setYearLimt(5);
        //设置标题
        dialog.setTitle("选择时间");
        //设置类型
        dialog.setType(type);
        //设置消息体的显示格式，日期格式
        dialog.setMessageFormat("yyyy-MM-dd");
        //设置选择回调
        dialog.setOnChangeLisener(null);
        //设置点击确定按钮回调
        dialog.setOnSureLisener(date -> {
            final long selectTime = date.getTime();
            final String selectTimeStr = TimeUtils.getTime(selectTime, "yyyy/MM/dd");
            mStartTime = selectTimeStr + " 00:00:01";
            mEndTime = selectTimeStr + " 23:59:59";
            mStartTimeL = TimeUtils.getTimeStamp(mStartTime, "yyyy/MM/dd HH:mm:ss");
            mEndTimeL = TimeUtils.getTimeStamp(mEndTime, "yyyy/MM/dd HH:mm:ss");
            selectDayTimeTv.setText(selectTimeStr);
        });
        dialog.show();
    }


    public interface OnAlarmChooseListener{
        void onConfirm(String startTime, String endTime);
        void onReset();
    }
}
