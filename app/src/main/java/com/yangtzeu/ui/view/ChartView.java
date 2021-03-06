package com.yangtzeu.ui.view;

import com.yangtzeu.entity.GradeBean;

import java.util.List;

import lecho.lib.hellocharts.view.LineChartView;

public interface ChartView {
    LineChartView getLineChartView();

    List<GradeBean> getData();
}
