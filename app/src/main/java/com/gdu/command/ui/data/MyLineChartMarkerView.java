package com.gdu.command.ui.data;

import android.content.Context;
import android.widget.TextView;

import com.gdu.command.R;
import com.gdu.util.TimeUtils;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MyLineChartMarkerView extends MarkerView {

    private TextView tv_data;
    private TextView tv_month;
    private TextView tv_percent;

    private int totalNum;

    public MyLineChartMarkerView(Context context, int layoutResource, int total) {
        super(context, layoutResource);
        tv_data = findViewById(R.id.tv_data);
        tv_month = findViewById(R.id.tv_month);
        tv_percent = findViewById(R.id.tv_percent);
        totalNum = total;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        String year = TimeUtils.getCurrentYear();
        String strMonth = String.format("%s年%s", year, e.getX() + 1);
        String strData = e.getY() + "";
        tv_month.setText(String.format("%s月", strMonth.substring(0, strMonth.indexOf("."))));
        tv_data.setText(strData);
        //计算占总数的百分比
        BigDecimal b1 = new BigDecimal(Double.toString(e.getY()));
        BigDecimal b2 = new BigDecimal(Double.toString(totalNum));
        BigDecimal b3 = BigDecimal.valueOf(b1.divide(b2, 2, BigDecimal.ROUND_HALF_DOWN).doubleValue() * 100);
        tv_percent.setText("占比:" + b3 + "%");
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }

}
