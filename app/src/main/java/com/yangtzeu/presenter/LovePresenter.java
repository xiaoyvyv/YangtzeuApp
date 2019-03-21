package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.LoveModel;
import com.yangtzeu.ui.view.LoveView;


public class LovePresenter {
    private Activity activity;
    private  LoveView view;
    private LoveModel mode;

    public LovePresenter(Activity activity, LoveView view) {
        this.activity = activity;
        this.view = view;
        mode = new LoveModel();
    }

    public void loadData() {
        mode.loadData(activity, view);
    }
}
