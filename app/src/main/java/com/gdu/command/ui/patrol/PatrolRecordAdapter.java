package com.gdu.command.ui.patrol;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.imageloade.MyImageLoadUtils;
import com.gdu.command.R;
import com.gdu.command.ui.patrol.bean.QueryDataBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 巡逻轨迹记录adapter
 * create by zyf
 */
public class PatrolRecordAdapter extends BaseQuickAdapter<QueryDataBean.DataBean, BaseViewHolder> {

    public PatrolRecordAdapter(@Nullable List<QueryDataBean.DataBean> data) {
        super(R.layout.item_patrol_record, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, QueryDataBean.DataBean dataBean) {
        final ImageView iconIv = holder.getView(R.id.left_imageview);
        final String imgStr = CommonUtils.getSinglePicRealPath(dataBean.getPatrolPathPic());
        MyImageLoadUtils.loadImage(getContext(), imgStr,
                R.mipmap.ic_item_default, iconIv);

        holder.setText(R.id.tv_start_time, dataBean.getStartTime());
        holder.setText(R.id.tv_end_time, dataBean.getEndTime());
        holder.setText(R.id.tv_person_name, dataBean.getPatrolUserName());
        final String patrolTypeNameStr = dataBean.getPatrolTypeName();
        if ("LHZF".equals(patrolTypeNameStr)
                || "联合执法".equals(patrolTypeNameStr)) {
            holder.setText(R.id.tv_patrolType, "[联合执法]");
        } else {
            holder.setText(R.id.tv_patrolType, "[" + patrolTypeNameStr + "]");
        }
        holder.setVisible(R.id.tv_one_person, false);
        holder.setVisible(R.id.tv_more_fish, false);
        if (dataBean.getPatrolRecordList() != null && dataBean.getPatrolRecordList().size() > 0) {
            if (dataBean.getPatrolRecordList().size() == 1) {
                holder.setText(R.id.tv_one_person, dataBean.getPatrolRecordList().get(0).getTypeName());
                holder.setVisible(R.id.tv_one_person, true);
            } else if (dataBean.getPatrolRecordList().size() >= 2) {
                holder.setText(R.id.tv_one_person, dataBean.getPatrolRecordList().get(0).getTypeName());
                holder.setVisible(R.id.tv_one_person, true);
                holder.setText(R.id.tv_more_fish, dataBean.getPatrolRecordList().get(1).getTypeName());
                holder.setVisible(R.id.tv_more_fish, true);
                //大于3条显示圆点
                holder.setVisible(R.id.tv_oval_1, dataBean.getPatrolRecordList().size() > 2);
            } else {
                holder.setVisible(R.id.tv_one_person, false);
                holder.setVisible(R.id.tv_more_fish, false);
            }
        } else {
            holder.setVisible(R.id.tv_one_person, false);
            holder.setVisible(R.id.tv_more_fish, false);
        }
    }
}
