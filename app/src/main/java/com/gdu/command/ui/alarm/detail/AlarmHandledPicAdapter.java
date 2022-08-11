package com.gdu.command.ui.alarm.detail;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gdu.baselibrary.utils.imageloade.MyImageLoadUtils;
import com.gdu.command.R;

import java.util.List;

public class AlarmHandledPicAdapter extends BaseQuickAdapter<AlarmDetailBean.DataBean.AssignmentAndHandleInfoBean.DispositionNodeDataBean.FileKeyBean, BaseViewHolder> {
    public AlarmHandledPicAdapter(@Nullable List<AlarmDetailBean.DataBean.AssignmentAndHandleInfoBean.DispositionNodeDataBean.FileKeyBean> data) {
        super(R.layout.item_alarm_handle_icon, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder,
                           AlarmDetailBean.DataBean.AssignmentAndHandleInfoBean.DispositionNodeDataBean.FileKeyBean bean) {
        final ImageView picIv = holder.getView(R.id.iv_alarm_detail_icon);
        MyImageLoadUtils.loadImage(getContext(), bean.getFileKey(), R.drawable.ic_pic_placehold, picIv);
        holder.setVisible(R.id.iv_videoPlayIcon, "2".equals(bean.getFileType()));
    }
}
