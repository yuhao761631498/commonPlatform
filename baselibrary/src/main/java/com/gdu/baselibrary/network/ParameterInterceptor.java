package com.gdu.baselibrary.network;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.gdu.baselibrary.BuildConfig;
import com.gdu.baselibrary.utils.MMKVUtil;
import com.gdu.baselibrary.utils.SPStringUtils;
import com.gdu.baselibrary.utils.logger.MyLogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;


/**
 * Created by Administrator on 2016/10/10.
 * 参数格式为Json格式的拦截器
 */

public class ParameterInterceptor implements Interceptor {
    private static final String TAG = ParameterInterceptor.class.getSimpleName();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        HttpUrl httpUrl = oldRequest.url().newBuilder().build();

        Request.Builder builder = oldRequest.newBuilder();
        builder.url(httpUrl);
        //添加header token
        builder.addHeader("Authorization", "Bearer "+ MMKVUtil.getString(SPStringUtils.TOKEN,"NULL"));
        if (oldRequest.body() != null) {
            builder.post(oldRequest.body());
        }
        Request newRequest = builder.build();

        Response response = chain.proceed(newRequest);
        //打印返回数据
        if (BuildConfig.DEBUG) {
            ResponseBody resultBody = response.body();
            final String result = resultBody.string();
//            try {
//                MyLogUtils.json("intercept() result = " + result);
//            } catch (Exception e) {
//                MyLogUtils.e("intercept() 参数获取出错", e);
//            }
            /*** 因为调用ResponseBody.string()后即关闭，后续无法获取内容 */
            response = response.newBuilder()
                    .body(ResponseBody.create(resultBody.contentType(), result))
                    .build();
        }
        return response;
    }

    @NonNull
    private RequestBody makeRequestBody(Request oldRequest) {
        HttpUrl oldUrl = oldRequest.url();

        JSONObject data = new JSONObject();
        try {
            if (oldRequest.body() instanceof FormBody) {
                /*** 当参数以 @Field @FieldMap 提交时 */
                MyLogUtils.d("instanceof FormBody");

                FormBody body = (FormBody) oldRequest.body();
                if (body != null) {
                    for (int i = body.size() - 1; i >= 0; i--) {
                        String name = body.name(i);
                        String value = body.value(i);
                        data.put(name, value);
                    }
                }
            } else if (oldRequest.body() instanceof MultipartBody) {
                /*** 当参数以 @MultipartBody 提交时 */
                MyLogUtils.d("instanceof MultipartBody");

            } else {/*** 当参数以 @Body 提交时 */
                String bodyString = bodyToString(oldRequest.body());
                if (!TextUtils.isEmpty(bodyString)) {
                    data = new JSONObject(bodyString);
                    MyLogUtils.d("bodyStr = " + bodyString);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /** * 添加Sign参数 */
        FormBody.Builder newBodyBuilder = new FormBody.Builder();
        newBodyBuilder.add("data", data.toString());
        MyLogUtils.d("请求地址RequestUrl = " + oldUrl.url().toString() + "; 请求参数Params = "
                + data.toString());
        return newBodyBuilder.build();
    }

    /**
     * body 中的内容
     *
     * @param request
     * @return
     */
    private String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null) {
                copy.writeTo(buffer);
            } else {
                return "";
            }
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
