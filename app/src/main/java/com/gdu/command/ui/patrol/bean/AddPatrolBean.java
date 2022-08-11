package com.gdu.command.ui.patrol.bean;

import java.io.Serializable;

public class AddPatrolBean implements Serializable {

    private int code;
    private String msg;
    private DataBean data;

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

    public static class DataBean implements Serializable {
        private int id;
        private int customerId;
        private String patrolTypeName;
        private String patrolUser;
        private double lonStart;
        private double latStart;
        private double lonEnd;
        private double latEnd;
        private String patrolDistance;
        private String startTime;
        private String endTime;

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

        public String getPatrolUser() {
            return patrolUser;
        }

        public void setPatrolUser(String patrolUser) {
            this.patrolUser = patrolUser;
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

        public String getPatrolDistance() {
            return patrolDistance;
        }

        public void setPatrolDistance(String patrolDistance) {
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
    }
}
