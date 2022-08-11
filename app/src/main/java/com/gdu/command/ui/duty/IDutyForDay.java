package com.gdu.command.ui.duty;

import java.util.List;

/**
 * Copyright (C), 2020-2030, 武汉中旗生物医疗电子有限公司
 * <p>
 * 功能描述:
 * <p>
 * 创建时间: 2022/5/13 9:55
 *
 * @author yuhao
 */
public interface IDutyForDay {

    void getDutyMember(List<DutyForDayBean.DutyMemberBean> dutyPersonBeans);
}
