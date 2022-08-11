package com.gdu.command.ui.my;

import com.gdu.baselibrary.base.BasePresenter;
import com.gdu.baselibrary.network.BaseBean;
import com.gdu.baselibrary.network.RetrofitClient;
import com.gdu.command.ui.splash.LoginService;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Copyright (C), 2020-2030, 武汉中旗生物医疗电子有限公司
 * <p>
 * 功能描述:
 * <p>
 * 创建时间: 2022/6/15 14:06
 *
 * @author yuhao
 */
public class ResetPswPresenter extends BasePresenter {

    private IResetPswView mView;

    public void setView(IResetPswView iView) {
        this.mView = iView;
    }

    public void getCheckCode(String phone) {
        LoginService apiService = RetrofitClient.getAPIService(LoginService.class);
        apiService.getCheckCode(phone).enqueue(new Callback<BaseBean>() {
            @Override
            public void onResponse(Call<BaseBean> call, Response<BaseBean> response) {
                BaseBean baseBean = response.body();
                final boolean isEmptyData =
                        baseBean == null || baseBean.code != 0;

                if (isEmptyData) {
                    mView.failure(false);
                    return;
                }
                mView.success(false);
            }

            @Override
            public void onFailure(Call<BaseBean> call, Throwable t) {
                mView.failure(false);
            }
        });
    }

    public void getPwdByPhone(String verification_code, String phone, String newPsw, String confirmPsw)  {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("password", newPsw);
            jsonObject.put("passwordConfirm", confirmPsw);
            jsonObject.put("phone", phone);
            jsonObject.put("validCode", verification_code);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String json = jsonObject.toString();

        final RequestBody mBody = RequestBody.create(json,
                MediaType.parse("application/json;charset=UTF-8"));

        LoginService apiService = RetrofitClient.getAPIService(LoginService.class);

        apiService.getPswByPhone(mBody).enqueue(new Callback<BaseBean>() {
            @Override
            public void onResponse(Call<BaseBean> call, Response<BaseBean> response) {
                BaseBean baseBean = response.body();
                final boolean isEmptyData =
                        baseBean == null || baseBean.code != 0;

                if (isEmptyData) {
                    mView.failure(true);
                    return;
                }
                mView.success(true);
            }

            @Override
            public void onFailure(Call<BaseBean> call, Throwable t) {
                mView.failure(true);
            }
        });
    }
}
