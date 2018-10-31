package com.yangtzeu.ui.activity;

import android.os.Bundle;

import com.yangtzeu.R;
import com.yangtzeu.entity.GradeBean;
import com.yangtzeu.presenter.ChartPresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.view.ChartView;
import com.yangtzeu.ui.view.LockView;
import com.yangtzeu.utils.MyUtils;

import java.util.List;

import androidx.appcompat.widget.Toolbar;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;


public class LockActivity extends BaseActivity implements LockView {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);
    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);

    }

    @Override
    public void setEvents() {


    }

}
