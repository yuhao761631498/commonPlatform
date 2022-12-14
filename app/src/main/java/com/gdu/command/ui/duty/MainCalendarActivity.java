package com.gdu.command.ui.duty;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.gdu.command.ui.duty.base.activity.BaseActivity;
import com.gdu.command.ui.duty.colorful.ColorfulActivity;
import com.gdu.command.ui.duty.custom.CustomActivity;
import com.gdu.command.ui.duty.full.FullActivity;
import com.gdu.command.ui.duty.index.IndexActivity;
import com.gdu.command.ui.duty.meizu.MeiZuActivity;
import com.gdu.command.ui.duty.meizu.MeiZuMonthView;
import com.gdu.command.ui.duty.meizu.MeizuWeekView;
import com.gdu.command.ui.duty.mix.MixActivity;
import com.gdu.command.ui.duty.multi.MultiActivity;
import com.gdu.command.ui.duty.pager.ViewPagerActivity;
import com.gdu.command.ui.duty.progress.ProgressActivity;
import com.gdu.command.ui.duty.range.RangeActivity;
import com.gdu.command.ui.duty.simple.SimpleActivity;
import com.gdu.command.ui.duty.single.SingleActivity;
import com.gdu.command.ui.duty.solay.SolarActivity;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.haibin.calendarview.TrunkBranchAnnals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainCalendarActivity extends BaseActivity implements
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnCalendarLongClickListener,
        CalendarView.OnMonthChangeListener,
        CalendarView.OnYearChangeListener,
        CalendarView.OnWeekChangeListener,
        CalendarView.OnViewChangeListener,
        CalendarView.OnCalendarInterceptListener,
        CalendarView.OnYearViewChangeListener,
        DialogInterface.OnClickListener,
        View.OnClickListener {

    TextView mTextMonthDay;

    TextView mTextYear;

    TextView mTextLunar;

    TextView mTextCurrentDay;

    CalendarView mCalendarView;

    RelativeLayout mRelativeTool;
    private int mYear;
    CalendarLayout mCalendarLayout;

    private AlertDialog mMoreDialog;

    private AlertDialog mFuncDialog;

    @Override
    protected int getLayoutId() {
        return com.gdu.command.R.layout.activity_main_calendar;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        setStatusBarDarkMode();
        mTextMonthDay = findViewById(com.gdu.command.R.id.tv_month_day);
        mTextYear = findViewById(com.gdu.command.R.id.tv_year);
        mTextLunar = findViewById(com.gdu.command.R.id.tv_lunar);

        mRelativeTool = findViewById(com.gdu.command.R.id.rl_tool);
        mCalendarView = findViewById(com.gdu.command.R.id.calendarView);

        //mCalendarView.setRange(2018, 7, 1, 2019, 4, 28);
        mTextCurrentDay = findViewById(com.gdu.command.R.id.tv_current_day);
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
        findViewById(com.gdu.command.R.id.iv_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMoreDialog == null) {
                    mMoreDialog = new AlertDialog.Builder(MainCalendarActivity.this)
                            .setTitle(com.gdu.command.R.string.list_dialog_title)
                            .setItems(com.gdu.command.R.array.list_dialog_items, MainCalendarActivity.this)
                            .create();
                }
                mMoreDialog.show();
            }
        });

        final DialogInterface.OnClickListener listener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                mCalendarLayout.expand();
                                break;
                            case 1:
                                boolean result = mCalendarLayout.shrink();
                                Log.e("shrink", " --  " + result);
                                break;
                            case 2:
                                mCalendarView.scrollToPre(false);
                                break;
                            case 3:
                                mCalendarView.scrollToNext(false);
                                break;
                            case 4:
                                //mCalendarView.scrollToCurrent(true);
                                mCalendarView.scrollToCalendar(2018, 12, 30);
                                break;
                            case 5:
                                mCalendarView.setRange(2018, 7, 1, 2019, 4, 28);
