package com.gdu.command.ui.alarm.detail;

import java.io.Serializable;

public class AlarmHandleFileBean implements Serializable {
    /** 文件路径 */
    private String fileKey;
    /** 文件类型 1 图片 2 视频 */
    private String fileType;

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
