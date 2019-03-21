package com.yangtzeu.model.imodel;

import android.app.Activity;

import com.yangtzeu.ui.view.ChangePassView;

public interface IChangePassModel {

    void changePassEvent(Activity activity, ChangePassView view);

    void changePasswordStep1(Activity activity, ChangePassView view, String passwordOld, String passwordNew);

    void changePasswordStep2(Activity activity, ChangePassView view, String passwordOld, String passwordNew, String value);
}
