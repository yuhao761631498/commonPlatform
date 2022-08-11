package com.gdu.mqttchat.chat.view;

import com.gdu.model.cases.IssueBean;

public interface IChatView {
    void onCaseGot(IssueBean issueBean);
    void onCaseDetailGet(CaseChatDetailBean.DataBean data);
}
