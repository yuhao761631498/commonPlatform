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
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.gyf.immersionbar.ImmersionBar;
import java.util.ArrayList;
import java.util.List;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ForewarnStatActivity extends BaseActivity<ForewarnStatPresenter> implements IForeWarnView {

    private ImageView iv_back;
    private PieChart pie_chart_forewarn;
    private TextView tv_staff_retention;
    private TextView tv_illegal_fishing;
    private TextView tv_find_people_number;
    private TextView tv_find_ship_number;

    private PieChart pie_chart_deal_result;
    private TextView tv_verbal_warning;
    private TextView tv_administrative_penalty;
    private TextView tv_administrative_attachment;
    private TextView tv_other_penalty;

    private RecyclerView rc_case_deal_rank;
    private List<DeviceRankBean.DeviceItemBean> deiceRankList = new ArrayList<>();
    private DeviceRankAdapter progressAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forewarn_stat;
    }

    @Override
    protected void initialize() {
        ImmersionBar.with(this).fitsSystemWindows(true).autoDarkModeEnable(true).statusBarColor(
                R.color.color_224CD0).init();

        getPresenter().setView(this);
        getPresenter().requestData();

        initView();
        initListener();
        setCasePieChart();
        setAlarmTypePieChartOne();

        initAdapter();
    }

    private void initListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        iv_back = ((ImageView) findViewById(R.id.iv_back));

        //预警数据统计
        pie_chart_forewarn = ((PieChart) findViewById(R.id.pie_chart_forewarn));
        tv_staff_retention = ((TextView) findViewById(R.id.tv_staff_retention));
        tv_illegal_fishing = ((TextView) findViewById(R.id.tv_illegal_fishing));
        tv_find_people_number = ((TextView) findViewById(R.id.tv_find_people_number));
        tv_find_ship_number = ((TextView) findViewById(R.id.tv_find_ship_number));

        //处理结果统计
        pie_chart_deal_result = ((PieChart) findViewById(R.id.pie_chart_deal_result));
        tv_verbal_warning = ((TextView) findViewById(R.id.tv_verbal_warning));  //口头警告
        tv_administrative_penalty = ((TextView) findViewById(R.id.tv_administrative_penalty));  //行政处罚
        tv_administrative_attachment = ((TextView) findViewById(R.id.tv_administrative_attachment));  //行政拘留
        tv_other_penalty = ((TextView) findViewById(R.id.tv_other_penalty));  //其它处罚

        //告警设备统计
        rc_case_deal_rank = ((RecyclerView) findViewById(R.id.rc_case_deal_rank));
    }


    private void initAdapter() {
        progressAdapter = new DeviceRankAdapter(deiceRankList);
        rc_case_deal_rank.setLayoutManager(new LinearLayoutManager(this));
        rc_case_deal_rank.setAdapter(progressAdapter);
    }

    /**
     * 设置预警数据饼图样式
     */
    private void setCasePieChart() {
        pie_chart_forewarn.setUsePercentValues(true);
        pie_chart_forewarn.getDescription().setEnabled(false);
        pie_chart_forewarn.setDrawHoleEnabled(true);
        pie_chart_forewarn.setHoleColor(Color.WHITE);
        pie_chart_forewarn.setTransparentCircleColor(Color.WHITE);
        pie_chart_forewarn.setDrawCenterText(true);
        pie_chart_forewarn.setRotationAngle(0);
        pie_chart_forewarn.setRotationEnabled(true);
        pie_chart_forewarn.setHighlightPerTapEnabled(true);
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
     * 预警数据统计饼状图
     */
    private void setCaseInfoFromPieChart(AlarmResInfoBean.DataBean bean) {
        ArrayList<PieEntry> entries = new ArrayList<>();

//        int countTotal = bean.getShipAppearedCount() + bean.getPersonnelIntrusionCount()
//                + bean.getIllegalFishingCount() + bean.getPersonnelAppearedCount();
//
        entries.add(new PieEntry(bean.getPersonnelIntrusionCount(), "人员活动"));
        entries.add(new PieEntry(bean.getPersonnelAppearedCount(), "非法捕捞"));
        entries.add(new PieEntry(bean.getIllegalFishingCount(), "非法垂钓"));
        entries.add(new PieEntry(bean.getShipAppearedCount(), "船舶闯入"));

        PieDataSet dataSet = new PieDataSet(entries, "");//右上角，依次排列
        dataSet.setSliceSpace(4f);//设置饼状Item之间的间隙

        //设置饼状区域颜色
        ArrayList<Integer> colors = new ArrayList<>();
        final int[] MY_COLORS = {
                Color.rgb(20, 158, 255),
                Color.rgb(62, 214, 155),
                Color.rgb(217, 219, 90),
                Color.rgb(255, 157, 129),
        };
        for (int c : MY_COLORS) {
            colors.add(c);
        }
        dataSet.setColors(colors);
        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.6f);
        dataSet.setValueLinePart2Length(0.5f);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueLineColor(R.color.color_EDEDED);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);

        pie_chart_forewarn.setData(data);
        pie_chart_forewarn.setDrawEntryLabels(false);// 设置pieChart是否只显示饼图上百分比不显示文字
        pie_chart_forewarn.setDrawHoleEnabled(false);//是否显示PieChart内部圆环(true:下面属性才有意义)

        /**
         * This property is deprecated - Use `horizontalAlignment`, `verticalAlignment`, `orientation`, `drawInside`,
         * `direction`.
         */
        Legend mLegend = pie_chart_forewarn.getLegend(); //设置比例图
        mLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        mLegend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        mLegend.setForm(Legend.LegendForm.CIRCLE); //设置比例图的形状，默认是方形
        mLegend.setXEntrySpace(5f);
        mLegend.setYEntrySpace(5f);
        pie_chart_forewarn.invalidate();
    }


    /**
     * 告警处理结果设置数据
     */
    private void setAlarmTypeDistributeDataOne(List<DealResultBean.DealItemBean> beans) {
        int administrativePenalty = 0;
        int otherPenalty = 0;
        int administrativeAttachment = 0;
        int verbalWarn = 0;
        for (DealResultBean.DealItemBean dealItemBean : beans) {
            if ("行政处罚".equals(dealItemBean.getAlarmHandleTypeCode())) {
                administrativePenalty = dealItemBean.getCount();
            } else if ("行政拘留".equals(dealItemBean.getAlarmHandleTypeCode())) {
                administrativeAttachment = dealItemBean.getCount();
            } else if ("口头警告".equals(dealItemBean.getAlarmHandleTypeCode())) {
                verbalWarn = dealItemBean.getCount();
            } else if ("其他".equals(dealItemBean.getAlarmHandleTypeCode())) {
                otherPenalty = dealItemBean.getCount();
            }
        }

        tv_verbal_warning.setText(String.valueOf(verbalWarn));
        tv_administrative_penalty.setText(String.valueOf(administrativePenalty));
        tv_administrative_attachment.setText(String.valueOf(administrativeAttachment));
        tv_other_penalty.setText(String.valueOf(otherPenalty));

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(administrativePenalty, "行政处罚"));
        entries.add(new PieEntry(otherPenalty, "其它处罚"));
        entries.add(new PieEntry(administrativeAttachment, "行政拘留"));
        entries.add(new PieEntry(verbalWarn, "口头警告"));

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
        pie_chart_deal_result.setCenterText("总数\n" + String.valueOf(administrativePenalty + otherPenalty + administrativeAttachment + verbalWarn));

        Legend mLegend1 = pie_chart_deal_result.getLegend(); //设置比例图
        mLegend1.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        mLegend1.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        mLegend1.setForm(Legend.LegendForm.CIRCLE); //设置比例图的形状，默认是方形

        mLegend1.setXEntrySpace(5f);
        mLegend1.setYEntrySpace(5f);

        pie_chart_deal_result.invalidate();
    }

    /**
     * 设置预警统计数据
     *
     * @param bean
     */
    private void setAlarmTypeNumber(AlarmResInfoBean.DataBean bean) {
        tv_staff_retention.setText(String.valueOf(bean.getPersonnelIntrusionCount()));
        tv_illegal_fishing.setText(String.valueOf(bean.getIllegalFishingCount()));
        tv_find_people_number.setText(String.valueOf(bean.getPersonnelAppearedCount()));
        tv_find_ship_number.setText(String.valueOf(bean.getShipAppearedCount()));
    }


    @Override
    public void getAlarmResInfo(AlarmResInfoBean.DataBean bean) {  //预警数据统计
        boolean isNotEmpty = null != bean;
        if (isNotEmpty) {
            setCaseInfoFromPieChart(bean);
            setAlarmTypeNumber(bean);
        }
    }

    @Override
    public void getDealResultInfo(List<DealResultBean.DealItemBean> beans) {
        setAlarmTypeDistributeDataOne(beans);
    }

    @Override
    public void getDeviceRankInfo(List<DeviceRankBean.DeviceItemBean> beans) {
        boolean isNotEmpty = null != beans && beans.size() != 0;
        if (isNotEmpty) {
            deiceRankList.clear();
            deiceRankList.addAll(beans);
            progressAdapter.notifyDataSetChanged();
        }
    }
}