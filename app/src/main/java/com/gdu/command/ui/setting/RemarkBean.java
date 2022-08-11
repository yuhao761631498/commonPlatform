package com.gdu.command.ui.setting;

import java.io.Serializable;

public class RemarkBean implements Serializable {

    private String mq_u;
    private String mq_p;
    private int img_port;
    private String remark;

    public String getMq_u() {
        return mq_u;
    }

    public void setMq_u(String mq_u) {
        this.mq_u = mq_u;
    }

    public String getMq_p() {
        return mq_p;
    }

    public void setMq_p(String mq_p) {
        this.mq_p = mq_p;
    }

    public int getImg_port() {
        return img_port;
    }

    public void setImg_port(int mImg_port) {
        img_port = mImg_port;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
