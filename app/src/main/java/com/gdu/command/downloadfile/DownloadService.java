package com.gdu.command.downloadfile;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface DownloadService {

    /**
     * 下载适配文件
     * @return
     */
    @Streaming
    @GET
    Observable<ResponseBody> downloadVideo(@Url String url);

}
