package com.gdu.command.event;

/**
 * 刷新我的案件事件
 */
public class RefreshMyCaseEvent {
    int type;
    String removeCaseId;
    public RefreshMyCaseEvent(int type, String removeCaseId){
        this.type = type;
        this.removeCaseId = removeCaseId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRemoveCaseId() {
        return removeCaseId;
    }

    public void setRemoveCaseId(String removeCaseId) {
        this.removeCaseId = removeCaseId;
    }
}