//                                mCalendarView.setRange(mCalendarView.getCurYear(), mCalendarView.getCurMonth(), 6,
//                                        mCalendarView.getCurYear(), mCalendarView.getCurMonth(), 23);
                                break;
                            case 6:
                                Log.e("scheme", "  " + mCalendarView.getSelectedCalendar().getScheme() + "  --  "
                                        + mCalendarView.getSelectedCalendar().isCurrentDay());
                                List<Calendar> weekCalendars = mCalendarView.getCurrentWeekCalendars();
                                for (Calendar calendar : weekCalendars) {
                                    Log.e("onWeekChange", calendar.toString() + "  --  " + calendar.getScheme());
                                }
                                new AlertDialog.Builder(MainCalendarActivity.this)
                                        .setMessage(String.format("Calendar Range: \n%s ?????? %s",
                                                mCalendarView.getMinRangeCalendar(),
                                                mCalendarView.getMaxRangeCalendar()))
                                        .show();
                                break;
                        }
                    }
                };

        findViewById(com.gdu.command.R.id.iv_func).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFuncDialog == null) {
                    mFuncDialog = new AlertDialog.Builder(MainCalendarActivity.this)
                            .setTitle(com.gdu.command.R.string.func_dialog_title)
                            .setItems(com.gdu.command.R.array.func_dialog_items, listener)
                            .create();
                }
                mFuncDialog.show();
            }
        });

        mCalendarLayout = findViewById(com.gdu.command.R.id.calendarLayout);
        mCalendarView.setOnYearChangeListener(this);
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnMonthChangeListener(this);
        mCalendarView.setOnCalendarLongClickListener(this, true);
        mCalendarView.setOnWeekChangeListener(this);
        mCalendarView.setOnYearViewChangeListener(this);

        //???????????????????????????????????????????????????????????????
        mCalendarView.setOnCalendarInterceptListener(this);

        mCalendarView.setOnViewChangeListener(this);
        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        mTextMonthDay.setText(mCalendarView.getCurMonth() + "???" + mCalendarView.getCurDay() + "???");
        mTextLunar.setText("??????");
        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
    }

    @SuppressWarnings("unused")
    @Override
    protected void initData() {

        final int year = mCalendarView.getCurYear();
        final int month = mCalendarView.getCurMonth();

        Map<String, Calendar> map = new HashMap<>();
        for (int y = 1997; y < 2082; y++) {
            for (int m = 1; m <= 12; m++) {

                map.put(getSchemeCalendar(y, m, 1, 0xFF40db25, "???").toString(),
                        getSchemeCalendar(y, m, 1, 0xFF40db25, "???"));

                map.put(getSchemeCalendar(y, m, 2, 0xFFe69138, "???").toString(),
                        getSchemeCalendar(y, m, 2, 0xFFe69138, "???"));
                map.put(getSchemeCalendar(y, m, 3, 0xFFdf1356, "???").toString(),
                        getSchemeCalendar(y, m, 3, 0xFFdf1356, "???"));
                map.put(getSchemeCalendar(y, m, 4, 0xFFaacc44, "???").toString(),
                        getSchemeCalendar(y, m, 4, 0xFFaacc44, "???"));
                map.put(getSchemeCalendar(y, m, 5, 0xFFbc13f0, "???").toString(),
                        getSchemeCalendar(y, m, 5, 0xFFbc13f0, "???"));
                map.put(getSchemeCalendar(y, m, 6, 0xFF542261, "???").toString(),
                        getSchemeCalendar(y, m, 6, 0xFF542261, "???"));
                map.put(getSchemeCalendar(y, m, 7, 0xFF4a4bd2, "???").toString(),
                        getSchemeCalendar(y, m, 7, 0xFF4a4bd2, "???"));
                map.put(getSchemeCalendar(y, m, 8, 0xFFe69138, "???").toString(),
                        getSchemeCalendar(y, m, 8, 0xFFe69138, "???"));
                map.put(getSchemeCalendar(y, m, 9, 0xFF542261, "???").toString(),
                        getSchemeCalendar(y, m, 9, 0xFF542261, "???"));
                map.put(getSchemeCalendar(y, m, 10, 0xFF87af5a, "???").toString(),
                        getSchemeCalendar(y, m, 10, 0xFF87af5a, "???"));
                map.put(getSchemeCalendar(y, m, 11, 0xFF40db25, "???").toString(),
                        getSchemeCalendar(y, m, 11, 0xFF40db25, "???"));
                map.put(getSchemeCalendar(y, m, 12, 0xFFcda1af, "???").toString(),
                        getSchemeCalendar(y, m, 12, 0xFFcda1af, "???"));
                map.put(getSchemeCalendar(y, m, 13, 0xFF95af1a, "???").toString(),
                        getSchemeCalendar(y, m, 13, 0xFF95af1a, "???"));
                map.put(getSchemeCalendar(y, m, 14, 0xFF33aadd, "???").toString(),
                        getSchemeCalendar(y, m, 14, 0xFF33aadd, "???"));
                map.put(getSchemeCalendar(y, m, 15, 0xFF1aff1a, "???").toString(),
                        getSchemeCalendar(y, m, 15, 0xFF1aff1a, "???"));
                map.put(getSchemeCalendar(y, m, 16, 0xFF22acaf, "???").toString(),
                        getSchemeCalendar(y, m, 16, 0xFF22acaf, "???"));
                map.put(getSchemeCalendar(y, m, 17, 0xFF99a6fa, "???").toString(),
                        getSchemeCalendar(y, m, 17, 0xFF99a6fa, "???"));
                map.put(getSchemeCalendar(y, m, 18, 0xFFe69138, "???").toString(),
                        getSchemeCalendar(y, m, 18, 0xFFe69138, "???"));
                map.put(getSchemeCalendar(y, m, 19, 0xFF40db25, "???").toString(),
                        getSchemeCalendar(y, m, 19, 0xFF40db25, "???"));
                map.put(getSchemeCalendar(y, m, 20, 0xFFe69138, "???").toString(),
                        getSchemeCalendar(y, m, 20, 0xFFe69138, "???"));
                map.put(getSchemeCalendar(y, m, 21, 0xFF40db25, "???").toString(),
                        getSchemeCalendar(y, m, 21, 0xFF40db25, "???"));
                map.put(getSchemeCalendar(y, m, 22, 0xFF99a6fa, "???").toString(),
                        getSchemeCalendar(y, m, 22, 0xFF99a6fa, "???"));
                map.put(getSchemeCalendar(y, m, 23, 0xFF33aadd, "???").toString(),
                        getSchemeCalendar(y, m, 23, 0xFF33aadd, "???"));
                map.put(getSchemeCalendar(y, m, 24, 0xFF40db25, "???").toString(),
                        getSchemeCalendar(y, m, 24, 0xFF40db25, "???"));
                map.put(getSchemeCalendar(y, m, 25, 0xFF1aff1a, "???").toString(),
                        getSchemeCalendar(y, m, 25, 0xFF1aff1a, "???"));
                map.put(getSchemeCalendar(y, m, 26, 0xFF40db25, "???").toString(),
                        getSchemeCalendar(y, m, 26, 0xFF40db25, "???"));
                map.put(getSchemeCalendar(y, m, 27, 0xFF95af1a, "???").toString(),
                        getSchemeCalendar(y, m, 27, 0xFF95af1a, "???"));
                map.put(getSchemeCalendar(y, m, 28, 0xFF40db25, "???").toString(),
                        getSchemeCalendar(y, m, 28, 0xFF40db25, "???"));
            }
        }

        //28560 ???????????????????????????UI??????????????????????????????API??????
        mCalendarView.setSchemeDate(map);

        //???????????????????????????
        //mCalendarView.setSchemeDate(schemes);

        findViewById(com.gdu.command.R.id.ll_flyme).setOnClickListener(this);
        findViewById(com.gdu.command.R.id.ll_simple).setOnClickListener(this);
        findViewById(com.gdu.command.R.id.ll_range).setOnClickListener(this);
        findViewById(com.gdu.command.R.id.ll_mix).setOnClickListener(this);
        findViewById(com.gdu.command.R.id.ll_colorful).setOnClickListener(this);
        findViewById(com.gdu.command.R.id.ll_index).setOnClickListener(this);
        findViewById(com.gdu.command.R.id.ll_tab).setOnClickListener(this);
        findViewById(com.gdu.command.R.id.ll_single).setOnClickListener(this);
        findViewById(com.gdu.command.R.id.ll_multi).setOnClickListener(this);
        findViewById(com.gdu.command.R.id.ll_solar_system).setOnClickListener(this);
        findViewById(com.gdu.command.R.id.ll_progress).setOnClickListener(this);
        findViewById(com.gdu.command.R.id.ll_custom).setOnClickListener(this);
        findViewById(com.gdu.command.R.id.ll_full).setOnClickListener(this);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case 0:
                mCalendarView.setWeekStarWithSun();
                break;
            case 1:
                mCalendarView.setWeekStarWithMon();
                break;
            case 2:
                mCalendarView.setWeekStarWithSat();
                break;
            case 3:
                if (mCalendarView.isSingleSelectMode()) {
                    mCalendarView.setSelectDefaultMode();
                } else {
                    mCalendarView.setSelectSingleMode();
                }
                break;
            case 4:
                mCalendarView.setWeekView(MeizuWeekView.class);
                mCalendarView.setMonthView(MeiZuMonthView.class);
                mCalendarView.setWeekBar(EnglishWeekBar.class);
                break;
            case 5:
                mCalendarView.setAllMode();
                break;
            case 6:
                mCalendarView.setOnlyCurrentMode();
                break;
            case 7:
                mCalendarView.setFixMode();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == com.gdu.command.R.id.ll_flyme) {
            MeiZuActivity.show(this);
            //CalendarActivity.show(this);
        } else if (id == com.gdu.command.R.id.ll_custom) {
            CustomActivity.show(this);
        } else if (id == com.gdu.command.R.id.ll_mix) {
            MixActivity.show(this);
        } else if (id == com.gdu.command.R.id.ll_full) {
            FullActivity.show(this);
        } else if (id == com.gdu.command.R.id.ll_range) {
            RangeActivity.show(this);
        } else if (id == com.gdu.command.R.id.ll_simple) {
            SimpleActivity.show(this);
        } else if (id == com.gdu.command.R.id.ll_colorful) {
            ColorfulActivity.show(this);
        } else if (id == com.gdu.command.R.id.ll_index) {
            IndexActivity.show(this);
        } else if (id == com.gdu.command.R.id.ll_tab) {
            ViewPagerActivity.show(this);
        } else if (id == com.gdu.command.R.id.ll_single) {
            SingleActivity.show(this);
        } else if (id == com.gdu.command.R.id.ll_multi) {
            MultiActivity.show(this);
        } else if (id == com.gdu.command.R.id.ll_solar_system) {
            SolarActivity.show(this);
        } else if (id == com.gdu.command.R.id.ll_progress) {
            ProgressActivity.show(this);
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
        Toast.makeText(this, String.format("%s : OutOfRange", calendar), Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        //Log.e("onDateSelected", "  -- " + calendar.getYear() + "  --  " + calendar.getMonth() + "  -- " + calendar.getDay());
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "???" + calendar.getDay() + "???");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
        if (isClick) {
            Toast.makeText(this, getCalendarText(calendar), Toast.LENGTH_SHORT).show();
        }
//        Log.e("lunar "," --  " + calendar.getLunarCalendar().toString() + "\n" +
//        "  --  " + calendar.getLunarCalendar().getYear());
        Log.e("onDateSelected", "  -- " + calendar.getYear() +
                "  --  " + calendar.getMonth() +
                "  -- " + calendar.getDay() +
                "  --  " + isClick + "  --   " + calendar.getScheme());
        Log.e("onDateSelected", "  " + mCalendarView.getSelectedCalendar().getScheme() +
                "  --  " + mCalendarView.getSelectedCalendar().isCurrentDay());
        Log.e("???????????? ??? ", " -- " + TrunkBranchAnnals.getTrunkBranchYear(calendar.getLunarCalendar().getYear()));
    }

    @Override
    public void onCalendarLongClickOutOfRange(Calendar calendar) {
        Toast.makeText(this, String.format("%s : LongClickOutOfRange", calendar), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCalendarLongClick(Calendar calendar) {
        Toast.makeText(this, "?????????????????????\n" + getCalendarText(calendar), Toast.LENGTH_SHORT).show();
    }

    private static String getCalendarText(Calendar calendar) {
        return String.format("??????%s \n ??????%s \n ???????????????%s \n ???????????????%s \n ?????????%s \n ???????????????%s",
                calendar.getMonth() + "???" + calendar.getDay() + "???",
                calendar.getLunarCalendar().getMonth() + "???" + calendar.getLunarCalendar().getDay() + "???",
                TextUtils.isEmpty(calendar.getGregorianFestival()) ? "???" : calendar.getGregorianFestival(),
                TextUtils.isEmpty(calendar.getTraditionFestival()) ? "???" : calendar.getTraditionFestival(),
                TextUtils.isEmpty(calendar.getSolarTerm()) ? "???" : calendar.getSolarTerm(),
                calendar.getLeapMonth() == 0 ? "???" : String.format("???%s???", calendar.getLeapMonth()));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onMonthChange(int year, int month) {
        Log.e("onMonthChange", "  -- " + year + "  --  " + month);
        Calendar calendar = mCalendarView.getSelectedCalendar();
        mTextLunar.setVisibility(View.VISIBLE);
        mTextYear.setVisibility(View.VISIBLE);
        mTextMonthDay.setText(calendar.getMonth() + "???" + calendar.getDay() + "???");
        mTextYear.setText(String.valueOf(calendar.getYear()));
        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();
    }

    @Override
    public void onViewChange(boolean isMonthView) {
        Log.e("onViewChange", "  ---  " + (isMonthView ? "?????????" : "?????????"));
    }


    @Override
    public void onWeekChange(List<Calendar> weekCalendars) {
        for (Calendar calendar : weekCalendars) {
            Log.e("onWeekChange", calendar.toString());
        }
    }

    @Override
    public void onYearViewChange(boolean isClose) {
        Log.e("onYearViewChange", "????????? -- " + (isClose ? "??????" : "??????"));
    }

    /**
     * ????????????????????????????????????????????????????????????????????????
     *
     * @param calendar calendar
     * @return ??????????????????????????????????????????MonthView???WeekView????????????API?????????
     */
    @Override
    public boolean onCalendarIntercept(Calendar calendar) {
        Log.e("onCalendarIntercept", calendar.toString());
        int day = calendar.getDay();
        return day == 1 || day == 3 || day == 6 || day == 11 || day == 12 || day == 15 || day == 20 || day == 26;
    }

    @Override
    public void onCalendarInterceptClick(Calendar calendar, boolean isClick) {
        Toast.makeText(this, calendar.toString() + "??????????????????", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
        Log.e("onYearChange", " ???????????? " + year);
    }

}


