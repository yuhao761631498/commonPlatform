package com.gdu.command.ui.duty.solay;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.gdu.command.ui.duty.Article;
import com.gdu.command.ui.duty.ArticleAdapter;
import com.gdu.command.ui.duty.base.activity.BaseActivity;
import com.gdu.command.ui.duty.colorful.ColorfulActivity;
import com.gdu.command.ui.duty.group.GroupItemDecoration;
import com.gdu.command.ui.duty.group.GroupRecyclerView;
import com.gdu.command.ui.duty.index.IndexActivity;
import com.gdu.command.ui.duty.simple.SimpleActivity;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.gdu.command.R;

import java.util.HashMap;
import java.util.Map;

public class SolarActivity extends BaseActivity implements
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener,
        View.OnClickListener {

    TextView mTextMonthDay;

    TextView mTextYear;

    TextView mTextLunar;

    TextView mTextCurrentDay;

    CalendarView mCalendarView;

    RelativeLayout mRelativeTool;
    private int mYear;
    CalendarLayout mCalendarLayout;
    GroupRecyclerView mRecyclerView;

    public static void show(Context context) {
        context.startActivity(new Intent(context, SolarActivity.class));
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_solay;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.solar_background));
        }
        mTextMonthDay = findViewById(R.id.tv_month_day);
        mTextYear = findViewById(R.id.tv_year);
        mTextLunar = findViewById(R.id.tv_lunar);
        mRelativeTool = findViewById(R.id.rl_tool);
        mCalendarView = findViewById(R.id.calendarView);
        mTextCurrentDay = findViewById(R.id.tv_current_day);
        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mCalendarLayout.isExpand()) {
                    mCalendarLayout.expand();
                    return;
                }
                mCalendarView.showYearSelectLayout(mYear);
                mTextLunar.setVisibility(View.GONE);
                mTextYear.setVisibility(View.GONE);
                mTextMonthDay.setText(String.valueOf(mYear));
            }
        });
        findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendarView.scrollToCurrent();
            }
        });
        mCalendarLayout = findViewById(R.id.calendarLayout);
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "???" + mCalendarView.getCurDay() + "???");
        mTextLunar.setText("??????");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
    }

    @Override
    protected void initData() {
        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();

        Map<String, Calendar> map = new HashMap<>();
        map.put(getSchemeCalendar(year, month, 3, "???").toString(),
                getSchemeCalendar(year, month, 3, "???"));
        map.put(getSchemeCalendar(year, month, 6, "???").toString(),
                getSchemeCalendar(year, month, 6, "???"));
        map.put(getSchemeCalendar(year, month, 9, "???").toString(),
                getSchemeCalendar(year, month, 9, "???"));
        map.put(getSchemeCalendar(year, month, 13, "???").toString(),
                getSchemeCalendar(year, month, 13, "???"));
        map.put(getSchemeCalendar(year, month, 14, "???").toString(),
                getSchemeCalendar(year, month, 14, "???"));
        map.put(getSchemeCalendar(year, month, 15, "???").toString(),
                getSchemeCalendar(year, month, 15, "???"));
        map.put(getSchemeCalendar(year, month, 18, "???").toString(),
                getSchemeCalendar(year, month, 18, "???"));
        map.put(getSchemeCalendar(year, month, 25, "???").toString(),
                getSchemeCalendar(year, month, 25, "???"));
        map.put(getSchemeCalendar(year, month, 27, "???").toString(),
                getSchemeCalendar(year, month, 27, "???"));
        //?????????????????????????????????????????????????????????????????????
        mCalendarView.setSchemeDate(map);


        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new GroupItemDecoration<String, Article>());
        mRecyclerView.setAdapter(new ArticleAdapter(this));
        mRecyclerView.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ll_flyme) {
            SolarActivity.show(this);
        } else if (id == R.id.ll_simple) {
            SimpleActivity.show(this);
        } else if (id == R.id.ll_colorful) {
            ColorfulActivity.show(this);
        } else if (id == R.id.ll_index) {
            IndexActivity.show(this);
        }
    }

    private Calendar getSchemeCalendar(int year, int month, int day, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(Color.WHITE);
        calendar.setScheme(text);
        calendar.addScheme(0xFFa8b015, "rightTop");
        calendar.addScheme(0xFF423cb0, "leftTop");
        calendar.addScheme(0xFF643c8c, "bottom");

        return calendar;
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "???" + calendar.getDay() + "???");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }


}
