package com.gdu.baselibrary.network;

import com.gdu.model.config.UrlConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit 获取单列
 */

public class RetrofitClientUnToken {
    public volatile static Retrofit retrofit = null;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            synchronized (RetrofitClientUnToken.class) {
                if (retrofit == null) {
                    retrofit = getRetrofit();
                }
            }
        }
        return retrofit;
    }

    public static void switchRetrofit() {
        retrofit = null;
        getInstance();
    }

    public static OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLogger())
                .setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                //设置超时
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                // 错误重连,默认重试一次。
                .retryOnConnectionFailure(true)
//                .addInterceptor(retryInterceptor)
                // 自定义请求日志打印拦截器
                .addNetworkInterceptor(loggingInterceptor);
        return builder.build();
    }

    public static Retrofit getRetrofit() {
        return new Retrofit.Builder().baseUrl(UrlConfig.getHostUrl())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient())
                .build();
    }

    public static <T> T getAPIService(Class<T> service) {
        return getInstance().create(service);
    }

}
