package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.LoginModel;
import com.yangtzeu.ui.view.LoginView;

public class LoginPresenter {
    private LoginModel model;
    private LoginView view;
    private Activity activity;

    public LoginPresenter(Activity activity, LoginView view) {
        this.view = view;
        this.activity = activity;
        model = new LoginModel();
    }

    public void loadLoginEvent() {
        model.loadLoginEvent(activity, view);
    }

    public void loadHistory() {
        model.loadHistory(activity, view);
    }

}
