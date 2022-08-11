package com.gdu.baselibrary.base.route;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.gdu.baselibrary.utils.CommonUtils;

public class Transfer {

    public static void startActivity(Activity activity, String path, Intent intent) {
        Class<?> clazz = parseActivityPath(path);
        if (clazz == null || !Activity.class.isAssignableFrom(clazz)) {
            throw new IllegalStateException("con't find the class!");
        }
        intent.setClass(activity, clazz);
        activity.startActivity(intent);
    }

    private static Class<?> parseActivityPath(String path) {
        String module = parseModule(path);

        GroupLoader groupLoader = Injector.getModuleLoader(module);

        String activityName = parseClass(path);

        return groupLoader.getActivity(activityName);
    }

    private static String parseModule(String path) {
        if (CommonUtils.isEmptyString(path)) {
            throw new IllegalArgumentException("path must not null!");
        }

        int separatorIndex = path.indexOf("/");
        if (separatorIndex == -1) {
            throw new IllegalStateException("path must has / ");
        }

        return path.substring(0, separatorIndex);
    }

    private static String parseClass(String path) {
        if (CommonUtils.isEmptyString(path)) {
            throw new IllegalArgumentException("path must not null!");
        }

        int separatorIndex = path.indexOf("/");
        if (separatorIndex == -1) {
            throw new IllegalStateException("path must has / ");
        }
        return path.substring(separatorIndex + 1);
    }
}
