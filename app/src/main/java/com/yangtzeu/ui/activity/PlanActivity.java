package com.yangtzeu.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.tabs.TabLayout;
import com.yangtzeu.R;
import com.yangtzeu.listener.OnResultListener;
import com.yangtzeu.presenter.PlanPresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.fragment.PlanPartFragment1;
import com.yangtzeu.ui.fragment.PlanPartFragment2;
import com.yangtzeu.ui.view.PlanView;
import com.yangtzeu.url.Url;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.YangtzeuUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class PlanActivity extends BaseActivity implements PlanView {
    private Toolbar toolbar;
    private FrameLayout container;
    public static TabLayout tablayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);
    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        container = findViewById(R.id.container);
        tablayout = findViewById(R.id.tabLayout);
    }

    @Override
    public void setEvents() {
        PlanPresenter planPresenter = new PlanPresenter(this, this);
        planPresenter.getPlanInfo();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("切换").setIcon(R.drawable.ic_change)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        YangtzeuUtils.showChooseModel(new OnResultListener<Integer>() {
                            @Override
                            public void onResult(Integer projectId) {
                                switch (projectId) {
                                    case 1:
                                        PlanPartFragment2.major_url_index = Url.Yangtzeu_Major_Mode_Index1;
                                        PlanPartFragment2.major_url = Url.Yangtzeu_Major_Mode1;

                                        if (PlanPartFragment1.presenter!=null)
                                            PlanPartFragment1.presenter.loadPlan();
                                        break;
                                    case 2:
                                        PlanPartFragment2.major_url_index = Url.Yangtzeu_Major_Mode_Index2;
                                        PlanPartFragment2.major_url = Url.Yangtzeu_Major_Mode2;
                                        if (PlanPartFragment2.refresh!=null)
                                            PlanPartFragment2.refresh.autoRefresh();
                                        break;
                                }
                            }
                        });
                        return false;
                    }
                }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public FrameLayout getContainer() {
        return container;
    }

    @Override
    public TabLayout getTabLayout() {
        return tablayout;
    }
}
