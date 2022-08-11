package com.gdu.command.ui.cases;

import com.gdu.model.cases.IssueBean;

import java.util.List;

public interface IMyCaseView {

     void issueNoHandle(List<IssueBean> issueBeans, boolean isLoadMoreData);
     void issueHandling(List<IssueBean> issueBeans, boolean isLoadMoreData);
     void issueWaitHandled(List<IssueBean> issueBeans, boolean isLoadMoreData);
     void issueHandled(List<IssueBean> issueBeans, boolean isLoadMoreData);
     void toast(String toast);
     void loadMoreCompleted();

     /**
      * 显示待批示数量
      * @param data
      */
     void setCommentsCount(List<IssueBean> data);

     /**
      * 是否显示批示数据
      * @param isShow
      */
     void isShowWaitView(boolean isShow);

     void showOrHidePb(boolean isShow, String tip);
}
