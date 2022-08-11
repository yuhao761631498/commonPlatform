package com.gdu.command.ui.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdu.baselibrary.utils.ScreenUtil;
import com.gdu.command.R;
import com.gdu.command.ui.resource.OnCheckBoxChangeListener;
import com.gdu.command.ui.resource.ResourceConfig;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;

/**
 * 资源分布marker显示隐藏弹框
 */
public class ResourceDistributeSelectDialog  extends Dialog {
    private Context mContext;
    private OnCheckBoxChangeListener onCheckBoxChangeListener;

    private CheckBox cb_organization;
    private CheckBox cb_person;
    private CheckBox cb_airplane;
    private CheckBox cb_high_monitor;
    private CheckBox cb_important_region;
    private CheckBox cb_important_circuit;
    private CheckBox cb_focus_point;
    private CheckBox cb_case_info;

    private TextView tv_organization_count;
    private TextView tv_person;
    private TextView tv_airplane;
    private TextView tv_high_monitor;
    private TextView tv_important_region;
    private TextView tv_focus_point;
    private TextView tv_important_circuit;
    private TextView tv_case_info;

    private int org;
    private int person;
    private int airplane;
    private int high;
    private int region;
    private int circuit;
    private int focus;
    private int case_no;

    public ResourceDistributeSelectDialog(Context context) {
        super(context, R.style.DialogTheme);
        this.mContext = context;
        setCanceledOnTouchOutside(true);
    }

    //组织架构复选框点击事件
    public void setOnCheckBoxChangeListener(OnCheckBoxChangeListener listener) {
        this.onCheckBoxChangeListener = listener;
    }

    //获取总数
    public void setTextValue(int org, int person, int airplane, int region, int circuit, int focus, int case_no, int high) {
        this.org = org;
        this.person = person;
        this.airplane = airplane;
        this.region = region;
        this.circuit = circuit;
        this.focus = focus;
        this.case_no = case_no;
        this.high = high;
    }

    /**
     * 选择显示或隐藏的地图marker
     */
    public void showSelectMarkerDialog() {
        View view = getLayoutInflater().inflate(R.layout.layout_resource_select_marker, null);
        setContentView(view);

        cb_organization = view.findViewById(R.id.cb_organization);
        cb_person = view.findViewById(R.id.cb_person);
        cb_airplane = view.findViewById(R.id.cb_airplane);
        cb_high_monitor = view.findViewById(R.id.cb_high_monitor);
        cb_important_region = view.findViewById(R.id.cb_important_region);
        cb_important_circuit = view.findViewById(R.id.cb_important_circuit);
        cb_focus_point = view.findViewById(R.id.cb_focus_point);
        cb_case_info = view.findViewById(R.id.cb_case_info);
        tv_organization_count = view.findViewById(R.id.tv_organization_count);
        tv_person = view.findViewById(R.id.tv_person);
        tv_airplane = view.findViewById(R.id.tv_airplane);
        tv_high_monitor = view.findViewById(R.id.tv_high_monitor);
        tv_important_region = view.findViewById(R.id.tv_important_region);
        tv_focus_point = view.findViewById(R.id.tv_focus_point);
        tv_important_circuit = view.findViewById(R.id.tv_important_circuit);
        tv_case_info = view.findViewById(R.id.tv_case_info);

        tv_organization_count.setText(org > 0 ? String.format("组织架构(%d)", org) : "组织架构");
        tv_person.setText(person > 0 ? String.format("组织人员(%d)", person) : "组织人员");
        tv_airplane.setText(airplane > 0 ? String.format("无人机(%d)", airplane) : "无人机");
        tv_high_monitor.setText(high > 0 ? String.format("高点监控(%d)", high) : "高点监控");
        tv_important_region.setText(region > 0 ? String.format("重点区域(%d)", region) : "重点区域");
        tv_focus_point.setText(focus > 0 ? String.format("关注点位(%d)", focus) : "关注点位");
        tv_important_circuit.setText(circuit > 0 ? String.format("重点线路(%d)", circuit) : "重点线路");
        tv_case_info.setText(case_no > 0 ? String.format("案件信息(%d)", case_no) : "案件信息");

        //获取资源分布Activity的点击状态
        cb_organization.setChecked(ResourceConfig.isOrganizationChecked);
        cb_person.setChecked(ResourceConfig.isPersonChecked);
        cb_airplane.setChecked(ResourceConfig.isAirplaneChecked);
        cb_high_monitor.setChecked(ResourceConfig.isMonitorChecked );
        cb_important_region.setChecked(ResourceConfig.isRegionChecked);
        cb_important_circuit.setChecked(ResourceConfig.isCircuitChecked);
        cb_focus_point.setChecked(ResourceConfig.isFocusChecked );
        cb_case_info.setChecked(ResourceConfig.isCaseChecked);

        //组织架构显示隐藏状态
        cb_organization.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (onCheckBoxChangeListener != null) {
                    onCheckBoxChangeListener.onOrganizationChanged(isChecked);
                }
            }
        });
        //人员信息显示隐藏状态
        cb_person.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (onCheckBoxChangeListener != null) {
                    onCheckBoxChangeListener.onPersonChanged(isChecked);
                }
            }
        });
        //无人机显示隐藏状态
        cb_airplane.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (onCheckBoxChangeListener != null) {
                    onCheckBoxChangeListener.onAirplaneChanged(isChecked);
                }
            }
        });
        //高点监控显示隐藏状态
        cb_high_monitor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (onCheckBoxChangeListener != null) {
                    onCheckBoxChangeListener.onHighPointChanged(isChecked);
                }
            }
        });
        //重点区域显示隐藏状态
        cb_important_region.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (onCheckBoxChangeListener != null) {
                    onCheckBoxChangeListener.onRegionChanged(isChecked);
                }
            }
        });
        //重点线路显示隐藏状态
        cb_important_circuit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (onCheckBoxChangeListener != null) {
                    onCheckBoxChangeListener.onCircuitChanged(isChecked);
                }
            }
        });
        //关注点位显示隐藏状态
        cb_focus_point.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (onCheckBoxChangeListener != null) {
                    onCheckBoxChangeListener.onFocusChanged(isChecked);
                }
            }
        });
        //案件信息显示隐藏状态
        cb_case_info.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (onCheckBoxChangeListener != null) {
                    onCheckBoxChangeListener.onCaseInfoChanged(isChecked);
                }
            }
        });

        setDialogStyle();
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
