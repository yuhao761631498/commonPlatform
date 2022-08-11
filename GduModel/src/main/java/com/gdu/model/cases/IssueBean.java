package com.gdu.model.cases;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @author by DELL
 * @date on 2018/4/17
 * @describe
 */
public class IssueBean implements Parcelable , Serializable {
    private String id;
    private String caseName;
    private String report_man;
    private String reportMan;
    private String reportAddr;
    private String reportTel;
    private String report_time;
    private String caseDesc;
    private int designateId;
    private String createTime;
    private String caseStartTime;
    private String dispositionTime;
    private int caseStatus;
    private double longitude;
    private double latitude;
    private String disFinishAttachment;
    private String caseNo;
    private String reportTime;
    private String importantRecord;
    private String deptName;
    private String caseFile;
    private int designateStatus;
    private int dispositionStatus;
    private String infoSource;
    private String reporterGender;
    private String reporterIdentity;
    private String receivingAlarmMan;
    private String dispositionMan;
    private String dispositionRecord;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.caseName);
        dest.writeString(this.report_man);
        dest.writeString(this.reportMan);
        dest.writeString(this.reportAddr);
        dest.writeString(this.reportTel);
        dest.writeString(this.report_time);
        dest.writeString(this.caseDesc);
        dest.writeInt(this.designateId);
        dest.writeString(this.createTime);
        dest.writeString(this.caseStartTime);
        dest.writeString(this.dispositionTime);
        dest.writeInt(this.caseStatus);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
        dest.writeString(this.disFinishAttachment);
        dest.writeString(this.caseNo);
        dest.writeString(this.reportTime);
        dest.writeString(this.importantRecord);
        dest.writeString(this.deptName);
        dest.writeString(this.caseFile);
        dest.writeInt(this.designateStatus);
        dest.writeInt(this.dispositionStatus);
        dest.writeString(this.infoSource);
        dest.writeString(this.reporterGender);
        dest.writeString(this.reporterIdentity);
        dest.writeString(this.receivingAlarmMan);
        dest.writeString(this.dispositionMan);
        dest.writeString(this.dispositionRecord);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.caseName = source.readString();
        this.report_man = source.readString();
        this.reportMan = source.readString();
        this.reportAddr = source.readString();
        this.reportTel = source.readString();
        this.report_time = source.readString();
        this.caseDesc = source.readString();
        this.designateId = source.readInt();
        this.createTime = source.readString();
        this.caseStartTime = source.readString();
        this.dispositionTime = source.readString();
        this.caseStatus = source.readInt();
        this.longitude = source.readDouble();
        this.latitude = source.readDouble();
        this.disFinishAttachment = source.readString();
        this.caseNo = source.readString();
        this.reportTime = source.readString();
        this.importantRecord = source.readString();
        this.deptName = source.readString();
        this.caseFile = source.readString();
        this.designateStatus = source.readInt();
        this.dispositionStatus = source.readInt();
        this.infoSource = source.readString();
        this.reporterGender = source.readString();
        this.reporterIdentity = source.readString();
        this.receivingAlarmMan = source.readString();
        this.dispositionMan = source.readString();
        this.dispositionRecord = source.readString();
    }

    public IssueBean() {
    }

    protected IssueBean(Parcel in) {
        this.id = in.readString();
        this.caseName = in.readString();
        this.report_man = in.readString();
        this.reportMan = in.readString();
        this.reportAddr = in.readString();
        this.reportTel = in.readString();
        this.report_time = in.readString();
        this.caseDesc = in.readString();
        this.designateId = in.readInt();
        this.createTime = in.readString();
        this.caseStartTime = in.readString();
        this.dispositionTime = in.readString();
        this.caseStatus = in.readInt();
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
        this.disFinishAttachment = in.readString();
        this.caseNo = in.readString();
        this.reportTime = in.readString();
        this.importantRecord = in.readString();
        this.deptName = in.readString();
        this.caseFile = in.readString();
        this.designateStatus = in.readInt();
        this.dispositionStatus = in.readInt();
        this.infoSource = in.readString();
        this.reporterGender = in.readString();
        this.reporterIdentity = in.readString();
        this.receivingAlarmMan = in.readString();
        this.dispositionMan = in.readString();
        this.dispositionRecord = in.readString();
    }

    public static final Creator<IssueBean> CREATOR = new Creator<IssueBean>() {
        @Override
        public IssueBean createFromParcel(Parcel source) {
            return new IssueBean(source);
        }

        @Override
        public IssueBean[] newArray(int size) {
            return new IssueBean[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String mId) {
        id = mId;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String mCaseName) {
        caseName = mCaseName;
    }

    public String getReport_man() {
        return report_man;
    }

    public void setReport_man(String mReport_man) {
        report_man = mReport_man;
    }

    public String getReportMan() {
        return reportMan;
    }

    public void setReportMan(String mReportMan) {
        reportMan = mReportMan;
    }

    public String getReportAddr() {
        return reportAddr;
    }

    public void setReportAddr(String mReportAddr) {
        reportAddr = mReportAddr;
    }

    public String getReportTel() {
        return reportTel;
    }

    public void setReportTel(String mReportTel) {
        reportTel = mReportTel;
    }

    public String getReport_time() {
        return report_time;
    }

    public void setReport_time(String mReport_time) {
        report_time = mReport_time;
    }

    public String getCaseDesc() {
        return caseDesc;
    }

    public void setCaseDesc(String mCaseDesc) {
        caseDesc = mCaseDesc;
    }

    public int getDesignateId() {
        return designateId;
    }

    public void setDesignateId(int mDesignateId) {
        designateId = mDesignateId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String mCreateTime) {
        createTime = mCreateTime;
    }

    public String getCaseStartTime() {
        return caseStartTime;
    }

    public void setCaseStartTime(String mCaseStartTime) {
        caseStartTime = mCaseStartTime;
    }

    public String getDispositionTime() {
        return dispositionTime;
    }

    public void setDispositionTime(String mDispositionTime) {
        dispositionTime = mDispositionTime;
    }

    public int getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(int mCaseStatus) {
        caseStatus = mCaseStatus;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double mLongitude) {
        longitude = mLongitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double mLatitude) {
        latitude = mLatitude;
    }

    public String getDisFinishAttachment() {
        return disFinishAttachment;
    }

    public void setDisFinishAttachment(String mDisFinishAttachment) {
        disFinishAttachment = mDisFinishAttachment;
    }

    public String getCaseNo() {
        return caseNo;
    }

    public void setCaseNo(String mCaseNo) {
        caseNo = mCaseNo;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String mReportTime) {
        reportTime = mReportTime;
    }

    public String getImportantRecord() {
        return importantRecord;
    }

    public void setImportantRecord(String mImportantRecord) {
        importantRecord = mImportantRecord;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String mDeptName) {
        deptName = mDeptName;
    }

    public String getCaseFile() {
        return caseFile;
    }

    public void setCaseFile(String mCaseFile) {
        caseFile = mCaseFile;
    }

    public int getDesignateStatus() {
        return designateStatus;
    }

    public void setDesignateStatus(int mDesignateStatus) {
        designateStatus = mDesignateStatus;
    }

    public int getDispositionStatus() {
        return dispositionStatus;
    }

    public void setDispositionStatus(int mDispositionStatus) {
        dispositionStatus = mDispositionStatus;
    }

    public String getInfoSource() {
        return infoSource;
    }

    public void setInfoSource(String mInfoSource) {
        infoSource = mInfoSource;
    }

    public String getReporterGender() {
        return reporterGender;
    }

    public void setReporterGender(String mReporterGender) {
        reporterGender = mReporterGender;
    }

    public String getReporterIdentity() {
        return reporterIdentity;
    }

    public void setReporterIdentity(String mReporterIdentity) {
        reporterIdentity = mReporterIdentity;
    }

    public String getReceivingAlarmMan() {
        return receivingAlarmMan;
    }

    public void setReceivingAlarmMan(String mReceivingAlarmMan) {
        receivingAlarmMan = mReceivingAlarmMan;
    }

    public String getDispositionMan() {
        return dispositionMan;
    }

    public void setDispositionMan(String mDispositionMan) {
        dispositionMan = mDispositionMan;
    }

    public String getDispositionRecord() {
        return dispositionRecord;
    }

    public void setDispositionRecord(String mDispositionRecord) {
        dispositionRecord = mDispositionRecord;
    }
}
