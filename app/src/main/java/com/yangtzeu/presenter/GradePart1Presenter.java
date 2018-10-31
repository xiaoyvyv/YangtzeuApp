package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.GradePart1Model;
import com.yangtzeu.model.HomePart1Model;
import com.yangtzeu.ui.view.GradePartView1;
import com.yangtzeu.ui.view.HomePartView1;

public class GradePart1Presenter {
    private GradePart1Model model;
    private GradePartView1 view;
    private Activity activity;

    public GradePart1Presenter(Activity activity, GradePartView1 view) {
        this.view = view;
        this.activity = activity;
        model = new GradePart1Model();
    }

    public void loadGradeData() {
        model.loadGradeData(activity, view);
    }

    public void getGradeXls() {
        model.getGradeXls(activity, view);
    }
}
