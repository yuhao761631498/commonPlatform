package com.gdu.command.ui.patrol.bean;

import java.io.Serializable;
import java.util.List;

public class QueryDataBean implements Serializable {

    private int code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        private int id;
        private int customerId;
        private String patrolTypeName;
        private int patrolUser;
        private String patrolUserName;
        private double lonStart;
        private double latStart;
        private double lonEnd;
        private double latEnd;
        private int patrolDistance;
        private String startTime;
        private String endTime;
        private String patrolPathPic;
        private List<PatrolRecordListBean> patrolRecordList;
        private List<PatrolLocateListBean> patrolLocateList;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCustomerId() {
            return customerId;
        }

        public void setCustomerId(int customerId) {
            this.customerId = customerId;
        }

        public String getPatrolTypeName() {
            return patrolTypeName;
        }

        public void setPatrolTypeName(String patrolTypeName) {
            this.patrolTypeName = patrolTypeName;
        }

        public int getPatrolUser() {
            return patrolUser;
        }

        public void setPatrolUser(int patrolUser) {
            this.patrolUser = patrolUser;
        }

        public String getPatrolUserName() {
            return patrolUserName;
        }

        public void setPatrolUserName(String patrolUserName) {
            this.patrolUserName = patrolUserName;
        }

        public double getLonStart() {
            return lonStart;
        }

        public void setLonStart(double lonStart) {
            this.lonStart = lonStart;
        }

        public double getLatStart() {
            return latStart;
        }

        public void setLatStart(double latStart) {
            this.latStart = latStart;
        }

        public double getLonEnd() {
            return lonEnd;
        }

        public void setLonEnd(double lonEnd) {
            this.lonEnd = lonEnd;
        }

        public double getLatEnd() {
            return latEnd;
        }

        public void setLatEnd(double latEnd) {
            this.latEnd = latEnd;
        }

        public int getPatrolDistance() {
            return patrolDistance;
        }

        public void setPatrolDistance(int patrolDistance) {
            this.patrolDistance = patrolDistance;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getPatrolPathPic() {
            return patrolPathPic;
        }

        public void setPatrolPathPic(String patrolPathPic) {
            this.patrolPathPic = patrolPathPic;
        }

        public List<PatrolRecordListBean> getPatrolRecordList() {
            return patrolRecordList;
        }

        public void setPatrolRecordList(List<PatrolRecordListBean> patrolRecordList) {
            this.patrolRecordList = patrolRecordList;
        }

        public List<PatrolLocateListBean> getPatrolLocateList() {
            return patrolLocateList;
        }

        public void setPatrolLocateList(List<PatrolLocateListBean> patrolLocateList) {
            this.patrolLocateList = patrolLocateList;
        }

        public static class PatrolRecordListBean implements Serializable {
            private int id;
            private int customerId;
            private int patrolId;
            private String typeName;
            private String content;
            private double lon;
            private double lat;
            private String locateName;
            private String imageUrl;
            private String createTime;
            private String updateTime;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getCustomerId() {
                return customerId;
            }

            public void setCustomerId(int customerId) {
                this.customerId = customerId;
            }

            public int getPatrolId() {
                return patrolId;
            }

            public void setPatrolId(int patrolId) {
                this.patrolId = patrolId;
            }

            public String getTypeName() {
                return typeName;
            }

            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public double getLon() {
                return lon;
            }

            public void setLon(double lon) {
                this.lon = lon;
            }

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }

            public String getLocateName() {
                return locateName;
            }

            public void setLocateName(String locateName) {
                this.locateName = locateName;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }
        }

        public static class PatrolLocateListBean implements Serializable {
            private int id;
            private int customerId;
            private int patrolId;
            private double lon;
            private double lat;
            private String createTime;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getCustomerId() {
                return customerId;
            }

            public void setCustomerId(int customerId) {
                this.customerId = customerId;
            }

            public int getPatrolId() {
                return patrolId;
            }

            public void setPatrolId(int patrolId) {
                this.patrolId = patrolId;
            }

            public double getLon() {
                return lon;
            }

            public void setLon(double lon) {
                this.lon = lon;
            }

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }
        }
    }
}
