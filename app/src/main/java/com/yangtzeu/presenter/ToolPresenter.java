package com.yangtzeu.presenter;

import android.app.Activity;

import com.yangtzeu.model.ToolModel;
import com.yangtzeu.ui.view.ToolView;

public class ToolPresenter {
    private Activity activity;
    private ToolView view;
    private ToolModel mode;

    public ToolPresenter(Activity activity, ToolView view) {
        this.activity = activity;
        this.view = view;
        mode = new ToolModel();
    }

    public void setQQLikeEvent() {
        mode.setQQlikeEvent(activity, view);
    }

    public void getCeHuiData() {
        mode.getCeHuiData(activity, view);
    }
}
