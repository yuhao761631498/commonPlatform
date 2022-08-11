package com.gdu.baselibrary.network;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class MyConverterFactory extends Converter.Factory {
    private static Gson mGson;

    public static MyConverterFactory create() {
        return new MyConverterFactory();
    }

    private MyConverterFactory() {
        if (mGson == null) {
            mGson = new Gson();
        }
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        TypeAdapter<?> adapter = mGson.getAdapter(TypeToken.get(type));
        return new MyGsonResponseBodyConverter<>(mGson, adapter);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = mGson.getAdapter(TypeToken.get(type));
        return new MyGsonRequestBodyConverter<>(mGson, adapter);
    }
}
