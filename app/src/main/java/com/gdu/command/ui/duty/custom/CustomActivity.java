package com.gdu.command.ui.duty.custom;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.command.R;
import com.gdu.command.ui.duty.Article;
import com.gdu.command.ui.duty.ArticleAdapter;
import com.gdu.command.ui.duty.DutyForDayBean;
import com.gdu.command.ui.duty.DutyForMeBean;
import com.gdu.command.ui.duty.DutyPresenter;
import com.gdu.command.ui.duty.IDutyView;
import com.gdu.command.ui.duty.group.GroupItemDecoration;
import com.gdu.command.ui.duty.group.GroupRecyclerView;
import com.gyf.immersionbar.ImmersionBar;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomActivity extends BaseActivity<DutyPresenter> implements
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener,
        View.OnClickListener, IDutyView {

    TextView mTextMonthDay;

    TextView mTextYear;

    TextView mTextLunar;

    TextView mTextCurrentDay;

    CalendarView mCalendarView;

    RelativeLayout mRelativeTool;
    private int mYear;
    CalendarLayout mCalendarLayout;
    GroupRecyclerView mRecyclerView;
    private View iv_back;
    private ArticleAdapter articleAdapter;
    public static final int REQUEST_CALL_PERMISSION = 10111; //拨号请求码
    private LinearLayout ll_show_month;

    public static void show(Context context) {
        context.startActivity(new Intent(context, CustomActivity.class));
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_custom;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true).statusBarColor(
                R.color.color_224CD0).init();

        initView();
        initData();


        long currentTime = System.currentTimeMillis();

        @SuppressLint("SimpleDateFormat") String currTime =
                new SimpleDateFormat("yyyy-MM-dd").format(currentTime);
        getPresenter().setView(this);
        getPresenter().getDutyForDayInfo(currTime);

        @SuppressLint("SimpleDateFormat") String currTimeMonth =
                new SimpleDateFormat("yyyy-MM").format(currentTime);
        getPresenter().getDutyForMe(currTimeMonth);
    }

    private void initView() {
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        ll_show_month = findViewById(R.id.ll_show_month);
        mTextMonthDay = findViewById(R.id.tv_month_day);
        mTextYear = findViewById(R.id.tv_year);
        mTextLunar = findViewById(R.id.tv_lunar);
        mRelativeTool = findViewById(R.id.rl_tool);
        mCalendarView = findViewById(R.id.calendarView);
        mTextCurrentDay = findViewById(R.id.tv_current_day);
        ll_show_month.setOnClickListener(this);
        mTextMonthDay.setOnClickListener(this);
//        mTextMonthDay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!mCalendarLayout.isExpand()) {
//                    mCalendarLayout.expand();
//                    return;
//                }
//                mCalendarView.showYearSelectLayout(mYear);
//                mTextLunar.setVisibility(View.GONE);
//                mTextYear.setVisibility(View.GONE);
//                mTextMonthDay.setText(String.valueOf(mYear));
//            }
//        });
//        findViewById(R.id.fl_current).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mCalendarView.scrollToCurrent();
        //mCalendarView.addSchemeDate(getSchemeCalendar(2019, 6, 1, 0xFF40db25, "假"));
//                int year = 2019;
//                int month = 6;
//                Map<String, Calendar> map = new HashMap<>();
//                map.put(getSchemeCalendar(year, month, 3, 0xFF40db25, "假").toString(),
//                        getSchemeCalendar(year, month, 3, 0xFF40db25, "假"));
//                map.put(getSchemeCalendar(year, month, 6, 0xFFe69138, "事").toString(),
//                        getSchemeCalendar(year, month, 6, 0xFFe69138, "事"));
//                map.put(getSchemeCalendar(year, month, 9, 0xFFdf1356, "议").toString(),
//                        getSchemeCalendar(year, month, 9, 0xFFdf1356, "议"));
//                map.put(getSchemeCalendar(year, month, 13, 0xFFedc56d, "记").toString(),
//                        getSchemeCalendar(year, month, 13, 0xFFedc56d, "记"));
//                mCalendarView.addSchemeDate(map);
//            }
//        });
        mCalendarLayout = findViewById(R.id.calendarLayout);
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnYearChangeListener(this);
//        mTextYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
//        mTextMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
//        mTextLunar.setText("今日");
//        mTextCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));

        mTextMonthDay.setText(mCalendarView.getCurYear() + "年" + mCalendarView.getCurMonth() + "月");


    }


    protected void initData() {
//        int year = mCalendarView.getCurYear();
//        int month = mCalendarView.getCurMonth();
//
//        Map<String, Calendar> map = new HashMap<>();
//        map.put(getSchemeCalendar(year, month, 3, 0xFF55FF00, " ").toString(),
//                getSchemeCalendar(year, month, 3, 0xFF55FF00, " "));
//        map.put(getSchemeCalendar(year, month, 6, 0xFFe69138, " ").toString(),
//                getSchemeCalendar(year, month, 6, 0xFFe69138, " "));
//        map.put(getSchemeCalendar(year, month, 9, 0xFFdf1356, " ").toString(),
//                getSchemeCalendar(year, month, 9, 0xFFdf1356, " "));
//        map.put(getSchemeCalendar(year, month, 13, 0xFFedc56d, " ").toString(),
//                getSchemeCalendar(year, month, 13, 0xFFedc56d, " "));
//        map.put(getSchemeCalendar(year, month, 14, 0xFFedc56d, " ").toString(),
//                getSchemeCalendar(year, month, 14, 0xFFedc56d, " "));
//        map.put(getSchemeCalendar(year, month, 15, 0xFFaacc44, " ").toString(),
//                getSchemeCalendar(year, month, 15, 0xFFaacc44, " "));
//        map.put(getSchemeCalendar(year, month, 18, 0xFFbc13f0, " ").toString(),
//                getSchemeCalendar(year, month, 18, 0xFFbc13f0, " "));
//        map.put(getSchemeCalendar(year, month, 25, 0xFF13acf0, " ").toString(),
//                getSchemeCalendar(year, month, 25, 0xFF13acf0, " "));
//        map.put(getSchemeCalendar(year, month, 27, 0xFF13acf0, " ").toString(),
//                getSchemeCalendar(year, month, 27, 0xFF13acf0, " "));
//        //此方法在巨大的数据量上不影响遍历性能，推荐使用
//        mCalendarView.setSchemeDate(map);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new GroupItemDecoration<String, Article>());
        articleAdapter = new ArticleAdapter(this);
        mRecyclerView.setAdapter(articleAdapter);
        mRecyclerView.notifyDataSetChanged();
        articleAdapter.setOnItemClickListener(new ArticleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String phoneNumber) {
                if (!TextUtils.isEmpty(phoneNumber)) {
                    call(phoneNumber);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CALL_PERMISSION: //拨打电话
                if (permissions.length != 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {//失败
                    Toast.makeText(this, "请允许拨号权限后再试", Toast.LENGTH_SHORT).show();
                } else {//成功
                    call("tel:" + "10086");
                }
                break;
        }
    }

    /**
     * 拨打电话（直接拨打）
     *
     * @param telPhone 电话
     */
    public void call(String telPhone) {
        if (checkReadPermission(Manifest.permission.CALL_PHONE, REQUEST_CALL_PERMISSION)) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telPhone));
            startActivity(intent);
        }
    }


    public boolean checkReadPermission(String string_permission, int request_code) {
        boolean flag = false;
        if (ContextCompat.checkSelfPermission(this, string_permission) == PackageManager.PERMISSION_GRANTED) {//已有权限
            flag = true;
        } else {//申请权限
            ActivityCompat.requestPermissions(this, new String[]{string_permission}, request_code);
        }
        return flag;
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
//        if (id == R.id.ll_flyme) {
//            CustomActivity.show(this);
//        } else if (id == R.id.ll_simple) {
//            SimpleActivity.show(this);
//        } else if (id == R.id.ll_colorful) {
//            ColorfulActivity.show(this);
//        } else if (id == R.id.ll_index) {
//            IndexActivity.show(this);
//        }

        if (id == R.id.tv_month_day || id == R.id.ll_show_month) {
            Log.e("yuhao", "onClick: ");
            if (!mCalendarLayout.isExpand()) {
                mCalendarLayout.expand();
                return;
            }
            ll_show_month.setVisibility(View.GONE);
            mCalendarView.showYearSelectLayout(mYear);
            mTextLunar.setVisibility(View.GONE);
            mTextYear.setVisibility(View.GONE);
            mTextMonthDay.setText(String.valueOf(mYear));
        } else if (id == R.id.iv_back) {
            finish();
        }
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        calendar.addScheme(new Calendar.Scheme());
        calendar.addScheme(0xFF008800, "假");
        calendar.addScheme(0xFF008800, "节");
        return calendar;
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        mTextLunar.setVisibility(View.VISIBLE);
//        mTextYear.setVisibility(View.VISIBLE);
//        mTextMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
//        mTextYear.setText(String.valueOf(calendar.getYear()));

        mTextMonthDay.setText(calendar.getYear() + "年" + calendar.getMonth() + "月");
//        mTextLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();

        ll_show_month.setVisibility(View.VISIBLE);

        if (isClick) {
            String date = calendar.getYear()
                    + "-" + (calendar.getMonth() >= 10 ? calendar.getMonth() : ("0" + calendar.getMonth()))
                    + "-" + (calendar.getDay() >= 10 ? calendar.getDay() : ("0" + calendar.getDay()));

            Log.e("yuhao", "onCalendarSelect_Click: " + date);
            getPresenter().getDutyForDayInfo(date);
        } else {
            String dateMonth = calendar.getYear()
                    + "-" + (calendar.getMonth() >= 10 ? calendar.getMonth() : ("0" + calendar.getMonth()));
            Log.e("yuhao", "onCalendarSelect_Swap: " + dateMonth);

            getPresenter().getDutyForMe(dateMonth);

            String dateDay = calendar.getYear()
                    + "-" + (calendar.getMonth() >= 10 ? calendar.getMonth() : ("0" + calendar.getMonth()))
                    + "-" + (calendar.getDay() >= 10 ? calendar.getDay() : ("0" + calendar.getDay()));

            Log.e("yuhao", "onCalendarSelect_Swap: " + dateDay);
            getPresenter().getDutyForDayInfo(dateDay);
        }


        Log.e("onDateSelected", "  -- " + calendar.getYear() +
                "  --  " + calendar.getMonth() +
                "  -- " + calendar.getDay() +
                "  --  " + isClick + "  --   " + calendar.getScheme());
    }

    @Override
    public void onYearChange(int year) {
        mTextMonthDay.setText(String.valueOf(year));
    }


    @Override
    public void getDutyForDay(List<DutyForDayBean.DutyMemberBean> beans) {
        if (beans != null && beans.size() > 0) {
            articleAdapter.setDutyData(beans);
        } else {
            articleAdapter.setDutyData(null);
        }
        articleAdapter.notifyDataSetChanged();
        mRecyclerView.notifyDataSetChanged();
    }

    @Override
    public void getDutyForMe(List<List<DutyForMeBean.DayDutyInfoBean>> beans) {
        if (beans != null && beans.size() > 0) {
            Map<String, Calendar> mapCalendar = new HashMap<>();
            for (List<DutyForMeBean.DayDutyInfoBean> dayDutyInfoBeans : beans) {
                if (dayDutyInfoBeans != null) {
                    for (DutyForMeBean.DayDutyInfoBean dayDutyInfoBean : dayDutyInfoBeans) {
                        String duty = dayDutyInfoBean.getDuty();
                        if (!TextUtils.isEmpty(duty)) {
                            String[] split = duty.split("-");
                            if (split.length > 2) {
                                int year = Integer.parseInt(split[0]);
                                int month;
                                if (split[1].startsWith("0")) {
                                    month = Integer.parseInt(split[1].substring(1));
                                } else {
                                    month = Integer.parseInt(split[1]);
                                }

                                int day;
                                if (split[2].startsWith("0")) {
                                    day = Integer.parseInt(split[2].substring(1));
                                } else {
                                    day = Integer.parseInt(split[2]);
                                }
                                Calendar schemeCalendar = getSchemeCalendar(year, month, day, 0xFF55FF00, "班");
                                String key = schemeCalendar.toString();
                                if (!mapCalendar.containsKey(key)) {
                                    mapCalendar.put(key, schemeCalendar);
                                }
                            }
                        }
                    }
                }
            }
            mCalendarView.setSchemeDate(mapCalendar);
        } else {
            mCalendarView.setSchemeDate(null);
        }
    }
}
