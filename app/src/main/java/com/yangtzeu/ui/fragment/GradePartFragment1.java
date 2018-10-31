package com.yangtzeu.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
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
import lecho.lib.hellocharts.view.LineChartView;

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
    private Button exportGrade;
    private Button sort_high;
    private Button to_chart;

    private String from_url;


    private TextView public_choose_score_tv;
    private TextView major_choose_score_tv;
    private TextView major_score_tv;
    private TextView practice_score_tv;
    private CardView all_score_container;

    private List<Double> public_choose_scores = new ArrayList<>();
    private List<Double> major_choose_scores = new ArrayList<>();
    private List<Double> major_scores = new ArrayList<>();
    private List<Double> practice_scores = new ArrayList<>();

    private boolean isAllGrade;
    private List<GradeBean> gradeBeans;
    private NestedScrollView nestedScrollView;

    public static GradePartFragment1 newInstance(String url) {
        GradePartFragment1 fragment = new GradePartFragment1();
        Bundle bundle = new Bundle();
        bundle.putString("from_url", url);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            from_url = bundle.getString("from_url");
        }
        if (from_url != null && from_url.contains(Url.Yangtzeu_AllGrade_Url)) {
            isAllGrade = true;
        }
        rootView = inflater.inflate(R.layout.fragment_grade_part1, container, false);
        findViews();
        isPrepared = true;
        lazyLoad();
        return rootView;
    }

    @Override
    public void findViews() {
        nestedScrollView= rootView.findViewById(R.id.nestedScrollView);
        recyclerView = rootView.findViewById(R.id.mRecyclerView);
        chooseTerm = rootView.findViewById(R.id.chooseTerm);
        sort_low = rootView.findViewById(R.id.sort_low);
        sort_high = rootView.findViewById(R.id.sort_high);
        exportGrade = rootView.findViewById(R.id.exportGrade);
        smartRefreshLayout = rootView.findViewById(R.id.refresh);
        public_choose_score_tv = rootView.findViewById(R.id.public_class_score);
        major_choose_score_tv = rootView.findViewById(R.id.major_choose_score);
        major_score_tv = rootView.findViewById(R.id.major_score);
        practice_score_tv = rootView.findViewById(R.id.practice_score);
        all_score_container = rootView.findViewById(R.id.all_score_container);
        to_chart = rootView.findViewById(R.id.to_chart);

    }

    @Override
    public void setEvents() {
        if (isAllGrade) {
            chooseTerm.setVisibility(View.GONE);
            exportGrade.setVisibility(View.VISIBLE);
        }

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

        exportGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getGradeXls();
            }
        });

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
    public NestedScrollView getNestedScrollView() {
        return nestedScrollView;
    }


    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public String getFromUrl() {
        return from_url;
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
    public boolean IsAllGrade() {
        return isAllGrade;
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
