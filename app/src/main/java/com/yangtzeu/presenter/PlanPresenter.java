package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.PlanModel;
import com.yangtzeu.ui.view.PlanView;

public class PlanPresenter {
    private Activity activity;
    private PlanView view;
    private PlanModel mode;

    public PlanPresenter(Activity activity, PlanView view) {
        this.activity = activity;
        this.view = view;
        mode = new PlanModel();
    }
    public void getPlanInfo() {
        mode.getPlanInfo(activity, view);
    }
}
