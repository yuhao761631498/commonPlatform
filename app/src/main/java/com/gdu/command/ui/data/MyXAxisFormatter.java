package com.gdu.command.ui.data;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.List;

public class MyXAxisFormatter extends ValueFormatter {

    private List<String> months;

    public MyXAxisFormatter(List<String> months) {
        this.months = months;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        try {
            return months.get((int) value);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
