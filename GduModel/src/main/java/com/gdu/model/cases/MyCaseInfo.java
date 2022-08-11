package com.gdu.model.cases;

import java.io.Serializable;
import java.util.List;

/**
 * 我的案件信息
 */
public class MyCaseInfo implements Serializable {

    private int total;
    private int size;
    private int current;
    private int pages;
    private boolean searchCount;
    private boolean isLoadFinish;
    private List<IssueBean> records;


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public boolean isSearchCount() {
        return searchCount;
    }

    public void setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
    }

    public boolean isLoadFinish() {
        return isLoadFinish;
    }

    public void setLoadFinish(boolean mLoadFinish) {
        isLoadFinish = mLoadFinish;
    }

    public List<IssueBean> getRecords() {
        return records;
    }

    public void setRecords(List<IssueBean> records) {
        this.records = records;
    }
}
