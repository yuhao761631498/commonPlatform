package com.gdu.mqttchat;

import java.io.Serializable;
import java.util.List;

/**
 * 一条聊天请求
 */
public class ChatRecordInfo implements Serializable {
    private List<ChatRecord> records;
    private double total;
    private double size;
    private double current;
    private boolean searchCount;
    private double  pages;

    public List<ChatRecord> getRecords() {
        return records;
    }

    public void setRecords(List<ChatRecord> records) {
        this.records = records;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public boolean isSearchCount() {
        return searchCount;
    }

    public void setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
    }

    public double getPages() {
        return pages;
    }

    public void setPages(double pages) {
        this.pages = pages;
    }
}
