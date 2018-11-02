package com.yangtzeu.model.imodel;

import android.app.Activity;

import com.yangtzeu.ui.view.ManyView;

public interface IManyModel {
    void loadMarqueeView(Activity activity, ManyView view);

    void loadBanner(Activity activity, ManyView view);

    void fitAdapter(Activity activity, ManyView view);

    void loadQQLikeEvents(Activity activity, ManyView view);

}
