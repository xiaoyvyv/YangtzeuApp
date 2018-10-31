package com.yangtzeu.model.imodel;

import android.app.Activity;

import com.yangtzeu.entity.MessageBean;
import com.yangtzeu.ui.view.MineView;

import java.util.List;

public interface IMineModel {

    void loadUserInfo(Activity activity, MineView view);

    void showChangeHeaderView(Activity activity, MineView mineView);

    void loadMessage(Activity activity, MineView view);


    void setMessageRead(List<String> ids);

    void setToolbarEvent(Activity activity, MineView view);

    void checkInternetView(Activity activity, MineView view);

    void showSchoolDialog(Activity activity);
}
