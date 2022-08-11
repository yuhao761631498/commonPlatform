package com.gdu.command.ui.alarm.dialog;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gdu.command.R;

import java.util.List;

public class AlarmDispatchedItemAdapter extends BaseQuickAdapter<AlarmDispatchedBean.DataBean, BaseViewHolder> {
    public AlarmDispatchedItemAdapter(@Nullable List<AlarmDispatchedBean.DataBean> data) {
        super(R.layout.item_alarm_dispatched, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, AlarmDispatchedBean.DataBean bean) {
//        holder.setImageResource(R.id.iv_icon, bean.getHeadIconId());
        holder.setText(R.id.tv_useName, bean.getUsername());
        holder.setText(R.id.tv_unitName, bean.getOrgName());
        holder.setText(R.id.tv_alarmTime, bean.getCreateTime());
        final TextView statusTv = holder.getView(R.id.tv_status);
        statusTv.setSelected(bean.getHandleType() > 3);
//        statusTv.setText(bean.getHandleTypeName());
        if (bean.getHandleType() > 3) {
            statusTv.setText("已接警");
        } else {
            statusTv.setText("未接警");
        }
    }
}
