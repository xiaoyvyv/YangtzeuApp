package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.MainModel;
import com.yangtzeu.ui.view.MainView;

public class MainPresenter {
    private MainModel model;
    private MainView view;
    private Activity activity;

    public MainPresenter(Activity activity, MainView view) {
        this.view = view;
        this.activity = activity;
        model = new MainModel();
    }


    public void setBottomViewWithFragment() {
        model.setBottomViewWithFragment(activity, view);
    }

    public void initEvents() {
        model.initEvents(activity, view);
    }

}
