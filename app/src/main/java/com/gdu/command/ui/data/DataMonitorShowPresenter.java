package com.gdu.command.ui.data;

import com.gdu.baselibrary.base.BasePresenter;
import com.gdu.baselibrary.network.BaseBean;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.gdu.model.data.DataAlarmTotalBean;
import com.gdu.model.data.DataCaseTotalBean;
import com.gdu.model.data.DataHighTotal;
import com.rxjava.rxlife.RxLife;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DataMonitorShowPresenter extends BasePresenter {

    private IDataMonitorShowView mView;

    public void setView(IDataMonitorShowView iView) {
        this.mView = iView;
    }

    public void requestData() {
        getCaseTotal();
        getAlarmTotal();
        getHighTotal();
        getCaseFromByYear();
        getCaseDealRank();
        getAlarmResInfo();
        getAlarmByYearDistribute();
    }

    /** 获取案件总数统计*/
    public void getCaseTotal() {
        DataMonitorService service = RetrofitClient.getAPIService(DataMonitorService.class);
        service.getDataCaseTotal().subscribeOn(Schedulers.io())
                .to(RxLife.toMain(getBaseActivity()))
                .subscribe(new Consumer<BaseBean<DataCaseTotalBean>>() {
                    @Override
                    public void accept(BaseBean<DataCaseTotalBean> bean) throws Throwable {
                        boolean isNotEmpty = null != bean && bean.data != null;
                        if (isNotEmpty) {
                            mView.getCaseTotalCount(bean.data);
                        }
                    }
                }, throwable -> {
                    MyLogUtils.e("获取案件总数统计出错", throwable);
                });
    }

    /** 获取告警总数*/
    public void getAlarmTotal() {
        DataMonitorService service = RetrofitClient.getAPIService(DataMonitorService.class);
        service.getDataAlarmTotal().subscribeOn(Schedulers.io())
                .to(RxLife.toMain(getBaseActivity()))
                .subscribe(new Consumer<DataAlarmTotalBean>() {
                    @Override
                    public void accept(DataAlarmTotalBean bean) throws Throwable {
                        boolean isNotEmpty = null != bean;
                        if (isNotEmpty) {
                            mView.getAlarmTotalCount(bean);
                        }
                    }
                }, throwable -> {
                    MyLogUtils.e("获取告警总数出错", throwable);
                });
    }

    /** 获取高点监控总数*/
    public void getHighTotal() {
        DataMonitorService service = RetrofitClient.getAPIService(DataMonitorService.class);
        service.getDataHighTotal().subscribeOn(Schedulers.io())
                .to(RxLife.toMain(getBaseActivity()))
                .subscribe(new Consumer<DataHighTotal>() {
                    @Override
                    public void accept(DataHighTotal bean) throws Throwable {
                        boolean isNotEmpty = null != bean && null != bean.getData();
                        if (isNotEmpty) {
                            mView.getHighTotalCount(bean);
                        }
                    }
                }, throwable -> {
                    MyLogUtils.e("获取高点监控总数出错", throwable);
                });
    }

    /** 获取今年案件来源分布*/
    public void getCaseFromByYear() {
        DataMonitorService service = RetrofitClient.getAPIService(DataMonitorService.class);
        service.getCaseResFrom().subscribeOn(Schedulers.io())
                .to(RxLife.toMain(getBaseActivity()))
                .subscribe(new Consumer<BaseBean<DataCaseResFromBean>>() {
                    @Override
                    public void accept(BaseBean<DataCaseResFromBean> bean) throws Throwable {
                        boolean isNotEmpty = null != bean && null != bean.data && null != bean.data.getCaseInfoSourceCountVOS();
                        if (isNotEmpty) {
                            mView.getCaseFromByYear(bean.data.getCaseInfoSourceCountVOS());
                        }
                    }
                }, throwable -> {
                    MyLogUtils.e("获取今年案件来源分布出错", throwable);
                });
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

    /** 告警事件类型分布，按告警等级、告警类型进行统计*/
    public void getAlarmResInfo() {
        DataMonitorService service = RetrofitClient.getAPIService(DataMonitorService.class);
        service.getAlarmResInfo().subscribeOn(Schedulers.io())
                .to(RxLife.toMain(getBaseActivity()))
                .subscribe(new Consumer<AlarmResInfoBean>() {
                    @Override
                    public void accept(AlarmResInfoBean bean) throws Throwable {
                        boolean isNotEmpty = null != bean && null != bean.getData();
                        if (isNotEmpty) {
                            mView.getAlarmResInfo(bean.getData());
                        }
                    }
                }, throwable -> {
                    MyLogUtils.e("告警事件类型分布出错", throwable);
                });
    }

    /** 告警数据时间分布趋势*/
    public void getAlarmByYearDistribute() {
        DataMonitorService service = RetrofitClient.getAPIService(DataMonitorService.class);
        service.getAlarmByYearDistribute().subscribeOn(Schedulers.io())
                .to(RxLife.toMain(getBaseActivity()))
                .subscribe(new Consumer<DataAlarmByYearBean>() {
                    @Override
                    public void accept(DataAlarmByYearBean bean) throws Throwable {
                        boolean isNotEmpty = null != bean && null != bean.getData() && bean.getData().size() != 0;
                        if (isNotEmpty) {
                            mView.getAlarmByYearDistribute(bean.getData());
                        }
                    }
                }, throwable -> {
                    MyLogUtils.e("告警数据时间分布趋势出错", throwable);
                });
    }

}
