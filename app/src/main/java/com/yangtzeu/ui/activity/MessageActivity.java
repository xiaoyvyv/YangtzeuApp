package com.yangtzeu.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yangtzeu.R;
import com.yangtzeu.entity.MessageBean;
import com.yangtzeu.presenter.MessagePresenter;
import com.yangtzeu.ui.activity.base.BaseActivity;
import com.yangtzeu.ui.adapter.MessageAdapter;
import com.yangtzeu.ui.view.MessageView;
import com.yangtzeu.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;


public class MessageActivity extends BaseActivity implements MessageView {
    private Toolbar toolbar;
    private MessageAdapter adapter;
    private RecyclerView mRecyclerView;
    private SmartRefreshLayout refresh;
    private MessagePresenter president;
    private List<MessageBean.DataBean> my_data = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        init();

        MyUtils.setToolbarBackToHome(this, toolbar);
    }

    @Override
    public void findViews() {
        toolbar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.mRecyclerView);
        refresh = findViewById(R.id.refresh);
    }

    @Override
    public void setEvents() {
        president = new MessagePresenter(this, this);
        adapter = new MessageAdapter(this);
        mRecyclerView.setAdapter(adapter);

        refresh.setEnableLoadMore(false);
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                president.loadMessageData();
            }
        });
        refresh.autoRefresh();
    }


    @Override
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }


    @Override
    public List<MessageBean.DataBean> getData() {
        return my_data;
    }

    @Override
    public MessageAdapter getAdapter() {
        return adapter;
    }

    @Override
    public SmartRefreshLayout getRefresh() {
        return refresh;
    }

    public void shop(View view) {
        MyUtils.startActivity(ShopActivity.class);
    }
}
