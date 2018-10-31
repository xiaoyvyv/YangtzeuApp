package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.SplashModel;
import com.yangtzeu.ui.view.SplashView;

public class SplashPresenter {
    private SplashModel model;
    private SplashView view;
    private Activity activity;

    public SplashPresenter(Activity activity, SplashView view) {
        this.view = view;
        this.activity = activity;
        model = new SplashModel();
    }

    public void loadAdImage() {
        model.loadAdImage(activity,view);
    }

    public void loadPermission(SplashModel.OnPermissionCallBack callBack) {
        model.loadPermission(activity,callBack);
    }

    public void loadUser() {
        model.loadUser(activity,view);
    }
}
