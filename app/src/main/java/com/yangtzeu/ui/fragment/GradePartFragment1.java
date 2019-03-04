package com.yangtzeu.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yangtzeu.R;
import com.yangtzeu.entity.GradeBean;
import com.yangtzeu.listener.OnResultListener;
import com.yangtzeu.presenter.GradePart1Presenter;
import com.yangtzeu.ui.activity.ChartActivity;
import com.yangtzeu.ui.activity.base.BaseFragment;
import com.yangtzeu.ui.adapter.GradeAdapter;
import com.yangtzeu.ui.view.GradePartView1;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.YangtzeuUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Administrator on 2018/3/6.
 */

public class GradePartFragment1 extends BaseFragment implements GradePartView1 {
    // 标志位，标志已经初始化完成。
    private boolean isPrepared = false;
    private View rootView;
    private GradePart1Presenter presenter;

    private RecyclerView recyclerView;
    private SmartRefreshLayout smartRefreshLayout;
    private GradeAdapter gradeAdapter;

    private Button chooseTerm;
    private Button sort_low;
    private Button sort_high;
    private Button to_chart;
    private Button change;

    private List<GradeBean> gradeBeans;

    private String index_url;
    private String url;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_grade_part1, container, false);
        findViews();
        isPrepared = true;
        lazyLoad();
        return rootView;
    }

    @Override
    public void findViews() {
        recyclerView = rootView.findViewById(R.id.mRecyclerView);
        chooseTerm = rootView.findViewById(R.id.chooseTerm);
        sort_low = rootView.findViewById(R.id.sort_low);
        sort_high = rootView.findViewById(R.id.sort_high);
        smartRefreshLayout = rootView.findViewById(R.id.refresh);
        to_chart = rootView.findViewById(R.id.to_chart);
        change = rootView.findViewById(R.id.change);

    }

    @Override
    public void setEvents() {
        index_url = Url.Yangtzeu_Grade_Url_Index1;
        url = Url.Yangtzeu_Grade_Url1;


        gradeBeans = new ArrayList<>();
        gradeAdapter = new GradeAdapter(getActivity());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(gradeAdapter);


        presenter = new GradePart1Presenter(getActivity(), this);

        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                presenter.loadGradeData();
            }
        });
        smartRefreshLayout.autoRefresh();


        chooseTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YangtzeuUtils.showChooseTerm(Objects.requireNonNull(getActivity()), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SPUtils.getInstance("user_info").put("term_id", String.valueOf(which));
                        presenter.loadGradeData();
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


        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YangtzeuUtils.showChooseModel(new OnResultListener<Integer>() {
                    @Override
                    public void onResult(Integer projectId) {
                        switch (projectId) {
                            case 1:
                                index_url = Url.Yangtzeu_Grade_Url_Index1;
                                url = Url.Yangtzeu_Grade_Url1;
                                smartRefreshLayout.autoRefresh();
                                break;
                            case 2:
                                index_url = Url.Yangtzeu_Grade_Url_Index2;
                                url = Url.Yangtzeu_Grade_Url2;
                                smartRefreshLayout.autoRefresh();
                                break;
                        }
                    }
                });
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
    public List<GradeBean> getGradeBeans() {
        return gradeBeans;
    }

    @Override
    public Toolbar getToolbar() {
        return  GradeFragment.toolbar;
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
