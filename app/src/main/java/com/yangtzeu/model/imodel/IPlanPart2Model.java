package com.yangtzeu.model.imodel;

import android.app.Activity;

import com.yangtzeu.ui.view.PlanPartView2;

public interface IPlanPart2Model {
    void showPlanList(Activity activity, PlanPartView2 view);

    void unbindPlan(Activity activity, PlanPartView2 view);

    void bindPlan(Activity activity, PlanPartView2 view);
}
