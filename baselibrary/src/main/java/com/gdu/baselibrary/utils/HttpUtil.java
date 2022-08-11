package com.gdu.baselibrary.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {

    private final int TIMEOUT=10;

    public String sendPost(HashMap<String,String> map,String url,String headKey,String HeadValue,
                           String mediaTypeParse){
        try {
            String params = getParams(map);

            OkHttpClient client = new OkHttpClient().newBuilder().readTimeout(TIMEOUT,
                    TimeUnit.SECONDS)
                    .build();
            MediaType mediaType = MediaType.parse(mediaTypeParse);
            RequestBody body = RequestBody.create(mediaType, params);
            Request request = new Request.Builder()
                    .url(url)
                    .method("POST", body)
                    .addHeader(headKey, HeadValue)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    //合并参数
    public String getParams(Map<String, String> args) {
        String params = "";
        if (args != null && args.size() > 0) {
            Iterator<Map.Entry<String, String>> iter = args.entrySet().iterator();
            StringBuilder sb = new StringBuilder();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                sb.append(entry.getKey());
                sb.append("=").append(entry.getValue());
                sb.append("&");
            }
            sb.deleteCharAt(sb.lastIndexOf("&"));
            params = sb.toString();
        }
        return params;
    }
}
