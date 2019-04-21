package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.AnswerLayout1Model;
import com.yangtzeu.ui.view.AnswerLayout1View;

public class AnswerLayout1Presenter {
    private Activity activity;
    private AnswerLayout1View view;
    private AnswerLayout1Model mode;

    public AnswerLayout1Presenter(Activity activity, AnswerLayout1View view) {
        this.activity = activity;
        this.view = view;
        mode = new AnswerLayout1Model();
    }

    public void loadBanner() {
        mode.loadBanner(activity, view);
    }

    public void loadHotAnswer() {
        mode.loadHotAnswer(activity, view);
    }

}
