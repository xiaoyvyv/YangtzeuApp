package com.yangtzeu.ui.view;

import com.lib.chat.adapter.ContactGroupAdapter;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public interface ChatPartView3 {
    SwipeRefreshLayout getRefresh();

    ContactGroupAdapter getAdapter();
}
