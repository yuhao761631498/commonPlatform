package com.gdu.command.ui.data;

import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gdu.command.R;

import java.util.List;

/**
 * 案件处置单位排名adapter
 */
public class DeviceRankAdapter extends BaseQuickAdapter<DeviceRankBean.DeviceItemBean, BaseViewHolder> {

    public DeviceRankAdapter(List<DeviceRankBean.DeviceItemBean> data) {
        super(R.layout.item_device_rank_progress_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, DeviceRankBean.DeviceItemBean bean) {
        holder.setText(R.id.tv_no1_device, "N0." + (holder.getAdapterPosition() + 1));
        holder.setText(R.id.tv_device_name, " " + bean.getDeviceName());
        holder.setText(R.id.tv_device_num, " " + bean.getCount());
        ProgressBar progressBar = holder.getView(R.id.progress_bar_data);
        progressBar.setProgress(bean.getCount());
    }
}
