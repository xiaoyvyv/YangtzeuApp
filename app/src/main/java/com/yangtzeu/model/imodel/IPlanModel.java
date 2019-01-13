package com.yangtzeu.model.imodel;

import android.app.Activity;

import com.yangtzeu.ui.view.PlanView;

public interface IPlanModel {
    void isPlanExist(Activity activity, PlanView view);

    void showPlanList(Activity activity, PlanView view);
    void unbindPlan(Activity activity, PlanView view);
    void bindPlan(Activity activity, PlanView view);
    void loadPlan(Activity activity, PlanView view);
}
