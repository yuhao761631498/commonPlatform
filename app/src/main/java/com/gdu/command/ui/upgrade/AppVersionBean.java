package com.gdu.command.ui.upgrade;

import java.io.Serializable;

/**
 * @author wixche
 */
public class AppVersionBean implements Serializable {

    private int error;
    private int code;
    private String msg;
    private DataBean data;

    public int getError() {
        return error;
    }

    public void setError(int errorParam) {
        error = errorParam;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean dataParam) {
        data = dataParam;
    }

    public static class DataBean implements Serializable {
        private int version_code;
        private String download_file = "";
        private int flag;
        private String version;
        private String version_log = "";
        // 新升级接口返回属性
        private String patchDownloadUrl = "";
        private String versionDownloadUrl = "";
        private boolean forceUpgrade;
        private String newVersionName = "";
        private long newVersionNum;
        private String newVersionMd5 = "";
        private String remark = "";

        public int getVersion_code() {
            return version_code;
        }

        public void setVersion_code(int version_codeParam) {
            version_code = version_codeParam;
        }

        public String getDownload_file() {
            return download_file;
        }

        public void setDownload_file(String download_fileParam) {
            download_file = download_fileParam;
        }

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flagParam) {
            flag = flagParam;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String versionParam) {
            version = versionParam;
        }

        public String getVersion_log() {
            return version_log;
        }

        public void setVersion_log(String version_logParam) {
            version_log = version_logParam;
        }

        public String getPatchDownloadUrl() {
            return patchDownloadUrl;
        }

        public void setPatchDownloadUrl(String patchDownloadUrl) {
            this.patchDownloadUrl = patchDownloadUrl;
        }

        public String getVersionDownloadUrl() {
            return versionDownloadUrl;
        }

        public void setVersionDownloadUrl(String versionDownloadUrl) {
            this.versionDownloadUrl = versionDownloadUrl;
        }

        public boolean isForceUpgrade() {
            return forceUpgrade;
        }

        public void setForceUpgrade(boolean forceUpgrade) {
            this.forceUpgrade = forceUpgrade;
        }

        public String getNewVersionName() {
            return newVersionName;
        }

        public void setNewVersionName(String newVersionName) {
            this.newVersionName = newVersionName;
        }

        public long getNewVersionNum() {
            return newVersionNum;
        }

        public void setNewVersionNum(long newVersionNum) {
            this.newVersionNum = newVersionNum;
        }

        public String getNewVersionMd5() {
            return newVersionMd5;
        }

        public void setNewVersionMd5(String newVersionMd5) {
            this.newVersionMd5 = newVersionMd5;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
