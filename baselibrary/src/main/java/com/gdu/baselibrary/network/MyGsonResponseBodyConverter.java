package com.gdu.baselibrary.network;

import android.annotation.TargetApi;
import android.os.Build;

import com.gdu.baselibrary.utils.MyConstants;
import com.gdu.baselibrary.utils.eventbus.EventMessage;
import com.gdu.baselibrary.utils.eventbus.GlobalEventBus;
import com.gdu.baselibrary.utils.logger.MyLogUtils;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

import static java.nio.charset.StandardCharsets.UTF_8;

final class MyGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    MyGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        try {
            final JSONObject jsonObject = new JSONObject(response);
            final int code = jsonObject.getInt("code");
            // 根据返回的code不同使用不同的处理方式,把非2000的返回码的返回内容以抛指定异常的方式传递到错误处理里面进行二次处理.
            if (code == MyConstants.CALLBACK_SUCCESS) {
                return normalCallback(value, response);
            } else {
                if (code == MyConstants.ERROR_CODE_401) {
                    GlobalEventBus.getBus().post(new EventMessage(MyConstants.TOKEN_LOSE_EFFICACY));
                }
                throw new ErrorCallBackException("RequestErrorCallback", response);
            }
        } catch (Exception e) {
            MyLogUtils.e("请求回调处理出错", e);
            throw new ErrorCallBackException("RequestException", response);
        }
    }

    /**
     * 正常回掉处理.
     * @param value 返回的Response内容.
     * @param response Response的string内容.
     * @return
     * @throws IOException
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private T normalCallback(ResponseBody value, String response) throws IOException {
        // 由于ResponseBody的string()只能调用一次.所以需要如下方式处理重新转为JsonReader.
        final MediaType type = value.contentType();
        final Charset charset = type != null ? type.charset(UTF_8) : UTF_8;
        final InputStream is = new ByteArrayInputStream(response.getBytes());
        final InputStreamReader reader = new InputStreamReader(is, charset);
        final JsonReader jsonReader = gson.newJsonReader(reader);
        try {
            final T result = adapter.read(jsonReader);
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw new JsonIOException("JSON document was not fully consumed.");
            }
            return result;
        } finally {
            value.close();
        }
    }
}