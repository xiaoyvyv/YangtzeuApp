package com.yangtzeu.presenter;

import android.app.Activity;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yangtzeu.R;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.ChartModel;
import com.yangtzeu.model.TestModel;
import com.yangtzeu.ui.view.ChartView;
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
