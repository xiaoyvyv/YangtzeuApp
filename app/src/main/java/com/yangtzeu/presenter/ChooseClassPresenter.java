package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.ChangePassModel;
import com.yangtzeu.model.ChooseClassModel;
import com.yangtzeu.ui.view.ChangePassView;
import com.yangtzeu.ui.view.ChooseClassView;

public class ChooseClassPresenter {
    private ChooseClassModel model;
    private ChooseClassView view;
    private Activity activity;

    public ChooseClassPresenter(Activity activity, ChooseClassView view) {
        this.view = view;
        this.activity = activity;
        model = new ChooseClassModel();
    }

    public void getChooseClassInfo() {
        model.getChooseClassInfo(activity, view);

    }
}
