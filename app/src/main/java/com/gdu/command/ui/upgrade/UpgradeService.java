package com.gdu.command.ui.upgrade;


import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;


public interface UpgradeService {

    @POST
    Observable<AppVersionBean> checkVersion(@Url String urlStr, @Body RequestBody jsonBody);

    @GET
    @Headers("accept:application/json")
    Observable<AppVersionBean> getDownloadUrl(@Url String urlStr);

}
