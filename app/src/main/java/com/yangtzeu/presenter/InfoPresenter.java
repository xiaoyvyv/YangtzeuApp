package com.yangtzeu.presenter;

import android.app.Activity;

import com.blankj.utilcode.util.ToastUtils;
import com.yangtzeu.R;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
import com.yangtzeu.model.InfoModel;
import com.yangtzeu.ui.view.InfoView;

public class InfoPresenter {
    private Activity activity;
    private InfoView view;
    private InfoModel model;

    public InfoPresenter(Activity activity, InfoView view) {
        this.activity = activity;
        this.view = view;
        model = new InfoModel();
    }


    public void showChangeHeaderView( ) {
        model.showChangeHeaderView(activity,view);
    }

    public void loadHeader() {
        model.loadHeader(activity, view);
    }

    public void loadMineInfo() {
        OkHttp.do_Get(view.getIndexUrl(), new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                model.loadMineInfo(activity, view);
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showShort(R.string.try_again);
            }
        });
    }
}
