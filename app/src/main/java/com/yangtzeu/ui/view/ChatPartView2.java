package com.yangtzeu.ui.view;

import com.lib.chat.adapter.ContactUserAdapter;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public interface ChatPartView2 {
    SwipeRefreshLayout getRefresh();

    ContactUserAdapter getAdapter();
}
