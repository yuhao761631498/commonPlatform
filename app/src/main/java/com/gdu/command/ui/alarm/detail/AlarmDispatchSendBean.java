package com.gdu.command.ui.alarm.detail;

import java.io.Serializable;
import java.util.List;

public class AlarmDispatchSendBean implements Serializable {
    private int alarmId;
    private List<DispatchManBean> designateMan;
    private String remark;

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public List<DispatchManBean> getDesignateMan() {
        return designateMan;
    }

    public void setDesignateMan(List<DispatchManBean> designateMan) {
        this.designateMan = designateMan;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
