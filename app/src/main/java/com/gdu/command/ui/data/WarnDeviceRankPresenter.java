package com.gdu.command.ui.data;

import com.gdu.baselibrary.base.BasePresenter;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.rxjava.rxlife.RxLife;

import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Copyright (C), 2020-2030, 普宙飞行器科技有限公司
 * <p>
 * 功能描述:
 * <p>
 * 创建时间: 2022/4/25 17:20
 *
 * @author yuhao
 */
public class WarnDeviceRankPresenter extends BasePresenter {

    private IDataMonitorShowView mView;

    public void setView(IDataMonitorShowView iView) {
        this.mView = iView;
    }

    public void requestData() {
        getCaseDealRank();  //案件处理设备排名
    }


    /** 获取案件处置排名*/
    public void getCaseDealRank() {
        DataMonitorService service = RetrofitClient.getAPIService(DataMonitorService.class);
        service.getDataCaseRank().subscribeOn(Schedulers.io())
                .to(RxLife.toMain(getBaseActivity()))
                .subscribe(new Consumer<DataCaseRankBean>() {
                    @Override
                    public void accept(DataCaseRankBean bean) throws Throwable {
                        boolean isNotEmpty = null != bean && null != bean.getData() && bean.getData().size() != 0;
                        if (isNotEmpty) {
                            mView.getCaseDealRank(bean.getData());
                        }
                    }
                }, throwable -> {
                    MyLogUtils.e("获取案件处置排名出错", throwable);
                });
    }
}
