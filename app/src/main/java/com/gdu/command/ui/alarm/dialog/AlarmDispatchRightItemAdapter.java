package com.gdu.command.ui.alarm.dialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gdu.command.R;
import com.gdu.model.organization.OrganizationInfo;

import java.util.List;

public class AlarmDispatchRightItemAdapter extends BaseQuickAdapter<OrganizationInfo.UseBean, BaseViewHolder> {
    public AlarmDispatchRightItemAdapter(@Nullable List<OrganizationInfo.UseBean> data) {
        super(R.layout.item_alarm_dispatch, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, OrganizationInfo.UseBean bean) {
        holder.setText(R.id.tv_content, bean.getUsername());
        if (bean.isSelected()) {
            holder.setImageResource(R.id.iv_status, R.drawable.ic_alarm_checked2);
        } else {
            holder.setImageResource(R.id.iv_status, R.drawable.ic_alarm_uncheck);
        }
    }
}
