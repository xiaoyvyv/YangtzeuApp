package com.yangtzeu.ui.view;

import android.widget.FrameLayout;

import com.lib.x5web.WebViewProgressBar;
import com.lib.x5web.X5WebView;

import androidx.appcompat.widget.Toolbar;

public interface HomePartView3 {
    X5WebView getWebView();

    WebViewProgressBar getProgressBar();

    Toolbar getToolbar();
    FrameLayout getContainer();
}
