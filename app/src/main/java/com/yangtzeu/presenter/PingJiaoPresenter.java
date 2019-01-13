package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.PingJiaoModel;
import com.yangtzeu.ui.view.PingJiaoView;

public class PingJiaoPresenter {
    private PingJiaoModel model;
    private PingJiaoView view;
    private Activity activity;

    public PingJiaoPresenter(Activity activity, PingJiaoView view) {
        this.view = view;
        this.activity = activity;
        model = new PingJiaoModel();
    }

    public void loadData() {
        model.loadData(activity, view);
    }
}
