package com.gdu.model.cases;

/**
 * 案件状态
 */
public enum  CaseStatus {
    /** 待处置 */
    NO_HANDLE(0, "待处置"),
    /** 处置中 */
    HANDLING(1, "处置中"),
    /** 已归档 */
    HANDLED(2, "已归档"),
    /** 待批示 */
    WAIT_COMMENT(3, "待批示");

    private int key;
    private String value;
    CaseStatus(int key, String value) {
        this.key = key;
        this.value = value;
    }


    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
