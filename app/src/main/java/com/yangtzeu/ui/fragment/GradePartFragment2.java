package com.yangtzeu.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yangtzeu.R;
import com.yangtzeu.entity.PointBean;
import com.yangtzeu.presenter.GradePart2Presenter;
import com.yangtzeu.ui.activity.base.BaseFragment;
import com.yangtzeu.ui.adapter.PointAdapter;
import com.yangtzeu.ui.view.GradePartView2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class GradePartFragment2 extends BaseFragment implements GradePartView2 {
    // 标志位，标志已经初始化完成。
    private boolean isPrepared = false;
    private boolean isLoadFinish = false;

    private View rootView;
    //绩点容器
    private List<PointBean> gradeBeans = new ArrayList<>();
    private GradePart2Presenter presenter;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView mRecyclerView;
    private PointAdapter pointAdapter;
    private TextView all_number;
    private TextView all_score;
    private TextView all_point;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_grade_part2, container, false);
        findViews();
        isPrepared = true;
        lazyLoad();
        return rootView;
    }

    @Override
    public void findViews() {
        smartRefreshLayout = rootView.findViewById(R.id.smartRefreshLayout);
        mRecyclerView = rootView.findViewById(R.id.mRecyclerView);
        all_number = rootView.findViewById(R.id.all_number);
        all_score = rootView.findViewById(R.id.all_score);
        all_point = rootView.findViewById(R.id.all_point);

    }

    @Override
    public void setEvents() {
        pointAdapter = new PointAdapter(getActivity());
        presenter = new GradePart2Presenter(getActivity(), this);


        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(pointAdapter);

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                pointAdapter.clear();
                presenter.loadPointData();
            }
        });

        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.autoRefresh();
    }


    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        if (!isLoadFinish) {
            setEvents();
            isLoadFinish = true;
        }
    }

    @Override
    public SmartRefreshLayout getRefresh() {
        return smartRefreshLayout;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public PointAdapter getPointAdapter() {
        return pointAdapter;
    }

    @Override
    public TextView getAllNumberView() {
        return all_number;
    }

    @Override
    public TextView getAllScoreView() {
        return all_score;
    }

    @Override
    public TextView getAllPointView() {
        return all_point;
    }

    @Override
    public List<PointBean> getGradeBeans() {
        return gradeBeans;
    }
}