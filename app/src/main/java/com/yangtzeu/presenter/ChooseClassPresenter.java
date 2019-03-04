package com.yangtzeu.presenter;

import android.app.Activity;

import com.blankj.utilcode.util.ToastUtils;
import com.yangtzeu.R;
import com.yangtzeu.http.OkHttp;
import com.yangtzeu.http.OnResultStringListener;
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
        OkHttp.do_Get(view.getIndexUrl(), new OnResultStringListener() {
            @Override
            public void onResponse(String response) {
                model.getChooseClassInfo(activity, view);
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showShort(R.string.load_error);
            }
        });

    }
}
