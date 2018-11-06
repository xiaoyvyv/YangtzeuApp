package com.yangtzeu.model.imodel;

import android.app.Activity;

import com.yangtzeu.ui.view.KGView;

public interface IKGMode {
    void playMusic(Activity activity, KGView view);
    void downloadMusic(Activity activity, KGView view);

    void loadMusic(Activity activity, KGView view, int which);
}
