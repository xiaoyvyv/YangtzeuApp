package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.KGModel;
import com.yangtzeu.ui.view.KGView;

public class KGPresident {
    private Activity activity;
    private KGView view;
    private KGModel mode;

    public KGPresident(Activity activity, KGView view) {
        this.activity = activity;
        this.view = view;
        mode = new KGModel();
    }

    public void playMusic() {
        mode.playMusic(activity, view);
    }

    public void downloadMusic() {
        mode.downloadMusic(activity, view);
    }
}
