package com.gdu.command.ui.alarm.dialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gdu.command.R;
import com.gdu.model.organization.OrganizationInfo;

import java.util.List;

public class AlarmDispatchLeftItemAdapter extends BaseQuickAdapter<OrganizationInfo, BaseViewHolder> {
    public AlarmDispatchLeftItemAdapter(@Nullable List<OrganizationInfo> data) {
        super(R.layout.item_alarm_dispatch, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, OrganizationInfo bean) {
        holder.setText(R.id.tv_content, bean.getDeptName());
        if (bean.isSelected()) {
            holder.setImageResource(R.id.iv_status, R.drawable.ic_alarm_checked1);
        } else {
            holder.setImageResource(R.id.iv_status, R.drawable.ic_alarm_uncheck);
        }
    }
}
