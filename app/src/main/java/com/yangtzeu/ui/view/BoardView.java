package com.yangtzeu.ui.view;

import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yangtzeu.entity.BoardBean;
import com.yangtzeu.ui.adapter.BoardAdapter;

import java.util.List;

public interface BoardView {
    RecyclerView getRecyclerView();

    int getPage();
    void setPage(int page);

    List<BoardBean.ResultBean> getBoardData();

    BoardAdapter getAdapter();

    SmartRefreshLayout getRefresh();
}
