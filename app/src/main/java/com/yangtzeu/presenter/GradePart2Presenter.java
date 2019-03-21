package com.yangtzeu.presenter;

import android.app.Activity;

import com.blankj.utilcode.util.ToastUtils;
import com.yangtzeu.R;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.GradePart2Model;
import com.yangtzeu.ui.view.GradePartView2;

public class GradePart2Presenter {
    private GradePart2Model model;
    private GradePartView2 view;
    private Activity activity;

    public GradePart2Presenter(Activity activity, GradePartView2 view) {
        this.view = view;
        this.activity = activity;
        model = new GradePart2Model();
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
                view.getRefresh().finishRefresh();
            }
        });
    }

    public void getGradeXls() {
        model.getGradeXls(activity, view);
    }
}
