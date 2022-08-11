package com.gdu.command.ui.alarm.detail;

import java.io.Serializable;
import java.util.List;

public class AlarmDetailBean implements Serializable {

    private int code;
    private String msg;
    private DataBean data;
    private Object extra;

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

    public void setData(DataBean data) {
        this.data = data;
    }

    public Object getExtra() {
        return extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }

    public static class DataBean implements Serializable {
        private String videoUrl;
        private List<String> imageUrls;
        private String alarmInfo;
        private String createTime;
        private String answerPoliceTime;
        private int currentPresetID;
        private double presetLat;
        private double presetLon;
        private String remark;
        private List<AssignmentAndHandleInfoBean> assignmentAndHandleInfo;

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public List<String> getImageUrls() {
            return imageUrls;
        }

        public void setImageUrls(List<String> imageUrls) {
            this.imageUrls = imageUrls;
        }

        public String getAlarmInfo() {
            return alarmInfo;
        }

        public void setAlarmInfo(String alarmInfo) {
            this.alarmInfo = alarmInfo;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getAnswerPoliceTime() {
            return answerPoliceTime;
        }

        public void setAnswerPoliceTime(String answerPoliceTime) {
            this.answerPoliceTime = answerPoliceTime;
        }

        public int getCurrentPresetID() {
            return currentPresetID;
        }

        public void setCurrentPresetID(int currentPresetID) {
            this.currentPresetID = currentPresetID;
        }

        public double getPresetLat() {
            return presetLat;
        }

        public void setPresetLat(double presetLat) {
            this.presetLat = presetLat;
        }

        public double getPresetLon() {
            return presetLon;
        }

        public void setPresetLon(double presetLon) {
            this.presetLon = presetLon;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public List<AssignmentAndHandleInfoBean> getAssignmentAndHandleInfo() {
            return assignmentAndHandleInfo;
        }

        public void setAssignmentAndHandleInfo(List<AssignmentAndHandleInfoBean> assignmentAndHandleInfo) {
            this.assignmentAndHandleInfo = assignmentAndHandleInfo;
        }

        public static class AssignmentAndHandleInfoBean implements Serializable {
            private String dispositionNode;
            private List<DispositionNodeDataBean> dispositionNodeData;

            public String getDispositionNode() {
                return dispositionNode;
            }

            public void setDispositionNode(String dispositionNode) {
                this.dispositionNode = dispositionNode;
            }

            public List<DispositionNodeDataBean> getDispositionNodeData() {
                return dispositionNodeData;
            }

            public void setDispositionNodeData(List<DispositionNodeDataBean> dispositionNodeData) {
                this.dispositionNodeData = dispositionNodeData;
            }

            public static class DispositionNodeDataBean implements Serializable {
                private String username;
                private String createTime;
                private int assignmentCount;
                private int answerPolice;
                private String remark;
                private String orgName;
                private int handleType;
                private String handleTypeName;

                // 已处理返回的字段
                private String handleUserName;
                private String handleUserOrgName;
                private String handleResultType;
                private String handleResultTypeName;
                private String handleContent;
                private String handleTime;
                private String answerPoliceTime;
                private String supplementHandleContent;
                private String supplementHandleTime;
                private List<FileKeyBean> fileKeys;

                public String getUsername() {
                    return username;
                }

                public void setUsername(String username) {
                    this.username = username;
                }

                public String getCreateTime() {
                    return createTime;
                }

                public void setCreateTime(String createTime) {
                    this.createTime = createTime;
                }

                public int getAssignmentCount() {
                    return assignmentCount;
                }

                public void setAssignmentCount(int assignmentCount) {
                    this.assignmentCount = assignmentCount;
                }

                public int getAnswerPolice() {
                    return answerPolice;
                }

                public void setAnswerPolice(int answerPolice) {
                    this.answerPolice = answerPolice;
                }

                public String getRemark() {
                    return remark;
                }

                public void setRemark(String remark) {
                    this.remark = remark;
                }

                public String getOrgName() {
                    return orgName;
                }

                public void setOrgName(String orgName) {
                    this.orgName = orgName;
                }

                public int getHandleType() {
                    return handleType;
                }

                public void setHandleType(int handleType) {
                    this.handleType = handleType;
                }

                public String getHandleTypeName() {
                    return handleTypeName;
                }

                public void setHandleTypeName(String handleTypeName) {
                    this.handleTypeName = handleTypeName;
                }

                public String getHandleUserName() {
                    return handleUserName;
                }

                public void setHandleUserName(String handleUserName) {
                    this.handleUserName = handleUserName;
                }

                public String getHandleUserOrgName() {
                    return handleUserOrgName;
                }

                public void setHandleUserOrgName(String handleUserOrgName) {
                    this.handleUserOrgName = handleUserOrgName;
                }

                public String getHandleResultType() {
                    return handleResultType;
                }

                public void setHandleResultType(String handleResultType) {
                    this.handleResultType = handleResultType;
                }

                public String getHandleResultTypeName() {
                    return handleResultTypeName;
                }

                public void setHandleResultTypeName(String handleResultTypeName) {
                    this.handleResultTypeName = handleResultTypeName;
                }

                public String getHandleContent() {
                    return handleContent;
                }

                public void setHandleContent(String handleContent) {
                    this.handleContent = handleContent;
                }

                public String getHandleTime() {
                    return handleTime;
                }

                public void setHandleTime(String handleTime) {
                    this.handleTime = handleTime;
                }

                public String getAnswerPoliceTime() {
                    return answerPoliceTime;
                }

                public void setAnswerPoliceTime(String answerPoliceTime) {
                    this.answerPoliceTime = answerPoliceTime;
                }

                public List<FileKeyBean> getFileKeys() {
                    return fileKeys;
                }

                public void setFileKeys(List<FileKeyBean> fileKeys) {
                    this.fileKeys = fileKeys;
                }

                public String getSupplementHandleContent() {
                    return supplementHandleContent;
                }

                public void setSupplementHandleContent(String supplementHandleContent) {
                    this.supplementHandleContent = supplementHandleContent;
                }

                public String getSupplementHandleTime() {
                    return supplementHandleTime;
                }

                public void setSupplementHandleTime(String supplementHandleTime) {
                    this.supplementHandleTime = supplementHandleTime;
                }

                public static class FileKeyBean {
                    private String fileType;
                    private String fileTypeName;
                    private String fileKey;

                    public String getFileType() {
                        return fileType;
                    }

                    public void setFileType(String fileType) {
                        this.fileType = fileType;
                    }

                    public String getFileTypeName() {
                        return fileTypeName;
                    }

                    public void setFileTypeName(String fileTypeName) {
                        this.fileTypeName = fileTypeName;
                    }

                    public String getFileKey() {
                        return fileKey;
                    }

                    public void setFileKey(String fileKey) {
                        this.fileKey = fileKey;
                    }
                }

            }
        }
    }
}
