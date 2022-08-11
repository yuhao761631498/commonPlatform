package com.gdu.command.ui.data;

public class AlarmResInfoBean {

    /**
     * code : 0
     * msg : 成功
     * data : {"redLevelAlarmCount":1832,"orangeLevelAlarmCount":5067,"yellowLevelAlarmCount":442,"blueLevelAlarmCount":4512,"totalCount":11912,"shipAppearedCount":4423,"personnelIntrusionCount":6472,"illegalFishingCount":992,"personnelAppearedCount":0,"unknownTypeCount":0}
     * extra : null
     */

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

    public static class DataBean {
        /**
         * redLevelAlarmCount : 1832
         * orangeLevelAlarmCount : 5067
         * yellowLevelAlarmCount : 442
         * blueLevelAlarmCount : 4512
         * totalCount : 11912
         * shipAppearedCount : 4423
         * personnelIntrusionCount : 6472
         * illegalFishingCount : 992
         * personnelAppearedCount : 0
         * unknownTypeCount : 0
         */

        private int redLevelAlarmCount;
        private int orangeLevelAlarmCount;
        private int yellowLevelAlarmCount;
        private int blueLevelAlarmCount;
        private int totalCount;
        private int shipAppearedCount;
        private int personnelIntrusionCount;
        private int illegalFishingCount;
        private int personnelAppearedCount;
        private int unknownTypeCount;

        public int getRedLevelAlarmCount() {
            return redLevelAlarmCount;
        }

        public void setRedLevelAlarmCount(int redLevelAlarmCount) {
            this.redLevelAlarmCount = redLevelAlarmCount;
        }

        public int getOrangeLevelAlarmCount() {
            return orangeLevelAlarmCount;
        }

        public void setOrangeLevelAlarmCount(int orangeLevelAlarmCount) {
            this.orangeLevelAlarmCount = orangeLevelAlarmCount;
        }

        public int getYellowLevelAlarmCount() {
            return yellowLevelAlarmCount;
        }

        public void setYellowLevelAlarmCount(int yellowLevelAlarmCount) {
            this.yellowLevelAlarmCount = yellowLevelAlarmCount;
        }

        public int getBlueLevelAlarmCount() {
            return blueLevelAlarmCount;
        }

        public void setBlueLevelAlarmCount(int blueLevelAlarmCount) {
            this.blueLevelAlarmCount = blueLevelAlarmCount;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getShipAppearedCount() {
            return shipAppearedCount;
        }

        public void setShipAppearedCount(int shipAppearedCount) {
            this.shipAppearedCount = shipAppearedCount;
        }

        public int getPersonnelIntrusionCount() {
            return personnelIntrusionCount;
        }

        public void setPersonnelIntrusionCount(int personnelIntrusionCount) {
            this.personnelIntrusionCount = personnelIntrusionCount;
        }

        public int getIllegalFishingCount() {
            return illegalFishingCount;
        }

        public void setIllegalFishingCount(int illegalFishingCount) {
            this.illegalFishingCount = illegalFishingCount;
        }

        public int getPersonnelAppearedCount() {
            return personnelAppearedCount;
        }

        public void setPersonnelAppearedCount(int personnelAppearedCount) {
            this.personnelAppearedCount = personnelAppearedCount;
        }

        public int getUnknownTypeCount() {
            return unknownTypeCount;
        }

        public void setUnknownTypeCount(int unknownTypeCount) {
            this.unknownTypeCount = unknownTypeCount;
        }
    }
}
