package com.yangtzeu.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yangtzeu.R;
import com.yangtzeu.presenter.VoaListPresenter;
import com.yangtzeu.presenter.VoaPresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.adapter.VoaListAdapter;
import com.yangtzeu.ui.view.VoaListView;
import com.yangtzeu.ui.view.VoaView;
import com.yangtzeu.utils.MyUtils;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class VoaListActivity extends BaseActivity implements VoaListView {
    private Toolbar toolbar;
    private String from_url_header;
    private RecyclerView recyclerView;
    private VoaListPresenter presenter;
    private SmartRefreshLayout refresh;
    private int page = 1;
    private boolean is_archiver = false;
    private VoaListAdapter adapter;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String title = getIntent().getStringExtra("title");
        String from_url = getIntent().getStringExtra("from_url");
        setContentView(R.layout.activity_voa_list);
        if (from_url == null) {
            ToastUtils.showShort(R.string.no_data);
        } else {
            //存档特殊情况
            if (from_url.contains("_archiver")) {
                is_archiver = true;
                from_url = from_url.replace("_archiver", "");
            } else {
                is_archiver = false;
            }

            String s = from_url.substring(from_url.lastIndexOf("/") + 1);
            from_url_header = s.substring(0, s.lastIndexOf(".") - 1);
        }

        init();

        toolbar.setTitle(title);
        MyUtils.setToolbarBackToHome(this, toolbar);
    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        refresh = findViewById(R.id.refresh);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setEvents() {
        LinearLayoutManager manger = new LinearLayoutManager(this);
        manger.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manger);

        adapter = new VoaListAdapter(this);
        presenter = new VoaListPresenter(this, this);

        recyclerView.setAdapter(adapter);

        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                page = 1;
                adapter.clear();
                presenter.loadData();
                refresh.finishRefresh();
            }
        });
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                page = page + 1;
                presenter.loadData();
                refresh.finishLoadMore();
            }
        });
        refresh.autoRefresh();
    }


    @Override
    public String getFromUrlHeader() {
        return from_url_header;
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public boolean is_archiver() {
        return is_archiver;
    }

    @Override
    public VoaListAdapter getAdapter() {
        return adapter;
    }
}
