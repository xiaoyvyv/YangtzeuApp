package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.AnswerListModel;
import com.yangtzeu.ui.view.AnswerListView;

public class AnswerListPresenter {
    private Activity activity;
    private AnswerListView view;
    private AnswerListModel mode;

    public AnswerListPresenter(Activity activity, AnswerListView view) {
        this.activity = activity;
        this.view = view;
        mode = new AnswerListModel();
    }

    public void loadAnswer() {
        mode.loadAnswer(activity, view);
    }
}
