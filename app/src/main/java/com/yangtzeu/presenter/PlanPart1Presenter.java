package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.PlanPart1Model;
import com.yangtzeu.ui.view.PlanPartView1;

public class PlanPart1Presenter {
    private PlanPart1Model model;
    private PlanPartView1 view;
    private Activity activity;

    public PlanPart1Presenter(Activity activity, PlanPartView1 view) {
        this.view = view;
        this.activity = activity;
        model = new PlanPart1Model();
    }

    public void loadPlan() {
        model.loadPlan(activity, view);
    }
}
