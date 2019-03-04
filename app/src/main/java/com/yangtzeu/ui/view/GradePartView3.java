package com.yangtzeu.ui.view;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yangtzeu.entity.PointBean;
import com.yangtzeu.ui.adapter.PointAdapter;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public interface GradePartView3 {

    String getUrl();

    String getIndexUrl();

    SmartRefreshLayout getRefresh();

    List<PointBean> getGradeBeans();

    TextView getAllNumberView();

    TextView getAllScoreView();

    TextView getAllPointView();

    RecyclerView getRecyclerView();

    LinearLayout getHeaderContainer();

    PointAdapter getPointAdapter();
}
