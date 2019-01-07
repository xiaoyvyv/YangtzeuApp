package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.HomeModel;
import com.yangtzeu.model.MineModel;
import com.yangtzeu.ui.activity.CardCenterActivity;
import com.yangtzeu.ui.view.HomeView;
import com.yangtzeu.ui.view.MineView;
import com.yangtzeu.utils.MyUtils;

public class MinePresenter {
    private MineModel model;
    private MineView view;
    private Activity activity;

    public MinePresenter(Activity activity, MineView view) {
        this.view = view;
        this.activity = activity;
        model = new MineModel();
    }

    public void loadUserInfo() {
        model.loadUserInfo(activity, view);
    }

    public void showChangeHeaderView( ) {
        model.showChangeHeaderView(activity,view);
    }

    public void loadMessage() {
        model.loadMessage(activity,view);
    }

    public void setToolbarEvent() {
        model.setToolbarEvent(activity,view);
    }

    public void checkInternetView() {
        model.checkInternetView(activity,view);
    }

    public void showSchoolDialog() {
        model.showSchoolDialog(activity);
    }

    public void loadDayTrip() {
        model.loadDayTrip(activity, view);
    }
}
