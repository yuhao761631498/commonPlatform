package com.gdu.command.ui.setting;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface SettingsService {

    /**
     * 服务器授权
     *
     * @param url    请求地址
     * @param params 参数
     * @return
     */
    @GET
    Call<ResponseBody> setServerAuthCode(@Url String url, @QueryMap Map<String, String> params);


}
