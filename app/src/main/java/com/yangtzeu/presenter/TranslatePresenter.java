package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.TranslateModel;
import com.yangtzeu.ui.view.TranslateView;

public class TranslatePresenter {
    private Activity activity;
    private TranslateView view;
    private TranslateModel mode;

    public TranslatePresenter(Activity activity, TranslateView view) {
        this.activity = activity;
        this.view = view;
        mode = new TranslateModel();
    }


    public void translate() {
        mode.translate(activity, view);
    }
}
