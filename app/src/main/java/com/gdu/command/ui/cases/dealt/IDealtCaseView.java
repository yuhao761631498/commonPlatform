package com.gdu.command.ui.cases.dealt;

import com.gdu.model.cases.CommentInfo;
import com.gdu.model.cases.IssueBean;

import java.util.List;
import java.util.Map;

public interface IDealtCaseView {

    /**
     * 显示批示列表
     * @param commentInfoList
     */
    void showComments(List<CommentInfo> commentInfoList);

    void showDetail(IssueBean issueBean);

    /**
     * 展示案件图片
     * @param pictures
     */
    void showPictures(List<String> pictures);

    /**
     * 展示视频
     * @param videos
     */
    void showVideos(List<String> videos);

    /**
     * 展示处置记录
     */
    void showRecords();

}
