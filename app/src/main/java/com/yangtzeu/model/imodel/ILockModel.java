package com.yangtzeu.model.imodel;

import android.app.Activity;

import com.yangtzeu.ui.view.LockView;

public interface ILockModel {
    void startLockPhone(Activity activity, LockView view);

    void setLockTime(Activity activity, LockView view);
}
