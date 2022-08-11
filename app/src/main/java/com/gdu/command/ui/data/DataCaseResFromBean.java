package com.gdu.command.ui.data;

import java.util.List;

public class DataCaseResFromBean {

    /**
     * caseCount : 64
     * caseInfoSourceCountVOS : [{"countNum":2,"countName":"市长热线"},{"countNum":10,"countName":"巡逻发现"},{"countNum":5,"countName":"群众举报"},{"countNum":1,"countName":"网络媒体"},{"countNum":0,"countName":"电视宣传"},{"countNum":42,"countName":"自动识别"}]
     * compareCount : 100
     */

    private int caseCount;
    private int compareCount;
    private List<CaseInfoSourceCountVOSBean> caseInfoSourceCountVOS;

    public int getCaseCount() {
        return caseCount;
    }

    public void setCaseCount(int caseCount) {
        this.caseCount = caseCount;
    }

    public int getCompareCount() {
        return compareCount;
    }

    public void setCompareCount(int compareCount) {
        this.compareCount = compareCount;
    }

    public List<CaseInfoSourceCountVOSBean> getCaseInfoSourceCountVOS() {
        return caseInfoSourceCountVOS;
    }

    public void setCaseInfoSourceCountVOS(List<CaseInfoSourceCountVOSBean> caseInfoSourceCountVOS) {
        this.caseInfoSourceCountVOS = caseInfoSourceCountVOS;
    }

    public static class CaseInfoSourceCountVOSBean {
        /**
         * countNum : 2
         * countName : 市长热线
         */

        private int countNum;
        private String countName;

        public int getCountNum() {
            return countNum;
        }

        public void setCountNum(int countNum) {
            this.countNum = countNum;
        }

        public String getCountName() {
            return countName;
        }

        public void setCountName(String countName) {
            this.countName = countName;
        }
    }
}
