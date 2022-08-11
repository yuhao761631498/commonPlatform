package com.gdu.command.ui.duty;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.gdu.command.ui.duty.base.activity.BaseActivity;
import com.haibin.calendarview.CalendarView;

/**
 * 测试界面
 * Created by huanghaibin on 2018/8/7.
 */

public class TestActivity extends BaseActivity implements View.OnClickListener {

    private CalendarView mCalendarView;
    public static void show(Context context) {
        context.startActivity(new Intent(context, TestActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return com.gdu.command.R.layout.activity_test;
    }

    @Override
    protected void initView() {
        setStatusBarDarkMode();
        findViewById(com.gdu.command.R.id.iv_next).setOnClickListener(this);
        findViewById(com.gdu.command.R.id.iv_pre).setOnClickListener(this);
        mCalendarView =  findViewById(com.gdu.command.R.id.calendar_view);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == com.gdu.command.R.id.iv_next) {
            mCalendarView.scrollToNext(false);
        } else if (id == com.gdu.command.R.id.iv_pre) {
            mCalendarView.scrollToPre(false);
        }
    }
}
