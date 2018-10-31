package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.GradePart2Model;
import com.yangtzeu.ui.view.GradePartView2;

public class GradePart2Presenter {
    private GradePart2Model model;
    private GradePartView2 view;
    private Activity activity;

    public GradePart2Presenter(Activity activity, GradePartView2 view) {
        this.view = view;
        this.activity = activity;
        model = new GradePart2Model();
    }

    public void loadPointData() {
        model.loadPointData(activity, view);
    }
}
