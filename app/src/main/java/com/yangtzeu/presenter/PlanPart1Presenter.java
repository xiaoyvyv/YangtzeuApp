package com.yangtzeu.presenter;

import android.app.Activity;

import com.blankj.utilcode.util.ToastUtils;
import com.yangtzeu.R;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.GradePart1Model;
import com.yangtzeu.model.PlanPart1Model;
import com.yangtzeu.ui.view.GradePartView1;
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
