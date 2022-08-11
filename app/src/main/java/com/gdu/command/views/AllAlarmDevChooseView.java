package com.gdu.command.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.command.R;
import com.gdu.command.views.drop.AlarmDevAdapter;
import com.gdu.command.views.drop.AlarmDeviceBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预警设备多选界面
 */
public class AllAlarmDevChooseView extends BaseView {

    private RecyclerView contentRv;
    private Button mResetButton;
    private Button mConfirmButton;

    private Map<String, Boolean> mSelectDevices = new HashMap<>();

    private List<AlarmDeviceBean.DataBean.RowsBean> alarmDevData = new ArrayList<>();
    private AlarmDevAdapter mAlarmDevAdapter;

    private OnAlarmChooseListener mOnAlarmChooseListener;

    public AllAlarmDevChooseView(Context context) {
        super(context);
    }

    public AllAlarmDevChooseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AllAlarmDevChooseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnAlarmChooseListener(OnAlarmChooseListener onAlarmChooseListener){
        mOnAlarmChooseListener = onAlarmChooseListener;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.view_all_alarm_dev_choose;
    }

    @Override
    public void initView() {
        super.initView();
        contentRv = findViewById(R.id.rv_content);
        mResetButton = findViewById(R.id.reset_button);
        mConfirmButton = findViewById(R.id.confirm_button);
        initAlarmDeviceRv();
    }

    @Override
    public void initListener() {
        mResetButton.setOnClickListener(this);
        mConfirmButton.setOnClickListener(this);

        mAlarmDevAdapter.setOnItemClickListener((adapter, view, position) -> {
            alarmDevData.get(position).setSelected(!alarmDevData.get(position).isSelected());
            mSelectDevices.put(alarmDevData.get(position).getId() + "", alarmDevData.get(position).isSelected());
            mAlarmDevAdapter.notifyDataSetChanged();

//            selectFilterDevicePosition = position;
//            haveDeviceFilter = true;
//            getPresenter().getAlarmDeviceInfo(alarmDevData.get(position).getDeviceId());
        });
    }

    private void initAlarmDeviceRv() {
        mAlarmDevAdapter = new AlarmDevAdapter(alarmDevData);
        contentRv.setAdapter(mAlarmDevAdapter);
    }

    public void setNewData(List<AlarmDeviceBean.DataBean.RowsBean> data) {
        alarmDevData.clear();
        CommonUtils.listAddAllAvoidNPE(alarmDevData, data);
        mAlarmDevAdapter.setList(data);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.confirm_button:
                mOnAlarmChooseListener.onConfirm(mSelectDevices, mAlarmDevAdapter.getData());
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
        for (final AlarmDeviceBean.DataBean.RowsBean mBean : alarmDevData) {
            mBean.setSelected(false);
        }
        mSelectDevices.clear();
        mAlarmDevAdapter.setList(alarmDevData);
    }

    public interface OnAlarmChooseListener{
        void onConfirm(Map<String, Boolean> devIds, List<AlarmDeviceBean.DataBean.RowsBean> data);
        void onReset();
    }
}
