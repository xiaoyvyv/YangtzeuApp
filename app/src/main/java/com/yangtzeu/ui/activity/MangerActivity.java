package com.yangtzeu.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yangtzeu.R;
import com.yangtzeu.entity.FeedBackBean;
import com.yangtzeu.presenter.MangerPresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.adapter.FeedBackAdapter;
import com.yangtzeu.ui.view.MangerView;
import com.yangtzeu.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

public class MangerActivity extends BaseActivity implements MangerView {
    private Toolbar toolbar;
    private RecyclerView container;
    private MangerPresenter presenter;
    private SmartRefreshLayout refreshLayout;
    private List<FeedBackBean.DataBean> beans = new ArrayList<>();
    private int page = 0;
    private FeedBackAdapter adapter;
    private boolean isRefresh = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manger);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("数据管理").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                MyUtils.openUrl(MangerActivity.this, "http://101.132.108.0/yangtzeu/admin/index.html", true);
                return false;
            }
        }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void setEvents() {
        presenter = new MangerPresenter(this, this);
        adapter = new FeedBackAdapter( this);
        LinearLayoutManager manger = new LinearLayoutManager(this);
        manger.setOrientation(RecyclerView.VERTICAL);
        container.setLayoutManager(manger);
        container.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                page = 0;
                isRefresh = true;
                presenter.loadFeedBack();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                page = page + 30;
                isRefresh = false;
                presenter.loadFeedBack();
            }
        });
        refreshLayout.autoRefresh();
    }




    @Override
    public RecyclerView getContainer() {
        return container;
    }

    @Override
    public SmartRefreshLayout getRefresh() {
        return refreshLayout;
    }

    @Override
    public List<FeedBackBean.DataBean> getData() {
        return beans;
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public boolean isRefresh() {
        return isRefresh;
    }

    @Override
    public FeedBackAdapter getAdapter() {
        return adapter;
    }
}
