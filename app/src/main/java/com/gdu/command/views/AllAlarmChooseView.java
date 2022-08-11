package com.gdu.command.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.R;
import com.gdu.picktime.DatePickDialog;
import com.gdu.picktime.bean.DateType;
import com.gdu.util.TimeUtils;
import com.gdu.utils.MyVariables;

import java.util.HashMap;
import java.util.Map;

/**
 * 多重选择
 */
public class AllAlarmChooseView extends BaseView implements CompoundButton.OnCheckedChangeListener {

    private TextView mStartTimeTextView;
    private TextView mEndTimeTextView;
    private OnAlarmChooseListener mOnAlarmChooseListener;
    private CheckBox eventType1Cb;
    private CheckBox eventType2Cb;
    private CheckBox eventType3Cb;
    private CheckBox eventType4Cb;
    private CheckBox mUnHandleCheckBox;
    private CheckBox mMisreportCheckBox;
    private CheckBox mAbnormalCheckBox;
    private CheckBox cb_changeCase;
    private CheckBox mRedAlarmCheckBox;
    private CheckBox mOrangeAlarmCheckBox;
    private CheckBox mYellowdAlarmCheckBox;
    private CheckBox mBlueAlarmCheckBox;
    private Button mResetButton;
    private Button mConfirmButton;
    private CheckBox lockAttentionCb;
    private Map<Integer, Boolean> mEventTypeMap;
    private Map<Integer, Boolean> mStatusMap;
    private Map<Integer, Boolean> mLevelMap;
    private String mStartTime = "";
    private String mEndTime = "";
    private long mStartTimeL = 0;
    private long mEndTimeL = 0;

    public AllAlarmChooseView(Context context) {
        super(context);
    }

    public AllAlarmChooseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AllAlarmChooseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnAlarmChooseListener(OnAlarmChooseListener onAlarmChooseListener){
        mOnAlarmChooseListener = onAlarmChooseListener;
    }

    @Override
    public void initListener() {
        eventType1Cb.setOnCheckedChangeListener(this);
        eventType2Cb.setOnCheckedChangeListener(this);
        eventType3Cb.setOnCheckedChangeListener(this);
        eventType4Cb.setOnCheckedChangeListener(this);
        mStartTimeTextView.setOnClickListener(this);
        mEndTimeTextView.setOnClickListener(this);
        mUnHandleCheckBox.setOnCheckedChangeListener(this);
        mMisreportCheckBox.setOnCheckedChangeListener(this);
        mAbnormalCheckBox.setOnCheckedChangeListener(this);
        cb_changeCase.setOnCheckedChangeListener(this);
        mRedAlarmCheckBox.setOnCheckedChangeListener(this);
        mOrangeAlarmCheckBox.setOnCheckedChangeListener(this);
        mYellowdAlarmCheckBox.setOnCheckedChangeListener(this);
        mBlueAlarmCheckBox.setOnCheckedChangeListener(this);
        mResetButton.setOnClickListener(this);
        mConfirmButton.setOnClickListener(this);
        lockAttentionCb.setOnCheckedChangeListener(this);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.view_all_alarm_choose;
    }

    @Override
    public void initView() {
        super.initView();
        mStartTimeTextView = findViewById(R.id.start_time_textview);
        mEndTimeTextView = findViewById(R.id.end_time_textview);
        eventType1Cb = findViewById(R.id.cb_eventType1);
        eventType2Cb = findViewById(R.id.cb_eventType2);
        eventType3Cb = findViewById(R.id.cb_eventType3);
        eventType4Cb = findViewById(R.id.cb_eventType4);
        mUnHandleCheckBox = findViewById(R.id.unhandle_checkbox);
        mMisreportCheckBox = findViewById(R.id.misreport_checkbox);
        mAbnormalCheckBox = findViewById(R.id.abnormal_checkbox);
        cb_changeCase = findViewById(R.id.cb_changeCase);
        mRedAlarmCheckBox = findViewById(R.id.red_alarm_checkbox);
        mOrangeAlarmCheckBox = findViewById(R.id.orange_alarm_checkbox);
        mYellowdAlarmCheckBox = findViewById(R.id.yellow_alarm_checkbox);
        mBlueAlarmCheckBox = findViewById(R.id.blue_alarm_checkbox);
        mResetButton = findViewById(R.id.reset_button);
        mConfirmButton = findViewById(R.id.confirm_button);
        lockAttentionCb = findViewById(R.id.cb_lockAttention);

        lockAttentionCb.setChecked(MyVariables.isLockAtAttention);

        initMap();
        initTime();
    }

    private void initMap(){
        mEventTypeMap = new HashMap<>();
        for (int i = 3; i <= 7; i++) {
            mEventTypeMap.put(i, false);
        }
        mStatusMap = new HashMap<>();
        for (int i = 1; i <= 4; i++) {
            mStatusMap.put(i, false);
        }
        mLevelMap = new HashMap<>();
        for (int i = 1; i <= 4; i++) {
            mLevelMap.put(i, false);
        }
    }

