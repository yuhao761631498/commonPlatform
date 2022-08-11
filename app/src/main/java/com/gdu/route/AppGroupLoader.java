package com.gdu.route;

import android.app.Activity;

import com.gdu.baselibrary.base.route.GroupLoader;
import com.gdu.baselibrary.utils.CommonUtils;

import java.util.HashMap;
import java.util.Map;

public class AppGroupLoader implements GroupLoader {
    private Map<String, Class<? extends Activity>> mActivityMap = null;

    @Override
    public Map<String, GroupLoader> injectModule() {
        final Map<String, GroupLoader> result = new HashMap<>();
        result.put("app", new AppGroupLoader());
        return result;
    }

    @Override
    public Class<? extends Activity> getActivity(String activityName) {
        if (mActivityMap == null) {
            mActivityMap = new AppActivityLoader().injectActivity();
        }
        if (CommonUtils.isEmptyString(activityName)) {
            throw new IllegalStateException(activityName + "not found!");
        }
        return mActivityMap.get(activityName);
    }
}
