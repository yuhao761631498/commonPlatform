package com.gdu.baselibrary.network;

import com.gdu.model.config.UrlConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;

/**
 * 上传文件专用的Client.
 */
public class RetrofitUploadClient {
    public volatile static Retrofit retrofit = null;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            synchronized (RetrofitUploadClient.class) {
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
        final Interceptor headerInterceptor = chain -> {
            Request original = chain.request();

            Request request = original.newBuilder()
                    .method(original.method(), original.body())
                    .build();

            return chain.proceed(request);
        };

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLogger())
                .setLevel(HttpLoggingInterceptor.Level.HEADERS);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                //设置超时
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                // 自定义重连拦截器,错误重连,默认重试一次。
//                .addInterceptor(retryInterceptor)
                // 添加固定头部字段的拦截器
                .addInterceptor(headerInterceptor)
                .addInterceptor(new ParameterInterceptor())
                .addNetworkInterceptor(loggingInterceptor);
        return builder.build();
    }

    public static Retrofit getRetrofit() {
        return new Retrofit.Builder().baseUrl(UrlConfig.getHostUrl())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(MyConverterFactory.create())
                .client(getOkHttpClient())
                .build();
    }

    public static <T> T getAPIService(Class<T> service) {
        return getInstance().create(service);
    }

}
