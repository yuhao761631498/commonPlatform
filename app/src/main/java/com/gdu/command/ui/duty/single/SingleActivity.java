package com.gdu.command.ui.duty.single;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.gdu.command.ui.duty.Article;
import com.gdu.command.ui.duty.ArticleAdapter;
import com.gdu.command.ui.duty.base.activity.BaseActivity;
import com.gdu.command.ui.duty.group.GroupItemDecoration;
import com.gdu.command.ui.duty.group.GroupRecyclerView;
import com.gdu.command.ui.duty.index.IndexActivity;
import com.gdu.command.ui.duty.meizu.MeiZuActivity;
import com.gdu.command.ui.duty.simple.SimpleActivity;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.gdu.command.R;

import java.util.HashMap;
import java.util.Map;

public class SingleActivity extends BaseActivity implements
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener,
        CalendarView.OnMonthChangeListener,
        CalendarView.OnCalendarInterceptListener,
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
        context.startActivity(new Intent(context, SingleActivity.class));
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_single;
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
                if (mYear == 0) {
                    mYear = mCalendarView.getCurYear();
                }
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
                //mCalendarView.clearSingleSelect();
                mCalendarView.scrollToCurrent();
                Log.e("onDateSelected",
                        "  --  " + mCalendarView.getSelectedCalendar().getScheme() +
                                "  --  " + mCalendarView.getSelectedCalendar().toString() +
                                "  --  " + mCalendarView.getSelectedCalendar().hasScheme());
            }
        });
        mCalendarLayout = findViewById(R.id.calendarLayout);
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mCalendarView.setOnMonthChangeListener(this);

        //???????????????????????????????????????????????????????????????
        mCalendarView.setOnCalendarInterceptListener(this);

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
            MeiZuActivity.show(this);
        } else if (id == R.id.ll_simple) {
            SimpleActivity.show(this);
        } else if (id == R.id.ll_colorful) {
            SingleActivity.show(this);
        } else if (id == R.id.ll_index) {
            IndexActivity.show(this);
        }
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//???????????????????????????????????????????????????
        calendar.setScheme(text);
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
        Log.e("onDateSelected", "  -- " + calendar.getYear() +
                "  --  " + calendar.getMonth() +
                "  -- " + calendar.getDay() +
                "  --  " + isClick);
        if (!calendar.isAvailable()) {
            mTextLunar.setText("");
            mTextYear.setText("");
            mTextMonthDay.setText("????????????");
        }
    }

    @Override
    public void onMonthChange(int year, int month) {
        Log.e("onMonthChange", " -- " + year + "  --  " + month);
    }

    /**
     * ????????????????????????????????????????????????????????????????????????
     * ??? calendar > 2018???1???1??? && calendar <= 2020???12???31???
     *
     * @param calendar calendar
     * @return ??????????????????????????????????????????MonthView???WeekView????????????API?????????
     */
    @Override
    public boolean onCalendarIntercept(Calendar calendar) {
        //Log.e("onCalendarIntercept", calendar.toString());
        int day = calendar.getDay();
        return day == 1 || day == 3 || day == 6 || day == 11 ||
                day == 12 || day == 15 || day == 20 || day == 26;
    }

    @Override
    public void onCalendarInterceptClick(Calendar calendar, boolean isClick) {
        Toast.makeText(this,
                calendar.toString() + (isClick ? "??????????????????" : "???????????????????????????"),
                Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
        Log.e("onYearChange", " ???????????? " + year);
    }


}
