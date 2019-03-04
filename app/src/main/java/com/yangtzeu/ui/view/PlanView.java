package com.yangtzeu.ui.view;

import android.widget.FrameLayout;

import com.google.android.material.tabs.TabLayout;
import com.lib.x5web.X5WebView;

import androidx.appcompat.widget.Toolbar;

public interface PlanView {
    Toolbar getToolbar();


    FrameLayout getContainer();

    TabLayout getTabLayout();
}
