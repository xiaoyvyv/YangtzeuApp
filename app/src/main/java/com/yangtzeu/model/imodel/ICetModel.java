package com.yangtzeu.model.imodel;

import android.app.Activity;

import com.yangtzeu.ui.view.CetView;

public interface ICetModel {
    void initData(Activity activity, CetView view);
    void getCetHistoryGrade(Activity activity, CetView view);

    void addTextWatcher(Activity activity, CetView view);
    void getNowTermGrade(Activity activity, CetView view);
    void getGradeYzm(Activity activity, CetView view);
    void getCardYzm(Activity activity, CetView view);

    void getCardInfo(Activity activity, CetView view);
}
