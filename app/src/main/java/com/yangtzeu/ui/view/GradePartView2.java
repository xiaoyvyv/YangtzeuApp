package com.yangtzeu.ui.view;

import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yangtzeu.entity.GradeBean;
import com.yangtzeu.ui.adapter.GradeAdapter;

import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;



public interface GradePartView2 {

    RecyclerView getRecyclerView();


    SmartRefreshLayout getRefresh();

    GradeAdapter getAdapter();


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

    Toolbar getToolbar();

    String getUrl();

    String getIndexUrl();

}
