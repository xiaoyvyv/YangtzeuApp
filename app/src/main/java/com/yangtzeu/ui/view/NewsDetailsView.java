package com.yangtzeu.ui.view;

import android.widget.RelativeLayout;

import com.lib.loading.LVBlock;
import com.lib.x5web.X5WebView;

import androidx.appcompat.widget.Toolbar;

public interface NewsDetailsView {
    String getFromUrl();
    LVBlock getLoadingView();

    RelativeLayout getContainer();
    X5WebView getWebView();

    Toolbar getToolbar();
}
