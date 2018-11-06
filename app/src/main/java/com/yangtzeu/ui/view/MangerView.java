package com.yangtzeu.ui.view;


import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yangtzeu.entity.FeedBackBean;
import com.yangtzeu.ui.adapter.FeedBackAdapter;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public interface MangerView {
    RecyclerView getContainer();

    SmartRefreshLayout getRefresh();

    List<FeedBackBean.DataBean> getData();

    int getPage();

    boolean isRefresh();
    FeedBackAdapter getAdapter();

}
