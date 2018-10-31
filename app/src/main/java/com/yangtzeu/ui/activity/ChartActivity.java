package com.yangtzeu.ui.activity;

import android.os.Bundle;

import com.yangtzeu.R;
import com.yangtzeu.entity.GradeBean;
import com.yangtzeu.presenter.ChartPresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.view.ChartView;
import com.yangtzeu.utils.MyUtils;

import java.util.List;

import androidx.appcompat.widget.Toolbar;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;


public class ChartActivity extends BaseActivity implements ChartView {

    private List<GradeBean> datas;
    private Toolbar toolbar;
    private LineChartView mLineChartView;
    private ColumnChartView mColumnChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        datas = ((GradeBean) getIntent().getSerializableExtra("data")).getGradeBeans();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);
    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        mLineChartView = findViewById(R.id.mLineChartView);
        mColumnChartView = findViewById(R.id.mColumnChartView);

    }

    @Override
    public void setEvents() {
        ChartPresenter president = new ChartPresenter(this, this);
        president.setChart();
        president.setColumnChart();



    }

    @Override
    public LineChartView getLineChartView() {
        return mLineChartView;
    }

    @Override
    public ColumnChartView getColumnChartView() {
        return mColumnChartView;
    }

    @Override
    public List<GradeBean> getData() {
        return datas;
    }
}
