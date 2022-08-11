package com.gdu.command.ui.data;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.command.R;
import com.gdu.model.data.DataAlarmTotalBean;
import com.gdu.model.data.DataCaseTotalBean;
import com.gdu.model.data.DataHighTotal;
import com.gdu.util.TimeUtils;
import com.gdu.utils.MyVariables;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据监测展示页
 */
public class DataMonitorShowActivity extends BaseActivity<DataMonitorShowPresenter> implements IDataMonitorShowView {
    private ImageView ivLeftBack;
    private TextView mTvDataUpdate;
    private RecyclerView rcCaseRank;
    private TextView mTvCaseTotal;
    private TextView mTvAlarmTotal;
    private TextView mTvHighTotal;
    private TextView mTvLocationName;
    private TextView mTvWeather;
    private PieChart mPieChartCase;
    private PieChart mPieChartAlarm;
    private PieChart mPieChartAlarm2;
    private LineChart mLineChart;

    private String location;
    private String weather;

    private DataProgressAdapter progressAdapter;
    private List<DataCaseResFromBean.CaseInfoSourceCountVOSBean> caseSourceList = new ArrayList<>();
    private List<DataCaseRankBean.DataBean> caseRankList = new ArrayList<>();
    private List<DataAlarmByYearBean.DataBean> alarmYearList = new ArrayList<>();
    private AlarmResInfoBean.DataBean alarmResInfoBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_data_monitor_show;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true).statusBarColor(
                R.color.color_224CD0).init();

        location = getIntent().getStringExtra(MyVariables.CURRENT_LOCATION);
        weather = getIntent().getStringExtra(MyVariables.CURRENT_WEATHER);

        getPresenter().setView(this);
        getPresenter().requestData();
        initView();
        initListener();
        initAdapter();
        setCasePieChart();
        setAlarmTypePieChartOne();
        setAlarmTypePieChartTwo();
    }

    private void initView() {
        ivLeftBack = findViewById(R.id.iv_left_back);
        mTvDataUpdate = findViewById(R.id.tv_data_update);
        rcCaseRank = findViewById(R.id.rc_case_deal_rank);
        mTvCaseTotal = findViewById(R.id.case_total_textview);
        mTvAlarmTotal = findViewById(R.id.warning_count_textview);
        mTvHighTotal = findViewById(R.id.high_point_textview);
        mPieChartCase = findViewById(R.id.pie_chart_case);
        mPieChartAlarm = findViewById(R.id.pie_chart_alarm);
        mPieChartAlarm2 = findViewById(R.id.pie_chart_alarm_2);
        mLineChart = findViewById(R.id.line_chart_alarm_time);
        mTvLocationName = findViewById(R.id.tv_location_name);
        mTvWeather = findViewById(R.id.tv_weather);

        mTvDataUpdate.setText(String.format("数据更新至 %s", TimeUtils.getCurrentTimeNoSecond()));
        mTvLocationName.setText(!isEmpty(location) ? location : "");
        mTvWeather.setText(!isEmpty(weather) ? weather : "");
    }

    private void initListener() {
        ivLeftBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void initAdapter() {
        progressAdapter = new DataProgressAdapter(caseRankList);
        rcCaseRank.setLayoutManager(new LinearLayoutManager(this));
        rcCaseRank.setAdapter(progressAdapter);
    }

    /**
     * 设置案件来源分布饼图样式
     */
    private void setCasePieChart() {
        mPieChartCase.setUsePercentValues(true);
        mPieChartCase.getDescription().setEnabled(false);
        mPieChartCase.setDrawHoleEnabled(true);
        mPieChartCase.setHoleColor(Color.WHITE);
        mPieChartCase.setTransparentCircleColor(Color.WHITE);
        mPieChartCase.setDrawCenterText(true);
        mPieChartCase.setRotationAngle(0);
        mPieChartCase.setRotationEnabled(true);
        mPieChartCase.setHighlightPerTapEnabled(true);
//        mPieChartCase.animateY(1400, Easing.EaseInOutQuad);
    }

    /**
     * 设置告警事件类型分布饼状图一样式
     */
    private void setAlarmTypePieChartOne() {
        mPieChartAlarm.setUsePercentValues(true);
        mPieChartAlarm.getDescription().setEnabled(false);
        mPieChartAlarm.setDragDecelerationFrictionCoef(0.95f);
        mPieChartAlarm.setDrawHoleEnabled(true);
        mPieChartAlarm.setHoleColor(Color.WHITE);
        mPieChartAlarm.setTransparentCircleColor(Color.WHITE);
        mPieChartAlarm.setHoleRadius(65f);
        mPieChartAlarm.setDrawCenterText(true);
        mPieChartAlarm.setRotationAngle(0);
        mPieChartAlarm.setRotationEnabled(true);
        mPieChartAlarm.setHighlightPerTapEnabled(true);
//        mPieChartAlarm.animateY(1400, Easing.EaseInOutQuad);
    }

    /**
     * 设置告警事件类型分布饼状图二样式
     */
    private void setAlarmTypePieChartTwo() {
        mPieChartAlarm2.setUsePercentValues(true);
        mPieChartAlarm2.getDescription().setEnabled(false);
        mPieChartAlarm2.setDragDecelerationFrictionCoef(0.95f);
        mPieChartAlarm2.setDrawHoleEnabled(true);
        mPieChartAlarm2.setHoleColor(Color.WHITE);
        mPieChartAlarm2.setTransparentCircleColor(Color.WHITE);
        mPieChartAlarm2.setHoleRadius(40f);
        mPieChartAlarm2.setDrawCenterText(true);
        mPieChartAlarm2.setRotationAngle(0);
        mPieChartAlarm2.setRotationEnabled(true);
        mPieChartAlarm2.setHighlightPerTapEnabled(true);
//        mPieChartAlarm2.animateY(1400, Easing.EaseInOutQuad);
    }

    /**
     * 今年案件来源分布饼状图
     */
    private void setCaseInfoFromPieChart(List<DataCaseResFromBean.CaseInfoSourceCountVOSBean> list) {
        List<PieEntry> entries = new ArrayList<>();
        for (DataCaseResFromBean.CaseInfoSourceCountVOSBean bean : list) {
            entries.add(new PieEntry(bean.getCountNum(), bean.getCountName()));
        }
        PieDataSet dataSet = new PieDataSet(entries, "");//右上角，依次排列
        dataSet.setSliceSpace(4f);//设置饼状Item之间的间隙

        //设置饼状区域颜色
        ArrayList<Integer> colors = new ArrayList<>();
        final int[] MY_COLORS = {
                Color.rgb(24,143,255),
                Color.rgb(19,193,194),
                Color.rgb(46,194,90),
                Color.rgb(249,203,21),
                Color.rgb(239,72,100),
                Color.rgb(253,137,91)
        };
        for(int c: MY_COLORS) {
            colors.add(c);
        }
        dataSet.setColors(colors);
        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.5f);
        dataSet.setValueLinePart2Length(0.6f);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueLineColor(R.color.color_EDEDED);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        mPieChartCase.setData(data);
        mPieChartCase.setDrawEntryLabels(false);// 设置pieChart是否只显示饼图上百分比不显示文字
        mPieChartCase.setDrawHoleEnabled(false);//是否显示PieChart内部圆环(true:下面属性才有意义)

        //扇形区域点击事件
        mPieChartCase.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (entries.get(0).getValue() == e.getY()) {
                    MyPieChartMarkerView markerView = new MyPieChartMarkerView(
                            DataMonitorShowActivity.this,
                            R.layout.marker_view_pie_chart,
                            list.get(0).getCountName() + ": " + list.get(0).getCountNum(),
                            Color.rgb(24,143,255));
                    markerView.setChartView(mPieChartCase);
                    mPieChartCase.setMarker(markerView);
                } else if (entries.get(1).getValue() == e.getY()) {
                    MyPieChartMarkerView markerView = new MyPieChartMarkerView(
                            DataMonitorShowActivity.this,
                            R.layout.marker_view_pie_chart,
                            list.get(1).getCountName() + ": " + list.get(1).getCountNum(),
                            Color.rgb(19,193,194));
                    markerView.setChartView(mPieChartCase);
                    mPieChartCase.setMarker(markerView);
                } else if (entries.get(2).getValue() == e.getY()) {
                    MyPieChartMarkerView markerView = new MyPieChartMarkerView(
                            DataMonitorShowActivity.this,
                            R.layout.marker_view_pie_chart,
                            list.get(2).getCountName() + ": " + list.get(2).getCountNum(),
                            Color.rgb(46,194,90));
                    markerView.setChartView(mPieChartCase);
                    mPieChartCase.setMarker(markerView);
                } else if (entries.get(3).getValue() == e.getY()) {
                    MyPieChartMarkerView markerView = new MyPieChartMarkerView(
                            DataMonitorShowActivity.this,
                            R.layout.marker_view_pie_chart,
                            list.get(3).getCountName() + ": " + list.get(3).getCountNum(),
                            Color.rgb(249,203,21));
                    markerView.setChartView(mPieChartCase);
                    mPieChartCase.setMarker(markerView);
                } else if (entries.get(4).getValue() == e.getY()) {
                    MyPieChartMarkerView markerView = new MyPieChartMarkerView(
                            DataMonitorShowActivity.this,
                            R.layout.marker_view_pie_chart,
                            list.get(4).getCountName() + ": " + list.get(4).getCountNum(),
                            Color.rgb(239,72,100));
                    markerView.setChartView(mPieChartCase);
                    mPieChartCase.setMarker(markerView);
                } else if (entries.get(5).getValue() == e.getY()) {
                    MyPieChartMarkerView markerView = new MyPieChartMarkerView(
                            DataMonitorShowActivity.this,
                            R.layout.marker_view_pie_chart,
                            list.get(5).getCountName() + ": " + list.get(5).getCountNum(),
                            Color.rgb(253,137,91));
                    markerView.setChartView(mPieChartCase);
                    mPieChartCase.setMarker(markerView);
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        Legend mLegend = mPieChartCase.getLegend(); //设置比例图
        mLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        mLegend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        mLegend.setForm(Legend.LegendForm.CIRCLE); //设置比例图的形状，默认是方形
        mLegend.setXEntrySpace(5f);
        mLegend.setYEntrySpace(5f);
        mPieChartCase.invalidate();
    }

    /**
     * 告警事件类型分布饼状图 一 数据填充
     */
    private void setAlarmTypeDistributeDataOne(AlarmResInfoBean.DataBean bean) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(bean.getShipAppearedCount(), "船舶出现"));
        entries.add(new PieEntry(bean.getPersonnelIntrusionCount(), "人员出现"));
        entries.add(new PieEntry(bean.getIllegalFishingCount(), "钓鱼行为"));
        entries.add(new PieEntry(bean.getPersonnelAppearedCount(), "人员滞留"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<>();

        final int[] MY_COLORS_ALARM = {
                Color.rgb(67,107,255),
                Color.rgb(59,213,179),
                Color.rgb(46,194,90),
                Color.rgb(249,203,21)
        };
        for(int c: MY_COLORS_ALARM) {
            colors.add(c);
        }
        dataSet.setColors(colors);
        dataSet.setValueLinePart1OffsetPercentage(50.f);
        dataSet.setValueLinePart1Length(0.3f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueLineColor(R.color.color_EDEDED);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        mPieChartAlarm.setData(data);
        mPieChartAlarm.setDrawEntryLabels(false);// 设置pieChart是否只显示饼图上百分比不显示文字
        mPieChartAlarm.setDrawHoleEnabled(true);//是否显示PieChart内部圆环(true:下面属性才有意义)
        mPieChartAlarm.setCenterText("总数\n" + bean.getTotalCount());

        //扇形区域点击事件
        mPieChartAlarm.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (entries.get(0).getValue() == e.getY()) {
                    MyPieChartMarkerView markerView = new MyPieChartMarkerView(
                            DataMonitorShowActivity.this,
                            R.layout.marker_view_pie_chart,
                            "船舶出现: " + bean.getShipAppearedCount(),
                            Color.rgb(67,107,255));
                    markerView.setChartView(mPieChartAlarm);
                    mPieChartAlarm.setMarker(markerView);
                } else if (entries.get(1).getValue() == e.getY()) {
                    MyPieChartMarkerView markerView = new MyPieChartMarkerView(
                            DataMonitorShowActivity.this,
                            R.layout.marker_view_pie_chart,
                            "人员出现: " + bean.getPersonnelIntrusionCount(),
                            Color.rgb(59,213,179));
                    markerView.setChartView(mPieChartAlarm);
                    mPieChartAlarm.setMarker(markerView);
                } else if (entries.get(2).getValue() == e.getY()) {
                    MyPieChartMarkerView markerView = new MyPieChartMarkerView(
                            DataMonitorShowActivity.this,
                            R.layout.marker_view_pie_chart,
                            "钓鱼行为: " + bean.getIllegalFishingCount(),
                            Color.rgb(46,194,90));
                    markerView.setChartView(mPieChartAlarm);
                    mPieChartAlarm.setMarker(markerView);
                } else if (entries.get(3).getValue() == e.getY()) {
                    MyPieChartMarkerView markerView = new MyPieChartMarkerView(
                            DataMonitorShowActivity.this,
                            R.layout.marker_view_pie_chart,
                            "人员滞留: " + bean.getPersonnelAppearedCount(),
                            Color.rgb(249,203,21));
                    markerView.setChartView(mPieChartAlarm);
                    mPieChartAlarm.setMarker(markerView);
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        Legend mLegend1 = mPieChartAlarm.getLegend(); //设置比例图
        mLegend1.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        mLegend1.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        mLegend1.setForm(Legend.LegendForm.CIRCLE); //设置比例图的形状，默认是方形
        mLegend1.setXEntrySpace(5f);
        mLegend1.setYEntrySpace(5f);

        mPieChartAlarm.invalidate();
    }

    /**
     * 告警事件类型分布 二
     */
    private void setAlarmTypeDistributeDataTwo(AlarmResInfoBean.DataBean bean) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(bean.getRedLevelAlarmCount(), "红色预警"));
        entries.add(new PieEntry(bean.getOrangeLevelAlarmCount(), "橙色预警"));
        entries.add(new PieEntry(bean.getYellowLevelAlarmCount(), "黄色预警"));
        entries.add(new PieEntry(bean.getBlueLevelAlarmCount(), "蓝色预警"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(5f);

        //设置饼状区域颜色
        ArrayList<Integer> colors = new ArrayList<>();

        final int[] MY_COLORS_ALARM2 = {
                Color.rgb(239,72,100),
                Color.rgb(255,159,0),
                Color.rgb(249,203,21),
                Color.rgb(67,107,255)
        };
        for(int c: MY_COLORS_ALARM2) {
            colors.add(c);
        }
        dataSet.setColors(colors);
        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.4f);
        dataSet.setValueLinePart2Length(0.5f);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueLineColor(R.color.color_EDEDED);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        mPieChartAlarm2.setData(data);
        mPieChartAlarm2.setDrawEntryLabels(false);// 设置pieChart是否只显示饼图上百分比不显示文字
        mPieChartAlarm2.setDrawHoleEnabled(true);//是否显示PieChart内部圆环(true:下面属性才有意义)

        //扇形区域点击事件
        mPieChartAlarm2.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (entries.get(0).getValue() == e.getY()) {
                    MyPieChartMarkerView markerView = new MyPieChartMarkerView(
                            DataMonitorShowActivity.this,
                            R.layout.marker_view_pie_chart,
                            "红色预警: " + bean.getRedLevelAlarmCount(),
                            Color.rgb(239,72,100));
                    markerView.setChartView(mPieChartAlarm2);
                    mPieChartAlarm2.setMarker(markerView);
                } else if (entries.get(1).getValue() == e.getY()) {
                    MyPieChartMarkerView markerView = new MyPieChartMarkerView(
                            DataMonitorShowActivity.this,
                            R.layout.marker_view_pie_chart,
                            "橙色预警: " + bean.getOrangeLevelAlarmCount(),
                            Color.rgb(255,159,0));
                    markerView.setChartView(mPieChartAlarm2);
                    mPieChartAlarm2.setMarker(markerView);
                } else if (entries.get(2).getValue() == e.getY()) {
                    MyPieChartMarkerView markerView = new MyPieChartMarkerView(
                            DataMonitorShowActivity.this,
                            R.layout.marker_view_pie_chart,
                            "黄色预警: " + bean.getYellowLevelAlarmCount(),
                            Color.rgb(249,203,21));
                    markerView.setChartView(mPieChartAlarm2);
                    mPieChartAlarm2.setMarker(markerView);
                } else if (entries.get(3).getValue() == e.getY()) {
                    MyPieChartMarkerView markerView = new MyPieChartMarkerView(
                            DataMonitorShowActivity.this,
                            R.layout.marker_view_pie_chart,
                            "蓝色预警: " + bean.getBlueLevelAlarmCount(),
                            Color.rgb(67,107,255));
                    markerView.setChartView(mPieChartAlarm2);
                    mPieChartAlarm2.setMarker(markerView);
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });

        Legend mLegend2 = mPieChartAlarm2.getLegend(); //设置比例图
        mLegend2.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        mLegend2.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        mLegend2.setForm(Legend.LegendForm.CIRCLE); //设置比例图的形状，默认是方形
        mLegend2.setXEntrySpace(5f);
        mLegend2.setYEntrySpace(5f);
        mPieChartAlarm2.invalidate();
    }

    /**
     * 告警时间分布趋势折线图
     */
    @TargetApi(Build.VERSION_CODES.O)
    private void initLineChart(List<DataAlarmByYearBean.DataBean> mList) {
        mLineChart.setNoDataText("当前暂无任何数据");
        mLineChart.setDrawGridBackground(true);// 是否显示表格颜色
        mLineChart.setGridBackgroundColor(Color.parseColor("#FAFAFA"));// 表格的的颜色

        List<Entry> entries = new ArrayList<>();
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            entries.add(new Entry(i, mList.get(i).getCount()));
            strings.add(mList.get(i).getTime());
        }

        //填充数据
        LineDataSet dataSet = new LineDataSet(entries, ""); // add entries to dataset
        //设置颜色
        dataSet.setColor(Color.parseColor("#5B8FF9"));//线条颜色
        dataSet.setCircleColor(Color.parseColor("#5B8FF9"));//圆点颜色
        dataSet.setLineWidth(3f);//线条宽度
        //x轴配置
        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置x轴的显示位置
        xAxis.setGranularity(1f);
        xAxis.enableGridDashedLine(10f, 10f, 0f);//网格虚线
        xAxis.setValueFormatter(new MyXAxisFormatter(strings));
        //左y轴配置
        YAxis lyAxis = mLineChart.getAxisLeft();
        lyAxis.enableGridDashedLine(10f, 10f, 0f);//网格虚线
        //右y轴配置
        YAxis ryAxis = mLineChart.getAxisRight();
        ryAxis.setEnabled(false);//是否可用
        //markerView
//        int total = 0;
//        for (DataAlarmByYearBean.DataBean bean : mList) {
//            total += bean.getCount();
//        }
        //总数
        int totalNum = mList.stream().map(DataAlarmByYearBean.DataBean::getCount).reduce(Integer::sum).orElse(0);
        MyLineChartMarkerView markerView = new MyLineChartMarkerView(this,
                R.layout.marker_view_alarm_time_distriubte,
                totalNum);
        markerView.setChartView(mLineChart);
        mLineChart.setMarker(markerView);

        //标签配置
        Legend legend = mLineChart.getLegend();
        legend.setEnabled(false);//是否可用

        //3.chart设置数据
        LineData lineData = new LineData(dataSet);
        mLineChart.setData(lineData);
        Description description = new Description();
        description.setText("");
        mLineChart.setDescription(description);
        //设置圆点数据显示
        lineData.setDrawValues(false);
        mLineChart.invalidate(); // refresh
        mLineChart.animateY(1500);//动画效果，MPAndroidChart中还有很多动画效果可以挖掘
    }

    @Override
    public void getCaseTotalCount(DataCaseTotalBean bean) {
        boolean isNotEmpty = null != bean ;
        if (isNotEmpty) {
            if (bean.getCaseCount() > 0) {
                mTvCaseTotal.setText(String.format("%d", bean.getCaseCount()));
            } else {
                mTvCaseTotal.setText("--");
            }
        }
    }

    @Override
    public void getAlarmTotalCount(DataAlarmTotalBean bean) {
        boolean isNotEmpty = null != bean;
        if (isNotEmpty) {
            if (bean.getData() > 0) {
                mTvAlarmTotal.setText(String.format("%d", bean.getData()));
            } else {
                mTvAlarmTotal.setText("--");
            }
        }
    }

    @Override
    public void getHighTotalCount(DataHighTotal bean) {
        boolean isNotEmpty = null != bean && null != bean.getData();
        if (isNotEmpty) {
            if (bean.getData().getDeviceCount() > 0) {
                mTvHighTotal.setText(String.format("%d", bean.getData().getDeviceCount()));
            } else {
                mTvHighTotal.setText("--");
            }
        }
    }

    @Override
    public void getCaseFromByYear(List<DataCaseResFromBean.CaseInfoSourceCountVOSBean> bean) {
        boolean isNotEmpty = null != bean && bean.size() != 0 ;
        if (isNotEmpty) {
            caseSourceList.addAll(bean);
            setCaseInfoFromPieChart(caseSourceList);
        }
    }

    @Override
    public void getCaseDealRank(List<DataCaseRankBean.DataBean> bean) {
        boolean isNotEmpty = null != bean && bean.size() != 0;
        if (isNotEmpty) {
            caseRankList.addAll(bean);
            progressAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getAlarmResInfo(AlarmResInfoBean.DataBean bean) {
        boolean isNotEmpty = null != bean;
        if (isNotEmpty) {
            alarmResInfoBean = bean;
            setAlarmTypeDistributeDataOne(alarmResInfoBean);
            setAlarmTypeDistributeDataTwo(alarmResInfoBean);
        }
    }

    @Override
    public void getAlarmByYearDistribute(List<DataAlarmByYearBean.DataBean> bean) {
        boolean isNotEmpty = null != bean && bean.size() != 0;
        if (isNotEmpty) {
            alarmYearList.addAll(bean);
            initLineChart(alarmYearList);
        }
    }

}
