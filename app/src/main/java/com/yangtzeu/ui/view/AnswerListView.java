package com.yangtzeu.ui.view;

import android.widget.LinearLayout;

import com.miui.zeus.mimo.sdk.ad.IAdWorker;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public interface AnswerListView {
    String getFromUrl();

    LinearLayout getContainer();

    SwipeRefreshLayout getRefresh();

    IAdWorker getAdWorker();
    void setAdWorker(IAdWorker adWorker);
}
