package com.yangtzeu.model.imodel;

import android.app.Activity;

import com.yangtzeu.ui.view.LoginView;

public interface ILoginModel {

    void loadLoginEvent(Activity activity, LoginView view);

    void loadHistory(Activity activity, LoginView view);
}
