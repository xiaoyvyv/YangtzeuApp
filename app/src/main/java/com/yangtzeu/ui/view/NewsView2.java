package com.yangtzeu.ui.view;


import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yangtzeu.entity.NewsBean;
import com.yangtzeu.ui.adapter.NewsAdapter;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public interface NewsView2 {
    RecyclerView getRecyclerView();

    List<NewsBean> getData();


    NewsAdapter getNewsAdapter();

    String getKind();
    SmartRefreshLayout getSmartRefreshLayout();

    String getUrl();

    boolean isRefresh();

    String getUrlHeader();

    int getStartIndex();
    int getAllPage();

    void setStartIndex(int start);
    void setAllPage(int page);
}
