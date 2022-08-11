package com.gdu.command.ui.message;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Copyright (C), 2020-2030
 * <p>
 * 功能描述:
 * <p>
 * 创建时间: 2022/6/17 17:02
 *
 * @author yuhao
 */

@Entity
public class NoticeShowInfoBean {
    @PrimaryKey(autoGenerate = true)//主键是否自动增长，默认为false
    private int id;

    private String message;

    private String timeTitle;

    private String messageTime;

    private boolean isRead;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeTitle() {
        return timeTitle;
    }

    public void setTimeTitle(String timeTitle) {
        this.timeTitle = timeTitle;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
