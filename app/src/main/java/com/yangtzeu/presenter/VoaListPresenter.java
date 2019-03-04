package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.BoardModel;
import com.yangtzeu.model.VoaListModel;
import com.yangtzeu.ui.view.BoardView;
import com.yangtzeu.ui.view.VoaListView;

public class VoaListPresenter {
    private Activity activity;
    private VoaListView view;
    private VoaListModel mode;

    public VoaListPresenter(Activity activity, VoaListView view) {
        this.activity = activity;
        this.view = view;
        mode = new VoaListModel();
    }

    public void loadData() {
        mode.loadData(activity, view);
    }
}
