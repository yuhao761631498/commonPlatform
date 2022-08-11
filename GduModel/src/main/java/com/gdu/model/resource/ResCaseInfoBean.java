package com.gdu.model.resource;

import java.io.Serializable;
import java.util.List;

/**
 * 案件信息
 */
public class ResCaseInfoBean implements Serializable {

    private List<RecordsDTO> records;

    public List<RecordsDTO> getRecords() {
        return records;
    }

    public void setRecords(List<RecordsDTO> records) {
        this.records = records;
    }

    public static class RecordsDTO {
        private String id;
        private String caseName;
        private String reportAddr;
        private String caseDesc;
        private String caseStartTime;
        private String createTime;
        private String caseStatus;
        private double longitude;
        private double latitude;
        private String caseFile;
        private Object disFinishAttachment;
        private String caseNo;

        public String getCaseNo() {
            return caseNo;
        }

        public void setCaseNo(String caseNo) {
            this.caseNo = caseNo;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCaseName() {
            return caseName;
        }

        public void setCaseName(String caseName) {
            this.caseName = caseName;
        }

        public String getReportAddr() {
            return reportAddr;
        }

        public void setReportAddr(String reportAddr) {
            this.reportAddr = reportAddr;
        }

        public String getCaseDesc() {
            return caseDesc;
        }

        public void setCaseDesc(String caseDesc) {
            this.caseDesc = caseDesc;
        }

        public String getCaseStartTime() {
            return caseStartTime;
        }

        public void setCaseStartTime(String caseStartTime) {
            this.caseStartTime = caseStartTime;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getCaseStatus() {
            return caseStatus;
        }

        public void setCaseStatus(String caseStatus) {
            this.caseStatus = caseStatus;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String getCaseFile() {
            return caseFile;
        }

        public void setCaseFile(String caseFile) {
            this.caseFile = caseFile;
        }

        public Object getDisFinishAttachment() {
            return disFinishAttachment;
        }

        public void setDisFinishAttachment(Object disFinishAttachment) {
            this.disFinishAttachment = disFinishAttachment;
        }
    }
}
