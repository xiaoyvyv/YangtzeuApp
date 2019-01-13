package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.NewsDetailsModel;
import com.yangtzeu.model.PlanModel;
import com.yangtzeu.ui.view.NewsDetailsView;
import com.yangtzeu.ui.view.PlanView;

public class NewsDetailsPresenter {
    private Activity activity;
    private NewsDetailsView view;
    private NewsDetailsModel mode;

    public NewsDetailsPresenter(Activity activity, NewsDetailsView view) {
        this.activity = activity;
        this.view = view;
        mode = new NewsDetailsModel();
    }

    public void loadData() {
        mode.loadData(activity, view);
    }
}
