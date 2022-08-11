package com.gdu.command.ui.cases.deal;

import com.gdu.model.cases.CommentInfo;

import java.util.List;

public interface IDealCaseView {

    /**
     * 取证完成
     */
    void onForensicsFinished();

    /**
     * 结案完成
     */
    void onCaseFinished();

    /**
     * 显示批示列表
     * @param commentInfoList
     */
    void showComments(List<CommentInfo> commentInfoList);

    /**
     * 案件批示完成
     */
    void onCaseCommented();

    void showToast(String toast);

}
