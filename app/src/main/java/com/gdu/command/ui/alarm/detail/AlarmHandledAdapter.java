package com.gdu.command.ui.alarm.detail;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gdu.command.R;

import java.util.List;

public class AlarmHandledAdapter extends BaseQuickAdapter<AlarmDetailBean.DataBean.AssignmentAndHandleInfoBean.DispositionNodeDataBean, BaseViewHolder> {
    private OnItemClickListener mSubRvOnItemClickListener;
    public AlarmHandledAdapter(@Nullable List<AlarmDetailBean.DataBean.AssignmentAndHandleInfoBean.DispositionNodeDataBean> data) {
        super(R.layout.item_alarm_handle, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder,
                           AlarmDetailBean.DataBean.AssignmentAndHandleInfoBean.DispositionNodeDataBean bean) {
        holder.setText(R.id.tv_useName, bean.getHandleUserName());
        holder.setText(R.id.tv_unitName, bean.getHandleUserOrgName());
        holder.setText(R.id.tv_punishType, bean.getHandleResultTypeName());
        holder.setText(R.id.tv_handleTime, bean.getHandleTime());
        holder.setText(R.id.tv_descContent, bean.getHandleContent());
        final RecyclerView picRv = holder.getView(R.id.rv_iconContent);
        AlarmHandledPicAdapter mPicAdapter = new AlarmHandledPicAdapter(bean.getFileKeys());
        picRv.setAdapter(mPicAdapter);
        if (mSubRvOnItemClickListener != null) {
            mPicAdapter.setOnItemClickListener(mSubRvOnItemClickListener);
        }
    }

    public void setPicRvItemClickListener(OnItemClickListener listener) {
        mSubRvOnItemClickListener = listener;
    }
}
