package com.gdu.command.ui.duty.pager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.gdu.command.ui.duty.base.activity.BaseActivity;
import com.gdu.command.ui.duty.base.fragment.FragmentAdapter;
import com.gdu.command.ui.duty.colorful.ColorfulActivity;
import com.gdu.command.ui.duty.index.IndexActivity;
import com.gdu.command.ui.duty.meizu.MeiZuActivity;
import com.gdu.command.ui.duty.simple.SimpleActivity;
import com.google.android.material.tabs.TabLayout;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.gdu.command.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewPagerActivity extends BaseActivity implements
        View.OnClickListener,
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener {

    TextView mTextMonthDay;

    TextView mTextYear;

    TextView mTextLunar;

    TextView mTextCurrentDay;

    CalendarView mCalendarView;

    RelativeLayout mRelativeTool;
    private int mYear;
    CalendarLayout mCalendarLayout;

    public static void show(Context context) {
        context.startActivity(new Intent(context, ViewPagerActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_view_pager;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        setStatusBarDarkMode();
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

        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        adapter.reset(new String[]{"??????", "??????", "??????"});
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(PagerFragment.newInstance());
        fragments.add(PagerFragment.newInstance());
        fragments.add(PagerFragment.newInstance());
        adapter.reset(fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void initData() {
        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();

        Map<String, Calendar> map = new HashMap<>();
        map.put(getSchemeCalendar(year, month, 3, 0xFF40db25, "???").toString(),
                getSchemeCalendar(year, month, 3, 0xFF40db25, "???"));
        map.put(getSchemeCalendar(year, month, 6, 0xFFe69138, "???").toString(),
                getSchemeCalendar(year, month, 6, 0xFFe69138, "???"));
        map.put(getSchemeCalendar(year, month, 9, 0xFFdf1356, "???").toString(),
                getSchemeCalendar(year, month, 9, 0xFFdf1356, "???"));
        map.put(getSchemeCalendar(year, month, 13, 0xFFedc56d, "???").toString(),
                getSchemeCalendar(year, month, 13, 0xFFedc56d, "???"));
        map.put(getSchemeCalendar(year, month, 14, 0xFFedc56d, "???").toString(),
                getSchemeCalendar(year, month, 14, 0xFFedc56d, "???"));
        map.put(getSchemeCalendar(year, month, 15, 0xFFaacc44, "???").toString(),
                getSchemeCalendar(year, month, 15, 0xFFaacc44, "???"));
        map.put(getSchemeCalendar(year, month, 18, 0xFFbc13f0, "???").toString(),
                getSchemeCalendar(year, month, 18, 0xFFbc13f0, "???"));
        map.put(getSchemeCalendar(year, month, 25, 0xFF13acf0, "???").toString(),
                getSchemeCalendar(year, month, 25, 0xFF13acf0, "???"));
        map.put(getSchemeCalendar(year, month, 27, 0xFF13acf0, "???").toString(),
                getSchemeCalendar(year, month, 27, 0xFF13acf0, "???"));
        //?????????????????????????????????????????????????????????????????????
        mCalendarView.setSchemeDate(map);


    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//???????????????????????????????????????????????????
        calendar.setScheme(text);
        calendar.addScheme(new Calendar.Scheme());
        calendar.addScheme(0xFF008800, "???");
        calendar.addScheme(0xFF008800, "???");
        return calendar;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ll_flyme) {
            MeiZuActivity.show(this);
        } else if (id == R.id.ll_simple) {
            SimpleActivity.show(this);
        } else if (id == R.id.ll_colorful) {
            ColorfulActivity.show(this);
        } else if (id == R.id.ll_index) {
            IndexActivity.show(this);
        }
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
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
}
