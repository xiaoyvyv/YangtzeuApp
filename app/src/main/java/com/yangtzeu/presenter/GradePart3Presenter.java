package com.yangtzeu.presenter;

import android.app.Activity;

import com.blankj.utilcode.util.ToastUtils;
import com.yangtzeu.R;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.GradePart3Model;
import com.yangtzeu.ui.view.GradePartView3;

public class GradePart3Presenter {
    private GradePart3Model model;
    private GradePartView3 view;
    private Activity activity;

    public GradePart3Presenter(Activity activity, GradePartView3 view) {
        this.view = view;
        this.activity = activity;
        model = new GradePart3Model();
    }

    public void loadPointData() {
        OkHttp.do_Get(view.getIndexUrl(), new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                model.loadPointData(activity, view);
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showShort(R.string.try_again);
                view.getRefresh().finishRefresh();
            }
        });
    }
}
