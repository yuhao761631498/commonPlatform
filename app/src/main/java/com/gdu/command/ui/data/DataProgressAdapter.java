package com.gdu.command.ui.data;

import android.widget.ProgressBar;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gdu.command.R;
import java.util.List;

/**
 * 案件处置单位排名adapter
 */
public class DataProgressAdapter extends BaseQuickAdapter<DataCaseRankBean.DataBean, BaseViewHolder> {

    public DataProgressAdapter(List<DataCaseRankBean.DataBean> data) {
        super(R.layout.item_data_monitor_progress_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, DataCaseRankBean.DataBean bean) {
        holder.setText(R.id.tv_no1, "N0." + (holder.getAdapterPosition()+1));
        holder.setText(R.id.tv_area_name, " " + bean.getDeptName());
        ProgressBar progressBar = holder.getView(R.id.progress_bar_data);
        progressBar.setProgress(bean.getCaseCount());
    }
}
