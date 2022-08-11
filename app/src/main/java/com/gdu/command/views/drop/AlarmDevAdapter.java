package com.gdu.command.views.drop;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gdu.command.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AlarmDevAdapter extends BaseQuickAdapter<AlarmDeviceBean.DataBean.RowsBean, BaseViewHolder> {

    public AlarmDevAdapter(List<AlarmDeviceBean.DataBean.RowsBean> list) {
        super(R.layout.item_list_drop_down, list);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, AlarmDeviceBean.DataBean.RowsBean bean) {
        final TextView contentTv = holder.getView(R.id.text);
        contentTv.setText(bean.getDeviceName());

        if (bean.isSelected()) {
            contentTv.setTextColor(getContext().getResources().getColor(R.color.drop_down_selected));
            contentTv.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    getContext().getResources().getDrawable(R.mipmap.drop_down_checked), null);
        } else {
            contentTv.setTextColor(getContext().getResources().getColor(R.color.drop_down_unselected));
            contentTv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

}
