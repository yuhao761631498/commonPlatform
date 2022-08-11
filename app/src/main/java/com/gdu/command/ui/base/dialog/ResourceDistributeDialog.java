package com.gdu.command.ui.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.gdu.baselibrary.utils.CommonUtils;
import com.gdu.baselibrary.utils.ScreenUtil;
import com.gdu.baselibrary.utils.imageloade.MyImageLoadUtils;
import com.gdu.command.R;
import com.gdu.command.ui.resource.OnDialogCallListener;
import com.gdu.command.ui.resource.OnDialogClickListener;
import com.gdu.model.config.UrlConfig;
import com.gdu.model.resource.ResAirplaneInfoBean;
import com.gdu.model.resource.ResCaseInfoBean;
import com.gdu.model.resource.ResCircuitBean;
import com.gdu.model.resource.ResFocusPointBean;
import com.gdu.model.resource.ResHighPointDeviceBean;
import com.gdu.model.resource.ResOrganizationBean;
import com.gdu.model.resource.ResPersonInfoBean;
import com.gdu.model.resource.ResRegionBean;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;

/**
 * 资源分布marker点击对话框
 * create by zyf
 */
public class ResourceDistributeDialog extends Dialog {

    private Context mContext;
    private OnDialogClickListener onDialogClickListener;
    private OnDialogCallListener onDialogCallListener;

    public ResourceDistributeDialog(Context context) {
        super(context, R.style.DialogTheme);
        this.mContext = context;
        setCanceledOnTouchOutside(true);
    }

    /**
     * 弹框点击事件外部调用方法
     */
    public void setOnDialogClickListener(OnDialogClickListener listener) {
        this.onDialogClickListener = listener;
    }