    private void initTime() {
//        mStartTimeL = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
//        mEndTimeL = System.currentTimeMillis();
//        mStartTime = TimeUtils.getTime(mStartTimeL, "yyyy/MM/dd HH:mm:ss");
//        mEndTime = TimeUtils.getTime(mEndTimeL, "yyyy/MM/dd HH:mm:ss");
        mStartTimeTextView.setText(mStartTime);
        mEndTimeTextView.setText(mEndTime);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.start_time_textview:
                showDatePickDialog(DateType.TYPE_YMDHM, true);
                break;
            case R.id.end_time_textview:
                showDatePickDialog(DateType.TYPE_YMDHM, false);
                break;
            case R.id.confirm_button:
//                if (CommonUtils.isEmptyString(mStartTime)) {
//                    ToastUtil.s("请设置开始时间");
//                    return;
//                }
//                if (CommonUtils.isEmptyString(mEndTime)) {
//                    ToastUtil.s("请设置结束时间");
//                    return;
//                }
                mOnAlarmChooseListener.onConfirm(mEventTypeMap, mStatusMap, mLevelMap, mStartTime, mEndTime);
                break;
            case R.id.reset_button:
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
        mStartTimeTextView.setText("");
        mEndTimeTextView.setText("");
        eventType1Cb.setChecked(false);
        eventType2Cb.setChecked(false);
        eventType3Cb.setChecked(false);
        eventType4Cb.setChecked(false);
        mUnHandleCheckBox.setChecked(false);
        mMisreportCheckBox.setChecked(false);
        mAbnormalCheckBox.setChecked(false);
        cb_changeCase.setChecked(false);
        mRedAlarmCheckBox.setChecked(false);
        mOrangeAlarmCheckBox.setChecked(false);
        mYellowdAlarmCheckBox.setChecked(false);
        mBlueAlarmCheckBox.setChecked(false);
        lockAttentionCb.setChecked(true);
        initMap();
        mOnAlarmChooseListener.onConfirm(mEventTypeMap, mStatusMap, mLevelMap, mStartTime, mEndTime);
    }

