package com.gdu.command.ui.video.model;

import com.gdu.command.ui.video.bean.PlayStreamBean;
import com.gdu.model.config.UrlConfig;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface VideoService {

    /**
     * 获取摄像头列表
     *
     * @return
     */
    @GET(UrlConfig.DeviceListInfo)
    Observable<ResponseBody> getDeviceList(@QueryMap HashMap<String, String> params);

    /**
     * 登陆
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConfig.getMp4RecordFolder)
    Call<RecordBean> getRecordFolder(@FieldMap Map<String, String> fields);

    /**
     * 获取设备推流地址
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConfig.playStream)
    @Headers("Content-type:application/x-www-form-urlencoded")
    Call<PlayStreamBean> getPlayStream(@Field("deviceInfoListJsonStr") String params);
}
