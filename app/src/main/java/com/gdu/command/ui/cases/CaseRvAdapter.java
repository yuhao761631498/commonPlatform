package com.gdu.command.ui.cases;

import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.baselibrary.utils.imageloade.MyImageLoadUtils;
import com.gdu.command.R;
import com.gdu.command.ui.cases.deal.DealCaseActivity;
import com.gdu.command.ui.cases.navigate.NavigateActivity;
import com.gdu.model.cases.CaseStatus;
import com.gdu.model.cases.IssueBean;
import com.gdu.model.config.GlobalVariable;
import com.gdu.mqttchat.chat.view.ChatActivity;

import java.util.List;

public class CaseRvAdapter extends BaseQuickAdapter<IssueBean, BaseViewHolder>
        implements LoadMoreModule {

    public CaseRvAdapter(@Nullable List<IssueBean> data) {
        super(R.layout.item_issue, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder holder, IssueBean data) {
        if (!CommonUtils.isEmptyString(data.getCaseFile())) {
            final String curPicShowUrl = CommonUtils.getSinglePicRealPath(data.getCaseFile());
            MyImageLoadUtils.loadImage(getContext(),
                    curPicShowUrl,
                    R.mipmap.icon_case_default1, holder.getView(R.id.case_picture_view));
        }

        final String issueNameStr = !CommonUtils.isEmptyString(data.getCaseName()) ?
                "【" + data.getCaseName() + "】" : "";
        holder.setText(R.id.tv_issue_name, issueNameStr);

        holder.setText(R.id.tv_issue_description, data.getCaseDesc());
        holder.setText(R.id.tv_issue_location, data.getReportAddr());

        final String timeStr = CommonUtils.isEmptyString(data.getCaseStartTime()) ?
                CommonUtils.isEmptyString(data.getReportTime()) ? "" :
                        data.getReportTime() : data.getCaseStartTime();
        holder.setText(R.id.tv_issue_time, timeStr);

        final String name = !CommonUtils.isEmptyString(data.getReportMan()) ? data.getReportMan()
                : !CommonUtils.isEmptyString(data.getReport_man()) ? data.getReport_man() : "";
        final String tel = !CommonUtils.isEmptyString(data.getReportTel()) ?
                "(" + data.getReportTel() + ")" : "";
        final String reportMan = name + tel;
        holder.setText(R.id.tv_issue_department,
                CommonUtils.isEmptyString(reportMan) ? "--" : reportMan);

        holder.setVisible(R.id.issue_handling_layout, false);
        holder.setVisible(R.id.issue_no_handle_layout, false);
        holder.setVisible(R.id.issue_end_textview, false);
        holder.setVisible(R.id.issue_handled_layout, false);
        holder.setVisible(R.id.issue_instruction_layout, false);
        if (data.getCaseStatus() == CaseStatus.NO_HANDLE.getKey()) {
            holder.setVisible(R.id.issue_no_handle_layout, true);
            holder.setImageResource(R.id.iv_issue_image, R.mipmap.issue_unhandle);
        } else if (data.getCaseStatus() == CaseStatus.HANDLING.getKey()) {
            holder.setVisible(R.id.issue_handling_layout, true);
            holder.setImageResource(R.id.iv_issue_image, R.mipmap.issue_handling);
        } else if (data.getCaseStatus() == CaseStatus.WAIT_COMMENT.getKey()) {
            holder.setVisible(R.id.issue_instruction_layout, true);
            holder.setImageResource(R.id.iv_issue_image, R.mipmap.issue_comment);
        } else {
            holder.setVisible(R.id.issue_handled_layout, true);
            holder.setImageResource(R.id.iv_issue_image, R.mipmap.issue_handled);
            String caseEnd = "案件已结束（" + data.getDispositionTime() + "）";
            holder.setText(R.id.issue_end_textview, caseEnd);
        }
        holder.getView(R.id.tv_instructionDail).setOnClickListener(view -> dialPhone(data.getReportTel()));

        holder.getView(R.id.tv_issue_location).setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), NavigateActivity.class);
            GlobalVariable.sCurrentIssueBean = data;
            getContext().startActivity(intent);
        });

        holder.getView(R.id.issue_locate_imageview).setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), NavigateActivity.class);
            GlobalVariable.sCurrentIssueBean = data;
            getContext().startActivity(intent);
        });
        holder.getView(R.id.dial_layout).setOnClickListener(view -> dialPhone(data.getReportTel()));
        holder.getView(R.id.issue_dial_imageview).setOnClickListener(view -> dialPhone(data.getReportTel()));
        holder.getView(R.id.issue_chat_imageview).setOnClickListener(view -> {
            Intent chatIntent = new Intent(getContext(), ChatActivity.class);
            chatIntent.putExtra(GlobalVariable.EXTRA_CASE_ID, data.getId());
            getContext().startActivity(chatIntent);
        });
        holder.getView(R.id.deal_issue_layout).setOnClickListener(view -> {
            Intent chatIntent = new Intent(getContext(), DealCaseActivity.class);
            GlobalVariable.sCurrentIssueBean = data;
            chatIntent.putExtra(GlobalVariable.EXTRA_CASE_STATUS, data.getCaseStatus());
            getContext().startActivity(chatIntent);
        });
    }

    /**
     * 拨打电话
     * @param phone
     */
    private void dialPhone(String phone){
        if (!CommonUtils.isEmptyString(phone)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phone));
            getContext().startActivity(intent);
        } else {
            ToastUtil.l("电话号码错误");
        }
    }
}
