package com.yangtzeu.ui.view;

import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yangtzeu.ui.adapter.JwcListAdapter;

public interface JwcListView {
    RecyclerView getRecyclerView();

    JwcListAdapter getAdapter();

    int getAllPage();

    SmartRefreshLayout getSmartRefreshLayout();

    String getUrl();

    void setAllPage(String all_page);

    String getKind();

    String getUrlHeader();

    int getStartIndex();

    void setStartIndex(int start);
}
