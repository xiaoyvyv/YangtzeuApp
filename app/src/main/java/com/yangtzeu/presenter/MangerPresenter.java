package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.ChatModel;
import com.yangtzeu.model.MangerModel;
import com.yangtzeu.ui.view.ChatView;
import com.yangtzeu.ui.view.MangerView;

public class MangerPresenter {
    private Activity activity;
    private MangerView view;
    private MangerModel mode;

    public MangerPresenter(Activity activity, MangerView view) {
        this.activity = activity;
        this.view = view;
        mode = new MangerModel();
    }

    public void loadFeedBack() {
        mode.loadFeedBack(activity, view);
    }
}
