package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.HomeModel;
import com.yangtzeu.ui.view.HomeView;

public class HomePresenter {
    private HomeModel model;
    private HomeView view;
    private Activity activity;

    public HomePresenter(Activity activity, HomeView view) {
        this.view = view;
        this.activity = activity;
        model = new HomeModel();
    }

    public void setToolbarWhitDrawer() {
        model.setToolbarWithDrawer(activity, view);
    }

    public void fitViewPagerAndTabLayout() {
        model.fitViewPagerAndTabLayout(activity,view);

    }

    public void getHoliday() {
        model.getHoliday(activity,view);
    }

    public void getWeather() {
        model.getWeather(activity,view);
    }

    public void setStudyTime() {
        model.setStudyTime(activity,view);
    }
}
