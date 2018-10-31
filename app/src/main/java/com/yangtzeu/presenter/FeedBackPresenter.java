package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.ChartModel;
import com.yangtzeu.model.FeedBackModel;
import com.yangtzeu.ui.view.ChartView;
import com.yangtzeu.ui.view.FeedBackView;

public class FeedBackPresenter {
    private Activity activity;
    private FeedBackView view;
    private FeedBackModel mode;

    public FeedBackPresenter(Activity activity, FeedBackView view) {
        this.activity = activity;
        this.view = view;
        mode = new FeedBackModel();
    }

    public void sendSuggestion() {
        mode.sendSuggestion(activity, view);
    }
}
