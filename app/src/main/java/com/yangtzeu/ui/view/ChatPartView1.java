package com.yangtzeu.ui.view;

import com.lib.chat.adapter.ContactAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public interface ChatPartView1 {
    SwipeRefreshLayout getRefresh();

    ContactAdapter getAdapter();
}
