package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.HomePart2Model;
import com.yangtzeu.ui.view.HomePartView2;

public class HomePart2Presenter {
    private HomePart2Model model;
    private HomePartView2 view;
    private Activity activity;

    public HomePart2Presenter(Activity activity, HomePartView2 view) {
        this.view = view;
        this.activity = activity;
        model = new HomePart2Model();
    }

    public void fitView() {
        model.fitView(activity, view);
    }
}
