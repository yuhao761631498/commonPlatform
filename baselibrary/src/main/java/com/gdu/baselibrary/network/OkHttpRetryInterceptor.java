package com.gdu.baselibrary.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpRetryInterceptor implements Interceptor {

    private int mMaxRetryCount;
    private long mRetryInterval;

    public OkHttpRetryInterceptor(int maxRetryCount, long retryInterval) {
        mMaxRetryCount = maxRetryCount;
        mRetryInterval = retryInterval;
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        Response response = doRequest(chain, request);
        int retryNum = 1;
        while (((response == null) || !response.isSuccessful()) && retryNum <= mMaxRetryCount) {
            try {
                Thread.sleep(mRetryInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retryNum++;
            response = doRequest(chain, request);
        }
        return response;
    }

    private Response doRequest(Chain chain, Request request) {
        try {
            return chain.proceed(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class Builder {

        private int mRetryCount = 1;
        private long mRetryInterval = 1000;

        public Builder buildRetryCount(int retryCount) {
            this.mRetryCount = retryCount;
            return this;
        }

        public Builder buildRetryInterval(long retryInterval) {
            this.mRetryInterval = retryInterval;
            return this;
        }

        public OkHttpRetryInterceptor build() {
            return new OkHttpRetryInterceptor(mRetryCount, mRetryInterval);
        }

    }

}