package com.yangtzeu.ui.activity;

import android.os.Bundle;

import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yangtzeu.R;
import com.yangtzeu.presenter.JwcListPresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.adapter.JwcListAdapter;
import com.yangtzeu.ui.view.JwcListView;
import com.yangtzeu.utils.MyUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

public class JwcListActivity extends BaseActivity implements JwcListView {
    private String from_url;
    private String url_header;
    private RecyclerView recyclerView;
    private JwcListAdapter adapter;
    private JwcListPresenter president;
    private Toolbar toolbar;
    private SmartRefreshLayout refreshLayout;
    private int allPage = 0;
    private int start = 0;
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        title = getIntent().getStringExtra("title");
        from_url = getIntent().getStringExtra("from_url");
        url_header = from_url.substring(0, from_url.lastIndexOf(".")) + "/";

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_jwc_list);
        init();
        MyUtils.setToolbarBackToHome(this, toolbar);
        toolbar.setTitle(title);
    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
    }

    @Override
    public void setEvents() {
        president = new JwcListPresenter(this, this);
        adapter = new JwcListAdapter(this);
        recyclerView.setAdapter(adapter);
        refreshLayout.setRefreshHeader(new MaterialHeader(this));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                allPage = 0;
                adapter.clear();
                from_url = getIntent().getStringExtra("from_url");
                president.loadData();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                allPage = allPage - 1;
                if (allPage <= 0) {
                    ToastUtils.showShort(R.string.no_more);
                    refreshLayout.finishLoadMore();
                    return;
                }
                from_url = url_header + allPage + ".htm";
                president.loadData();

            }
        });
        refreshLayout.autoRefresh();
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public JwcListAdapter getAdapter() {
        return adapter;
    }

    @Override
    public int getAllPage() {
        return allPage;
    }


    @Override
    public SmartRefreshLayout getSmartRefreshLayout() {
        return refreshLayout;
    }

    @Override
    public String getUrl() {
        return from_url;
    }

    @Override
    public void setAllPage(String all_page) {
        this.allPage = Integer.parseInt(all_page);
    }

    @Override
    public String getKind() {
        return title;
    }

    @Override
    public String getUrlHeader() {
        return url_header;
    }

    @Override
    public int getStartIndex() {
        return start;
    }

    @Override
    public void setStartIndex(int start) {
        this.start = start;
    }

}
