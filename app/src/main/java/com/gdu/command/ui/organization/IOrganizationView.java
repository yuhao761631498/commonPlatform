package com.gdu.command.ui.organization;

import com.gdu.model.organization.OrganizationEmployeeBean;
import com.gdu.model.organization.OrganizationInfo;

import java.util.List;
import java.util.Map;

public interface IOrganizationView {
    void showOrganization(List<OrganizationInfo> organizationInfos);
    void showEmptyView();
    void showSearchList(OrganizationEmployeeBean bean);
}
