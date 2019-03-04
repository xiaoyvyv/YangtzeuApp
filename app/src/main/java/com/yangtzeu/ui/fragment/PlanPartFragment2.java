package com.yangtzeu.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yangtzeu.R;
import com.yangtzeu.presenter.PlanPart2Presenter;
import com.yangtzeu.ui.activity.base.BaseFragment;
import com.yangtzeu.ui.view.PlanPartView2;
import com.yangtzeu.url.Url;

import androidx.annotation.NonNull;

/**
 * Created by Administrator on 2018/3/6.
 */

public class PlanPartFragment2 extends BaseFragment implements PlanPartView2 {
    // 标志位，标志已经初始化完成。
    private boolean isPrepared = false;
    private View rootView;
    @SuppressLint("StaticFieldLeak")
    public static PlanPart2Presenter presenter;
    private LinearLayout container;
    public static SmartRefreshLayout refresh;
    public static String major_url_index;
    public static String major_url;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_plan_part2, container, false);
        findViews();
        isPrepared = true;
        lazyLoad();
        return rootView;
    }

    @Override
    public void findViews() {
        refresh = rootView.findViewById(R.id.refresh);
        container = rootView.findViewById(R.id.container);
    }

    @Override
    public void setEvents() {
        major_url_index = Url.Yangtzeu_Major_Mode_Index1;
        major_url = Url.Yangtzeu_Major_Mode1;

        presenter = new PlanPart2Presenter(getActivity(), this);
        refresh.setEnableLoadMore(false);
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                presenter.showPlanList();
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
    public void onDestroy() {
        refresh = null;
        presenter = null;
        super.onDestroy();
    }

    @Override
    public LinearLayout getContainer() {
        return container;
    }

    @Override
    public String getMajorModeUrlIndex() {
        return major_url_index;
    }

    @Override
    public String getMajorModeUrl() {
        return major_url;
    }

    @Override
    public SmartRefreshLayout getRefresh() {
        return refresh;
    }
}
