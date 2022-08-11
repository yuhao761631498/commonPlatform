package com.gdu.route;

import android.app.Activity;

import com.gdu.baselibrary.base.route.ActivityLoader;
import com.gdu.command.ui.cases.deal.CaseClosedActivity;
import com.gdu.command.ui.message.ChatGroupActivity;

import java.util.HashMap;
import java.util.Map;

public class AppActivityLoader implements ActivityLoader {
    @Override
    public Map<String, Class<? extends Activity>> injectActivity() {
        Map<String, Class<? extends Activity>> result = new HashMap<>();
        result.put("ChatGroupActivity", ChatGroupActivity.class);
        result.put("CaseClosedActivity", CaseClosedActivity.class);
        return result;
    }
}
