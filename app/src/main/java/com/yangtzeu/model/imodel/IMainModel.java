package com.yangtzeu.model.imodel;

import android.app.Activity;

import com.yangtzeu.ui.view.MainView;

public interface IMainModel {
    void setBottomViewWithFragment(Activity activity, MainView view);

    void initEvents(Activity activity, MainView view);


}
