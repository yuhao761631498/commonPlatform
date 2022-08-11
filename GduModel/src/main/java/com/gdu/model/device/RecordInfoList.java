package com.gdu.model.device;

import java.io.Serializable;
import java.util.List;

/**
 * 录像信息列表
 */
public class RecordInfoList implements Serializable {

    /**
     * 时间段内录像列表
     */
    private List<RecordInfo> paths;

    /**
     * 录像列表根路径
     */
    private String rootPath;


    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public List<RecordInfo> getPaths() {
        return paths;
    }

    public void setPaths(List<RecordInfo> paths) {
        this.paths = paths;
    }

}
