package com.yangtzeu.ui.view;

import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yangtzeu.entity.GradeBean;
import com.yangtzeu.ui.adapter.GradeAdapter;

import java.util.List;


public interface GradePartView1 {
    NestedScrollView getNestedScrollView();

    RecyclerView getRecyclerView();

    String getFromUrl();

    SmartRefreshLayout getRefresh();

    GradeAdapter getAdapter();

    boolean IsAllGrade();

    TextView public_choose_score_tv();

    TextView major_choose_score_tv();

    TextView major_score_tv();

    TextView practice_score_tv();

    CardView all_score_container();

    List<Double> public_choose_scores();

    List<Double> major_choose_scores();

    List<Double> major_scores();

    List<Double> practice_scores();

    //成绩容器
    List<GradeBean> getGradeBeans();
}
