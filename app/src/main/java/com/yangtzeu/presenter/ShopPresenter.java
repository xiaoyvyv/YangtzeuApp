package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.ShopModel;
import com.yangtzeu.ui.view.ShopView;


public class ShopPresenter {
    private Activity activity;
    private  ShopView view;
    private ShopModel mode;

    public ShopPresenter(Activity activity, ShopView view) {
        this.activity = activity;
        this.view = view;
        mode = new ShopModel();
    }

    public void loadData() {
        mode.loadData(activity, view);
    }
}
