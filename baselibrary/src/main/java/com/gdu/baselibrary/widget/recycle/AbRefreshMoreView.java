package com.gdu.baselibrary.widget.recycle;

import android.content.Context;
import android.widget.LinearLayout;

public abstract  class AbRefreshMoreView  extends AbRefreshView {

    public AbRefreshMoreView(Context context) {
        super(context);
        mMainView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    //默认状态
    protected abstract void onNormalState();

    //正在刷新
    protected abstract void onLoadingMore();

    //刷新成功
    protected abstract void onResultSuccess();

    //刷新失败
    protected abstract void onResultFail();
}
