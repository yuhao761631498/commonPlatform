package com.gdu.command.ui.data;

import android.annotation.SuppressLint;
import android.content.Intent;

import com.gdu.baselibrary.base.BasePresenter;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.command.ui.my.LoginActivity;
import com.rxjava.rxlife.RxLife;

import java.text.SimpleDateFormat;

import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Copyright (C), 2020-2030, 普宙飞行器科技有限公司
 * <p>
 * 功能描述:
 * <p>
 * 创建时间: 2022/4/25 16:12
 *
 * @author yuhao
 */
public class ForewarnStatPresenter extends BasePresenter {

    private IForeWarnView mView;

    public void setView(IForeWarnView iView) {
        this.mView = iView;
    }

    public void requestData() {
        getCaseDealRank();  //案件处理设备排名
        getAlarmResInfo();
        getDealResultInfo();
    }

    private void getDealResultInfo() {
        ForeWarnService service = RetrofitClient.getAPIService(ForeWarnService.class);

        long currentTime = System.currentTimeMillis();

        @SuppressLint("SimpleDateFormat") String startTime =
                new SimpleDateFormat("yyyy/MM/dd").format(currentTime - 24 * 30 * 60 * 60);

        @SuppressLint("SimpleDateFormat") String endTime =
                new SimpleDateFormat("yyyy/MM/dd").format(currentTime);

        service.getDealResult(startTime, endTime).subscribeOn(Schedulers.io())
                .to(RxLife.toMain(getBaseActivity()))
                .subscribe(new Consumer<DealResultBean>() {
                    @Override
                    public void accept(DealResultBean bean) throws Throwable {
                        boolean isNotEmpty = null != bean && null != bean.getData();
                        if (isNotEmpty) {
                            mView.getDealResultInfo(bean.getData());
                        }else {
                            if (bean!=null&&bean.getCode() == 401) {
                                getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                            }
                        }
                    }
                }, throwable -> {
                    MyLogUtils.e("告警事件类型分布出错", throwable);
                });
    }


    /**
     * 告警事件类型分布，按告警等级、告警类型进行统计
     */
    public void getAlarmResInfo() {
        ForeWarnService service = RetrofitClient.getAPIService(ForeWarnService.class);
        service.getAlarmResInfo().subscribeOn(Schedulers.io())
                .to(RxLife.toMain(getBaseActivity()))
                .subscribe(new Consumer<AlarmResInfoBean>() {
                    @Override
                    public void accept(AlarmResInfoBean bean) throws Throwable {
                        boolean isNotEmpty = null != bean && null != bean.getData();
                        if (isNotEmpty) {
                            mView.getAlarmResInfo(bean.getData());
                        }else {
                            if (bean!=null&&bean.getCode() == 401) {
                                getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                            }
                        }
                    }
                }, throwable -> {
                    MyLogUtils.e("告警事件类型分布出错", throwable);
                });
    }


    /**
     * 获取案件处置排名
     */
    public void getCaseDealRank() {
        ForeWarnService service = RetrofitClient.getAPIService(ForeWarnService.class);
        service.getRankInfo().subscribeOn(Schedulers.io())
                .to(RxLife.toMain(getBaseActivity()))
                .subscribe(new Consumer<DeviceRankBean>() {
                    @Override
                    public void accept(DeviceRankBean bean) throws Throwable {
                        boolean isNotEmpty = null != bean && null != bean.getData() && bean.getData().size() != 0;
                        if (isNotEmpty) {
                            mView.getDeviceRankInfo(bean.getData());
                        }else {
                            if (bean!=null&&bean.getCode() == 401) {
                                getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                            }
                        }
                    }
                }, throwable -> {
                    MyLogUtils.e("获取案件处置排名出错", throwable);
                });
    }

}
