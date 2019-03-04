package com.yangtzeu.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yangtzeu.R;
import com.yangtzeu.entity.PointBean;
import com.yangtzeu.listener.OnResultListener;
import com.yangtzeu.presenter.GradePart3Presenter;
import com.yangtzeu.ui.activity.base.BaseFragment;
import com.yangtzeu.ui.adapter.PointAdapter;
import com.yangtzeu.ui.view.GradePartView3;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.YangtzeuUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class GradePartFragment3 extends BaseFragment implements GradePartView3 {
    // 标志位，标志已经初始化完成。
    private boolean isPrepared = false;
    private boolean isLoadFinish = false;

    private View rootView;
    //绩点容器
    private List<PointBean> gradeBeans = new ArrayList<>();
    private GradePart3Presenter presenter;
    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView mRecyclerView;
    private PointAdapter pointAdapter;
    private TextView all_number;
    private TextView all_score;
    private TextView all_point;
    private Button change;
    private String url;
    private String index_url;
    private LinearLayout header;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_grade_part3, container, false);
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
        change = rootView.findViewById(R.id.change);
        header = rootView.findViewById(R.id.header);

    }

    @Override
    public void setEvents() {
        index_url = Url.Yangtzeu_AllGrade_Url_Index1;
        url = Url.Yangtzeu_AllGrade_Url1;

        pointAdapter = new PointAdapter(getActivity());
        presenter = new GradePart3Presenter(getActivity(), this);


        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(pointAdapter);

        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                pointAdapter.clear();
                header.setVisibility(View.GONE);
                presenter.loadPointData();
            }
        });
        smartRefreshLayout.autoRefresh();


        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YangtzeuUtils.showChooseModel(new OnResultListener<Integer>() {
                    @Override
                    public void onResult(Integer projectId) {
                        switch (projectId) {
                            case 1:
                                index_url = Url.Yangtzeu_AllGrade_Url_Index1;
                                url = Url.Yangtzeu_AllGrade_Url1;
                                smartRefreshLayout.autoRefresh();
                                break;
                            case 2:
                                index_url = Url.Yangtzeu_AllGrade_Url_Index2;
                                url = Url.Yangtzeu_AllGrade_Url2;
                                smartRefreshLayout.autoRefresh();
                                break;
                        }
                    }
                });
            }
        });
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
    public LinearLayout getHeaderContainer() {
        return header;
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

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getIndexUrl() {
        return index_url;
    }
}