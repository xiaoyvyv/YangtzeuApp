package com.yangtzeu.ui.view;

import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

public interface PlanPartView2 {
    LinearLayout getContainer();

    String getMajorModeUrlIndex();
    String getMajorModeUrl();

    SmartRefreshLayout getRefresh();
}
