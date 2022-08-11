package com.gdu.model.device;

import java.io.Serializable;

/**
 * 单条录像信息
 */
public class RecordInfo implements Serializable {
    private String file_name;
    private long file_size;
    private long start_time;
    private long time_len;

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public long getFile_size() {
        return file_size;
    }

    public void setFile_size(long file_size) {
        this.file_size = file_size;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getTime_len() {
        return time_len;
    }

    public void setTime_len(long time_len) {
        this.time_len = time_len;
    }
}
