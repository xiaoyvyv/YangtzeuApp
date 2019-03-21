package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.LoveAddModel;
import com.yangtzeu.ui.view.LoveAddView;

public class LoveAddPresenter {
    private Activity activity;
    private LoveAddView view;
    private LoveAddModel mode;

    public LoveAddPresenter(Activity activity, LoveAddView view) {
        this.activity = activity;
        this.view = view;
        mode = new LoveAddModel();
    }

    public void sendLove() {
        mode.sendLove(activity, view);
    }
}
