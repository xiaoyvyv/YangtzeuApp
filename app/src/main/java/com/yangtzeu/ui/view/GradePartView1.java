package com.yangtzeu.ui.view;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yangtzeu.entity.GradeBean;
import com.yangtzeu.ui.adapter.GradeAdapter;

import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;


public interface GradePartView1 {

    RecyclerView getRecyclerView();

    SmartRefreshLayout getRefresh();

    GradeAdapter getAdapter();

    //成绩容器
    List<GradeBean> getGradeBeans();

    Toolbar getToolbar();

    String getUrl();

    String getIndexUrl();

}
