package com.gdu.baselibrary.network;

import com.gdu.baselibrary.utils.logger.MyLogUtils;

import androidx.annotation.NonNull;
import okhttp3.logging.HttpLoggingInterceptor;

public class HttpLogger implements HttpLoggingInterceptor.Logger {
    private StringBuilder mMessage = new StringBuilder();

    @Override
    public void log(@NonNull String message) {
        // 请求或者响应开始
        if (message.startsWith("--> POST") || message.startsWith("--> GET")) {
            mMessage.setLength(0);
        }
//        // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
//        final boolean isBodyContent = (message.startsWith("{") && message.endsWith("}"))
//                || (message.startsWith("[") && message.endsWith("]"));
//        if (isBodyContent) {
//            MyLogUtils.d("log() message = " + message);
////            message = JsonUtil.formatJson(message);
//        }
        mMessage.append(message.concat("\n"));
        // 请求或者响应结束，打印整条日志
        if (message.startsWith("<-- END HTTP")) {
            MyLogUtils.d(mMessage.toString());
        }
    }
}