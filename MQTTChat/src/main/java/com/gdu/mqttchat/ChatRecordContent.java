package com.gdu.mqttchat;

import java.io.Serializable;

/**
 * 一条聊天记录内容
 */
public class ChatRecordContent implements Serializable {
    private long sendTime;
    private int userid;
    private String username;
    private String type;
    private String  msg;


    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
