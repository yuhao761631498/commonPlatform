package com.gdu.baselibrary.utils;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author by DELL
 * @date on 2018/1/11
 * @describe
 */

public class RecyclerViewUtil {
    /**
     * 设置自适应高度
     * @param recyclerView
     * @param layoutManager
     */
    public static void autoFixHeight(RecyclerView recyclerView, LinearLayoutManager layoutManager){
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
    }
}
