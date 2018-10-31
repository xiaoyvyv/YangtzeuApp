package com.yangtzeu.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yangtzeu.R;
import com.yangtzeu.entity.ShopBean;
import com.yangtzeu.presenter.ShopPresenter;
import com.yangtzeu.ui.activity.base.BaseFragment;
import com.yangtzeu.ui.adapter.ShopAdapter;
import com.yangtzeu.ui.view.ShopView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Administrator on 2018/3/6.
 */

public class ShopFragment1 extends BaseFragment implements ShopView {
    // 标志位，标志已经初始化完成。
    private boolean isPrepared = false;
    private View rootView;
    private String type;
    private String text;
    private ShopPresenter president;
    private RecyclerView mRecyclerView;
    private int start = 0;
    private ShopAdapter adapter;
    private SmartRefreshLayout refresh;
    private List<ShopBean.DataBean> data = new ArrayList<>();
    private boolean is_manger = false;

    public static ShopFragment1 newInstance(String type,String text,boolean is_manger) {
        ShopFragment1 fragment = new ShopFragment1();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("text", text);
        bundle.putBoolean("is_manger", is_manger);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getString("type");
            text = bundle.getString("text");
            is_manger = bundle.getBoolean("is_manger", false);
        }
        rootView = inflater.inflate(R.layout.fragment_shop, container, false);
        findViews();
        isPrepared = true;
        lazyLoad();
        return rootView;
    }

    @Override
    public void findViews() {
        mRecyclerView = rootView.findViewById(R.id.mRecyclerView);
        refresh = rootView.findViewById(R.id.refresh);

    }

    @Override
    public void setEvents() {
        adapter = new ShopAdapter(getActivity(), is_manger);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(adapter);

        president = new ShopPresenter(getActivity(), this);
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                start = 0;
                data.clear();
                adapter.clear();
                president.loadData();
            }
        });
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                start =start+30 ;
                president.loadData();
            }
        });
        refresh.autoRefresh();
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
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }


    @Override
    public int getStart() {
        return start;
    }

    @Override
    public SmartRefreshLayout getRefresh() {
        return refresh;
    }

    @Override
    public ShopAdapter getAdapter() {
        return adapter;
    }

    @Override
    public List<ShopBean.DataBean> getData() {
        return data;
    }

    @Override
    public String getText() {
        return text;
    }


    @Override
    public String getType() {
        return type;
    }
}
