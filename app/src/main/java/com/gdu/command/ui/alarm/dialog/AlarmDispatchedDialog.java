package com.gdu.command.ui.alarm.dialog;

import android.content.Context;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gdu.baselibrary.dialog.BottomPushDialog;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.command.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 预警详情已经分派列表弹窗
 * @author wixche
 */
public class AlarmDispatchedDialog extends BottomPushDialog {
    private TextView cancelBtn;
    private RecyclerView rv_dispatchContent;

    private List<AlarmDispatchedBean.DataBean> mData = new ArrayList<>();
    private AlarmDispatchedItemAdapter mAlarmDispatchedItemAdapter;

    public AlarmDispatchedDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_alarm_dispatched);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        initView();
        initListener();
    }

    private void initView() {
        cancelBtn = findViewById(R.id.tv_cancelBtn);
        rv_dispatchContent = findViewById(R.id.rv_dispatchContent);

        initAdapter();
    }

    private void initAdapter() {
        mAlarmDispatchedItemAdapter = new AlarmDispatchedItemAdapter(mData);

        rv_dispatchContent.setAdapter(mAlarmDispatchedItemAdapter);
    }

    public void updateListData(List<AlarmDispatchedBean.DataBean> data) {
        mData.clear();
        CommonUtils.listAddAllAvoidNPE(mData, data);
        mAlarmDispatchedItemAdapter.notifyDataSetChanged();
    }

    private void initListener() {
        cancelBtn.setOnClickListener(v -> {
            dismiss();
        });

    }

}
