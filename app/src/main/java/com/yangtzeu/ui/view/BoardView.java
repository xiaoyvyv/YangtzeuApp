package com.yangtzeu.ui.view;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yangtzeu.entity.BoardBean;
import com.yangtzeu.ui.adapter.BoardAdapter;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public interface BoardView {
    boolean isRefresh();
    RecyclerView getRecyclerView();

    int getPage();
    void setPage(int page);

    List<BoardBean.ResultBean> getBoardData();

    BoardAdapter getAdapter();

    SmartRefreshLayout getRefresh();
}