    /**
     * 案件信息
     */
    public void showCaseInfoDialog(ResCaseInfoBean.RecordsDTO bean) {
        View view = getLayoutInflater().inflate(R.layout.layout_resource_case_info, null);
        setContentView(view);

        //组织架构
        TextView tvOrganization = view.findViewById(R.id.tv_organization);
        //报案地址
        TextView tvReportCaseAddress = view.findViewById(R.id.tv_report_case_address);
        //报案时间
        TextView tvCaseTime = view.findViewById(R.id.tv_case_time);
        //报案描述
        TextView tvCaseDesc = view.findViewById(R.id.tv_case_desc);
        //案件状态
        TextView tvCaseStatus = view.findViewById(R.id.tv_case_status);
        //到这里去
        RelativeLayout rlGoHere = view.findViewById(R.id.rl_go_here);

        tvOrganization.setText(!TextUtils.isEmpty(bean.getCaseName()) ? bean.getCaseName() : "");
        tvReportCaseAddress.setText(!TextUtils.isEmpty(bean.getReportAddr()) ? bean.getReportAddr() : "");
        tvCaseTime.setText(!TextUtils.isEmpty(bean.getCreateTime()) ? bean.getCreateTime() : "");
        tvCaseDesc.setText(!TextUtils.isEmpty(bean.getCaseDesc()) ? bean.getCaseDesc() : "");
        switch (bean.getCaseStatus()) {
            case "0":
                tvCaseStatus.setText("未处置");
                break;
            case "1":
                tvCaseStatus.setText("已处置");
                break;
            case "2":
                tvCaseStatus.setText("处置中");
                break;
        }

        //点击到这里去
        rlGoHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDialogClickListener != null) {
                    onDialogClickListener.onCaseInfoGuideListener(
                            bean.getLatitude(),
                            bean.getLongitude(),
                            bean.getReportAddr());
                }
                dismiss();
            }
        });

        setDialogStyle();
        show();
    }

    /**
     * 组织机构
     */
    public void showOrganizationDialog(ResOrganizationBean bean) {
        View view = getLayoutInflater().inflate(R.layout.layout_resource_organization, null);
        setContentView(view);

        //名称
        TextView tvOrganization = view.findViewById(R.id.tv_organization_name);
        //位置
        TextView tvReportCaseAddress = view.findViewById(R.id.tv_position);
        //到这里去
        ImageView rlGoHere = view.findViewById(R.id.rl_go_here_org);

        tvOrganization.setText(!TextUtils.isEmpty(bean.getDeptName()) ? bean.getDeptName() : "");
        tvReportCaseAddress.setText(!TextUtils.isEmpty(bean.getDeptAddress()) ? bean.getDeptAddress() : "");

        //点击到这里去
        rlGoHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDialogClickListener != null) {
                    onDialogClickListener.onOrganizationGuideListener(
                            bean.getDeptLatitude(),
                            bean.getDeptLongitude(),
                            bean.getDeptAddress());
                }
                dismiss();
            }
        });

        setDialogStyle();
        show();
    }

    /**
     * 人员信息
     */
    public void showPersonInfoDialog(ResPersonInfoBean bean) {
        View view = getLayoutInflater().inflate(R.layout.layout_resource_person_info, null);
        setContentView(view);

        //姓名
        TextView tvName = view.findViewById(R.id.tv_person_name);
        //所属单位
        TextView tvBelongUnit = view.findViewById(R.id.tv_belong_unit);
        //联系电话
        TextView tvPhone = view.findViewById(R.id.tv_contact_phone);
        //拨打联系电话
        ImageView ivCallContact = view.findViewById(R.id.iv_call_phone);
        //机构电话
        TextView tvOrganizationPhone = view.findViewById(R.id.tv_organization_phone);
        //拨打机构电话
        ImageView ivCallOrganization = view.findViewById(R.id.iv_call_organization_phone);
        //人员状态
        TextView tvPersonStatus = view.findViewById(R.id.tv_person_status);

        tvName.setText(!TextUtils.isEmpty(bean.getEmployeeName()) ? bean.getEmployeeName() : "");
        tvBelongUnit.setText(!TextUtils.isEmpty(bean.getDeptName()) ? bean.getDeptName() : "");
        tvPhone.setText(!TextUtils.isEmpty(bean.getEmployeeTel()) ? bean.getEmployeeTel() : "");
        tvOrganizationPhone.setText(!TextUtils.isEmpty(bean.getOfficePhone()) ? bean.getOfficePhone() : "");
        tvPersonStatus.setText(!TextUtils.isEmpty(bean.getEmStatus()) ? bean.getEmStatus() : "");
        tvPersonStatus.setTextColor(tvPersonStatus.getText().equals("任务中") ? Color.parseColor("#FF0000") : Color.parseColor("#343434"));
        ivCallContact.setVisibility(!TextUtils.isEmpty(tvPhone.getText()) ? View.GONE : View.VISIBLE);
        ivCallOrganization.setVisibility(!TextUtils.isEmpty(tvOrganizationPhone.getText()) ? View.GONE : View.VISIBLE);

        //拨打联系电话
        ivCallContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDialogCallListener != null) {
                    onDialogCallListener.onClick(tvPhone.getText().toString());
                }
                dismiss();
            }
        });

        //拨打机构电话
        ivCallOrganization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDialogCallListener != null) {
                    onDialogCallListener.onClick(tvOrganizationPhone.getText().toString());
                }
                dismiss();
            }
        });

        setDialogStyle();
        show();
    }

    /**
     * 人员信息点击拨打联系电话
     */
    public void setOnPersonPhoneListener(OnDialogCallListener listener) {
        this.onDialogCallListener = listener;
    }

    /**
     * 人员信息点击拨打机构电话
     */
    public void setOnPersonOrgListener(OnDialogCallListener listener) {
        this.onDialogCallListener = listener;
    }

    /**
     * 无人机信息
     */
    public void showAirplaneInfoDialog(ResAirplaneInfoBean bean) {
        View view = getLayoutInflater().inflate(R.layout.layout_resource_airplane, null);
        setContentView(view);

        //左侧图片
        ImageView imageView = view.findViewById(R.id.iv_airplane_info);
        //名称
        TextView tvName = view.findViewById(R.id.tv_airplane_name);
        //SN码
        TextView tvSN = view.findViewById(R.id.tv_SN_no);
        //状态
        TextView tvState = view.findViewById(R.id.tv_airplane_state);
        //品牌
        TextView tvBrand = view.findViewById(R.id.tv_brand_name);
        //经度
        TextView tvLongitude = view.findViewById(R.id.tv_longitude);
        //纬度
        TextView tvLatitude = view.findViewById(R.id.tv_latitude);
        //高度
        TextView tvHigh = view.findViewById(R.id.tv_high);
        //水平速度
        TextView tvLevelSpeed = view.findViewById(R.id.tv_level_speed);
        //高度方向速度
        TextView tvHighSpeed = view.findViewById(R.id.tv_high_speed);
        //电量
        TextView tvBattery = view.findViewById(R.id.tv_battery);
        //航向角
        TextView tvAngel = view.findViewById(R.id.tv_course_angle);
        //通话
        TextView tvConverse = view.findViewById(R.id.tv_converse);
        //调度
        TextView tvDispatch = view.findViewById(R.id.tv_dispatch);

        MyImageLoadUtils.loadImage(mContext, UrlConfig.BASE_IP + bean.getIconUrl(),imageView);
        tvName.setText(!TextUtils.isEmpty(bean.getDeviceName()) ? bean.getDeviceName() : "");
        tvState.setText(bean.getOnlineStatus().equals("online") ? "在线" : "离线");
        tvSN.setText(!TextUtils.isEmpty(bean.getDeviceCode()) ? bean.getDeviceCode() : "");
        tvBrand.setText(!TextUtils.isEmpty(bean.getDeviceBrand()) ? bean.getDeviceBrand() : "");
        tvLongitude.setText( "-");
        tvLatitude.setText( "-");
        tvHigh.setText("-");
        tvLevelSpeed.setText( "-");
        tvHighSpeed.setText( "-");
        tvBattery.setText( "-");
        tvAngel.setText("-");

        tvConverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

        tvDispatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

        setDialogStyle();
        show();
    }

    /**
     * 掌控范围（重点区域）信息
     */
    public void showControlScopeDialog(ResRegionBean bean) {
        View view = getLayoutInflater().inflate(R.layout.layout_resource_control_scope, null);
        setContentView(view);

        //名称
        TextView tvControlName = view.findViewById(R.id.tv_control_name);
        //类型
        TextView tvType = view.findViewById(R.id.tv_type);
        //备注
        TextView tRemark = view.findViewById(R.id.tv_remark);

        tvControlName.setText(bean.getResourcesName());
        tvType.setText(bean.getResourcesTypeName());
        tRemark.setText(bean.getResourcesRemark());

        setDialogStyle();
        show();
    }

    /**
     * 线路信息
     */
    public void showCircuitDialog(ResCircuitBean bean) {
        View view = getLayoutInflater().inflate(R.layout.layout_resource_circuit_info, null);
        setContentView(view);

        //线路名称
        TextView tv_circuit_name = view.findViewById(R.id.tv_circuit_name);
        //类型
        TextView tv_circuit_type = view.findViewById(R.id.tv_circuit_type);
        //总长度
        TextView tv_total_extend = view.findViewById(R.id.tv_total_extend);
        //起点
        TextView tv_start_point = view.findViewById(R.id.tv_start_point);
        //终点
        TextView tv_end_point = view.findViewById(R.id.tv_end_point);

        tv_circuit_name.setText(!TextUtils.isEmpty(bean.getResourcesName()) ? bean.getResourcesName() : "");
        tv_circuit_type.setText(!TextUtils.isEmpty(bean.getResourcesTypeName()) ? bean.getResourcesTypeName() : "");
        tv_total_extend.setText(bean.getTotalLength() > 0 ? bean.getTotalLength() + "/km" : "");
        tv_start_point.setText(!TextUtils.isEmpty(bean.getLineOrigin()) ? bean.getLineOrigin() : "");
        tv_end_point.setText(!TextUtils.isEmpty(bean.getLineDestination()) ? bean.getLineDestination() : "");

        setDialogStyle();
        show();
    }

    /**
     * 重点点位
     */
    public void showEmphasisPointDialog(ResFocusPointBean bean) {
        View view = getLayoutInflater().inflate(R.layout.layout_resource_emphasis_point, null);
        setContentView(view);

        //点位名称
        TextView tv_point_name = view.findViewById(R.id.tv_point_name);
        //点位类型
        TextView tv_point_type = view.findViewById(R.id.tv_point_type);
        //位置
        TextView tv_place = view.findViewById(R.id.tv_place);
        //到这里去
        RelativeLayout rlGoHere = view.findViewById(R.id.rl_go_here_point);

        tv_point_name.setText(!TextUtils.isEmpty(bean.getResourcesName()) ? bean.getResourcesName() : "");
        tv_point_type.setText(!TextUtils.isEmpty(bean.getResourcesTypeName()) ? bean.getResourcesTypeName() : "");
        tv_place.setText(!TextUtils.isEmpty(bean.getResourcesAddr()) ? bean.getResourcesAddr() : "");

        rlGoHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDialogClickListener != null) {
                    onDialogClickListener.onEmphasisPointGuideListener(
                            bean.getResourcesLatitude(),
                            bean.getResourcesLongitude(),
                            bean.getResourcesAddr());
                }
                dismiss();
            }
        });

        setDialogStyle();
        show();
    }

    /**
     * 高点监控信息
     */
    public void showHighMonitorInfoDialog(ResHighPointDeviceBean bean) {
        View view = getLayoutInflater().inflate(R.layout.layout_resource_high_monitor_info, null);
        setContentView(view);

        //图片
        ImageView iv_device = view.findViewById(R.id.iv_device);
        //设备名称
        TextView tv_device_name = view.findViewById(R.id.tv_device_name_title);
        //设备编号
        TextView tv_device_no = view.findViewById(R.id.tv_device_no_title);
        //设备品牌
        TextView tv_device_brand = view.findViewById(R.id.tv_device_brand_tile);
        //设备状态
        TextView tv_device_state = view.findViewById(R.id.tv_device_state_tile);
        //设备所在地
        TextView tv_place = view.findViewById(R.id.tv_place_tile);
        //视频
        TextView tv_video = view.findViewById(R.id.tv_video);
        //云台操作
        TextView tv_operate = view.findViewById(R.id.tv_operate);

        final String realUrl = CommonUtils.getSinglePicRealPath(bean.getIconUrl());
        MyImageLoadUtils.loadImage(mContext, realUrl, iv_device);
        tv_device_name.setText(!TextUtils.isEmpty(bean.getDeviceName()) ? "设备名称：" + bean.getDeviceName() : "");
        tv_device_no.setText(!TextUtils.isEmpty(bean.getDeviceCode()) ? "设备编号：" + bean.getDeviceCode() : "");
        tv_device_brand.setText(!TextUtils.isEmpty(bean.getDeviceBrand()) ? "设备品牌：" + bean.getDeviceBrand() : "");
        tv_device_state.setText(bean.getOnlineStatus().equals("online") ? "设备状态：" + "在线" : "设备状态：" + "离线");
        tv_place.setText(!TextUtils.isEmpty(bean.getDeviceAddress()) ? "所在地点：" + bean.getDeviceAddress() : "");

        tv_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDialogClickListener != null) {
                    onDialogClickListener.onMonitorVideoListener();
                }
                dismiss();
            }
        });

        tv_operate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDialogClickListener != null) {
                    onDialogClickListener.onMonitorDispatchListener();
                }
                dismiss();
            }
        });

        setDialogStyle();
        show();
    }

    /**
     * 设置弹框样式
     */
    private void setDialogStyle() {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = ScreenUtil.getScreenWidth(mContext) - CommonUtil.dip2px(mContext, 20);
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.gravity = Gravity.BOTTOM;
        }
    }
}
