package com.gdu.baselibrary.base.route;

import android.app.Activity;

import java.util.Map;

public interface GroupLoader {
    Map<String, GroupLoader> injectModule();

    Class<? extends Activity> getActivity(String activityName);
}
