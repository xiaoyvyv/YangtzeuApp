package com.yangtzeu.ui.view;


import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yangtzeu.entity.MessageBean;
import com.yangtzeu.ui.adapter.MessageAdapter;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public interface MessageView {
    RecyclerView getRecyclerView();


    List<MessageBean.DataBean> getData();
    MessageAdapter getAdapter();

    SmartRefreshLayout getRefresh();
}
