package com.gdu.baselibrary.base.route;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huweiqiang on 2017/4/25.
 */

public class Injector {
    private static Map<String, GroupLoader> sModuleLoaderMap = new HashMap<>();

    public static void inject() {
        try {
            GroupLoader mainGroupLoader = (GroupLoader) Class.forName("com.gdu.route.AppGroupLoader").newInstance();
            Map<String, GroupLoader> mainModuleLoaderMap = mainGroupLoader.injectModule();
            if (mainModuleLoaderMap != null) {
                sModuleLoaderMap.putAll(mainModuleLoaderMap);
            }

            GroupLoader mineGroupLoader = (GroupLoader) Class.forName("com.huweiqiang.mine.MineGroupLoader").newInstance();
            Map<String, GroupLoader> mineModuleLoaderMap = mineGroupLoader.injectModule();
            if (mineModuleLoaderMap != null) {
                sModuleLoaderMap.putAll(mineModuleLoaderMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static GroupLoader getModuleLoader(String moduleName) {
        return sModuleLoaderMap.get(moduleName);
    }
}
