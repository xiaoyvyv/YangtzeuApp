package com.yangtzeu.ui.view;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yangtzeu.entity.ShopBean;
import com.yangtzeu.ui.adapter.ShopAdapter;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public interface ShopView {
    RecyclerView getRecyclerView();

    int getStart();
    SmartRefreshLayout getRefresh();
    ShopAdapter getAdapter();

    List<ShopBean.DataBean> getData();
    String getText();

    String getType();
}
