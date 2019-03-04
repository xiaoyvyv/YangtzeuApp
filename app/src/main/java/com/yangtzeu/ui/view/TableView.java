package com.yangtzeu.ui.view;

import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yangtzeu.entity.Course;
import com.yangtzeu.ui.adapter.TableFragmentAdapter;

import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

public interface TableView {
    Toolbar getToolbar();

    LinearLayout getWeekLayout();

    RecyclerView getRecyclerView();

    List<Course> getCourse();

    SmartRefreshLayout getRefreshLayout();

    TabLayout getTabLayout();

    TableFragmentAdapter getTableFragmentAdapter();

    ImageView getTableBackgroundView();


    String getIdsUrl();

    String getIndexUrl();
}
