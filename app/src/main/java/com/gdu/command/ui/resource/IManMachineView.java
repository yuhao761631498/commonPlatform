package com.gdu.command.ui.resource;

import com.gdu.command.ui.video.model.ChildrenBean;

import java.util.List;

/**
 * Copyright (C), 2020-2030, 武汉中旗生物医疗电子有限公司
 * <p>
 * 功能描述:
 * <p>
 * 创建时间: 2022/5/11 10:01
 *
 * @author yuhao
 */
public interface IManMachineView {

    void getDeviceList(List<ChildrenBean> mDeviceInfoList);
}
