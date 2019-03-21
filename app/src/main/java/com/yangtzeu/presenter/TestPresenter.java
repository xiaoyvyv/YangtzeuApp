package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.TestModel;
import com.yangtzeu.ui.view.TestView;

public class TestPresenter {
    private Activity activity;
    private TestView view;
    private TestModel mode;

    public TestPresenter(Activity activity, TestView view) {
        this.activity = activity;
        this.view = view;
        mode = new TestModel();
    }

    public void getTestInfo() {
        mode.getTestInfo(activity, view);

    }
}
