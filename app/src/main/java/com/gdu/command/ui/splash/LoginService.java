package com.gdu.command.ui.splash;

import com.gdu.baselibrary.network.BaseBean;
import com.gdu.command.ui.setting.UpdateUserInfoBean;
import com.gdu.model.config.UrlConfig;
import com.gdu.model.user.LoginBean;
import com.gdu.model.user.PersonInfoBean;

import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LoginService {

    /**
     * 获取用户信息
     *
     * @return
     */
    @POST(UrlConfig.PersonInfo)
    @Headers({"Content-type:text/plain"})
    Call<PersonInfoBean> getPersonInfo();

    /**
     * 登陆
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConfig.loginUrl)
    @Headers("Content-type:application/x-www-form-urlencoded;charset=UTF-8")
    Call<LoginBean> login(@FieldMap Map<String, String> fields);

    /**
     * 修改用户信息
     *
     * @param jsonBody
     * @return
     */
    @POST(UrlConfig.updateUserInfo)
    Observable<UpdateUserInfoBean> updateUserInfo(@Body RequestBody jsonBody);


    @POST(UrlConfig.getPswByPhone)
    @Headers({
            "Content-type:application/json",
            "Authorization: "
    })
    Call<BaseBean> getPswByPhone(@Body RequestBody body);


    @Headers({
            "Authorization: "
    })
    @GET(UrlConfig.getCheckCode)
    Call<BaseBean> getCheckCode(@Path("phone") String phone);

}
