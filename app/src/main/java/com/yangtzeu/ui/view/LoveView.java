package com.yangtzeu.ui.view;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yangtzeu.entity.LoveBean;
import com.yangtzeu.ui.adapter.LoveAdapter;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public interface LoveView {

    boolean isRefresh();

    RecyclerView getRecyclerView();

    int getStart();
    SmartRefreshLayout getRefresh();
    LoveAdapter getAdapter();

    String getText();
    String getType();

    List<LoveBean.DataBean> getData();
}
