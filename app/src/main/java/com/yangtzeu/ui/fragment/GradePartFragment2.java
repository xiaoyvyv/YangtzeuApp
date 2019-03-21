package com.yangtzeu.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yangtzeu.R;
import com.yangtzeu.entity.GradeBean;
import com.yangtzeu.listener.OnResultListener;
import com.yangtzeu.presenter.GradePart2Presenter;
import com.yangtzeu.ui.activity.ChartActivity;
import com.yangtzeu.ui.activity.base.BaseFragment;
import com.yangtzeu.ui.adapter.GradeAdapter;
import com.yangtzeu.ui.view.GradePartView2;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.YangtzeuUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Administrator on 2018/3/6.
 */

public class GradePartFragment2 extends BaseFragment implements GradePartView2 {
    // 标志位，标志已经初始化完成。
    private boolean isPrepared = false;
    private View rootView;
    private GradePart2Presenter presenter;

    private RecyclerView recyclerView;
    private SmartRefreshLayout smartRefreshLayout;
    private GradeAdapter gradeAdapter;

    private Button change;
    private Button sort_low;
    private Button exportGrade;
    private Button sort_high;
    private Button to_chart;


    private TextView public_choose_score_tv;
    private TextView major_choose_score_tv;
    private TextView major_score_tv;
    private TextView practice_score_tv;
    private CardView all_score_container;

    private List<Double> public_choose_scores = new ArrayList<>();
    private List<Double> major_choose_scores = new ArrayList<>();
    private List<Double> major_scores = new ArrayList<>();
    private List<Double> practice_scores = new ArrayList<>();

    private List<GradeBean> gradeBeans;
    private String index_url;
    private String url;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_grade_part2, container, false);
        findViews();
        isPrepared = true;
        lazyLoad();
        return rootView;
    }

    @Override
    public void findViews() {
        recyclerView = rootView.findViewById(R.id.mRecyclerView);
        change = rootView.findViewById(R.id.change);
        sort_low = rootView.findViewById(R.id.sort_low);
        sort_high = rootView.findViewById(R.id.sort_high);
        exportGrade = rootView.findViewById(R.id.exportGrade);
        smartRefreshLayout = rootView.findViewById(R.id.refresh);
        to_chart = rootView.findViewById(R.id.to_chart);
        all_score_container = rootView.findViewById(R.id.all_score_container);

        public_choose_score_tv = rootView.findViewById(R.id.public_class_score);
        major_choose_score_tv = rootView.findViewById(R.id.major_choose_score);
        major_score_tv = rootView.findViewById(R.id.major_score);
        practice_score_tv = rootView.findViewById(R.id.practice_score);

    }

    @Override
    public void setEvents() {
        index_url = Url.Yangtzeu_AllGrade_Url_Index1;
        url = Url.Yangtzeu_AllGrade_Url1;

        gradeBeans = new ArrayList<>();
        gradeAdapter = new GradeAdapter(getActivity());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(gradeAdapter);


        presenter = new GradePart2Presenter(getActivity(), this);

        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                all_score_container.setVisibility(View.INVISIBLE);
                presenter.loadGradeData();
            }
        });
        smartRefreshLayout.autoRefresh();

        exportGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getGradeXls();
            }
        });

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

        sort_low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gradeAdapter.sort(false);

            }
        });
        to_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ObjectUtils.isNotEmpty(gradeBeans)) {
                    GradeBean list = new GradeBean();
                    list.setGradeBeans(gradeBeans);
                    Intent intent = new Intent(getActivity(), ChartActivity.class);
                    intent.putExtra("data", list);
                    MyUtils.startActivity(intent);
                } else {
                    ToastUtils.showShort(R.string.no_data);
                }
            }
        });


        sort_high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gradeAdapter.sort(true);
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
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }


    @Override
    public SmartRefreshLayout getRefresh() {
        return smartRefreshLayout;
    }

    @Override
    public GradeAdapter getAdapter() {
        return gradeAdapter;
    }

    @Override
    public List<Double> public_choose_scores() {
        return public_choose_scores;
    }

    @Override
    public List<Double> major_choose_scores() {
        return major_choose_scores;
    }

    @Override
    public List<Double> major_scores() {
        return major_scores;
    }

    @Override
    public List<Double> practice_scores() {
        return practice_scores;
    }

    @Override
    public List<GradeBean> getGradeBeans() {
        return gradeBeans;
    }

    @Override
    public Toolbar getToolbar() {
        return GradeFragment.toolbar;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getIndexUrl() {
        return index_url;
    }

    @Override
    public TextView public_choose_score_tv() {
        return public_choose_score_tv;
    }

    @Override
    public TextView major_choose_score_tv() {
        return major_choose_score_tv;
    }

    @Override
    public TextView major_score_tv() {
        return major_score_tv;
    }

    @Override
    public TextView practice_score_tv() {
        return practice_score_tv;
    }

    @Override
    public CardView all_score_container() {
        return all_score_container;
    }
}
