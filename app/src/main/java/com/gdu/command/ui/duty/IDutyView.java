package com.gdu.command.ui.duty;

import java.util.List;

public interface IDutyView {

    void getDutyForDay(List<DutyForDayBean.DutyMemberBean> beans);

    void getDutyForMe(List<List<DutyForMeBean.DayDutyInfoBean>> beans);
}
