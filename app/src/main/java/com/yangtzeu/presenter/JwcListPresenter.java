package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.JwcListModel;
import com.yangtzeu.ui.view.JwcListView;

public class JwcListPresenter {
    private Activity activity;
    private JwcListView view;
    private JwcListModel mode;

    public JwcListPresenter(Activity activity, JwcListView view) {
        this.activity = activity;
        this.view = view;
        mode = new JwcListModel();
    }

    public void loadData() {
        mode.loadData(activity, view);
    }
}
