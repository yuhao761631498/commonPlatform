package com.gdu.command.ui.patrol.presenter;

public interface IPatrolDiaryView {

    void showOrHidePb(boolean isShow, String tip);

    void onRequestCallback(String requestName, boolean isSuc, Object result);

}
