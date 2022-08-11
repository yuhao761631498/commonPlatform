package com.gdu.baselibrary.utils.logger;

import android.text.TextUtils;
import android.util.Log;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Log日志工具，封装logger
 * @author wixche
 */
public class MyLogUtils {
    private MyLogUtils() {
    }

    /**
     * 初始化log工具，在app入口处调用
     *
     * @param isLogEnable 是否打印log
     */
    public static void init(final boolean isLogEnable) {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                // 展示线程信息
                .showThreadInfo(false)
                // 展示调用的方法个数，默认是 2
                .methodCount(2)
                //  全局的TAG内容. 默认是 PRETTY_LOGGER
                .tag("LoggerInfo")
                .logStrategy(new CustomLogCatStrategy())
                .build();
        Logger.clearLogAdapters();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy){
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return isLogEnable;
            }
        });
    }

    public static void d(String message) {
        if (TextUtils.isEmpty(message) || message.trim().isEmpty()) {
            Logger.d("");
        } else {
            Logger.d(message);
        }
    }

    public static void i(String message) {
        if (TextUtils.isEmpty(message) || message.trim().isEmpty()) {
            Logger.i("");
        } else {
            Logger.i(message);
        }
    }

    public static void v(String message) {
        if (TextUtils.isEmpty(message) || message.trim().isEmpty()) {
            Logger.v("");
        } else {
            Logger.v(message);
        }
    }

    public static void w(String message) {
        if (TextUtils.isEmpty(message) || message.trim().isEmpty()) {
            Logger.w("");
        } else {
            Logger.w(message);
        }
    }

    public static void e(String message, Throwable e) {
        if (TextUtils.isEmpty(message) || message.trim().isEmpty()) {
            Logger.e(e,"");
        } else {
            Logger.e(e, message);
        }
    }

    public static void json(String json) {
        if (TextUtils.isEmpty(json) || json.trim().isEmpty()) {
            Logger.json("");
        } else {
            Logger.json(json);
        }
    }

    private static class CustomLogCatStrategy implements LogStrategy {
        @Override
        public void log(int priority, @Nullable String tag, @NonNull String message) {
            Log.println(priority, randomKey() + tag, message);
        }

        private int last;

        private String randomKey() {
            int random = (int) (10 * Math.random());
            if (random == last) {
                random = (random + 1) % 10;
            }
            last = random;
            return String.valueOf(random);
        }

    }

}
