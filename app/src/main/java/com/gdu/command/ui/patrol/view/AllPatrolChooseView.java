package com.gdu.command.ui.patrol.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.R;
import com.gdu.command.views.BaseView;
import com.gdu.picktime.DatePickDialog;
import com.gdu.picktime.bean.DateType;
import com.gdu.util.TimeUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 巡逻多重选择
 */
public class AllPatrolChooseView extends BaseView implements CompoundButton.OnCheckedChangeListener {

    private TextView mStartTimeTextView;
    private TextView mEndTimeTextView;
    private OnAlarmChooseListener mOnAlarmChooseListener;
    private CheckBox eventType1Cb;
    private CheckBox eventType2Cb;
    private CheckBox cb_recordType_1;
    private CheckBox cb_recordType_2;
    private Button mResetButton;
    private Button mConfirmButton;
    private String[] patrolTypes;
    private String[] recordTypes;
    private Map<String, Boolean> patrolTypeMap;
    private Map<String, Boolean> recordTypeMap;
    private String mStartTime = "";
    private String mEndTime = "";
    private long mStartTimeL;
    private long mEndTimeL;

    public AllPatrolChooseView(Context context) {
        super(context);
    }

    public AllPatrolChooseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AllPatrolChooseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnAlarmChooseListener(OnAlarmChooseListener onAlarmChooseListener){
        mOnAlarmChooseListener = onAlarmChooseListener;
    }

    @Override
    public void initListener() {
        eventType1Cb.setOnCheckedChangeListener(this);
        eventType2Cb.setOnCheckedChangeListener(this);
        cb_recordType_1.setOnCheckedChangeListener(this);
        cb_recordType_2.setOnCheckedChangeListener(this);
        mStartTimeTextView.setOnClickListener(this);
        mEndTimeTextView.setOnClickListener(this);
        mResetButton.setOnClickListener(this);
        mConfirmButton.setOnClickListener(this);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.view_all_patrol_choose;
    }

    @Override
    public void initView() {
        super.initView();
        patrolTypes = new String[]{"日常巡逻", "联合执法"};
        recordTypes = new String[]{"一人多杆", "网鱼行为"};

        mStartTimeTextView = findViewById(R.id.start_time_textview);
        mEndTimeTextView = findViewById(R.id.end_time_textview);
        eventType1Cb = findViewById(R.id.cb_eventType1);
        eventType2Cb = findViewById(R.id.cb_eventType2);
        cb_recordType_1 = findViewById(R.id.cb_recordType_1);
        cb_recordType_2 = findViewById(R.id.cb_recordType_2);
        mResetButton = findViewById(R.id.reset_button);
        mConfirmButton = findViewById(R.id.confirm_button);
        initMap();
        initTime();
    }

    private void initMap(){
        patrolTypeMap = new HashMap<>();
        patrolTypeMap.put(patrolTypes[0], false);
        patrolTypeMap.put(patrolTypes[1], false);

        recordTypeMap = new HashMap<>();
        recordTypeMap.put(recordTypes[0], false);
        recordTypeMap.put(recordTypes[1], false);
    }

    private void initTime() {
//        mStartTimeL = System.currentTimeMillis() - 24 * 60 * 60 * 1000;
//        mEndTimeL = System.currentTimeMillis();
//        mStartTime = TimeUtils.getTime(mStartTimeL, DateType.TYPE_YMDHMS.getFormat());
//        mEndTime = TimeUtils.getTime(mEndTimeL, DateType.TYPE_YMDHMS.getFormat());
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
                mOnAlarmChooseListener.onConfirm(patrolTypeMap, recordTypeMap, mStartTime, mEndTime);
                break;

            case R.id.reset_button:
                reset();
                mOnAlarmChooseListener.onReset();
                break;
            default:
                break;
        }
    }

    private void reset(){
        eventType1Cb.setChecked(false);
        eventType2Cb.setChecked(false);
        cb_recordType_1.setChecked(false);
        cb_recordType_2.setChecked(false);
        initMap();
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
        dialog.setMessageFormat(DateType.TYPE_YMDHM.getFormat());
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
                mStartTime = TimeUtils.getTime(selectTime, DateType.TYPE_YMDHMS.getFormat());
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
                mEndTime = TimeUtils.getTime(selectTime, DateType.TYPE_YMDHMS.getFormat());
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
                    eventType1Cb.setBackground(getResources().getDrawable(R.drawable.shape_alarm_checked));
                } else {
                    eventType1Cb.setBackground(getResources().getDrawable(R.drawable.shape_alarm_uncheck));
                }
                patrolTypeMap.put(patrolTypes[0], b);
                break;

            case R.id.cb_eventType2:
                if (b) {
                    eventType2Cb.setBackground(getResources().getDrawable(R.drawable.shape_alarm_checked));
                } else {
                    eventType2Cb.setBackground(getResources().getDrawable(R.drawable.shape_alarm_uncheck));
                }
                patrolTypeMap.put(patrolTypes[1], b);
                break;

            case R.id.cb_recordType_1:
                if (b) {
                    cb_recordType_1.setBackground(getResources().getDrawable(R.drawable.shape_alarm_checked));
                } else {
                    cb_recordType_1.setBackground(getResources().getDrawable(R.drawable.shape_alarm_uncheck));
                }
                recordTypeMap.put(recordTypes[0], b);
                break;

            case R.id.cb_recordType_2:
                if (b) {
                    cb_recordType_2.setBackground(getResources().getDrawable(R.drawable.shape_alarm_checked));
                } else {
                    cb_recordType_2.setBackground(getResources().getDrawable(R.drawable.shape_alarm_uncheck));
                }
                recordTypeMap.put(recordTypes[1], b);
                break;

            default:
                break;
        }
    }

    public interface OnAlarmChooseListener{
        void onConfirm(Map<String, Boolean> patrolType, Map<String, Boolean> recordType,
                       String startTime, String endTime);

        void onReset();
    }

    public void setPatrolTypes(String[] patrolTypes) {
        this.patrolTypes = patrolTypes;
    }

    public void setRecordTypes(String[] recordTypes) {
        this.recordTypes = recordTypes;
    }
}
