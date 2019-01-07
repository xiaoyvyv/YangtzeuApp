package com.yangtzeu.presenter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;

import com.yangtzeu.model.ChartModel;
import com.yangtzeu.model.LockModel;
import com.yangtzeu.ui.view.ChartView;
import com.yangtzeu.ui.view.LockView;

public class LockPresenter {
    private Activity activity;
    private LockView view;
    private LockModel mode;

    public LockPresenter(Activity activity, LockView view) {
        this.activity = activity;
        this.view = view;
        mode = new LockModel();
    }

    public void setLockTime() {
        mode.setLockTime(activity, view);
    }
    public void checkIsLockFinish(final Context context, BroadcastReceiver broadcastReceiver, String action) {
        mode.checkIsLockFinish(context, broadcastReceiver, action);
    }
}
