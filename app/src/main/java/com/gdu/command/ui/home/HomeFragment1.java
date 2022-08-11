package com.gdu.command.ui.home;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdu.baselibrary.base.BaseFragment;
import com.gdu.command.R;
import com.gdu.command.ui.alarm.center.AlarmCenterActivityNew;
import com.gdu.command.ui.data.ForewarnStatActivity;
import com.gdu.command.ui.patrol.view.PatrolDiaryActivity;
import com.gdu.command.ui.resource.ManMachineProtectionActivity;
import com.gdu.model.config.GlobalVariable;

import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;

public class HomeFragment1 extends BaseFragment<HomePresenter1> implements View.OnClickListener, IHomeView1 {

    private TextView alarm_center_layout;
    private TextView video_control_layout;
    private TextView tv_patrolDiary;
    private TextView tv_fore_warn;
    private TextView tv_today_alarm_count;
    private ConstraintLayout inc_find_ship;
    private ConstraintLayout inc_find_people;
    private TextView tv_find_people_number;
    private ConstraintLayout inc_illegal_fish;
    private ConstraintLayout inc_staff_retention;
    private TextView tv_find_ship_number;
    private TextView tv_staff_retention_number;
    private TextView tv_illegal_fish_number;
    private ConstraintLayout inc_wait_receive_alarm;
    private TextView tv_wait_receive_alarm_number;
    private ConstraintLayout inc_wait_deal;
    private TextView tv_wait_deal_alarm_number;
    private ConstraintLayout inc_has_deal;
    private TextView tv_has_deal_alarm_number;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_1;
    }

    @Override
    protected void initialize() {
        initView();
        initListener();

        getPresenter().setView(this);
        getPresenter().requestData();
    }

    private void initListener() {
        alarm_center_layout.setOnClickListener(this);
        video_control_layout.setOnClickListener(this);
        tv_patrolDiary.setOnClickListener(this);
        tv_fore_warn.setOnClickListener(this);
    }

    private void initView() {
        alarm_center_layout = requireView().findViewById(R.id.alarm_center_layout);  //预警信息
        video_control_layout = requireView().findViewById(R.id.video_control_layout);  //
        tv_patrolDiary = requireView().findViewById(R.id.tv_patrolDiary);
        tv_fore_warn = requireView().findViewById(R.id.tv_fore_warn);

        tv_today_alarm_count = requireView().findViewById(R.id.tv_today_alarm_count);

        initFindShip();
        initFindPeople();
        initIllegalFish();
        initStaffRetention();

        initWaitReceiveAlarm();
        initWaitDealAlarm();
        initHasDealAlarm();

        tv_today_alarm_count.setText(GlobalVariable.alarmTotalNum);
    }

    private void initHasDealAlarm() {
        inc_has_deal = requireView().findViewById(R.id.inc_has_deal);
        ImageView iv_wait_deal_alarm = inc_has_deal.findViewById(R.id.iv_alarm_status_icon);
        iv_wait_deal_alarm.setImageResource(R.mipmap.has_deal_alarm);
        tv_has_deal_alarm_number = inc_has_deal.findViewById(R.id.tv_alarm_status_number);
        TextView tv_alarm_status_name = inc_has_deal.findViewById(R.id.tv_alarm_status);
        tv_alarm_status_name.setText("已处理");
        tv_has_deal_alarm_number.setText(GlobalVariable.hasDealAlarm);

    }

    private void initWaitDealAlarm() {
        inc_wait_deal = requireView().findViewById(R.id.inc_wait_deal);
        ImageView iv_wait_deal_alarm = inc_wait_deal.findViewById(R.id.iv_alarm_status_icon);
        iv_wait_deal_alarm.setImageResource(R.mipmap.wait_deal_alarm);
        tv_wait_deal_alarm_number = inc_wait_deal.findViewById(R.id.tv_alarm_status_number);
        TextView tv_alarm_status_name = inc_wait_deal.findViewById(R.id.tv_alarm_status);
        tv_alarm_status_name.setText("待处理");
        tv_wait_deal_alarm_number.setText(GlobalVariable.waitDealAlarm);
    }

    private void initWaitReceiveAlarm() {
        inc_wait_receive_alarm = requireView().findViewById(R.id.inc_wait_receive_alarm);
        ImageView iv_wait_receive_alarm = inc_wait_receive_alarm.findViewById(R.id.iv_alarm_status_icon);
        iv_wait_receive_alarm.setImageResource(R.mipmap.wait_receive_alarm);
        tv_wait_receive_alarm_number = inc_wait_receive_alarm.findViewById(R.id.tv_alarm_status_number);
        TextView tv_alarm_status_name = inc_wait_receive_alarm.findViewById(R.id.tv_alarm_status);
        tv_alarm_status_name.setText("待接警");
        tv_wait_receive_alarm_number.setText(GlobalVariable.waitReceiveAlarm);
    }

    private void initStaffRetention() {
        inc_staff_retention = requireView().findViewById(R.id.inc_staff_retention);
        ImageView iv_illegal_fish = inc_staff_retention.findViewById(R.id.iv_find_ship);
        tv_staff_retention_number = inc_staff_retention.findViewById(R.id.tv_find_ship_number);
        iv_illegal_fish.setImageResource(R.mipmap.icon_staff_retention);
        TextView tv_illegal_fish = inc_staff_retention.findViewById(R.id.tv_illegal_name);
        tv_illegal_fish.setText("非法捕捞");
//      tv_illegal_fish.setText("人员滞留");
        tv_staff_retention_number.setText(GlobalVariable.staffRetention);
    }

    private void initIllegalFish() {
        inc_illegal_fish = requireView().findViewById(R.id.inc_illegal_fish);
        ImageView iv_illegal_fish = inc_illegal_fish.findViewById(R.id.iv_find_ship);
        iv_illegal_fish.setImageResource(R.mipmap.icon_illegal_finsh);
        tv_illegal_fish_number = inc_illegal_fish.findViewById(R.id.tv_find_ship_number);
        TextView tv_illegal_fish = inc_illegal_fish.findViewById(R.id.tv_illegal_name);
        tv_illegal_fish.setText("非法垂钓");
        tv_illegal_fish_number.setText(GlobalVariable.illegalFish);
    }

    private void initFindShip() {
        inc_find_ship = requireView().findViewById(R.id.inc_find_ship);
        ImageView iv_find_ship = inc_find_ship.findViewById(R.id.iv_find_ship);
        tv_find_ship_number = inc_find_ship.findViewById(R.id.tv_find_ship_number);
        TextView tv_illegal_name = inc_find_ship.findViewById(R.id.tv_illegal_name);
        tv_illegal_name.setText("船舶闯入");
        iv_find_ship.setImageResource(R.mipmap.icon_find_ship);
        tv_find_ship_number.setText(GlobalVariable.findShip);
    }

    private void initFindPeople() {
        inc_find_people = requireView().findViewById(R.id.inc_find_people);
        ImageView iv_find_people = inc_find_people.findViewById(R.id.iv_find_ship);
        tv_find_people_number = inc_find_people.findViewById(R.id.tv_find_ship_number);
        TextView tv_illegal_name = inc_find_people.findViewById(R.id.tv_illegal_name);
        iv_find_people.setImageResource(R.mipmap.icon_find_people);
        tv_illegal_name.setText("人员活动");
        tv_find_people_number.setText(GlobalVariable.findPeople);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

//            case R.id.my_case_layout:
//                // 我的案件
//                Intent caseIntent = new Intent(getActivity(), MyCasesActivity.class);
//                caseIntent.putExtra(MyConstants.IS_SHOW_WAIT_VIEW_KEY, isShowWaitView);
//                startActivity(caseIntent);
//                break;

            case R.id.video_control_layout:   //人机联防
//                Intent videoIntent = new Intent(getActivity(), VideoControlActivity.class);
//                startActivity(videoIntent);

                Intent intent = new Intent(getActivity(), ManMachineProtectionActivity.class);
                startActivity(intent);
                break;

//            case R.id.ll_resourceLayout:
//                // 资源分布
//                Intent resIntent = new Intent(getActivity(), ResDistributionActivity.class);
//                startActivity(resIntent);
//                break;

            case R.id.tv_patrolDiary:
                Intent mIntent = new Intent(requireContext(), PatrolDiaryActivity.class);
                startActivity(mIntent);
                break;

//            case R.id.tv_dataMonitoring:
//                //数据监测
//                Intent intent = new Intent(getActivity(), DataMonitorShowActivity.class);
//                intent.putExtra(MyVariables.CURRENT_LOCATION, mPositionTextView.getText().toString());
//                intent.putExtra(MyVariables.CURRENT_WEATHER,
//                        mTempTextView.getText().toString()
//                                + "  "
//                                + mWeatherTextView.getText().toString());
//                startActivity(intent);
//                break;

            case R.id.alarm_center_layout:   //预警信息
                // 预警中心
                Intent alarmCenterIntent = new Intent(getActivity(), AlarmCenterActivityNew.class);
                startActivity(alarmCenterIntent);
                break;

            case R.id.tv_fore_warn:   //预警统计
                Intent forewarnIntent = new Intent(getActivity(), ForewarnStatActivity.class);
                startActivity(forewarnIntent);

//                Intent forewarnIntent = new Intent(getActivity(), DealResultStatActivity.class);
//                startActivity(forewarnIntent);

//                Intent warnRankIntent = new Intent(getActivity(), WarnDeviceRankActivity.class);
//                startActivity(warnRankIntent);

//                Intent warnRankIntent = new Intent(getActivity(), PublicAlarmActivity.class);
//                startActivity(warnRankIntent);

//                Intent warnRankIntent = new Intent(getActivity(), AddCaseActivity.class);
//                startActivity(warnRankIntent);
                break;

            default:
                break;
        }
    }


    @Override
    public void alarmTypeForDay(List<AlarmTypeBean.TypeBean> bean) {
        boolean isNotEmpty = null != bean;
        if (isNotEmpty) {
            int findPeopleCount = 0;
            int illegalFishCount = 0;
            int findShipCount = 0;
            int staffRetentionCount = 0;
            for (AlarmTypeBean.TypeBean typeBean : bean) {
                if (typeBean.getAlarmTypeValue() == 5) {
                    findPeopleCount = typeBean.getCount();
                    String people = String.valueOf(findPeopleCount);
                    tv_find_people_number.setText(people);
                    GlobalVariable.findPeople = people;
                } else if (typeBean.getAlarmTypeValue() == 3) {
                    illegalFishCount = typeBean.getCount();
                    String illegal = String.valueOf(illegalFishCount);
                    tv_illegal_fish_number.setText(illegal);
                    GlobalVariable.illegalFish = illegal;
                } else if (typeBean.getAlarmTypeValue() == 4) {
                    findShipCount = typeBean.getCount();
                    String ship = String.valueOf(findShipCount);
                    tv_find_ship_number.setText(ship);
                    GlobalVariable.findShip = ship;
                } else if (typeBean.getAlarmTypeValue() == 7) {
                    staffRetentionCount = typeBean.getCount();
                    String staff = String.valueOf(staffRetentionCount);
                    tv_staff_retention_number.setText(staff);
                    GlobalVariable.staffRetention = staff;
                }
            }

            String alarmTotalNum =
                    String.valueOf(findPeopleCount + illegalFishCount + findShipCount + staffRetentionCount);
            tv_today_alarm_count.setText(alarmTotalNum);
            GlobalVariable.alarmTotalNum = alarmTotalNum;
        }
    }

    @Override
    public void alarmDealResult(List<AlarmDealBean.DealResultBean> bean) {
        boolean isNotEmpty = null != bean;
        if (isNotEmpty) {
            for (AlarmDealBean.DealResultBean dealResultBean : bean) {
                String numberStr = String.valueOf(dealResultBean.getCount());
                if (dealResultBean.getAlarmHandleTypeValue() == 3) {
                    tv_wait_receive_alarm_number.setText(numberStr);
                    GlobalVariable.waitReceiveAlarm = numberStr;
                } else if (dealResultBean.getAlarmHandleTypeValue() == 4) {
                    tv_wait_deal_alarm_number.setText(numberStr);
                    GlobalVariable.waitDealAlarm = numberStr;
                } else if (dealResultBean.getAlarmHandleTypeValue() == 5) {
                    tv_has_deal_alarm_number.setText(numberStr);
                    GlobalVariable.hasDealAlarm = numberStr;
                }
            }
        }
    }
}