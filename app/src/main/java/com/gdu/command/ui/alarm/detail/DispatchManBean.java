package com.gdu.command.ui.alarm.detail;

import java.io.Serializable;

public class DispatchManBean implements Serializable {
    /** 指派人id */
    private int employeeId;
    /** 指派人姓名 */
    private String employeeName;
    /** 指派人组织机构名称 */
    private String orgName;

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
