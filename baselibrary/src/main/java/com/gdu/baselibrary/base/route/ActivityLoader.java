package com.gdu.baselibrary.base.route;

import android.app.Activity;

import java.util.Map;

public interface ActivityLoader {
    Map<String, Class<? extends Activity>> injectActivity();
}
