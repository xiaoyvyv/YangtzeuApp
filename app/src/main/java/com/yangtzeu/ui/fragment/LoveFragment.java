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
import com.yangtzeu.entity.LoveBean;
import com.yangtzeu.presenter.LovePresenter;
import com.yangtzeu.ui.activity.base.BaseFragment;
import com.yangtzeu.ui.adapter.LoveAdapter;
import com.yangtzeu.ui.view.LoveView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Administrator on 2018/3/6.
 */

public class LoveFragment extends BaseFragment implements LoveView {
    // 标志位，标志已经初始化完成。
    private boolean isPrepared = false;
    private View rootView;
    private String type;
    private String text;
    private LovePresenter president;
    private RecyclerView mRecyclerView;
    private int start = 0;
    private LoveAdapter adapter;
    private SmartRefreshLayout refresh;
    private List<LoveBean.DataBean> data=new ArrayList<>();
    private boolean is_manger = false;
    private boolean isRefresh = true;

    @Override
    public boolean isRefresh() {
        return isRefresh;
    }

    public static LoveFragment newInstance(String type, String text, boolean is_manger) {
        LoveFragment fragment = new LoveFragment();
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
            is_manger = bundle.getBoolean("is_manger");
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
        adapter = new LoveAdapter(getActivity(), is_manger);
        mRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);

        president = new LovePresenter(getActivity(), this);
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                start = 0;
                isRefresh = true;
                president.loadData();
            }
        });
        refresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                start =start+30 ;
                isRefresh = false;
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
    public LoveAdapter getAdapter() {
        return adapter;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public List<LoveBean.DataBean> getData() {
        return data;
    }
}
