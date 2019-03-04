package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.VoaModel;
import com.yangtzeu.ui.view.VoaView;

public class VoaPresenter {
    private Activity activity;
    private VoaView view;
    private VoaModel mode;

    public VoaPresenter(Activity activity, VoaView view) {
        this.activity = activity;
        this.view = view;
        mode = new VoaModel();
    }

    public void getSlowVoaTitleList() {
        mode.getVoaTitleList(activity, view);
    }

    public void getNormalVoaTitleList() {
        mode.getNormalVoaTitleList(activity, view);
    }
}
