package com.gdu.command.ui.video.presenter;

import com.gdu.baselibrary.network.BaseBean;
import com.gdu.model.config.UrlConfig;

import java.util.HashMap;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface DevControlService {

    @GET(UrlConfig.ptzControl)
    Observable<BaseBean> ptzControl(@QueryMap HashMap<String, String> params);
}
