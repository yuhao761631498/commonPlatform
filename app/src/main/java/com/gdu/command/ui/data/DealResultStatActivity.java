package com.gdu.command.ui.data;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gdu.baselibrary.base.BaseActivity;
import com.gdu.command.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;

public class DealResultStatActivity extends BaseActivity implements View.OnClickListener {


    private ImageView iv_back;
    private PieChart pie_chart_deal_result;
    private TextView tv_verbal_warning;
    private TextView tv_administrative_penalty;
    private TextView tv_administrative_attachment;
    private TextView tv_other_penalty;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_deal_result_stat;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true).statusBarColor(
                R.color.color_224CD0).init();

        initView();
        initListener();

        setAlarmTypePieChartOne();
        setAlarmTypeDistributeDataOne();
    }

    private void initListener() {
        iv_back.setOnClickListener(this);
    }

    private void initView() {
        iv_back = ((ImageView) findViewById(R.id.iv_back));
        pie_chart_deal_result = ((PieChart) findViewById(R.id.pie_chart_deal_result));
        tv_verbal_warning = ((TextView) findViewById(R.id.tv_verbal_warning));  //口头警告
        tv_administrative_penalty = ((TextView) findViewById(R.id.tv_administrative_penalty));  //行政处罚
        tv_administrative_attachment = ((TextView) findViewById(R.id.tv_administrative_attachment));  //行政拘留
        tv_other_penalty = ((TextView) findViewById(R.id.tv_other_penalty));  //其它处罚
    }


    /**
     * 处理结果统计
     */
    private void setAlarmTypePieChartOne() {
        pie_chart_deal_result.setUsePercentValues(true);
        pie_chart_deal_result.getDescription().setEnabled(false);
        pie_chart_deal_result.setDragDecelerationFrictionCoef(0.95f);
        pie_chart_deal_result.setDrawHoleEnabled(true);
        pie_chart_deal_result.setHoleColor(Color.WHITE);
        pie_chart_deal_result.setTransparentCircleColor(Color.WHITE);
        pie_chart_deal_result.setHoleRadius(65f);
        pie_chart_deal_result.setDrawCenterText(true);
        pie_chart_deal_result.setRotationAngle(0);
        pie_chart_deal_result.setRotationEnabled(true);
        pie_chart_deal_result.setHighlightPerTapEnabled(true);
    }


    /**
     * 告警事件类型分布饼状图 一 数据填充
     */
    private void setAlarmTypeDistributeDataOne() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(20, "行政处罚"));
        entries.add(new PieEntry(40, "其它处罚"));
        entries.add(new PieEntry(60, "行政拘留"));
        entries.add(new PieEntry(80, "口头警告"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<>();

        final int[] MY_COLORS_ALARM = {
                Color.rgb(20, 158, 225),
                Color.rgb(62, 214, 155),
                Color.rgb(217, 219, 90),
                Color.rgb(255, 157, 129),
        };
        for (int c : MY_COLORS_ALARM) {
            colors.add(c);
        }
        dataSet.setColors(colors);
        dataSet.setValueLinePart1OffsetPercentage(50.f);
        dataSet.setValueLinePart1Length(0.3f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueLineColor(R.color.color_EDEDED);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new DefaultValueFormatter(0));
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);

        pie_chart_deal_result.setData(data);
        pie_chart_deal_result.setDrawEntryLabels(false);// 设置pieChart是否只显示饼图上百分比不显示文字
        pie_chart_deal_result.setDrawHoleEnabled(true);//是否显示PieChart内部圆环(true:下面属性才有意义)
        pie_chart_deal_result.setCenterText("总数\n" + 200);

        Legend mLegend1 = pie_chart_deal_result.getLegend(); //设置比例图
        mLegend1.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        mLegend1.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        mLegend1.setForm(Legend.LegendForm.CIRCLE); //设置比例图的形状，默认是方形

        mLegend1.setXEntrySpace(5f);
        mLegend1.setYEntrySpace(5f);

        pie_chart_deal_result.invalidate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

        }
    }
}