package com.gdu.command.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;

import com.gdu.baselibrary.base.BasePresenter;
import com.gdu.baselibrary.base.IBaseDisplay;
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
 * 创建时间: 2022/5/6 11:19
 *
 * @author yuhao
 */
public class HomePresenter1 extends BasePresenter {

    private IHomeView1 mView;

    @Override
    public void attachView(IBaseDisplay display) {
        super.attachView(display);
    }

    public void setView(IHomeView1 iView) {
        this.mView = iView;
    }

    public void requestData() {
        getAlarmTypeForDay();
        getAlarmDealForDay();
    }



    /** 某一天告警事件类型分布*/
    public void getAlarmTypeForDay() {
        HomeService service = RetrofitClient.getAPIService(HomeService.class);
        long currentTime = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat") String timeNow = new SimpleDateFormat("yyyy-MM-dd").format(currentTime);
        service.getAlarmTypeForDay(timeNow).subscribeOn(Schedulers.io())
                .to(RxLife.toMain(getBaseActivity()))
                .subscribe(new Consumer<AlarmTypeBean>() {
                    @Override
                    public void accept(AlarmTypeBean bean) throws Throwable {
                        boolean isNotEmpty = null != bean && null != bean.getData();
                        if (isNotEmpty) {
                            mView.alarmTypeForDay(bean.getData());
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


    /** 某一天处理结果*/
    public void getAlarmDealForDay() {
        HomeService service = RetrofitClient.getAPIService(HomeService.class);
        long currentTime = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat") String timeNow = new SimpleDateFormat("yyyy-MM-dd").format(currentTime);
        service.getAlarmDealForDay(timeNow).subscribeOn(Schedulers.io())
                .to(RxLife.toMain(getBaseActivity()))
                .subscribe(new Consumer<AlarmDealBean>() {
                    @Override
                    public void accept(AlarmDealBean bean) throws Throwable {
                        boolean isNotEmpty = null != bean && null != bean.getData();
                        if (isNotEmpty) {
                            mView.alarmDealResult(bean.getData());
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

}
