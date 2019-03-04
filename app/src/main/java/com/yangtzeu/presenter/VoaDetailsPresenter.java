package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.VoaDetailsModel;
import com.yangtzeu.model.VoaModel;
import com.yangtzeu.ui.view.VoaDetailsView;
import com.yangtzeu.ui.view.VoaView;

public class VoaDetailsPresenter {
    private Activity activity;
    private VoaDetailsView view;
    private VoaDetailsModel mode;

    public VoaDetailsPresenter(Activity activity, VoaDetailsView view) {
        this.activity = activity;
        this.view = view;
        mode = new VoaDetailsModel();
    }

    public void loadData() {
        mode.loadData(activity, view);
    }
}
