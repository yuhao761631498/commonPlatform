package com.gdu.model.data;

import java.io.Serializable;

/**
 * 数据监测 - 案件总数统计
 */
public class DataCaseTotalBean implements Serializable {
    private int caseCount;

    public int getCaseCount() {
        return caseCount;
    }

    public void setCaseCount(int caseCount) {
        this.caseCount = caseCount;
    }
}
