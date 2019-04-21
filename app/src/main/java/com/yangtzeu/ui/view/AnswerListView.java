package com.yangtzeu.ui.view;

import android.widget.LinearLayout;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public interface AnswerListView {
    String getFromUrl();

    LinearLayout getContainer();

    SwipeRefreshLayout getRefresh();

}
