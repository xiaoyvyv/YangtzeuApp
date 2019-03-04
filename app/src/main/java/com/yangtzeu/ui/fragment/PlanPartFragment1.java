package com.yangtzeu.ui.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lib.x5web.X5WebView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yangtzeu.R;
import com.yangtzeu.entity.GradeBean;
import com.yangtzeu.listener.OnResultListener;
import com.yangtzeu.model.PlanModel;
import com.yangtzeu.presenter.GradePart1Presenter;
import com.yangtzeu.presenter.PlanPart1Presenter;
import com.yangtzeu.ui.activity.ChartActivity;
import com.yangtzeu.ui.activity.PlanActivity;
import com.yangtzeu.ui.activity.base.BaseFragment;
import com.yangtzeu.ui.adapter.GradeAdapter;
import com.yangtzeu.ui.view.GradePartView1;
import com.yangtzeu.ui.view.PlanPartView1;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.YangtzeuUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Administrator on 2018/3/6.
 */

public class PlanPartFragment1 extends BaseFragment implements PlanPartView1 {
    public static String plan_term = "1";
    // 标志位，标志已经初始化完成。
    private boolean isPrepared = false;
    private View rootView;
    @SuppressLint("StaticFieldLeak")
    public static PlanPart1Presenter presenter;
    private X5WebView webView;
    private FloatingActionButton button;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_plan_part1, container, false);
        findViews();
        isPrepared = true;
        lazyLoad();
        return rootView;
    }

    @Override
    public void findViews() {
        button = rootView.findViewById(R.id.button);
        webView = rootView.findViewById(R.id.webView);
    }

    @Override
    public void setEvents() {
        presenter = new PlanPart1Presenter(getActivity(), this);
        presenter.loadPlan();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null)
                    YangtzeuUtils.showChoosePlan(getActivity(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PlanPartFragment1.plan_term = String.valueOf(which);
                            presenter.loadPlan();
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
    public void onDestroy() {
        presenter = null;
        super.onDestroy();
    }
    @Override
    public X5WebView getWebView() {
        return webView;
    }

    @Override
    public FloatingActionButton getButton() {
        return button;
    }
}
