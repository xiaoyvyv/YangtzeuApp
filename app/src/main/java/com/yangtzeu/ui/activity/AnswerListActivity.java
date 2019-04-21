package com.yangtzeu.ui.activity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.yangtzeu.R;
import com.yangtzeu.presenter.AnswerListPresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.view.AnswerListView;
import com.yangtzeu.utils.MyUtils;

public class AnswerListActivity extends BaseActivity implements AnswerListView {
    private String from_url;
    private LinearLayout container;
    private SwipeRefreshLayout refreshLayout;
    private Toolbar toolbar;
    private AnswerListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        from_url = getIntent().getStringExtra("from_url");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_list);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);
    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        container = findViewById(R.id.slow_container);
        refreshLayout = findViewById(R.id.refreshLayout);
    }


    @Override
    public void setEvents() {
        presenter = new AnswerListPresenter(this, this);
        presenter.loadAnswer();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadAnswer();
                refreshLayout.setRefreshing(false);
            }
        });

    }


    @Override
    public String getFromUrl() {
        return from_url;
    }

    @Override
    public LinearLayout getContainer() {
        return container;
    }

    @Override
    public SwipeRefreshLayout getRefresh() {
        return refreshLayout;
    }

}
