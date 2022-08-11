package com.gdu.command.ui.organization.sub;

import com.gdu.model.organization.OrganizationEmployeeBean;

public interface ISubOrganizationView {
    /** 组织机构人员列表*/
    void showPersonList(OrganizationEmployeeBean bean);
    /** 没有数据显示空布局*/
    void showEmptyView();
}
