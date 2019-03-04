package com.yangtzeu.model.imodel;

import android.app.Activity;

import com.yangtzeu.ui.view.InfoView;

public interface IChatInfoModel {
    void showChangeHeaderView(Activity activity, InfoView view);

    void loadHeader(Activity activity, InfoView view);

    void loadMineInfo(Activity activity, InfoView view);
}
