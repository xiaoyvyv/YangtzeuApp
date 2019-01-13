package com.yangtzeu.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lib.x5web.X5WebView;
import com.yangtzeu.R;
import com.yangtzeu.model.PlanModel;
import com.yangtzeu.presenter.PlanPresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.view.PlanView;
import com.yangtzeu.utils.MyUtils;
import com.yangtzeu.utils.YangtzeuUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class PlanActivity extends BaseActivity implements PlanView {

    private PlanPresenter planPresenter;
    private Toolbar toolbar;
    private X5WebView webView;

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
        webView = findViewById(R.id.webView);

    }
    @Override
    public void setEvents() {
        planPresenter = new PlanPresenter(this, this);
        planPresenter.isPlanExist();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("选择学期")
                .setIcon(R.drawable.ic_choose)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        YangtzeuUtils.showChoosePlan(PlanActivity.this, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                PlanModel.plan_term = String.valueOf(which);
                                planPresenter.loadPlan();
                            }
                        });
                        return false;
                    }
                })
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add("培养计划列表")
                .setIcon(R.drawable.ic_mess_board)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        planPresenter.showPlanList();
                        return false;
                    }
                })
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public X5WebView getWebView() {
        return webView;
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }
}
