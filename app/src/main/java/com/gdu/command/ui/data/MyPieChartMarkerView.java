package com.gdu.command.ui.data;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.gdu.command.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

public class MyPieChartMarkerView extends MarkerView {

    private TextView tv_show_content;
    private String content;

    public MyPieChartMarkerView(Context context, int layoutResource, String showValue, int color) {
        super(context, layoutResource);
        tv_show_content = findViewById(R.id.tv_show_content);
        tv_show_content.setTextColor(color);
        content = showValue;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tv_show_content.setText(content);
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
