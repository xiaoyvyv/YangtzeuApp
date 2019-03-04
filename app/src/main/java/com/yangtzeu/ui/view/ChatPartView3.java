package com.yangtzeu.ui.view;

import com.lib.chat.adapter.ContactGroupAdapter;
import com.lib.chat.adapter.ContactUserAdapter;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public interface ChatPartView3 {
    SwipeRefreshLayout getRefresh();

    ContactGroupAdapter getAdapter();
}