    /**
     * 显示时间选择
     * @param type
     */
    private void showDatePickDialog(DateType type, final boolean isStartTime) {
        DatePickDialog dialog = new DatePickDialog(mContext);
        //设置上下年分限制
        dialog.setYearLimt(5);
        //设置标题
        dialog.setTitle("选择时间");
        //设置类型
        dialog.setType(type);
        //设置消息体的显示格式，日期格式
        dialog.setMessageFormat("yyyy-MM-dd HH:mm");
        //设置选择回调
        dialog.setOnChangeLisener(null);
        //设置点击确定按钮回调
        dialog.setOnSureLisener(date -> {
            final long selectTime = date.getTime();
            final long curTime = System.currentTimeMillis();
            if (selectTime > curTime) {
                ToastUtil.s("所选时间不能超过当前时间");
                return;
            }
            if (isStartTime) {
                final long diff = mEndTimeL - selectTime;
                MyLogUtils.d("showDatePickDialog() mEndTimeL = " + mEndTimeL
                        + "; selectTime = " + selectTime + "; diff = " + diff);
                final boolean haveOver = mEndTimeL != 0 && diff < 0;
                if(haveOver){
                    ToastUtil.s("开始时间不能大于结束时间");
                    return;
                }
                mStartTimeL = selectTime;
                mStartTime = TimeUtils.getTime(selectTime, "yyyy/MM/dd HH:mm:ss");
                mStartTimeTextView.setText(mStartTime);
            } else {
                final long diff = selectTime - mStartTimeL;
                MyLogUtils.d("showDatePickDialog() selectTime = " + selectTime
                        + "; mStartTimeL = " + mStartTimeL + "; diff = " + diff);
                final boolean haveOver = mStartTimeL != 0 && diff < 0;
                if(haveOver){
                    ToastUtil.s("结束时间不能小于开始时间");
                    return;
                }
                mEndTimeL = selectTime;
                mEndTime = TimeUtils.getTime(selectTime, "yyyy/MM/dd HH:mm:ss");
                mEndTimeTextView.setText(mEndTime);
            }
        });
        dialog.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        switch (compoundButton.getId()){

            case R.id.cb_eventType1:
                if (b) {
                    eventType1Cb.setBackground(ContextCompat.getDrawable(getContext(),
                            R.drawable.shape_alarm_checked));
                } else {
                    eventType1Cb.setBackground(ContextCompat.getDrawable(getContext(),
                            R.drawable.shape_alarm_uncheck));
                }
                mEventTypeMap.put(4, b);
                break;

            case R.id.cb_eventType2:
                if (b) {
                    eventType2Cb.setBackground(ContextCompat.getDrawable(getContext(),
                            R.drawable.shape_alarm_checked));
                } else {
                    eventType2Cb.setBackground(ContextCompat.getDrawable(getContext(),
                            R.drawable.shape_alarm_uncheck));
                }
                mEventTypeMap.put(3, b);
                break;

            case R.id.cb_eventType3:
                if (b) {
                    eventType3Cb.setBackground(ContextCompat.getDrawable(getContext(),
                            R.drawable.shape_alarm_checked));
                } else {
                    eventType3Cb.setBackground(ContextCompat.getDrawable(getContext(),
                            R.drawable.shape_alarm_uncheck));
                }
                mEventTypeMap.put(5, b);
                break;

            case R.id.cb_eventType4:
                if (b) {
                    eventType4Cb.setBackground(ContextCompat.getDrawable(getContext(),
                            R.drawable.shape_alarm_checked));
                } else {
                    eventType4Cb.setBackground(ContextCompat.getDrawable(getContext(),
                            R.drawable.shape_alarm_uncheck));
                }
                mEventTypeMap.put(7, b);
                break;

            case R.id.unhandle_checkbox:
                if (b) {
                    mUnHandleCheckBox.setBackground(ContextCompat.getDrawable(getContext(),
                            R.drawable.shape_alarm_checked));
                } else {
                    mUnHandleCheckBox.setBackground(ContextCompat.getDrawable(getContext(),
                            R.drawable.shape_alarm_uncheck));
                }
                mStatusMap.put(1, b);
                break;

            case R.id.misreport_checkbox:
                if (b) {
                    mMisreportCheckBox.setBackground(ContextCompat.getDrawable(getContext(),
                            R.drawable.shape_alarm_checked));
                } else {
                    mMisreportCheckBox.setBackground(ContextCompat.getDrawable(getContext(),
                            R.drawable.shape_alarm_uncheck));
                }
                mStatusMap.put(3, b);
                break;

            case R.id.abnormal_checkbox:
                if (b) {
                    mAbnormalCheckBox.setBackground(ContextCompat.getDrawable(getContext(),
                            R.drawable.shape_alarm_checked));
                } else {
                    mAbnormalCheckBox.setBackground(ContextCompat.getDrawable(getContext(),
                            R.drawable.shape_alarm_uncheck));
                }
                mStatusMap.put(2, b);
                break;

            case R.id.cb_changeCase:
                if (b) {
                    cb_changeCase.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.shape_alarm_checked));
                } else {
                    cb_changeCase.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.shape_alarm_uncheck));
                }
                mStatusMap.put(4, b);
                break;

            case R.id.red_alarm_checkbox:
                if (b) {
                    mRedAlarmCheckBox.setBackground(ContextCompat.getDrawable(getContext(),
                            R.drawable.shape_alarm_checked));
                } else {
                    mRedAlarmCheckBox.setBackground(ContextCompat.getDrawable(getContext(),
                            R.drawable.shape_alarm_uncheck));
                }
                mLevelMap.put(4, b);
                break;

            case R.id.orange_alarm_checkbox:
                if (b) {
                    mOrangeAlarmCheckBox.setBackground(ContextCompat.getDrawable(getContext(),
                            R.drawable.shape_alarm_checked));
                } else {
                    mOrangeAlarmCheckBox.setBackground(ContextCompat.getDrawable(getContext(),
                            R.drawable.shape_alarm_uncheck));
                }
                mLevelMap.put(3, b);
                break;

            case R.id.yellow_alarm_checkbox:
                if (b) {
                    mYellowdAlarmCheckBox.setBackground(ContextCompat.getDrawable(getContext(),
                            R.drawable.shape_alarm_checked));
                } else {
                    mYellowdAlarmCheckBox.setBackground(ContextCompat.getDrawable(getContext(),
                            R.drawable.shape_alarm_uncheck));
                }
                mLevelMap.put(2, b);
                break;

            case R.id.blue_alarm_checkbox:
                if (b) {
                    mBlueAlarmCheckBox.setBackground(ContextCompat.getDrawable(getContext(),
                            R.drawable.shape_alarm_checked));
                } else {
                    mBlueAlarmCheckBox.setBackground(ContextCompat.getDrawable(getContext(),
                            R.drawable.shape_alarm_uncheck));
                }
                mLevelMap.put(1, b);
                break;

            case R.id.cb_lockAttention:
                MyVariables.isLockAtAttention = b;
                break;

            default:
                break;
        }
    }

    public interface OnAlarmChooseListener{
        void onConfirm(Map<Integer, Boolean> statusMap, Map<Integer, Boolean> levelMap,
                       String startTime, String endTime);

        void onConfirm(Map<Integer, Boolean> eventTypeMap, Map<Integer, Boolean> statusMap,
                       Map<Integer, Boolean> levelMap, String startTime, String endTime);
        void onReset();
    }
}
