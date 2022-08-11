package com.gdu.command.ui.home;

import com.gdu.command.ui.home.weather.WeatherBeans;
import com.gdu.model.cases.IssueBean;

import java.util.List;
import java.util.Map;

public interface IHomeView {

    void showData(List<IssueBean> beanList);

    /**
     * 显示天气信息
     * @param weatherBeans
     */
    void setWeatherBeans(WeatherBeans weatherBeans);

    /**
     * 显示案件数量
     * @param caseCounts
     */
    void setCaseCount(Map<String, Double> caseCounts);

    /**
     * 显示待批示数量
     * @param count
     */
    void setCommentsCount(int count);

    /**
     * 显示待批示tab
     */
    void showComments();

    /**
     * 显示提示消息
     * @param toast
     */
    void showToast(String toast);

    void showOrHidePb(boolean isShow, String tip);
}
