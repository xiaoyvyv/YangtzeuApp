package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.ChartModel;
import com.yangtzeu.ui.view.ChartView;
public class ChartPresenter {
    private Activity activity;
    private ChartView view;
    private ChartModel mode;

    public ChartPresenter(Activity activity, ChartView view) {
        this.activity = activity;
        this.view = view;
        mode = new ChartModel();
    }

    public void setChart() {
        mode.setChart(activity, view);
    }

}
