package com.gdu.command.ui.alarm;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.imageloade.MyImageLoadUtils;
import com.gdu.command.R;
import com.gdu.model.alarm.AlarmInfo;
import com.gdu.util.TimeUtils;

import java.util.List;

public class AlarmCenterAdapter extends BaseQuickAdapter<AlarmInfo, BaseViewHolder> {

    public AlarmCenterAdapter(@Nullable List<AlarmInfo> data) {
        super(R.layout.item_alarm_center, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, AlarmInfo data,
                           @NonNull List<?> payloads) {
        if (payloads != null && payloads.size() > 0) {
            final ImageView attentionView = holder.getView(R.id.iv_attention);
            attentionView.setSelected(data.getAttention() == 1);
        } else {
            super.convert(holder, data, payloads);
        }
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, AlarmInfo data) {
        final ImageView headIv = holder.getView(R.id.header_imageview);
        String nameTvStr;
        if (CommonUtils.isEmptyString(data.getDeviceName())) {
            nameTvStr = data.getCreateTime() + ", " + data.getAlarmTypeName();
        } else {
            nameTvStr = data.getCreateTime() + ", " + data.getDeviceName() + ", " + data.getAlarmTypeName();
        }
        holder.setText(R.id.alarm_name_textview, nameTvStr);
        setAlarmStatus(holder, data);

        setAlarmLevelStatus(holder, data, headIv);

        holder.setText(R.id.alarm_device_textview, "告警设备：" + data.getDeviceName());

        holder.setText(R.id.alarm_position_textview, data.getDeviceAddress());
        final String time = TimeUtils.getTime(data.getCaptureTime());
        holder.setText(R.id.alarm_time_textview,  time);

        //alarmType = 2，stayTime才有值
//        if (data.getAlarmType() == 2) {
//            holder.setText(R.id.alarm_detained_textview, "滞留" + data.getStayTime() + "分钟");
//            holder.setVisible(R.id.alarm_detained_textview, true);
//        } else {
//            holder.setVisible(R.id.alarm_detained_textview, false);
//        }
    }

    private void setAlarmLevelStatus(@NonNull BaseViewHolder holder, AlarmInfo data, ImageView headIv) {
        int levelColor = R.color.color_5099FD;
        int levelBkRes = R.drawable.shape_alarm_level_blue;
        String level;
        int defHeadImgId;
        switch (data.getAlarmLevel()) {

            case 2:
                level = "黄色预警";
                levelColor = R.color.color_F7B500;
                levelBkRes = R.drawable.shape_alarm_level_yellow;
                defHeadImgId = R.mipmap.icon_alarm_center_yellow_def;
                break;

            case 3:
                level = "橙色预警";
                levelColor = R.color.color_FF8200;
                levelBkRes = R.drawable.shape_alarm_level_origin;
                defHeadImgId = R.mipmap.icon_alarm_center_orange_def;
                break;

            case 4:
                level = "红色预警";
                levelColor = R.color.color_F24343;
                levelBkRes = R.drawable.shape_alarm_level_red;
                defHeadImgId = R.mipmap.icon_alarm_center_red_def;
                break;

            default:
                level = "蓝色预警";
                defHeadImgId = R.mipmap.icon_alarm_center_blue_def;
                break;
        }
        holder.setText(R.id.alarm_level_textview, level);
        if (!CommonUtils.isEmptyString(data.getAbbrFrameUrl())) {
            MyImageLoadUtils.loadImage(getContext(), data.getAbbrFrameUrl(),
                    defHeadImgId, headIv);
        } else {
            headIv.setImageResource(defHeadImgId);
        }
        holder.setTextColor(R.id.alarm_level_textview, getContext().getResources().getColor(levelColor));
        holder.setBackgroundResource(R.id.alarm_level_textview, levelBkRes);
        final ImageView attentionView = holder.getView(R.id.iv_attention);
        attentionView.setSelected(data.getAttention() == 1);
    }

    private void setAlarmStatus(@NonNull BaseViewHolder holder, AlarmInfo data) {
        String status = "";
        int bkRes = 0;
        int textColorId = 0;
        switch (data.getHandleType()) {
            case 1:
                status = "待核实";
                bkRes = R.drawable.shape_alarm_status_blue;
                textColorId = R.color.color_DC9954;
                break;

            case 3:
                status = "待接警";
                bkRes = R.drawable.shape_alarm_status_yellow;
                textColorId = R.color.color_DCD726;
                break;

            case 4:
                status = "待处理";
                bkRes = R.drawable.shape_alarm_status_red;
                textColorId = R.color.color_FF5B5B;
                break;

            case 5:
                status = "已处理";
                bkRes = R.drawable.shape_alarm_status_green;
                textColorId = R.color.color_79AA57;
                break;

            default:
                break;
        }
        if (CommonUtils.isEmptyString(status)) {
            return;
        }
        final TextView statusTv = holder.getView(R.id.alarm_status_textview);
        statusTv.setText(status);
        statusTv.setTextColor(ContextCompat.getColor(getContext(), textColorId));
        statusTv.setBackgroundResource(bkRes);
    }
}
