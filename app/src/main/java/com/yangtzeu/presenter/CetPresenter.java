package com.yangtzeu.presenter;

import android.app.Activity;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yangtzeu.R;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.CetModel;
import com.yangtzeu.model.ChartModel;
import com.yangtzeu.ui.view.CetView;
import com.yangtzeu.ui.view.ChartView;
import com.yangtzeu.url.Url;

public class CetPresenter {
    private Activity activity;
    private CetView view;
    private CetModel mode;

    public CetPresenter(Activity activity, CetView view) {
        this.activity = activity;
        this.view = view;
        mode = new CetModel();
    }

    public void initData() {
        mode.initData(activity, view);

    }
    public void getCetHistoryGrade() {
        OkHttp.do_Get(view.getIndexUrl(), new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                mode.getCetHistoryGrade(activity, view);
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showShort(R.string.try_again);
            }
        });
    }

    public void getNowTermGrade() {
        mode.getNowTermGrade(activity, view);
    }

    public void getGradeYzm() {
        mode.getGradeYzm(activity, view);
    }

    public void getCardYzm() {
        mode.getCardYzm(activity, view);
    }


    public void getCardInfo() {
        mode.getCardInfo(activity, view);
    }
}
