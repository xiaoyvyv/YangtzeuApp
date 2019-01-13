package com.yangtzeu.model.imodel;

import android.app.Activity;

import com.yangtzeu.model.SplashModel;
import com.yangtzeu.ui.view.HomeView;
import com.yangtzeu.ui.view.MainView;
import com.yangtzeu.ui.view.SplashView;

public interface IMainModel {
    void setBottomViewWithFragment(Activity activity, MainView view);

    void initEvents(Activity activity, MainView view);


}
