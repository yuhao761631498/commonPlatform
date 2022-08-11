package com.gdu.command.ui.cases;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.baselibrary.utils.imageloade.MyImageLoadUtils;
import com.gdu.command.R;
import com.gdu.command.ui.cases.navigate.NavigateActivity;
import com.gdu.model.alarm.AlarmInfo;
import com.gdu.model.config.GlobalVariable;
import com.gdu.mqttchat.chat.view.ChatActivity;
import com.gdu.util.TimeUtils;

import java.util.List;

public class AlarmRvAdapter extends BaseQuickAdapter<AlarmInfo, BaseViewHolder>
        implements LoadMoreModule {

    public AlarmRvAdapter(@Nullable List<AlarmInfo> data) {
        super(R.layout.item_issue, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, AlarmInfo data) {
        final ImageView headIv = holder.getView(R.id.case_picture_view);
        setAlarmLevelStatus(holder, data, headIv);

        final String issueNameStr = !CommonUtils.isEmptyString(data.getAlarmTypeName()) ?
                "【" + data.getAlarmTypeName() + "】" : "";
        holder.setText(R.id.tv_issue_name, issueNameStr);

        holder.setText(R.id.tv_issue_description, "");
        holder.setText(R.id.tv_issue_location, data.getDeviceAddress());

        final String time = TimeUtils.getTime(data.getCaptureTime());
        holder.setText(R.id.tv_issue_time, time);

//        final String name = !CommonUtils.isEmptyString(data.getReportMan()) ? data.getReportMan()
//                : !CommonUtils.isEmptyString(data.getReport_man()) ? data.getReport_man() : "";
//        final String tel = !CommonUtils.isEmptyString(data.getReportTel()) ?
//                "(" + data.getReportTel() + ")" : "";
//        final String reportMan = name + tel;
        holder.setText(R.id.tv_issue_department, data.getDeviceName());

        holder.setVisible(R.id.issue_handling_layout, false);
        holder.setVisible(R.id.issue_no_handle_layout, false);
        holder.setVisible(R.id.issue_end_textview, false);
        holder.setVisible(R.id.issue_handled_layout, false);
        holder.setVisible(R.id.issue_instruction_layout, false);
        holder.setVisible(R.id.iv_issue_image, false);
//        if (data.getCaseStatus() == CaseStatus.NO_HANDLE.getKey()) {
//            holder.setVisible(R.id.issue_no_handle_layout, false);
//            holder.setImageResource(R.id.iv_issue_image, R.mipmap.issue_unhandle);
//        } else if (data.getCaseStatus() == CaseStatus.HANDLING.getKey()) {
//            holder.setVisible(R.id.issue_handling_layout, false);
//            holder.setImageResource(R.id.iv_issue_image, R.mipmap.issue_handling);
//        } else if (data.getCaseStatus() == CaseStatus.WAIT_COMMENT.getKey()) {
//            holder.setVisible(R.id.issue_instruction_layout, false);
//            holder.setImageResource(R.id.iv_issue_image, R.mipmap.issue_comment);
//        } else {
//            holder.setVisible(R.id.issue_handled_layout, false);
//            holder.setImageResource(R.id.iv_issue_image, R.mipmap.issue_handled);
//            String caseEnd = "案件已结束（" + data.getDispositionTime() + "）";
//            holder.setText(R.id.issue_end_textview, caseEnd);
//        }
//        holder.getView(R.id.tv_instructionDail).setOnClickListener(view -> dialPhone(data.getReportTel()));

        holder.getView(R.id.tv_issue_location).setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), NavigateActivity.class);
//            GlobalVariable.sCurrentIssueBean = data;
            getContext().startActivity(intent);
        });

        holder.getView(R.id.issue_locate_imageview).setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), NavigateActivity.class);
//            GlobalVariable.sCurrentIssueBean = data;
            getContext().startActivity(intent);
        });
//        holder.getView(R.id.dial_layout).setOnClickListener(view -> dialPhone(data.getReportTel()));
        holder.getView(R.id.dial_layout).setVisibility(View.GONE);
//        holder.getView(R.id.issue_dial_imageview).setOnClickListener(view -> dialPhone(data.getReportTel()));
        holder.getView(R.id.issue_chat_imageview).setOnClickListener(view -> {
            Intent chatIntent = new Intent(getContext(), ChatActivity.class);
            chatIntent.putExtra(GlobalVariable.EXTRA_CASE_ID, data.getId());
            getContext().startActivity(chatIntent);
        });
//        holder.getView(R.id.deal_issue_layout).setOnClickListener(view -> {
//            Intent chatIntent = new Intent(getContext(), DealCaseActivity.class);
////            GlobalVariable.sCurrentIssueBean = data;
//            chatIntent.putExtra(GlobalVariable.EXTRA_CASE_STATUS, data.getCaseStatus());
//            getContext().startActivity(chatIntent);
//        });
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
//        holder.setText(R.id.alarm_level_textview, level);
        if (!CommonUtils.isEmptyString(data.getAbbrFrameUrl())) {
            MyImageLoadUtils.loadImage(getContext(), data.getAbbrFrameUrl(),
                    defHeadImgId, headIv);
        } else {
            headIv.setImageResource(defHeadImgId);
        }
//        holder.setTextColor(R.id.alarm_level_textview, getContext().getResources().getColor(levelColor));
//        holder.setBackgroundResource(R.id.alarm_level_textview, levelBkRes);
//        final ImageView attentionView = holder.getView(R.id.iv_attention);
//        attentionView.setSelected(data.getAttention() == 1);
    }

}
