package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.LoveAddModel;
import com.yangtzeu.model.ShopAddModel;
import com.yangtzeu.ui.view.LoveAddView;
import com.yangtzeu.ui.view.ShopAddView;

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
