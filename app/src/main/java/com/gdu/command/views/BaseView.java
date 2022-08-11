package com.gdu.command.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;


/**
 * Created by zhangzhilai on 2018/5/3.
 * 自定义基类
 */

public abstract class BaseView extends RelativeLayout implements View.OnClickListener{


    public Context mContext;

    public BaseView(Context context) {
        this(context, null);
    }

    public BaseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
        initListener();
    }

    /**
     * 初始化view
     */
    public void initView() {
        LayoutInflater.from(mContext).inflate(getLayoutRes(), this);
    }

    /**
     * 初始化监听事件
     */
    public abstract void initListener();

    /**
     * 获取界面的res id
     * @return
     */
    public abstract int getLayoutRes();

    @Override
    public void onClick(View view) {

    }
}
