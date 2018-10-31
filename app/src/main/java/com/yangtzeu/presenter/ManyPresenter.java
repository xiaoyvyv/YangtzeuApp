package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.ManyModel;
import com.yangtzeu.ui.view.ManyView;

public class ManyPresenter {
    private ManyModel model;
    private ManyView view;
    private Activity activity;

    public ManyPresenter(Activity activity, ManyView view) {
        this.view = view;
        this.activity = activity;
        model = new ManyModel();
    }


    public void loadMarqueeView() {
        model.loadMarqueeView(activity, view);
    }

    public void loadBanner() {
        model.loadBanner(activity, view);
    }

    public void fitAdapter() {
        model.fitAdapter(activity, view);
    }
}
