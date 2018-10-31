package com.yangtzeu.ui.view;

import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yangtzeu.entity.PointBean;
import com.yangtzeu.ui.adapter.PointAdapter;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public interface GradePartView2 {

    SmartRefreshLayout getRefresh();

    RecyclerView getRecyclerView();

    PointAdapter getPointAdapter();

    TextView getAllNumberView();

    TextView getAllScoreView();

    TextView getAllPointView();

    List<PointBean> getGradeBeans();
}
