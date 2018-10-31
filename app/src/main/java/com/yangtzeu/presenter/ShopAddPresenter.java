package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.ChartModel;
import com.yangtzeu.model.ShopAddModel;
import com.yangtzeu.ui.view.ChartView;
import com.yangtzeu.ui.view.ShopAddView;

public class ShopAddPresenter {
    private Activity activity;
    private ShopAddView view;
    private ShopAddModel mode;

    public ShopAddPresenter(Activity activity, ShopAddView view) {
        this.activity = activity;
        this.view = view;
        mode = new ShopAddModel();
    }

    public void sendGoods() {
        mode.sendGoods(activity, view);
    }
}
