package com.gdu.command.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.gdu.model.config.StorageConfig;
import com.gdu.command.ui.splash.SplashActivity;

public abstract class Base2Activity extends Activity {

    private TextView titleTv,rightTv;
    protected boolean isThisNormalBoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!normalStarUp()) {
            return;
        }
        setContentView(getContentView());
//        getCommonTitle();
//        GduCPApplication.getInstance().addActivity(this);
        findViews();
        initViews();
        initLisenter();

    }

    public boolean normalStarUp() {
        isThisNormalBoot = StorageConfig.isNormalBoot;
        if (!StorageConfig.isNormalBoot) {
            gotoSplashActivity();
            return false;
        }
        return true;
    }

    private void gotoSplashActivity() {
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

//    protected void getCommonTitle()
//    {
//        titleTv = (TextView) findViewById(R.id.tv_layout_head_title);
//        rightTv = (TextView) findViewById(R.id.tv_title_bar_right);
//    }
//
//    public TextView getTitleTv()
//    {
//        return titleTv;
//    }
//
//    public TextView getRightTv()
//    {
//        return rightTv;
//    }


    /**
     * <p>使用findViewById方法，找到所有的Views</p>
     * <hr/>
     * <p>主要是为了方便优化维护</p>
     */
    public abstract  void findViews();

    /**
     * <p>初始化Views</p>
     * <hr/>
     * <p>主要是为了方便优化维护</p>
     */
    public abstract  void initViews();

    /**
     * <p>设置Views的监听事件</p>
     * <hr/>
     * <p>主要是为了方便优化维护</p>
     */
    public abstract  void initLisenter();

    /**
     * <p>设置setContentView的Res文件ID号</p>
     * @return
     */
    public abstract int getContentView();


    public void onHeadBack(View v)
    {
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        GduCPApplication.getInstance().removeActivity(this);
    }
}
