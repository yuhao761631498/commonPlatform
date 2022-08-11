package com.gdu.command.ui.alarm.dialog;

import android.content.Context;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gdu.baselibrary.dialog.BottomPushDialog;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.ToastUtil;
import com.gdu.command.R;
import com.gdu.model.organization.OrganizationInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 预警详情分派弹窗
 * @author wixche
 */
public class AlarmDispatchDialog extends BottomPushDialog {
    private TextView cancelBtn, confirmBtn;
    private RecyclerView rv_leftGroup, rv_rightGroup;
    private EditText et_remarkContent;

    private List<OrganizationInfo> mLeftItemData = new ArrayList<>();
    private AlarmDispatchLeftItemAdapter mLeftItemAdapter;

    private List<OrganizationInfo.UseBean> mRightItemData = new ArrayList<>();
    private AlarmDispatchRightItemAdapter mRightItemAdapter;

    private List<OrganizationInfo.UseBean> mDispatchUsers = new ArrayList<>();

    private IDispatchCallback mDispatchCallback;

    public AlarmDispatchDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_alarm_dispatch);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        cancelBtn = findViewById(R.id.tv_cancelBtn);
        confirmBtn = findViewById(R.id.tv_confirmBtn);
        rv_leftGroup = findViewById(R.id.rv_leftGroup);
        rv_rightGroup = findViewById(R.id.rv_rightGroup);
        et_remarkContent = findViewById(R.id.et_remarkContent);
    }

    private void initData() {
        mLeftItemAdapter = new AlarmDispatchLeftItemAdapter(mLeftItemData);
        mRightItemAdapter = new AlarmDispatchRightItemAdapter(mRightItemData);
        
        mLeftItemAdapter.setOnItemClickListener(((adapter, view, position) -> {
            for (int i = 0; i < adapter.getData().size(); i++) {
                final OrganizationInfo bean = (OrganizationInfo)adapter.getData().get(i);
                bean.setSelected(false);
            }
            final OrganizationInfo leftSelectBean = (OrganizationInfo) adapter.getData().get(position);
            leftSelectBean.setSelected(true);
            mLeftItemAdapter.notifyDataSetChanged();
            mRightItemData.clear();
            CommonUtils.listAddAllAvoidNPE(mRightItemData, leftSelectBean.getUsers());
            mRightItemAdapter.notifyDataSetChanged();
        }));

        mRightItemAdapter.setOnItemClickListener(((adapter, view, position) -> {
            final OrganizationInfo.UseBean dispatchUseBean =
                    (OrganizationInfo.UseBean) adapter.getData().get(position);
            if (!dispatchUseBean.isSelected()) {
                boolean haveUse = false;
                for (int i = 0; i < mDispatchUsers.size(); i++) {
                    if (mDispatchUsers.get(i).getId() == dispatchUseBean.getId()) {
                        haveUse = true;
                        break;
                    }
                }
                if (!haveUse) {
                    CommonUtils.listAddAvoidNull(mDispatchUsers, dispatchUseBean);
                }
            } else {
                mDispatchUsers.remove(dispatchUseBean);
            }
            dispatchUseBean.setSelected(!dispatchUseBean.isSelected());
            mRightItemAdapter.notifyItemChanged(position);
        }));

        rv_leftGroup.setAdapter(mLeftItemAdapter);
        rv_rightGroup.setAdapter(mRightItemAdapter);
    }

    private void clearSelectUse() {
        for (int i = 0; i < mLeftItemData.size(); i++) {
            mLeftItemData.get(i).setSelected(false);
            for (int j = 0; j < mLeftItemData.get(i).getUsers().size(); j++) {
                final OrganizationInfo.UseBean mUseBean = mLeftItemData.get(i).getUsers().get(j);
                mUseBean.setSelected(false);
            }
        }
        if (mDispatchUsers != null && mDispatchUsers.size() > 0) {
            for (int j = 0; j < mDispatchUsers.size(); j++) {
                mDispatchUsers.get(j).setSelected(false);
            }
        }
        mDispatchUsers.clear();
    }

    public void updateOrganizationList(List<OrganizationInfo> organizationInfos) {
        if (CommonUtils.isEmptyList(organizationInfos)) {
            return;
        }
        mLeftItemData.clear();
        CommonUtils.listAddAllAvoidNPE(mLeftItemData, organizationInfos);
        mLeftItemAdapter.setList(mLeftItemData);
    }

    private void initListener() {
        cancelBtn.setOnClickListener(v -> {
            dismiss();
        });
        confirmBtn.setOnClickListener(v -> {
            final String remarkStr = et_remarkContent.getText().toString().trim();
            if (CommonUtils.isEmptyList(mDispatchUsers)) {
                ToastUtil.s("请选择分派用户");
                return;
            }
            if (mDispatchCallback != null) {
                mDispatchCallback.callBackParam(mDispatchUsers, remarkStr);
            }

            clearSelectUse();
            et_remarkContent.setText("");

            dismiss();
        });
    }

    public void setDispatchCallback(IDispatchCallback dispatchCallback) {
        mDispatchCallback = dispatchCallback;
    }

    public interface IDispatchCallback {
        void callBackParam(List<OrganizationInfo.UseBean> dispatchUsers, String remark);
    }

}
