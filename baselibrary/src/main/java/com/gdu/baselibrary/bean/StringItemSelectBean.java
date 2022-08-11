package com.gdu.baselibrary.bean;

import java.io.Serializable;

public class StringItemSelectBean implements Serializable {
    private String content;
    private boolean isSelected;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
