package com.yangtzeu.presenter;

import android.app.Activity;

import com.blankj.utilcode.util.ToastUtils;
import com.yangtzeu.R;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.PlanPart1Model;
import com.yangtzeu.model.PlanPart2Model;
import com.yangtzeu.ui.view.PlanPartView1;
import com.yangtzeu.ui.view.PlanPartView2;

public class PlanPart2Presenter {
    private PlanPart2Model model;
    private PlanPartView2 view;
    private Activity activity;

    public PlanPart2Presenter(Activity activity, PlanPartView2 view) {
        this.view = view;
        this.activity = activity;
        model = new PlanPart2Model();
    }

    public void showPlanList() {
        OkHttp.do_Get(view.getMajorModeUrlIndex(), new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                model.showPlanList(activity, view);
            }

            @Override
            public void onFailure(String error) {
                view.getRefresh().finishRefresh();
                ToastUtils.showShort(R.string.try_again);
            }
        });
    }
}
