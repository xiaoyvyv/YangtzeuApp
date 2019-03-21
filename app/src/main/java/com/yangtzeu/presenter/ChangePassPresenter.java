package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.ChangePassModel;
import com.yangtzeu.ui.view.ChangePassView;

public class ChangePassPresenter {
    private ChangePassModel model;
    private ChangePassView view;
    private Activity activity;

    public ChangePassPresenter(Activity activity, ChangePassView view) {
        this.view = view;
        this.activity = activity;
        model = new ChangePassModel();
    }

    public void changePassEvent() {
        model.changePassEvent(activity, view);
    }
}
