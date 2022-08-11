package com.gdu.command.ui.duty;

import android.content.Intent;

import com.gdu.baselibrary.base.BasePresenter;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.command.ui.my.LoginActivity;
import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DutyPresenter extends BasePresenter {

    private IDutyView iDutyView;

    public void setView(IDutyView iDutyView) {
        this.iDutyView = iDutyView;
    }

//    /**
//     * 预警处理
//     *
//     * @param dataBean 封装所有参数的对象
//     */
//    public void alarmHandle(AlarmHandleSendBean dataBean) {
//        MyLogUtils.i("alarmHandle()");
//        final String paramStr = new Gson().toJson(dataBean);
//        final RequestBody mBody = RequestBody.create(paramStr,
//                MediaType.parse("application/json;charset=UTF-8"));
//        mAlarmService.alarmHandle(mBody)
//                .subscribeOn(Schedulers.io())
//                .to(RxLife.toMain(getBaseActivity()))
//                .subscribe(data -> {
//                    final boolean isSuc = data != null && data.code == 0;
//                    if (isSuc) {
//                        mAlarmDetailView.caseHandleResult(true);
//                    } else {
//                        mAlarmDetailView.caseHandleResult(false);
//                    }
//                }, throwable -> {
//                    mAlarmDetailView.caseHandleResult(false);
//                    MyLogUtils.e("预警处理出错", throwable);
//                });
//    }


    public void getDutyForMe(String dutyMonth) {
        DutyParamBean dutyParamBean = new DutyParamBean();
        dutyParamBean.setRegionCode("department_fire_815049");
        dutyParamBean.setDutyMonth(dutyMonth);
        final String paramStr = new Gson().toJson(dutyParamBean);
        final RequestBody mBody = RequestBody.create(paramStr,
                MediaType.parse("application/json;charset=UTF-8"));

        DutyService apiService = RetrofitClient.getAPIService(DutyService.class);
        apiService.getDutyForMe(mBody).enqueue(new Callback<DutyForMeBean>() {
            @Override
            public void onResponse(Call<DutyForMeBean> call, Response<DutyForMeBean> response) {
                final DutyForMeBean dutyForMeBean = response.body();
                final boolean isEmptyData =
                        dutyForMeBean == null || dutyForMeBean.getCode() != 0 || dutyForMeBean.getData() == null;
                getBaseActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isEmptyData) {
                            if (dutyForMeBean.getCode() == 401) {
                                getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                            }
                            iDutyView.getDutyForMe(null);
                            return;
                        }

                        iDutyView.getDutyForMe(dutyForMeBean.getData());
                    }
                });
            }

            @Override
            public void onFailure(Call<DutyForMeBean> call, Throwable t) {
                getBaseActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iDutyView.getDutyForMe(null);
                    }
                });
            }
        });
    }

    public void getDutyForDayInfo(String date) {
        DutyService apiService = RetrofitClient.getAPIService(DutyService.class);
        apiService.getDutyForDay("department_fire_815049", date).enqueue(new Callback<DutyForDayBean>() {
            @Override
            public void onResponse(Call<DutyForDayBean> call, Response<DutyForDayBean> response) {
                final DutyForDayBean dutyForDayBean = response.body();
                final boolean isEmptyData =
                        dutyForDayBean == null || dutyForDayBean.getCode() != 0 || dutyForDayBean.getData() == null;
                getBaseActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isEmptyData) {
                            if (dutyForDayBean.getCode() == 401) {
                                getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                            }
                            iDutyView.getDutyForDay(null);
                            return;
                        }
                        iDutyView.getDutyForDay(dutyForDayBean.getData());
                    }
                });
            }

            @Override
            public void onFailure(Call<DutyForDayBean> call, Throwable t) {
                getBaseActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        iDutyView.getDutyForDay(null);
                    }
                });
            }
        });
    }

}
