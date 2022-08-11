package com.gdu.command.ui.video.model;

import com.gdu.model.device.RecordInfoList;

import java.io.Serializable;

public class RecordBean implements Serializable {
    private int code;
    private RecordInfoList data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public RecordInfoList getData() {
        return data;
    }

    public void setData(RecordInfoList data) {
        this.data = data;
    }
}
