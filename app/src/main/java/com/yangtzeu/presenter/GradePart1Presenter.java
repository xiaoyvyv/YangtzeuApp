package com.yangtzeu.presenter;

import android.app.Activity;

import com.blankj.utilcode.util.ToastUtils;
import com.yangtzeu.R;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.GradePart1Model;
import com.yangtzeu.model.HomePart1Model;
import com.yangtzeu.ui.view.GradePartView1;
import com.yangtzeu.ui.view.HomePartView1;

public class GradePart1Presenter {
    private GradePart1Model model;
    private GradePartView1 view;
    private Activity activity;

    public GradePart1Presenter(Activity activity, GradePartView1 view) {
        this.view = view;
        this.activity = activity;
        model = new GradePart1Model();
    }

    public void loadGradeData() {
        OkHttp.do_Get(view.getIndexUrl(), new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                model.loadGradeData(activity, view);
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showShort(R.string.try_again);
            }
        });
    }

}
