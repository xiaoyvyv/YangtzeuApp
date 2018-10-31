package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.GradeModel;
import com.yangtzeu.model.HomeModel;
import com.yangtzeu.ui.view.GradeView;
import com.yangtzeu.ui.view.HomeView;

public class GradePresenter {
    private GradeModel model;
    private GradeView view;
    private Activity activity;

    public GradePresenter(Activity activity, GradeView view) {
        this.view = view;
        this.activity = activity;
        model = new GradeModel();
    }

    public void setViewPager() {
        model.setViewPager(activity, view);
    }
}
